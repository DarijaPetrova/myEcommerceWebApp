package com.example.proekt.service;

import com.example.proekt.model.Contact;

import javax.mail.MessagingException;

public interface MailService {

    void send(String fromAddress, String toAddress, String subject, String content) throws MessagingException;

}
