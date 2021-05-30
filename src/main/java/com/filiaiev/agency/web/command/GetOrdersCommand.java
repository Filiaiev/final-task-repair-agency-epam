package com.filiaiev.agency.web.command;

import com.filiaiev.agency.database.dao.EmployeeDAO;
import com.filiaiev.agency.database.dao.OrderDAO;
import com.filiaiev.agency.entity.Order;
import com.filiaiev.agency.entity.Person;
import com.filiaiev.agency.entity.Role;
import com.filiaiev.agency.web.util.Paths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class GetOrdersCommand implements Command{

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Integer roleId = (Integer)session.getAttribute("roleId");
        String forward = null;

        Role role = Role.values()[roleId];
        List<Order> orders = null;
        OrderDAO orderDAO = new OrderDAO();
        forward = Paths.JSP__HOME;

        switch (role){
            case CLIENT:
                orders = orderDAO.findOrdersByPersonId(((Person)req.getSession(false)
                        .getAttribute("person")).getId());
                 break;
            case MANAGER:
                orders = orderDAO.getAllOrders();
                break;
            case REPAIRER:
                int personId = ((Person)session.getAttribute("person")).getId();
                int repairerId = new EmployeeDAO().getEmployeeByPersonId(personId).getId();
                orders = orderDAO.getOrdersByRepairerId(repairerId);
                break;
        }
        req.setAttribute("orders", orders);
        return forward;
    }
}
