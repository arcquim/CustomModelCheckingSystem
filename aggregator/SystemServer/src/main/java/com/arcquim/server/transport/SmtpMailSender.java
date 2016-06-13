package com.arcquim.server.transport;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class SmtpMailSender implements MailSender {
    
    private static final String USERNAME = "modelcheckingsystem@gmail.com";
    private static final String PASSWORD = ">Mg4639Vl.vE<tkltU>uG-eo*e[EJ7";
    
    private static final String SMTP_AUTH_KEY = "mail.smtp.auth";
    private static final String SMTH_AUTH_VALUE = "true";
    
    private static final String SMTP_STARTTLS_KEY = "mail.smtp.starttls.enable";
    private static final String SMTP_STARTTLS_VALUE = "true";
    
    private static final String SMTP_HOST_KEY = "mail.smtp.host";
    private static final String SMTP_HOST_VALUE = "smtp.gmail.com";
    
    private static final String SMTP_PORT_KEY = "mail.smtp.port";
    private static final String SMTP_PORT_VALUE = "587";
    
    private static final Properties EMAIL_CONFIGURATION;
    
    static {
        EMAIL_CONFIGURATION = new Properties();
        EMAIL_CONFIGURATION.put(SMTP_AUTH_KEY, SMTH_AUTH_VALUE);
        EMAIL_CONFIGURATION.put(SMTP_STARTTLS_KEY, SMTP_STARTTLS_VALUE);
        EMAIL_CONFIGURATION.put(SMTP_HOST_KEY, SMTP_HOST_VALUE);
        EMAIL_CONFIGURATION.put(SMTP_PORT_KEY, SMTP_PORT_VALUE);
    }

    @Override
    public void sendEmail(String address, String subject, String body) {
        try {
            Session session = createSession();
            Message message = new MimeMessage(session);
            message.setFrom();
            message.setRecipients(Message.RecipientType.TO, 
                    InternetAddress.parse(address));
            message.setSubject(subject);
            message.setText(body);
            
            Transport.send(message);
        }
        catch (MessagingException ex) {
            
        }
    }
    
    private Session createSession() {
        Session session = Session.getInstance(EMAIL_CONFIGURATION,
		  new Authenticator() {
                        
                        @Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(USERNAME, PASSWORD);
			}
		  });
        return session;
    }
    
}
