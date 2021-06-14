package com.filiaiev.agency.web.listener;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ContextListener implements ServletContextListener {

    private static final Logger logger = Logger.getLogger(ContextListener.class);

    // Initializing context
    public void contextInitialized(ServletContextEvent sce) {
        logger.debug("Context initialization started");
        ServletContext servletContext = sce.getServletContext();
        log4jInit(servletContext);
        commandContainerInit(servletContext);
        i18nInit(servletContext);
        logger.debug("Context initialization successfully finished");
    }

    // Initializing commandContainer class obviously
    private void commandContainerInit(ServletContext servletContext){
        logger.debug("Command container init started");
        try{
            Class.forName("com.filiaiev.agency.web.command.CommandContainer");
        }catch (ClassNotFoundException e){
            logger.fatal("Command container class not found");
            throw new Error(e);
        }
        logger.debug("Command container init successfully finished");
    }

    // Initializing i18n part (avaliable locales)
    private void i18nInit(ServletContext servletContext){
        logger.debug("i18n init started");
        String localeNames = servletContext.getInitParameter("locales");
        if (localeNames == null || localeNames.isEmpty()) {
            logger.debug("'locales' init parameter is empty, the default encoding will be used");
        } else {
            List<String> locales = new ArrayList<String>();
            StringTokenizer st = new StringTokenizer(localeNames);
            while (st.hasMoreTokens()) {
                String localeName = st.nextToken();
                locales.add(localeName);
            }

            logger.trace("Application attribute set: locales --> " + locales);
            servletContext.setAttribute("locales", locales);
            logger.debug("i18n init successfully finished");
        }
    }

    // Initializing logger
    private void log4jInit(ServletContext servletContext){
        logger.debug("Log4J initialization started");
        PropertyConfigurator.configure(servletContext.getRealPath("/WEB-INF/log4j.properties"));
        logger.debug("Log4J initialization successfully finished");
    }

    public void contextDestroyed(ServletContextEvent sce) {
        logger.debug("Context destroyed");
    }
}
