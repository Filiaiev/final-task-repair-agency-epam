package com.filiaiev.agency.web.command.client;

import com.filiaiev.agency.database.dao.OrderDAO;
import com.filiaiev.agency.entity.Client;
import com.filiaiev.agency.entity.Order;
import com.filiaiev.agency.entity.User;
import com.filiaiev.agency.web.command.Command;
import com.filiaiev.agency.web.command.CommandContainer;
import com.filiaiev.agency.web.util.Paths;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CreateCommentCommand implements Command {

    private static Logger logger = Logger.getLogger(CreateCommentCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String comment = req.getParameter("commentText");
        if(comment == null || comment.isEmpty()){
            req.setAttribute("errorKey", "comment_cannot_be_empty");
            return Paths.JSP__ERROR;
        }

        int orderId = Integer.parseInt(req.getParameter("orderId"));

        OrderDAO orderDAO = new OrderDAO();
        Order order = orderDAO.getOrderByOrderId(orderId);

        Client client = (Client) req.getSession().getAttribute("client");
        User user = (User)req.getSession().getAttribute("user");

        if(order == null || order.getClientId() != client.getId()){
            req.setAttribute("errorKey", "no_permission_to_this_order");

            logger.info("Client #" + client.getId() + "(" + user.getLogin() + ") tried to set comment to Order #" + orderId
                        + " but had no permission");

            return Paths.JSP__ERROR;
        }

        orderDAO.updateOrderCommentById(orderId, comment);

        logger.info("User " + user.getLogin() + " created comment for Order #" + orderId);
        return "/controller?command=" + CommandContainer.GET_ORDER_INFO +
                "&orderId=" + orderId;
    }
}
