package com.filiaiev.agency.web.command.manager;

import com.filiaiev.agency.database.dao.OrderDAO;
import com.filiaiev.agency.entity.OrderStatus;
import com.filiaiev.agency.entity.User;
import com.filiaiev.agency.web.command.Command;
import com.filiaiev.agency.web.command.CommandContainer;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

// Servlet whose task is to set payment to requested order
public class SetPaymentCommand implements Command {

    private static Logger logger = Logger.getLogger(SetPaymentCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> orderInfo = (Map<String, String>)req.getSession().getAttribute("orderInfo");
        OrderDAO orderDAO = new OrderDAO();
        int orderId = Integer.parseInt(req.getParameter("orderId"));
        String cost = req.getParameter("costValue");

        // Updating DB table
        orderDAO.setOrderCostById(BigDecimal.valueOf(Double.parseDouble(cost)), orderId);

        // Updating session orderInfo Map
        orderInfo.put("cost", cost);
        orderInfo.put("statusId", String.valueOf(OrderStatus.WAITING_FOR_PAYMENT.ordinal()));

        // Updating session Map attribute to be able to show changes at client-side
        req.getSession().setAttribute("orderInfo", orderInfo);

        logger.info("Payment for Order #" + orderId + " has been set by " +
                ((User)req.getSession().getAttribute("user")).getLogin() +
                " to '" + cost + "' UAH");

        req.getRequestDispatcher("/controller?command=" + CommandContainer.SET_ORDER_STATUS
                + "&statusId=" + OrderStatus.WAITING_FOR_PAYMENT.ordinal()
                + "&orderId=" + orderId).forward(req, resp);
        return null;
    }
}
