package com.filiaiev.agency.mail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

public class MailSender {

    private static Properties properties;
    private static Session session;

    static {
        properties = new Properties();
        try {
            properties.load(Service.class.getResourceAsStream("/mail.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        properties.getProperty("mail.login"),
                        properties.getProperty("mail.password"));
            }
        });
    }

    public static void sendEmail(String recipient, String subject, String text) throws MessagingException {
        Message message = prepareMessage(session, recipient, subject, text);
        Transport.send(message);
    }

    private static Message prepareMessage(Session session, String recipient, String subject, String text) throws MessagingException{
        Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(properties.getProperty("mail.login")));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subject);
            message.setContent(text, "text/html; charset=windows-1251");
            return message;
    }
}