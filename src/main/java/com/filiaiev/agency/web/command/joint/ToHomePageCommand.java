package com.filiaiev.agency.web.command.joint;

import com.filiaiev.agency.database.dao.OrderDAO;
import com.filiaiev.agency.database.util.Field;
import com.filiaiev.agency.entity.OrderStatus;
import com.filiaiev.agency.entity.Role;
import com.filiaiev.agency.entity.User;
import com.filiaiev.agency.web.command.Command;
import com.filiaiev.agency.web.util.Paths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;

public class ToHomePageCommand implements Command {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Role role = (Role)req.getSession().getAttribute("role");
        OrderDAO orderDAO = new OrderDAO();
        int todaysOrders = 0;
        long toWorkCount = 0;
        if(role == Role.MANAGER){
            toWorkCount = orderDAO.getOrdersByStatus(OrderStatus.CREATED).size()
                        + orderDAO.getOrdersByStatus(OrderStatus.PAID)
                            .stream()
                            .filter(v -> v.getWorkerId() == 0)
                            .count();
            todaysOrders = orderDAO.getOrdersCountByField(
                    "date(" + Field.ORDERS__ORDER_DATE + ")", new Date(System.currentTimeMillis()).toString());
            req.setAttribute("todaysOrders", todaysOrders);
        }else if(role == Role.REPAIRER){
            toWorkCount = orderDAO.getOrdersByRepairerId(
                    (Integer)req.getSession().getAttribute("employeeId"),
                    0, 0)
                    .stream()
                    .filter(v ->
                            (v.getStatusId() == OrderStatus.IN_WORK.ordinal()
                            || v.getStatusId() == OrderStatus.PAID.ordinal()))
                    .count();
        }
        req.setAttribute("toWorkCount", toWorkCount);
        req.getRequestDispatcher(Paths.JSP__HOME).forward(req, resp);
        return null;
    }
}
