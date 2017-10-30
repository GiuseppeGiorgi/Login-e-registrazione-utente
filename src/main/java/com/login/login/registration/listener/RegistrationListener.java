package com.login.login.registration.listener;

import com.login.login.domain.User;
import com.login.login.registration.OnRegistrationCompleteEvent;
import com.login.login.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


import java.util.UUID;


/**
 * Created by giuseppe on 28/08/17.
 */

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Environment env;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {

        this.confirmRegistration(event);
    }


    private void confirmRegistration(OnRegistrationCompleteEvent event){
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationTokenForUser(user,token);

        SimpleMailMessage email = constructEmailMessage(event, user, token);
        javaMailSender.send(email);

    }

    private SimpleMailMessage constructEmailMessage(OnRegistrationCompleteEvent event, User user, String token){



        String recipientAddress = user.getEmail();
        String subject = "Conferma registrazione";
        String confirmationUrl = event.getAppUrl() + "/confermaRegistrazione.html?token=" + token;
        String message = messageSource.getMessage("message.regSucc", null, event.getLocale());
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + " \r\n" + confirmationUrl);
        email.setFrom(env.getProperty("support.email"));
        return email;

    }
}
