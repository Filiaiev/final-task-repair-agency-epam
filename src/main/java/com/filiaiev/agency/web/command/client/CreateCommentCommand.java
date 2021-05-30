package com.filiaiev.agency.web.command.client;

import com.filiaiev.agency.database.dao.OrderDAO;
import com.filiaiev.agency.web.command.Command;
import com.filiaiev.agency.web.command.CommandContainer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CreateCommentCommand implements Command {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String comment = req.getParameter("commentText");
        System.out.println("comment text = " + comment);
        int orderId = Integer.parseInt(req.getParameter("orderId"));
        System.out.println("order id comment = " + orderId);
        new OrderDAO().updateOrderCommentById(orderId, comment);
        return "/controller?command=" + CommandContainer.getOrderInfoCmd +
                "&orderId=" + orderId;
    }
}
