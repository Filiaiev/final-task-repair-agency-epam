package com.filiaiev.agency.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Context initialization start");
        ServletContext servletContext = sce.getServletContext();
        commandContainerInit(servletContext);
        System.out.println("Context initialization finish");
    }

    public void commandContainerInit(ServletContext servletContext){
        try{
            Class.forName("com.filiaiev.agency.web.command.CommandContainer");
        }catch (ClassNotFoundException e){
            throw new RuntimeException(e);
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Context destroyed");
    }
}
