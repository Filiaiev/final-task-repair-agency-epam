package com.filiaiev.agency.web.command;

import com.filiaiev.agency.web.command.client.*;
import com.filiaiev.agency.web.command.joint.HomePage;
import com.filiaiev.agency.web.command.joint.SetLocaleCommand;
import com.filiaiev.agency.web.command.joint.SetOrderStatusCommand;
import com.filiaiev.agency.web.command.joint.ToHomePageCommand;
import com.filiaiev.agency.web.command.auth.LogInCommand;
import com.filiaiev.agency.web.command.auth.LogOutCommand;
import com.filiaiev.agency.web.command.joint.order.GetOrderInfoCommand;
import com.filiaiev.agency.web.command.joint.order.GetOrdersCommand;
import com.filiaiev.agency.web.command.manager.*;

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
        commands.put("toHome", new ToHomePageCommand());
        commands.put("homePage", new HomePage());
        commands.put("toProfile", new ToProfileCommand());
        commands.put("toRegistration", new ToRegistrationCommand());

        //bundle
        commands.put("setLocale", new SetLocaleCommand());

        //client
        commands.put("createComment", new CreateCommentCommand());
        commands.put(clientPayCmd, new PayCommand());
        commands.put("toCreationForm", new ToCreationFormCommand());

        //manager
        commands.put("setPayment", new SetPaymentCommand());
        commands.put("setRepairer", new SetRepairerCommand());
        commands.put("setClientCash", new SetClientCashCommand());

        //filtering
        // NONE

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
