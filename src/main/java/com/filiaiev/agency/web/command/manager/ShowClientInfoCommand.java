package com.filiaiev.agency.web.command.manager;

import com.filiaiev.agency.database.dao.ClientDAO;
import com.filiaiev.agency.database.dao.PersonDAO;
import com.filiaiev.agency.database.dao.UserDAO;
import com.filiaiev.agency.entity.Client;
import com.filiaiev.agency.entity.Entity;
import com.filiaiev.agency.entity.Person;
import com.filiaiev.agency.web.command.Accessor;
import com.filiaiev.agency.web.command.Command;
import com.filiaiev.agency.web.util.Paths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ShowClientInfoCommand implements Command, Accessor {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int clientId = Integer.parseInt(req.getParameter("clientId"));
        Client client = new ClientDAO().getClientById(clientId);

        if(!isAccessible(req, client)){
            String errorMessage = "no_client_found";
            req.setAttribute("errorMessage", errorMessage);
            return Paths.JSP__ERROR;
        }

        Person person = new PersonDAO().getPersonById(client.getPersonId());
        String userEmail = new UserDAO().getUserById(person.getUserId()).getEmail();

        HttpSession session = req.getSession();
        session.setAttribute("clientInfo", client);
        session.setAttribute("personInfo", person);
        session.setAttribute("userEmail", userEmail);
        return Paths.JSP__CLIENT_INFO;
    }

    @Override
    public boolean isAccessible(HttpServletRequest req, Entity o) {
        Client client = (Client)o;
        return client != null;
    }
}
