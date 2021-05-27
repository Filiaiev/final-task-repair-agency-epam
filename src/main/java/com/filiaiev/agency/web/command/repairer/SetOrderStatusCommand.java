package com.filiaiev.agency.web.command.repairer;

import com.filiaiev.agency.database.dao.OrderDAO;
import com.filiaiev.agency.entity.OrderStatus;
import com.filiaiev.agency.web.command.Command;
import com.filiaiev.agency.web.command.CommandContainer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SetOrderStatusCommand extends Command {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int statusId = Integer.parseInt(req.getParameter("statusId"));
        int orderId = Integer.parseInt(req.getParameter("orderId"));
        new OrderDAO().updateStatusById(OrderStatus.values()[statusId], orderId);
        return CommandContainer.getCommand("getOrderInfo").execute(req, resp);
    }
}
