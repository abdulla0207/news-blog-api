package com.company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public MailService(JavaMailSender javaMailSender){
        this.javaMailSender=javaMailSender;
    }

    public void sendMail(String toAccount, String subject, String text){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("UzNewsBlog");
        mailMessage.setTo(toAccount);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);

        javaMailSender.send(mailMessage);
    }
}
