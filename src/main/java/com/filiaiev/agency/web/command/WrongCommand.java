package com.filiaiev.agency.web.command;

import com.filiaiev.agency.web.util.Paths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WrongCommand implements Command{
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("errorMessage", "bad_request");
        return Paths.JSP__ERROR;
    }
}
