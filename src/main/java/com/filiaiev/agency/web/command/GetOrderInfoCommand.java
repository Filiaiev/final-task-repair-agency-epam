package com.filiaiev.agency.web.command;

import com.filiaiev.agency.database.dao.OrderDAO;
import com.filiaiev.agency.web.util.Paths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class GetOrderInfoCommand extends Command{

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int orderId = Integer.parseInt(req.getParameter("orderId"));
        System.out.println("order is " + orderId);
        Map<String, String> orderInfo = new OrderDAO().getOrderInfoByOrderId(orderId);
//        req.removeAttribute("orders");
//        req.removeAttribute("order");
        req.getSession().setAttribute("order_info", orderInfo);

//        int role = (int)req.getSession().getAttribute("role_id");
//        if(role == Role.MANAGER.ordinal()){
//            req.setAttribute("repairers", new EmployeeDAO().getAllRepairersInfo());
//        }
        return Paths.JSP__ORDER;
    }
}
