package com.filiaiev.agency.web.command.client;

import com.filiaiev.agency.web.command.Command;
import com.filiaiev.agency.web.util.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ToCreationFormCommand implements Command {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        return Paths.JSP__CREATION_FORM;
    }
}
