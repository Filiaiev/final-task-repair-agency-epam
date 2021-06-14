package com.filiaiev.agency.web.command.client;

import com.filiaiev.agency.database.dao.ClientDAO;
import com.filiaiev.agency.entity.Client;
import com.filiaiev.agency.web.command.Command;
import com.filiaiev.agency.web.util.Paths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

// Servlet whose task is to provide redirect to client`s profile page
public class ToProfileCommand implements Command {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Client sessionClient = (Client)session.getAttribute("client");
        Client client = new ClientDAO().getClientById(sessionClient.getId());

        // Setting session attribute to show it on the client`s profile
        session.setAttribute("client", client);
        return Paths.JSP__PROFILE;
    }
}
