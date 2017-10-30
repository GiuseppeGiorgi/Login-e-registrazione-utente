package com.login.login.web.controller;

import com.login.login.domain.User;
import com.login.login.domain.VerificationToken;
import com.login.login.registration.OnRegistrationCompleteEvent;
import com.login.login.service.UserService;
import com.login.login.web.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;



import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.Locale;



/**
 * Created by giuseppe on 22/08/17.
 */
@Controller
public class RegistrationController {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private MessageSource messageSource;




    @RequestMapping(value = "/user/registrazione", method = RequestMethod.GET)
    public String showRegistrationForm(HttpServletRequest request, Model model){

        LOGGER.debug("Rendering restritation page");
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "registrazione";
    }

    @RequestMapping(value = "user/registrazione", method = RequestMethod.POST)
    public ModelAndView registerUserAccount(@ModelAttribute ("user") @Valid UserDto userDto, HttpServletRequest request, Errors errors){
        LOGGER.debug("Registering user account with information: {}", userDto);


        User registered = userService.registerNewUserAccount(userDto);

        if (registered == null){
            return new ModelAndView("registrazione", "user", userDto);
        }

        try{
            final String appUrl = request.getContextPath();

            applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), appUrl));

        }
        catch (Exception e){
            LOGGER.warn("Unable to register user", e);
            return new ModelAndView("emailError", "user", userDto);
        }

        return new ModelAndView("successRegister", "user", userDto);

    }

    @RequestMapping(value = "/confermaRegistrazione", method = RequestMethod.GET)
    public String confirmRegistration(HttpServletRequest request, Model model, @RequestParam("token") String token){

        Locale locale = request.getLocale();

        VerificationToken verificationToken = userService.getVerificationToken(token);

        if (verificationToken == null) {

            String message = messageSource.getMessage("auth.message.invalidToken", null, locale);
            model.addAttribute("mesage", message);
            return "redirect:/badUser.html?lang=" + locale.getLanguage();
        }

        User user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if (verificationToken.getExpiryDate().getTime() - calendar.getTime().getTime() <= 0){

            model.addAttribute("message", messageSource.getMessage("auth.message.expired", null, locale));
            model.addAttribute("expired", true);
            model.addAttribute("token", token);
            return "redirect:/badUser.html?lang=\" + locale.getLanguage()";
        }

        user.setEnabled(true);
        userService.saveRegisteredUser(user);


        model.addAttribute("message", messageSource.getMessage("message.accountVerified", null, locale));
        return "redirect:/login?lang=" + locale.getLanguage();
    }

    @RequestMapping(value = "/personale", method = RequestMethod.GET)
    public String paginaPersonale(){
        return "personale";
    }

}
