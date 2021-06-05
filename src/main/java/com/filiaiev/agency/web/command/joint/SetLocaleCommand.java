package com.filiaiev.agency.web.command.joint;

import com.filiaiev.agency.web.command.Command;
import com.filiaiev.agency.web.command.CommandContainer;
import com.filiaiev.agency.web.util.Paths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SetLocaleCommand implements Command {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String locale = req.getParameter("lang");
        if(!((List<String>)req.getServletContext().getAttribute("locales"))
                .contains(locale)){
            Config.set(req.getSession(), Config.FMT_LOCALE, new Locale("uk"));
        }else{
            Config.set(req.getSession(), Config.FMT_LOCALE, new Locale(locale));
        }
        return req.getSession().getAttribute("user") == null ? "/"
                : CommandContainer.getCommand("toHome").execute(req, resp);
    }
}
