package com.filiaiev.agency.web.command.manager;

import com.filiaiev.agency.database.dao.OrderDAO;
import com.filiaiev.agency.entity.Order;
import com.filiaiev.agency.web.command.Command;
import com.filiaiev.agency.web.util.Paths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class SortOrdersCommand extends Command {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String forward = Paths.JSP__HOME;
        String field = req.getParameter("orderBy");
        if(field == null){
            return forward;
        }

        List<Order> orders = new OrderDAO().getSortedOrders(field);
        req.setAttribute("orders", orders);
        return forward;
    }
}
