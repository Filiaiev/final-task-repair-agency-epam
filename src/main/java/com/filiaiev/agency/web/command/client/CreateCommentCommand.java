package com.filiaiev.agency.web.command.client;

import com.filiaiev.agency.database.dao.OrderDAO;
import com.filiaiev.agency.web.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CreateCommentCommand extends Command {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String comment = req.getParameter("commentText");
        System.out.println("comment text = " + comment);
        System.out.println("order id comment = " + req.getParameter("order_id"));
        new OrderDAO().updateOrderCommentById(Integer.parseInt
                (req.getParameter("order_id")), comment);
        return "";
    }
}
