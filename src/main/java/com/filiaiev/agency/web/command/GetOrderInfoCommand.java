package com.filiaiev.agency.web.command;

import com.filiaiev.agency.database.dao.OrderDAO;
import com.filiaiev.agency.entity.Client;
import com.filiaiev.agency.entity.Entity;
import com.filiaiev.agency.entity.Order;
import com.filiaiev.agency.web.util.Paths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class GetOrderInfoCommand implements Command, Accessor {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int orderId = Integer.parseInt(req.getParameter("orderId"));
        OrderDAO orderDAO = new OrderDAO();
        Order order = orderDAO.getOrderByOrderId(orderId);

        String forward = null;
        System.out.println("order is " + orderId);

        if(isAccessible(req, order)){
            Map<String, String> orderInfo = orderDAO.getOrderInfoByOrderId(orderId);
            req.getSession().setAttribute("order_info", orderInfo);
            forward = "/WEB-INF/views/order.jsp";
        }else{
            String errorMessage = "no_order_found_for_this_request";
            forward = Paths.JSP__ERROR;
            req.setAttribute("errorMessage", errorMessage);
        }
        return forward;
    }

    @Override
    public boolean isAccessible(HttpServletRequest req, Entity o){
        Order order = (Order)o;
        if(order == null){
            return false;
        }

        Client client = null;
        if((client = (Client)req.getSession().getAttribute("client")) != null
                && client.getId() != order.getClientId()){
            return false;
        }

        Integer employeeId = null;
        if((employeeId = (Integer)req.getSession().getAttribute("employeeId")) != null
                && employeeId != order.getWorkerId()) {
            return false;
        }

        return true;
    }
}
