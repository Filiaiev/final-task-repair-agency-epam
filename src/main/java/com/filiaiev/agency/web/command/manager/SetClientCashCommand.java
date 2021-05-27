package com.filiaiev.agency.web.command.manager;

import com.filiaiev.agency.database.dao.ClientDAO;
import com.filiaiev.agency.web.command.Command;
import com.filiaiev.agency.web.command.CommandContainer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

public class SetClientCashCommand extends Command {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int clientId = Integer.parseInt(req.getParameter("clientId"));
        BigDecimal clientCash = BigDecimal.valueOf(Double.parseDouble(req.getParameter("cashValue")));
        new ClientDAO().updateClientCashById(clientCash, clientId);
        req.setAttribute("clientId", clientId);
        return CommandContainer.getCommand("goToClientInfo").execute(req, resp);
    }
}
