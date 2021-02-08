package com.example.proekt.service.impl;

import com.example.proekt.model.Contact;
import com.example.proekt.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@Service
public class MailServiceImp implements MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void send(String fromAddress, String toAddress, String subject, String content) throws MessagingException {

        MimeMessage mimeMessage=javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage,true);
        mimeMessageHelper.setFrom(fromAddress);
        mimeMessageHelper.setTo(toAddress);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(content,true);
        mimeMessageHelper.setSentDate(new Date());
        javaMailSender.send(mimeMessage);
    }
}
