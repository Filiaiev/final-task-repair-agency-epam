package com.filiaiev.agency.web.command.order;

import com.filiaiev.agency.database.dao.OrderDAO;
import com.filiaiev.agency.entity.Client;
import com.filiaiev.agency.entity.Entity;
import com.filiaiev.agency.entity.Order;
import com.filiaiev.agency.entity.User;
import com.filiaiev.agency.web.command.Accessor;
import com.filiaiev.agency.web.command.Command;
import com.filiaiev.agency.web.util.Paths;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class GetOrderInfoCommand implements Command {

    private static Logger logger = Logger.getLogger(GetOrderInfoCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer orderId = null;
        try{
            orderId = Integer.parseInt(req.getParameter("orderId"));
        }catch (NumberFormatException e){
            req.setAttribute("errorKey", "order_id_cannot_be_empty");
            return Paths.JSP__ERROR;
        }

        OrderDAO orderDAO = new OrderDAO((String)req.getSession().getAttribute("lang"));

        String forward = null;
        Map<String, String> orderInfo = orderDAO.getOrderInfoByOrderId(orderId);
        Order orderDummy = null;

        if(orderInfo != null) {
            orderDummy = new Order();
            orderDummy.setClientId(Integer.parseInt(orderInfo.get("clientId")));
            orderDummy.setWorkerId(
                    orderInfo.get("workerId") == null ? null :
                    Integer.parseInt(orderInfo.get("workerId")));
        }

        if(new OrderInfoAccessor().isAccessible(req, orderDummy)){
            req.getSession().setAttribute("orderInfo", orderInfo);
            forward = Paths.JSP__ORDER;
        }else{
            User user = (User)req.getSession().getAttribute("user");
            logger.info("User " + user.getLogin() + " tried to request order #" + orderId +
                    " but has no access or order does not exist");
            String errorKey = "no_order_found_for_this_request";
            forward = Paths.JSP__ERROR;
            req.setAttribute("errorKey", errorKey);
        }

        req.setAttribute("paymentStatus", req.getParameter("paymentStatus"));
        return forward;
    }

    private static class OrderInfoAccessor implements Accessor{
        @Override
        public boolean isAccessible(HttpServletRequest req, Entity o){
            Order order = (Order)o;
            // Check if passed order is null ( no access )
            if(order == null){
                return false;
            }

            /*
             *    Check if request has been made by client
             *    If yes, check if client`s id corresponds
             *    order`s client id field
             */
            Client client = null;
            if((client = (Client)req.getSession().getAttribute("client")) != null
                    && client.getId() != order.getClientId()){
                return false;
            }

            /*
             *    Check if request has been made by repairer
             *    If yes, check if repairer`s id corresponds
             *    worker`s id field
             */
            Integer employeeId = null;
            if((employeeId = (Integer)req.getSession().getAttribute("employeeId")) != null
                    && !employeeId.equals(order.getWorkerId())) {
                return false;
            }

            return true;
        }
    }
}
