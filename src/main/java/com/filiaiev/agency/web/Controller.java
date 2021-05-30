package com.filiaiev.agency.web;

import com.filiaiev.agency.web.command.CommandContainer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Controller extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    private void process(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        System.out.println("Controller started it`s work");
        String cmd = req.getParameter("command");
        String forward = CommandContainer.getCommand(cmd).execute(req, resp);
        System.out.println(cmd + " processed!");
        String method = req.getMethod();
        System.out.println("Method = " + method + "\t" + "forward = " + forward);

        if(forward != null && !forward.isEmpty()){
            if(method.equals("GET") || req.getAttribute("errorMessage") != null){
                System.out.println("Req forward");
                req.getRequestDispatcher(forward).forward(req, resp);
            }else{
                System.out.println("Req redirect");
                resp.sendRedirect(req.getServletContext().getContextPath() + forward);
            }
//            req.getRequestDispatcher(forward).forward(req, resp);
        }
    }
}
