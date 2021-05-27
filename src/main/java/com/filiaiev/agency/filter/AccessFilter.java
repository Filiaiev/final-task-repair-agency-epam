package com.filiaiev.agency.filter;

import com.filiaiev.agency.entity.Role;
import com.filiaiev.agency.web.util.Paths;

import javax.servlet.*;
import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

public class AccessFilter implements Filter {

    private static Map<Role, List<String>> accessMap = new HashMap<>();
    private static List<String> outOfControl = new ArrayList<>();
    private static List<String> logged = new ArrayList<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("Access EncodingFilter init start");
//        EncodingFilter.super.init(filterConfig);
        ServletContext servletContext = filterConfig.getServletContext();
        accessMap.put(Role.MANAGER, asList(filterConfig.getInitParameter("manager")));
        accessMap.put(Role.CLIENT, asList(filterConfig.getInitParameter("client")));
        accessMap.put(Role.REPAIRER, asList(filterConfig.getInitParameter("repairer")));

        outOfControl = asList(filterConfig.getInitParameter("out-of-control"));
        logged = asList(filterConfig.getInitParameter("logged"));
        System.out.println("Access EncodingFilter init end");
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
//        ServletContext servletContext = servletRequest.getServletContext();
        if(accessAllowed(req)){
            filterChain.doFilter(req, resp);
        }else{
            String errorMessage = "You don`t have permission to access this resource";
            req.setAttribute("errorMessage", errorMessage);
            req.getRequestDispatcher(Paths.JSP__ERROR).forward(req, resp);
        }
    }

    private boolean accessAllowed(ServletRequest request){
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        String commandName = request.getParameter("command");
        System.out.println(Collections.list(request.getParameterNames()));
        if(commandName == null || commandName.isEmpty()){
            return false;
        }

        if(outOfControl.contains(commandName)){
            return true;
        }

        HttpSession session = httpRequest.getSession(false);
        if(session == null){
            return false;
        }

        Integer roleId = (Integer)session.getAttribute("roleId");
        if(roleId == null){
            return false;
        }else if(logged.contains(commandName)){
            return true;
        }

        return accessMap.get(Role.values()[roleId]).contains(commandName);
    }

    private List<String> asList(String params){
        List<String> list = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(params);
        while(tokenizer.hasMoreTokens()){
            list.add(tokenizer.nextToken());
        }
        return list;
    }
}
