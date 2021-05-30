package com.filiaiev.agency.web.command.manager;

import com.filiaiev.agency.database.dao.ClientDAO;
import com.filiaiev.agency.entity.Client;
import com.filiaiev.agency.web.command.Command;
import com.filiaiev.agency.web.command.CommandContainer;
import com.filiaiev.agency.web.util.Paths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

public class SetClientCashCommand implements Command {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int clientId = Integer.parseInt(req.getParameter("clientId"));
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(req.getParameter("cashValue")));

        ClientDAO clientDAO = new ClientDAO();
        Client client = clientDAO.getClientById(clientId);

        amount = client.getCash().add(amount);
        clientDAO.setClientCashById(amount, clientId);
        client.setCash(amount);
//        req.setAttribute("clientId", clientId);
        req.getSession().setAttribute("clientInfo", client);
//        resp.sendRedirect(Paths.JSP__CLIENT_INFO);
//        return null;
//        String forward =  CommandContainer.getCommand("goToClientInfo").execute(req, resp);
//        req.getRequestDispatcher(forward).forward(req, resp);
        return "/controller?command=" + CommandContainer.goToClientInfoCmd +
                "&clientId=" + clientId;
    }
}
