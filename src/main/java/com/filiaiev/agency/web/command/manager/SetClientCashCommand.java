package com.filiaiev.agency.web.command.manager;

import com.filiaiev.agency.database.dao.ClientDAO;
import com.filiaiev.agency.entity.Client;
import com.filiaiev.agency.entity.User;
import com.filiaiev.agency.web.command.Command;
import com.filiaiev.agency.web.command.CommandContainer;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

public class SetClientCashCommand implements Command {

    private static Logger logger = Logger.getLogger(SetClientCashCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int clientId = Integer.parseInt(req.getParameter("clientId"));
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(req.getParameter("cashValue")));

        ClientDAO clientDAO = new ClientDAO();
        Client client = clientDAO.getClientById(clientId);

        // amountToAdd passing to logger
        BigDecimal amountToAdd = amount;
        amount = client.getCash().add(amount);
        clientDAO.setClientCashById(amount, clientId);
        client.setCash(amount);

        req.getSession().setAttribute("clientInfo", client);

        logger.info("Client`s #" + client.getId() + " cash has been changed by "
                + amountToAdd + " by manager (" + ((User)req.getSession().getAttribute("user")).getLogin() + ")");

        return "/controller?command=" + CommandContainer.TO_CLIENT_INFO +
                "&clientId=" + clientId;
    }
}
