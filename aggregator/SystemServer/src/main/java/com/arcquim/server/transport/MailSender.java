package com.arcquim.server.transport;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public interface MailSender {
    
    void sendEmail(String address, String subject, String body);
    
}
