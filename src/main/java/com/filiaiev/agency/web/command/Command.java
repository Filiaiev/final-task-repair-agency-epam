package com.filiaiev.agency.web.command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Command {

    /**
     * Processing user request
     *
     * @return String path to move after request processing
    */
    String execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException;
}
