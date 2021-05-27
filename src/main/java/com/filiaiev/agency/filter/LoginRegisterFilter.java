package com.filiaiev.agency.filter;

import com.filiaiev.agency.entity.User;
import com.filiaiev.agency.web.util.Paths;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginRegisterFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("Login com.filiaiev.agency.filter init");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)req;
//        System.out.println("Req URL = " + request.getRequestURL());
//
        HttpSession session =  request.getSession();
//        System.out.println("Session is " + session);
        User user = (User)session.getAttribute("user");
        System.out.println("User is " + user);
//
//        if(user != null){
//            req.getRequestDispatcher(Paths.JSP__HOME).forward(req, resp);
//            return;
//        }

        String forward = null;
//        Map<String, String[]> params = request.getParameterMap();
//        System.out.println("Params = " + params);

//        if((session.getAttribute("user") == null) && !(params.get("command")[0].equals("login"))){
//            forward = Paths.JSP__LOGIN;
//        }else{
//            filterChain.doFilter(req, resp);
//            forward = Paths.JSP__HOME;
//        }
        System.out.println(request.getRequestURI());
        if(user != null){
            forward = Paths.JSP__HOME;
        }else{
            forward = Paths.JSP__LOGIN;
        }
        System.out.println("Path to go: " + forward);
        req.getRequestDispatcher(forward).forward(req, resp);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
