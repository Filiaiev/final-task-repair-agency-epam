package com.filiaiev.agency.filter;

import com.filiaiev.agency.entity.Role;
import com.filiaiev.agency.web.command.CommandContainer;
import com.filiaiev.agency.web.util.Paths;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

public class AccessFilter implements Filter {

    private static Logger logger = Logger.getLogger(AccessFilter.class);

    /**
     * Collections that holds accessible actions for each
     * role according to filter config init params
    */
    private static Map<Role, List<String>> accessMap = new HashMap<>();
    private static List<String> joint = new ArrayList<>();
    private static List<String> getMethodDisallowed = new ArrayList<>();
    private static List<String> allowedAnywhere = new ArrayList<>();

    // Initializing collections desribed above
     @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.debug("Access filter init started");

        accessMap.put(Role.MANAGER, asList(filterConfig.getInitParameter("manager")));
        accessMap.put(Role.CLIENT, asList(filterConfig.getInitParameter("client")));
        accessMap.put(Role.REPAIRER, asList(filterConfig.getInitParameter("repairer")));

        joint = asList(filterConfig.getInitParameter("joint"));
        getMethodDisallowed = asList(filterConfig.getInitParameter("get-method-disallowed"));
        allowedAnywhere = asList(filterConfig.getInitParameter("allowed-anywhere"));

        logger.debug("Access filter init successfully ended");
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        if(accessAllowed(req)){
            filterChain.doFilter(req, resp);
        }else{
            logger.trace("Access to command '" + req.getParameter("command") + "' disallowed. "
                    + "Forwarding to error page");
            req.getRequestDispatcher(Paths.JSP__ERROR).forward(req, resp);
        }
    }

    /**
     * Checking if requested action is allowed for user
     * that did this request
     *
     * @param req ServletRequest instance
     *
     * @return boolean value telling if action is accessible or not
    */
    private boolean accessAllowed(ServletRequest req){
        HttpServletRequest httpRequest = (HttpServletRequest)req;
        String commandName = req.getParameter("command");

        // If command is null, empty or does not exists - dismiss access
        if(commandName == null || commandName.isEmpty() || CommandContainer.getCommand(commandName) == null){
            req.setAttribute("errorKey", "bad_request");
            return false;
        }

        /*
         * If requested action doesn`t need logging in, or accessible for all roles
         * then give access
         */
        if(allowedAnywhere.contains(commandName)){
            return true;
        }

        // Checking if request Method is GET and command is disallowed for this method
        if(httpRequest.getMethod().equals("GET") && getMethodDisallowed.contains(commandName)){
            req.setAttribute("errorKey", "get_disallowed");
            logger.trace("Requested command '" + commandName + "' has disallowed access via GET");
            return false;
        }else if(httpRequest.getMethod().equals("POST") && getMethodDisallowed.contains(commandName)){
            return true;
        }

        HttpSession session = httpRequest.getSession(false);
        if(session == null){
            logger.trace("Session is null. No access to command '" + commandName + "'");
            return false;
        }

        /*
         * If user not logged in - disallow access
         * else if he is logged it and requesting action allowed for anyone
         * then give access
        */
        Role role = (Role)session.getAttribute("role");
        if(role == null){
            logger.trace("Unlogged user tried to access command '" + commandName + "'");
            req.setAttribute("errorKey", "user_not_logged");
            return false;
        }else if(joint.contains(commandName)){
            return true;
        }

        if(accessMap.get(role).contains(commandName)){
            return true;
        }else{
            req.setAttribute("errorKey", "no_permission_to_resource");
            return false;
        }
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
