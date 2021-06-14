package com.filiaiev.agency.web.command.joint;

import com.filiaiev.agency.database.dao.ClientDAO;
import com.filiaiev.agency.entity.Client;
import com.filiaiev.agency.entity.Role;
import com.filiaiev.agency.web.command.Command;
import com.filiaiev.agency.web.command.CommandContainer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

// Servlet whose task is to change application locale
public class SetLocaleCommand implements Command {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String locale = req.getParameter("lang");
        HttpSession session = req.getSession();
        session.setAttribute("lang", locale);

        /*
        * If passed to the request locale is absent at context locales
        * set it by default to 'uk'
        */
        if(!((List<String>)req.getServletContext().getAttribute("locales"))
                .contains(locale)){
            Config.set(session, Config.FMT_LOCALE, new Locale("uk"));
        }else{
            Config.set(session, Config.FMT_LOCALE, new Locale(locale));
        }

        // If current user role is client --> update his preferable locale
        Role role = (Role)session.getAttribute("role");
        if(role == Role.CLIENT){
            Client client = (Client)session.getAttribute("client");
            new ClientDAO().updatePreferredLocaleById(client.getId(), locale);
        }

        // Moving to home page if user is logged in, and to main page, if not
        return req.getSession().getAttribute("user") == null ? "/"
                : CommandContainer.getCommand(CommandContainer.TO_HOME).execute(req, resp);
    }
}
