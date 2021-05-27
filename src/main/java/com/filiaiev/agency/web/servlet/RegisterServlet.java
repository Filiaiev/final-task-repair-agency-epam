package com.filiaiev.agency.web.servlet;

import com.filiaiev.agency.web.util.Paths;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegisterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher rd =  req.getRequestDispatcher(Paths.JSP__REGISTER);
        rd.forward(req, resp);
    }
}
