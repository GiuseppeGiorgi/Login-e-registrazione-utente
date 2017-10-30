package com.login.login.registration;

import com.login.login.domain.User;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

/**
 * Created by giuseppe on 28/08/17.
 */
public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private String appUrl;
    private Locale locale;
    private User user;

    public OnRegistrationCompleteEvent( User user, Locale locale, String appUrl) {
        super(user);
        this.appUrl = appUrl;
        this.locale = locale;
        this.user = user;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public Locale getLocale() {
        return locale;
    }

    public User getUser() {
        return user;
    }
}
