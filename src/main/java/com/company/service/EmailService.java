package com.company.service;

import com.company.exception.EmailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService implements EmailSender{
    private final JavaMailSender javaMailSender;
    private final ResourceMessageService resourceMessageService;

    @Autowired
    public EmailService(JavaMailSender javaMailSender, ResourceMessageService resourceMessageService){
        this.javaMailSender=javaMailSender;
        this.resourceMessageService = resourceMessageService;
    }

    @Override
    @Async
    public void sendMail(String toAccount, String text, String lang){
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
            mimeMessageHelper.setText(text);
            mimeMessageHelper.setTo(toAccount);
            mimeMessageHelper.setSubject("[News Blog] Confirm Your Email");
            mimeMessageHelper.setFrom("abdulla.ermatov0407@gmail.com");
            javaMailSender.send(mimeMessage);
        }catch (MessagingException e){
            log.warn("Email not send {}", toAccount);
            throw new EmailException(resourceMessageService.getMessage("email.notification", lang));
        }
    }
}
