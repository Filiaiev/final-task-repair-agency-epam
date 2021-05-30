package com.filiaiev.agency.web.command;

import com.filiaiev.agency.database.dao.OrderDAO;
import com.filiaiev.agency.entity.OrderStatus;
import com.filiaiev.agency.web.command.Command;
import com.filiaiev.agency.web.command.CommandContainer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SetOrderStatusCommand implements Command {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int statusId = Integer.parseInt(req.getParameter("statusId"));
        int orderId = Integer.parseInt(req.getParameter("orderId"));
        OrderDAO orderDAO = new OrderDAO();

        if(statusId == OrderStatus.COMPLETED.ordinal()){
            orderDAO.completeOrderById(orderId);
        }else{
            orderDAO.updateStatusById(OrderStatus.values()[statusId], orderId);
        }
        return "/controller?command=" + CommandContainer.getOrderInfoCmd
                + "&orderId=" + orderId;
    }
}
