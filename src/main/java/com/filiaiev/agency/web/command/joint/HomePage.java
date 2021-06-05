package com.filiaiev.agency.web.command.joint;

import com.filiaiev.agency.web.command.Command;
import com.filiaiev.agency.web.util.Paths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HomePage implements Command {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(Paths.JSP__HOME).forward(req, resp);
        return null;
    }
}
