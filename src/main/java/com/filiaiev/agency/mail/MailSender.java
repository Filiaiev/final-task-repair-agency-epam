package com.filiaiev.agency.mail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;

public class MailSender {

    private static final Properties properties;
    private static final Session session;

    // Loading properites, describing mail config in order to initialize javax.mail.Session
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

    /**
     * Sending message after preparing it
     *
     * @see #prepareMessage(Session, String, String, String)
     */
    public static void sendEmail(String recipient, String subject, String text) throws MessagingException {
        Message message = prepareMessage(session, recipient, subject, text);
        Transport.send(message);
    }

    /**
     * Initializing Message instance
     * Setting content type to text/html in order to
     * use html <a> tag to describe profile link
     *
     * @param session   initialized javax.mail.Session
     * @param recipient recipient e-mail address
     * @param subject   subject of the email
     * @param text      body of the email
     *
     * @return ready Message instance
     */
    private static Message prepareMessage(Session session, String recipient, String subject, String text) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(properties.getProperty("mail.login")));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        message.setSubject(subject);
        message.setContent(text, "text/html; charset=windows-1251");
        return message;
    }
}