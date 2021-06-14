package com.filiaiev.agency.web.command.client;

import com.filiaiev.agency.database.dao.ClientDAO;
import com.filiaiev.agency.database.dao.OrderDAO;
import com.filiaiev.agency.entity.Client;
import com.filiaiev.agency.entity.Order;
import com.filiaiev.agency.entity.OrderStatus;
import com.filiaiev.agency.entity.User;
import com.filiaiev.agency.web.command.Command;
import com.filiaiev.agency.web.command.CommandContainer;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

// Servlet whose task is to provide client`s payment logic
public class PayCommand implements Command {

    private static Logger logger = Logger.getLogger(PayCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        int orderId = Integer.parseInt(req.getParameter("orderId"));

        OrderDAO orderDAO = new OrderDAO();
        ClientDAO clientDAO = new ClientDAO();

        Client client = (Client)session.getAttribute("client");
        Order order = orderDAO.getOrderByOrderId(orderId);
        User user = (User)session.getAttribute("user");

        // Getting current client`s cash
        BigDecimal clientCash = clientDAO.getClientCashByClientId(client.getId());

        String paymentStatus = null;

        /*
        * If client doesn`t have enough money to pay
        * show him according message
        */
        if(clientCash.compareTo(order.getCost()) < 0){
            paymentStatus = "payment_not_enough_money";
            logger.trace("Client #" + client.getId() + "(" + user.getLogin() + ") " +
                    "hasn`t got enough cash to pay for Order #" + order.getId());
        }else{
            // Substracting order cost from client`s cash and setting PAID status
            BigDecimal newCash = clientCash.subtract(order.getCost());
            clientDAO.setClientCashById(newCash, client.getId());
            orderDAO.updateStatusById(OrderStatus.PAID, order.getId());

            client.setCash(newCash);
            order.setStatusId(OrderStatus.PAID.ordinal());

            // Updating orderInfo Map
            Map<String, String> orderInfo = (Map<String, String>)session.getAttribute("orderInfo");
            orderInfo.put("statusId", String.valueOf(order.getStatusId()));

            // Updating session attributes
            session.setAttribute("order", order);
            session.setAttribute("client", client);
            session.setAttribute("order_info", orderInfo);
            paymentStatus = "success";

            logger.info("Client #" + client.getId() + "(" + user.getLogin() + ") " +
                    "paid for Order #" + orderId);
        }

        return "/controller?command=" + CommandContainer.GET_ORDER_INFO +
                "&orderId=" + orderId + "&paymentStatus=" + paymentStatus;
    }
}
