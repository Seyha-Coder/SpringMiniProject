package org.example.springminiproject.Service.MailService;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSenderService {
    private JavaMailSender javaMailSender;

    public void sendEmail(String toEmail, String otp ) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your OTP for Email Verification");
        message.setText("Your OTP is: " + otp);
        javaMailSender.send(message);
    }
}
