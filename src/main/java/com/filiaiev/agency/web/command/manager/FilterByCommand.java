package com.filiaiev.agency.web.command.manager;

import com.filiaiev.agency.database.dao.EmployeeDAO;
import com.filiaiev.agency.database.dao.OrderDAO;
import com.filiaiev.agency.entity.Order;
import com.filiaiev.agency.entity.OrderStatus;
import com.filiaiev.agency.web.command.Command;
import com.filiaiev.agency.web.util.Paths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class FilterByCommand implements Command {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String statusFilter = req.getParameter("filterTaskStatus");
        String repairerFilter = req.getParameter("filterTaskRepairer");
//        String forward = CommandContainer.getCommands().get("receive_orders").execute(req, resp);
//        List<Order> orders = (List<Order>)req.getAttribute("orders");
        List<Order> orders = null;
        OrderDAO orderDAO = new OrderDAO();
        System.out.println("Status = " + statusFilter + "\n" + "Repairer = " + repairerFilter);
        if(statusFilter == null){
            int repairerId = new EmployeeDAO().getEmployeeByPersonId(Integer.parseInt(repairerFilter)).getId();
            orders = orderDAO.getOrdersByRepairerId(repairerId);
        }else{
            orders = orderDAO.getOrdersByStatus(OrderStatus.valueOf(statusFilter));
        }

        req.setAttribute("orders", orders);
        return Paths.JSP__HOME;
    }
}
