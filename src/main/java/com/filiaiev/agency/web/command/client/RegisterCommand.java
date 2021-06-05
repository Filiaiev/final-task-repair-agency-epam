package com.filiaiev.agency.web.command.client;

import com.filiaiev.agency.database.dao.ClientDAO;
import com.filiaiev.agency.database.dao.PersonDAO;
import com.filiaiev.agency.database.dao.UserDAO;
import com.filiaiev.agency.database.util.DBUtil;
import com.filiaiev.agency.entity.Role;
import com.filiaiev.agency.web.command.Command;
import com.filiaiev.agency.web.util.Paths;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
        Integer userId = new UserDAO().insertUser(email, login, DBUtil.hash(pass), Role.CLIENT.ordinal());
        if(userId == null){
            logger.info("Client tried to register with login: '" + login
                        + "', email: '" + email + "'. One of them corresponds to another client`s account"
            );
            registerMessage = "user_exists";
            req.getSession().setAttribute("registerMessage", registerMessage);
            return Paths.JSP__REGISTER;
        }
        Integer personId = new PersonDAO().insertPerson(firstName, middleName, lastName, Date.valueOf(birthDate), userId);
        new ClientDAO().insertClient(personId);

        req.getSession().setAttribute("registerMessage", "user_registered");
        logger.info("New user has been registered. '" + login + "' (" + email + ")");

        return Paths.JSP__REGISTER;
    }
}
