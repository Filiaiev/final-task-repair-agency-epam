package com.filiaiev.agency.web.command.client;

import com.filiaiev.agency.database.dao.ClientDAO;
import com.filiaiev.agency.database.dao.PersonDAO;
import com.filiaiev.agency.database.dao.UserDAO;
import com.filiaiev.agency.database.exception.InsertingDuplicateException;
import com.filiaiev.agency.database.util.DBUtil;
import com.filiaiev.agency.entity.Role;
import com.filiaiev.agency.web.command.Command;
import com.filiaiev.agency.web.util.Paths;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

public class RegisterCommand implements Command {

    private static Logger logger = Logger.getLogger(RegisterCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String email = req.getParameter("email");
        String login = req.getParameter("userLogin");
        String pass = req.getParameter("userPass");
        String firstName = req.getParameter("firstName");
        String middleName = req.getParameter("middleName");
        String lastName = req.getParameter("lastName");
        String birthDate = req.getParameter("birthDate");

        String registerMessage = null;

        try{
            Integer userId = new UserDAO().insertUser(email, login, pass, Role.CLIENT.ordinal());
            Integer personId = new PersonDAO().insertPerson(firstName, middleName, lastName, Date.valueOf(birthDate), userId);
            new ClientDAO().insertClient(personId);
        }catch (InsertingDuplicateException e){
            logger.warn("Client tried to register with login: '" + login
                    + "', email: '" + email + "', but " + e.getMessage(), e.getCause());
            registerMessage = "user_exists";
            req.getSession().setAttribute("registerMessage", registerMessage);
            return Paths.URL__REGISTER;
        }

        req.getSession().setAttribute("registerMessage", "user_registered");
        logger.info("New user has been registered. '" + login + "' (" + email + ")");

        return Paths.URL__REGISTER;
    }
}
