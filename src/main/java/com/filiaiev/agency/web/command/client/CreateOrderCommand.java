package com.filiaiev.agency.web.command.client;

import com.filiaiev.agency.database.DBManager;
import com.filiaiev.agency.database.dao.OrderDAO;
import com.filiaiev.agency.entity.Client;
import com.filiaiev.agency.entity.User;
import com.filiaiev.agency.web.command.Command;
import com.filiaiev.agency.web.util.Paths;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CreateOrderCommand implements Command {

    private static Logger logger = Logger.getLogger(CreateOrderCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String orderText = req.getParameter("orderText");
        if(orderText == null || orderText.isEmpty()){
            req.setAttribute("errorKey", "order_description_cannot_be_empty");
            return Paths.JSP__ERROR;
        }

        User user = (User)req.getSession().getAttribute("user");
        Client client = (Client)req.getSession().getAttribute("client");

        Integer genId = new OrderDAO().insertOrderByClientId(client.getId(), orderText);
        logger.info("Client # " + client.getId() + "(" + user.getLogin() + ") created new Order. No is '" + genId + "'");
        return null;
    }
}
