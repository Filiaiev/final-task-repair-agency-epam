package com.filiaiev.agency.web.command;

import com.filiaiev.agency.web.command.client.*;
import com.filiaiev.agency.web.command.joint.SetLocaleCommand;
import com.filiaiev.agency.web.command.joint.SetOrderStatusCommand;
import com.filiaiev.agency.web.command.joint.ToHomePageCommand;
import com.filiaiev.agency.web.command.auth.LogInCommand;
import com.filiaiev.agency.web.command.auth.LogOutCommand;
import com.filiaiev.agency.web.command.order.GetOrderInfoCommand;
import com.filiaiev.agency.web.command.order.GetOrdersCommand;
import com.filiaiev.agency.web.command.manager.*;

import java.util.HashMap;
import java.util.Map;

public abstract class CommandContainer {

    private static Map<String, Command> commands = new HashMap<>();

    // Auth
    public static final String REGISTER = "register";
    public static final String LOGIN = "login";
    public static final String LOGOUT = "logOut";

    // Orders
    public static final String GET_ORDER_INFO = "getOrderInfo";
    public static final String GET_ORDERS = "getOrders";

    // Go to
    public static final String TO_HOME = "toHome";
    public static final String TO_CLIENT_INFO = "toClientInfo";
    public static final String TO_PROFILE = "toProfile";
    public static final String TO_CREATION_FORM = "toCreationForm";

    public static final String SET_LOCALE = "setLocale";

    public static final String CREATE_COMMENT = "createComment";
    public static final String CREATE_ORDER = "createOrder";

    public static final String CLIENT_PAY = "clientPay";

    // Setters
    public static final String SET_REPAIRER = "setRepairer";
    public static final String SET_PAYMENT = "setPayment";
    public static final String SET_CLIENT_CASH = "setClientCash";
    public static final String SET_ORDER_STATUS = "setOrderStatus";

    public static final String WRONG_COMMAND = "wrongCommand";

    static{
        commands.put(REGISTER, new RegisterCommand());
        commands.put(LOGIN, new LogInCommand());
        commands.put(LOGOUT, new LogOutCommand());

        commands.put(CREATE_ORDER, new CreateOrderCommand());
        commands.put(GET_ORDERS, new GetOrdersCommand());
        commands.put(GET_ORDER_INFO, new GetOrderInfoCommand());

        // Go to
        commands.put(TO_HOME, new ToHomePageCommand());
        commands.put(TO_PROFILE, new ToProfileCommand());
        commands.put(TO_CREATION_FORM, new ToCreationFormCommand());
        commands.put(TO_CLIENT_INFO, new ShowClientInfoCommand());

        //bundle
        commands.put(SET_LOCALE, new SetLocaleCommand());

        //client
        commands.put(CREATE_COMMENT, new CreateCommentCommand());
        commands.put(CLIENT_PAY, new PayCommand());

        //manager
        commands.put(SET_PAYMENT, new SetPaymentCommand());
        commands.put(SET_REPAIRER, new SetRepairerCommand());
        commands.put(SET_CLIENT_CASH, new SetClientCashCommand());
        commands.put("generateReport", new GenerateReportCommand());

        commands.put(SET_ORDER_STATUS, new SetOrderStatusCommand());

        //helper
        commands.put(WRONG_COMMAND, new WrongCommand());
    }

    public static Command getCommand(String commandName){
        if(commandName == null || !commands.containsKey(commandName)){
            return commands.get(WRONG_COMMAND);
        }
        return commands.get(commandName);
    }

}
