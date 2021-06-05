package com.filiaiev.agency.web.command.joint.order;

import com.filiaiev.agency.database.dao.EmployeeDAO;
import com.filiaiev.agency.database.dao.OrderDAO;
import com.filiaiev.agency.entity.Order;
import com.filiaiev.agency.entity.Person;
import com.filiaiev.agency.entity.Role;
import com.filiaiev.agency.web.command.Command;
import com.filiaiev.agency.web.util.Paths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GetOrdersCommand implements Command {

    private static final int OFFSET = 5;

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        Integer roleId = (Integer)session.getAttribute("roleId");
        String forward = null;
        int currentPage = req.getParameter("page") == null ? 1 : Integer.parseInt(req.getParameter("page"));

        Role role = Role.values()[roleId];
        List<Order> orders = null;
        OrderDAO orderDAO = new OrderDAO();

        int start = (currentPage-1)*OFFSET;
        double pagesCount = 0;
        double ordersCount = 0;

        int personId = ((Person)req.getSession(false)
                .getAttribute("person")).getId();

        switch (role){
            case CLIENT:
                orders = orderDAO.getOrdersByPersonId(personId, start, OFFSET);
                ordersCount = orderDAO.getOrdersByPersonId(personId, 0, 0).size();
                forward = Paths.JSP__ORDER_LIST;
                break;

            case MANAGER:
                forward = Paths.JSP__ORDER_LIST_MANAGER;

                boolean resetFilter = Boolean.parseBoolean(req.getParameter("resetFilter"));
                Map<String, String> filters = null;
                List<String> params = Collections.list(req.getParameterNames());
                params = params.stream()
                        .filter(v -> v.equals("status_id") || v.equals("worker_id"))
                        .collect(Collectors.toList());

                if(!resetFilter && params.size() > 0){
                    filters = new HashMap<>();
                    for (String p : params){
                        filters.put(p, req.getParameter(p));
                        req.setAttribute(p, req.getParameter(p));
                    }
                }

                String sortBy = null;
                String ordering = null;

                if(!resetFilter){
                    sortBy = req.getParameter("sortBy");
                    ordering = req.getParameter("ordering");
                    req.setAttribute("sortBy", sortBy);
                    req.setAttribute("ordering", ordering);
                }

                orders = orderDAO.getOrders(filters, sortBy, ordering, start, OFFSET);
                ordersCount = orderDAO.getOrders(filters, sortBy, ordering, 0, 0).size();
                break;

            case REPAIRER:
                int repairerId = new EmployeeDAO().getEmployeeByPersonId(personId).getId();
                orders = orderDAO.getOrdersByRepairerId(repairerId, start, OFFSET);
                ordersCount = orderDAO.getOrdersByRepairerId(repairerId, 0, 0).size();
                forward = Paths.JSP__ORDER_LIST;
                break;
        }
        pagesCount = (int)Math.ceil(ordersCount/OFFSET);
        session.setAttribute("orders", orders);
        session.setAttribute("pagesCount", pagesCount);

        return forward;
    }
}
