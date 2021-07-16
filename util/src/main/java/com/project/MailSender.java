package com.project;

import com.project.exception.MessageSenderException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

@Slf4j
public class MailSender {

    @Value("${email}")
    private String sendingEmail;

    @Value("${password}")
    private String password;

    @Value("${mail.smtp.host}")
    private String host;

    @Value("${mail.smtp.port}")
    private String port;

    @Value("${mail.smtp.auth}")
    private String auth;

    @Value("${mail.smtp.socketFactory.port}")
    private String socketFactoryPort;

    @Value("${mail.smtp.socketFactory.class}")
    private String socketFactoryClass;


    public void send(String email, String text, String title)  {

        Properties prop = new Properties();
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.auth", auth);
        prop.put("mail.smtp.socketFactory.port", socketFactoryPort);
        prop.put("mail.smtp.socketFactory.class", socketFactoryClass);

        Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sendingEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sendingEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(title);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(text, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);
            Transport.send(message);
        } catch (MessagingException e){
            log.error("Message can't send " + e.getMessage());
            throw new MessageSenderException(e);
        }
    }
}


