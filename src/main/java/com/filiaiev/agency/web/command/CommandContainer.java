package com.filiaiev.agency.web.command;

import com.filiaiev.agency.web.command.client.CreateOrderCommand;
import com.filiaiev.agency.web.command.client.GoToCreationFormCommand;
import com.filiaiev.agency.web.command.manager.*;
import com.filiaiev.agency.web.command.client.CreateCommentCommand;
import com.filiaiev.agency.web.command.client.PayCommand;
import com.filiaiev.agency.web.command.client.moveto.GoToHomePage;

import java.util.Map;
import java.util.TreeMap;

public class CommandContainer {

    private static Map<String, Command> commands = new TreeMap<>();
    public static final String getOrderInfoCmd = "getOrderInfo";
    public static final String goToClientInfoCmd = "goToClientInfo";
    public static final String clientPayCmd = "clientPay";

    static{
        commands.put("register", new RegisterCommand());
        commands.put("login", new LogInCommand());
        commands.put("logout", new LogOutCommand());

        commands.put("createOrder", new CreateOrderCommand());
        commands.put("getOrders", new GetOrdersCommand());
        commands.put(getOrderInfoCmd, new GetOrderInfoCommand());

        //go_to
        commands.put("goToHome", new GoToHomePage());

        //client
        commands.put("createComment", new CreateCommentCommand());
        commands.put(clientPayCmd, new PayCommand());
        commands.put("goToCreationForm", new GoToCreationFormCommand());

        //manager
        commands.put("setPayment", new SetPaymentCommand());
        commands.put("setRepairer", new SetRepairerCommand());
        commands.put("setClientCash", new SetClientCashCommand());

        commands.put("filterBy", new FilterByCommand());
        commands.put("sortOrders", new SortOrdersCommand());

        commands.put(goToClientInfoCmd, new ShowClientInfoCommand());

        //repairer
        commands.put("setOrderStatus", new SetOrderStatusCommand());

        //helper
        commands.put("wrongCommand", new WrongCommand());
    }

    public static Command getCommand(String commandName){
        if(commandName == null || !commands.containsKey(commandName)){
            return commands.get("no_command");
        }
        return commands.get(commandName);
    }

}
