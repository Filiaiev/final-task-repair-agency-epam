package com.filiaiev.agency.web.command.client;

import com.filiaiev.agency.database.dao.ClientDAO;
import com.filiaiev.agency.database.dao.OrderDAO;
import com.filiaiev.agency.entity.Client;
import com.filiaiev.agency.entity.Order;
import com.filiaiev.agency.entity.OrderStatus;
import com.filiaiev.agency.entity.User;
import com.filiaiev.agency.web.command.Command;
import com.filiaiev.agency.web.command.CommandContainer;
import com.filiaiev.agency.web.util.Paths;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

public class PayCommand implements Command {

    private static Logger logger = Logger.getLogger(PayCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        int orderId = Integer.parseInt(req.getParameter("orderId"));

        OrderDAO orderDAO = new OrderDAO();
        ClientDAO clientDAO = new ClientDAO();

        Client client = (Client)session.getAttribute("client");
        Order order = orderDAO.getOrderByOrderId(orderId);
        User user = (User)session.getAttribute("user");

        BigDecimal clientCash = clientDAO.getClientCashByClientId(client.getId());

        String paymentStatus = null;
        if(clientCash.compareTo(order.getCost()) < 0){
            paymentStatus = "not_enough_money";
            logger.trace("Client #" + client.getId() + "(" + user.getLogin() + ") " +
                    "hasn`t got enough cash to pay for Order #" + order.getId());
            session.setAttribute("paymentStatus", paymentStatus);
        }else{
            BigDecimal newCash = clientCash.subtract(order.getCost());
            clientDAO.setClientCashById(newCash, client.getId());
            orderDAO.updateStatusById(OrderStatus.PAID, order.getId());

            // update session info
            client.setCash(newCash);
            order.setStatusId(OrderStatus.PAID.ordinal());
            // TODO: changed session to req
            Map<String, String> order_info = (Map<String, String>)session.getAttribute("order_info");
            order_info.put("status_id", String.valueOf(order.getStatusId()));

            session.setAttribute("order", order);
            session.setAttribute("client", client);
            session.setAttribute("order_info", order_info);

            logger.info("Client #" + client.getId() + "(" + user.getLogin() + ") " +
                    "paid for Order #" + orderId);
        }

        System.out.println(paymentStatus);
        return "/controller?command=" + CommandContainer.getOrderInfoCmd +
                "&orderId=" + orderId;
    }
}
