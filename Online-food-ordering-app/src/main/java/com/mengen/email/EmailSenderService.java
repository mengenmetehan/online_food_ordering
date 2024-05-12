package com.mengen.email;

import com.mengen.service.impl.CategoryServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaEmailSender;

@Service
public class EmailSenderService {

    @Autowired
    private JavaEmailSender emailSender;

    @Autowired
    private static final Logger LOG = LogManager.getLogger(CategoryServiceImpl.class);

    public void sendEmail(String to, String subject, String body) {
        SimpleEmail email = new SimpleEmail();
        email.setHostName("smtp.gmail.com");
        email.setSmtpPort(587);
        email.setAuthenticator(new DefaultAuthenticator("metehan1007@gmail", "gbjvfiblqexsodhv"));
        email.setSSLOnConnect(true);
        email.setFrom("metehan1007@gmail");
        email.setSubject(subject);
        email.setMsg(body);
        email.addTo(to);
        try {
            email.send();
            LOG.info("Email sent successfully to: " + to);
        } catch (Exception e) {
            LOG.error("Email could not be sent to: " + to);
            e.printStackTrace();
        }
    }



}
