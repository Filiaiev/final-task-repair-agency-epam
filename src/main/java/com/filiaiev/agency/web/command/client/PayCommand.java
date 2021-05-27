package com.filiaiev.agency.web.command.client;

import com.filiaiev.agency.database.dao.ClientDAO;
import com.filiaiev.agency.database.dao.OrderDAO;
import com.filiaiev.agency.entity.Client;
import com.filiaiev.agency.entity.Order;
import com.filiaiev.agency.entity.OrderStatus;
import com.filiaiev.agency.web.command.Command;
import com.filiaiev.agency.web.util.Paths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

public class PayCommand extends Command {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("PAY COMMAND EXECUTION");
        HttpSession session = req.getSession(false);

        OrderDAO orderDAO = new OrderDAO();

        Client client = (Client)session.getAttribute("client");
        Order order = orderDAO.getOrderInstanceByOrderId(Integer.parseInt(req.getParameter("orderId")));
        BigDecimal clientCash = client.getCash();

        String forward = Paths.JSP__ORDER;
        String paymentMessageKey = null;

        if(clientCash.compareTo(order.getCost()) < 0){
            paymentMessageKey = "payment_not_enough_money";
        }else{
            paymentMessageKey = "payment_success";

            BigDecimal newCash = clientCash.subtract(order.getCost());
            new ClientDAO().updateClientCashById(newCash, client.getId());
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
        }
        req.setAttribute("payment_message_key", paymentMessageKey);
        return forward;
    }
}
