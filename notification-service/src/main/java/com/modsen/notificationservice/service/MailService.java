package com.modsen.notificationservice.service;

public interface MailService {

    void sendEmail(String subject, String to, String emailContent);
}
