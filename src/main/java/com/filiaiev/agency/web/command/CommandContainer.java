package com.filiaiev.agency.web.command;

import com.filiaiev.agency.web.command.client.CreateOrderCommand;
import com.filiaiev.agency.web.command.manager.*;
import com.filiaiev.agency.web.command.manager.moveto.GoToClientInfoPage;
import com.filiaiev.agency.web.command.repairer.SetOrderStatusCommand;
import com.filiaiev.agency.web.command.client.CreateCommentCommand;
import com.filiaiev.agency.web.command.client.PayCommand;
import com.filiaiev.agency.web.command.client.moveto.GoToHomePage;

import java.util.Map;
import java.util.TreeMap;

public class CommandContainer {

    private static Map<String, Command> commands = new TreeMap<>();

    static{
        commands.put("register", new RegisterCommand());
        commands.put("login", new LogInCommand());
        commands.put("logout", new LogOutCommand());

        commands.put("createOrder", new CreateOrderCommand());
        commands.put("getOrders", new GetOrdersCommand());
        commands.put("getOrderInfo", new GetOrderInfoCommand());

        //go_to
        commands.put("goToHome", new GoToHomePage());

        //user
        commands.put("createComment", new CreateCommentCommand());
        commands.put("clientPay", new PayCommand());

        //manager
        commands.put("setPayment", new SetPaymentCommand());
        commands.put("setRepairer", new SetRepairerCommand());
        commands.put("setClientCash", new SetClientCashCommand());

        commands.put("filterBy", new FilterByCommand());
        commands.put("sortOrders", new SortOrdersCommand());

        commands.put("goToClientInfo", new GoToClientInfoPage());

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