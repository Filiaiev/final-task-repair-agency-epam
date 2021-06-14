package com.filiaiev.agency.web.command.manager;

import com.filiaiev.agency.database.dao.ClientDAO;
import com.filiaiev.agency.database.dao.PersonDAO;
import com.filiaiev.agency.database.dao.UserDAO;
import com.filiaiev.agency.entity.*;
import com.filiaiev.agency.web.command.Accessor;
import com.filiaiev.agency.web.command.Command;
import com.filiaiev.agency.web.util.Paths;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Servlet whose task is to return client info to person who requested it
public class GetClientInfoCommand implements Command {

    private static Logger logger = Logger.getLogger(GetClientInfoCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int clientId = Integer.parseInt(req.getParameter("clientId"));
        Client client = new ClientDAO().getClientById(clientId);

        /*
         * If client is not accessible for current user role, or client does not exist
         * show error message
         */
        if(!new ClientInfoAccessor().isAccessible(req, client)){
            String errorKey = "no_client_found";
            req.setAttribute("errorKey", errorKey);
            logger.trace("Client #" + clientId + "has been requested by " +
                    ((Role)req.getSession().getAttribute("role")).name()
                    + " (" + ((User)req.getSession().getAttribute("user")).getLogin() + ")" +
                    " but no client was found for this role");
            return Paths.JSP__ERROR;
        }

        Person person = new PersonDAO().getPersonById(client.getPersonId());
        User user = new UserDAO().getUserById(person.getUserId());
        String userEmail = user.getEmail();

        // Setting attributes to show them on client-side
        req.setAttribute("clientInfo", client);
        req.setAttribute("personInfo", person);
        req.setAttribute("userEmail", userEmail);
        return Paths.JSP__CLIENT_INFO;
    }

    private static class ClientInfoAccessor implements Accessor{
        @Override
        public boolean isAccessible(HttpServletRequest req, Entity o) {
            Client client = (Client)o;
            return client != null;
        }
    }
}
