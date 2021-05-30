package com.filiaiev.agency.filter;

import com.filiaiev.agency.entity.User;
import com.filiaiev.agency.web.util.Paths;
import com.sun.deploy.net.HttpResponse;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public class LoggedFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("Login com.filiaiev.agency.filter init");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("SERVLET FILTER");
        HttpServletRequest request = (HttpServletRequest)req;
        HttpSession session =  request.getSession();

        User user = (User)session.getAttribute("user");
        System.out.println("User is " + user);

        String forward = null;
        String URI = request.getRequestURI();
        forward = URI.substring(request.getContextPath().length());
        System.out.println("Forward now is " + forward);
        if(user != null && !forward.equals(Paths.JSP__HOME)){
            ((HttpServletResponse)resp).sendRedirect(req.getServletContext().getContextPath() + Paths.JSP__HOME);
        }

        if(user == null && forward.equals(Paths.JSP__HOME)){
            ((HttpServletResponse)resp).sendRedirect(req.getServletContext().getContextPath() + Paths.JSP__LOGIN);
        }

        System.out.println("Filter do chain");
        filterChain.doFilter(req, resp);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
