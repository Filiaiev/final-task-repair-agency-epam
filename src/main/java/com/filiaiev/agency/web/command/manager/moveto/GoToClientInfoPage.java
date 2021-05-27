package com.filiaiev.agency.web.command.manager.moveto;

import com.filiaiev.agency.database.dao.ClientDAO;
import com.filiaiev.agency.database.dao.PersonDAO;
import com.filiaiev.agency.database.dao.UserDAO;
import com.filiaiev.agency.entity.Client;
import com.filiaiev.agency.entity.Person;
import com.filiaiev.agency.web.command.Command;
import com.filiaiev.agency.web.util.Paths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GoToClientInfoPage extends Command {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int clientId = Integer.parseInt(req.getParameter("clientId"));
        Client client = new ClientDAO().getClientById(clientId);
        Person person = new PersonDAO().getPersonById(client.getPersonId());
        String userEmail = new UserDAO().getUserById(person.getUserId()).getEmail();

        req.setAttribute("clientInfo", client);
        req.setAttribute("personInfo", person);
        req.setAttribute("userEmail", userEmail);
        return Paths.JSP__CLIENT_INFO;
    }
}
