package com.filiaiev.agency.web.command.auth;

import com.filiaiev.agency.entity.User;
import com.filiaiev.agency.web.command.Command;
import com.filiaiev.agency.web.util.Paths;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogOutCommand implements Command {

    private static Logger logger = Logger.getLogger(LogOutCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User)req.getSession(false).getAttribute("user");
        req.getSession(false).invalidate();
        logger.info("User " + user.getLogin() + " has logged out");
        return Paths.URL__WELCOME;
    }
}
