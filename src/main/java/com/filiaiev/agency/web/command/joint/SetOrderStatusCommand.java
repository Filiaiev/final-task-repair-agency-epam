package com.filiaiev.agency.web.command.joint;

import com.filiaiev.agency.database.dao.OrderDAO;
import com.filiaiev.agency.entity.Order;
import com.filiaiev.agency.entity.OrderStatus;
import com.filiaiev.agency.web.command.Command;
import com.filiaiev.agency.web.command.CommandContainer;
import com.filiaiev.agency.web.util.Paths;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SetOrderStatusCommand implements Command {

    private static Logger logger = Logger.getLogger(SetOrderStatusCommand.class);

    // No validation because get-method is disallowed in web.xml
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int statusId = Integer.parseInt(req.getParameter("statusId"));
        int orderId = Integer.parseInt(req.getParameter("orderId"));

        OrderDAO orderDAO = new OrderDAO();
        OrderStatus[] statusValues = OrderStatus.values();
        orderDAO.updateStatusById(statusValues[statusId], orderId);

        logger.info("Order #" + orderId + " status updated --> (" + statusValues[statusId] + ")");

        return "/controller?command=" + CommandContainer.getOrderInfoCmd
                + "&orderId=" + orderId;
    }
}
