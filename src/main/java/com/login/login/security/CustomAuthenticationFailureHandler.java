package com.login.login.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by giuseppe on 31/08/17.
 */

@Component("authenticationFailureHandler")
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleResolver localeResolver;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        setDefaultFailureUrl("/login?error=true");

        super.onAuthenticationFailure(request, response, exception);

        Locale locale = localeResolver.resolveLocale(request);

        String errorMessage = messageSource.getMessage("message.badCredentials", null, locale);


        if (exception.getMessage().equalsIgnoreCase("blocked")) {
            errorMessage = messageSource.getMessage("auth.message.blocked", null, locale);
        }
        else if (exception.getMessage().equalsIgnoreCase("User is disabled")){
            errorMessage = messageSource.getMessage("auth.message.disabled", null, locale);
        }
        else if (exception.getMessage().equalsIgnoreCase("User account has expired")){
            errorMessage = messageSource.getMessage("auth.message.expired", null, locale);
        }
        else if (exception.getMessage().equalsIgnoreCase("blocked")){
            errorMessage = messageSource.getMessage("auth.message.blocked", null, locale);
        }

        request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, errorMessage);
    }

}
