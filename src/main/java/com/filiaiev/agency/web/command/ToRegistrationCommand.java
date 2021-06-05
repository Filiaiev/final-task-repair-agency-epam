package com.filiaiev.agency.web.command;

import com.filiaiev.agency.web.util.Paths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ToRegistrationCommand implements Command{
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        return Paths.JSP__REGISTER;
    }
}
