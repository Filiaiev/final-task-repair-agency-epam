package com.filiaiev.agency.web.command.manager;

import com.filiaiev.agency.database.dao.OrderDAO;
import com.filiaiev.agency.entity.OrderStatus;
import com.filiaiev.agency.entity.User;
import com.filiaiev.agency.web.command.Command;
import com.filiaiev.agency.web.util.Paths;
import com.mysql.cj.log.Log;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

public class SetPaymentCommand implements Command {

    private static Logger logger = Logger.getLogger(SetPaymentCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> orderInfo = (Map<String, String>)req.getSession().getAttribute("order_info");
        OrderDAO orderDAO = new OrderDAO();
        String orderId = req.getParameter("order_id");
        String cost = req.getParameter("cost_value");

        // Updating SQL tables
        orderDAO.updateStatusById(OrderStatus.WAITING_FOR_PAYMENT, Integer.parseInt(orderId));
        orderDAO.setOrderCostById(BigDecimal.valueOf(Double.parseDouble(cost)), Integer.parseInt(orderId));

        // Updating session order_info map
        orderInfo.put("cost", cost);
        orderInfo.put("status_id", String.valueOf(OrderStatus.WAITING_FOR_PAYMENT.ordinal()));

        // Updating session value
        req.getSession().setAttribute("order_info", orderInfo);

        logger.info("Payment for Order #" + orderId + " has been set by " +
                ((User)req.getSession().getAttribute("user")).getLogin() +
                " to '" + cost + "' UAH");

        return "/controller?command=getOrderInfo&orderId=" + orderId;
    }
}
