package com.filiaiev.agency.web.command;

import com.filiaiev.agency.database.dao.ClientDAO;
import com.filiaiev.agency.database.dao.PersonDAO;
import com.filiaiev.agency.database.dao.UserDAO;
import com.filiaiev.agency.database.util.DBUtil;
import com.filiaiev.agency.entity.Role;
import com.filiaiev.agency.web.util.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.sql.*;

public class RegisterCommand extends Command {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws UnsupportedEncodingException {
        String email = req.getParameter("email");
        String login = req.getParameter("userLogin");
        String pass = req.getParameter("userPass");
        String firstName = req.getParameter("firstName");
        String middleName = req.getParameter("middleName");
        String lastName = req.getParameter("lastName");
        String birthDate = req.getParameter("birthDate");

        Integer userId = new UserDAO().insertUser(email, login, DBUtil.hash(pass), Role.CLIENT.ordinal());
        Integer personId = new PersonDAO().insertPerson(firstName, middleName, lastName, Date.valueOf(birthDate), userId);
        new ClientDAO().insertClient(personId);

        System.out.println("User registered!");
        req.setAttribute("registerMessage", "Register successful");
        return Paths.JSP__LOGIN;
    }
}
