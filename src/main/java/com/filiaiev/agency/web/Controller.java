package com.filiaiev.agency.web;

import com.filiaiev.agency.web.command.CommandContainer;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Front controller
public class Controller extends HttpServlet {

    private static Logger logger = Logger.getLogger(Controller.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    private void process(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String cmd = req.getParameter("command");
        String forward = CommandContainer.getCommand(cmd).execute(req, resp);
        String method = req.getMethod();

        // If executed command didn`t return null or empty forward String
        if(forward != null && !forward.isEmpty()){
            // If method is 'GET' --> forward request, else --> redirect (PRG)
            if(method.equals("GET") || req.getAttribute("errorMessage") != null){
                logger.trace("Command '" + cmd + "', forwarding to --> " + forward);
                req.getRequestDispatcher(forward).forward(req, resp);
            }else{
                String fullRedirectPath = req.getServletContext().getContextPath() + forward;
                logger.trace("Command '" + cmd + "', redirecting to --> " + fullRedirectPath);
                resp.sendRedirect(fullRedirectPath);
            }
        }
    }
}
