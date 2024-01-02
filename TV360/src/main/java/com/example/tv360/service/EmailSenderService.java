package com.example.tv360.service;

import com.example.tv360.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail, User user) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("thaonxth2108014@fpt.edu.vn");
        message.setTo(toEmail);
        message.setSubject("Stay Updated with Our Latest News!");
        message.setText("Dear " + user.getFullName() + ",\n\n" +
                "Thank you for subscribing to receive the latest updates and news from us. We're thrilled to have you on board!\n" +
                "You'll now be among the first to know about our latest products, promotions, and exciting announcements.\n\n" +
                "If you have any questions or if there's anything specific you'd like to hear more about, feel free to let us know.\n\n" +
                "Best regards,\n" +
                "Media360 Team");

        mailSender.send(message);
    }
}
