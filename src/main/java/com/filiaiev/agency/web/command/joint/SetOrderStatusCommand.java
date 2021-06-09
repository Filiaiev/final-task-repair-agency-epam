package com.filiaiev.agency.web.command.joint;

import com.filiaiev.agency.database.dao.ClientDAO;
import com.filiaiev.agency.database.dao.OrderDAO;
import com.filiaiev.agency.database.dao.UserDAO;
import com.filiaiev.agency.entity.Client;
import com.filiaiev.agency.entity.Order;
import com.filiaiev.agency.entity.OrderStatus;
import com.filiaiev.agency.mail.MailSender;
import com.filiaiev.agency.web.command.Command;
import com.filiaiev.agency.web.command.CommandContainer;
import org.apache.log4j.Logger;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class SetOrderStatusCommand implements Command {

    private static Logger logger = Logger.getLogger(SetOrderStatusCommand.class);

    // No validation because get-method is disallowed in web.xml
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int statusId = Integer.parseInt(req.getParameter("statusId"));
        int orderId = Integer.parseInt(req.getParameter("orderId"));

        OrderDAO orderDAO = new OrderDAO();
        Order order = orderDAO.getOrderByOrderId(orderId);
        OrderStatus[] statusValues = OrderStatus.values();
        orderDAO.updateStatusById(statusValues[statusId], orderId);

        Map<String, String> orderInfo = (Map<String, String>)req.getSession().getAttribute("orderInfo");

        if(statusValues[statusId] == OrderStatus.COMPLETED ||
                statusValues[statusId] == OrderStatus.WAITING_FOR_PAYMENT){
            Client client = new ClientDAO().getClientById(order.getClientId());
            String clientEmail = new UserDAO().getUserByOrderClientId(client.getId()).getEmail();
            try {
                orderStatusEmailNotify(statusValues[statusId], client, orderInfo);
                logger.trace("Email notification has been sent to --> "
                    + clientEmail + "... status(" + statusValues[statusId] + ")");
            } catch (MessagingException e) {
                logger.error("Cannot send status update notification to email '"
                    + clientEmail + "'", e);
            }
        }

        logger.info("Order #" + orderId + " status updated --> (" + statusValues[statusId] + ")");

        return "/controller?command=" + CommandContainer.GET_ORDER_INFO
                + "&orderId=" + orderId;
    }

    private void orderStatusEmailNotify(OrderStatus status, Client client,
                                        Map<String, String> orderInfo) throws MessagingException {
        String subject = null;
        String text = null;

        ResourceBundle rb = ResourceBundle.getBundle("i18n.app", new Locale(client.getPreferredLocale()));
        int orderId = Integer.parseInt(orderInfo.get("id"));
        switch (status){
            case COMPLETED:
                subject = String.format(rb.getString("email.message.complete.subject"), orderId);
                text = String.format(rb.getString("email.message.complete.text"),
                        orderInfo.get("clientLastName") + " " + orderInfo.get("clientFirstName"));
                break;
            case WAITING_FOR_PAYMENT:
                subject = String.format(rb.getString("email.message.waiting_for_payment_subject"), orderId);
                text = String.format(rb.getString("email.message.waiting_for_payment_text"),
                        orderInfo.get("clientLastName") + " " + orderInfo.get("clientFirstName"));
                break;
        }
        MailSender.sendEmail(new UserDAO().getUserByOrderClientId(client.getId()).getEmail(), subject, text);
    }
}
