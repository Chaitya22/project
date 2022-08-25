package com.homeloan.project.mailSender;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(String toEmail,String body,String subject) {
        try {
			SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom("springemail97@gmail.com");
            message.setTo(toEmail);
            message.setText(body);
            message.setSubject(subject);

            mailSender.send(message);
            log.info("Mail Sent...");
        } catch (Exception e) {
            log.error("Error while sending mail");
        }
    }
}
