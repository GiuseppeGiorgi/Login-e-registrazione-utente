package com.login.login.web.error;

/**
 * Created by giuseppe on 23/08/17.
 */
public class UserAlreadyExistException extends RuntimeException {

    private static final Long serialVersionUID = 5861310537366287163L;

    public UserAlreadyExistException() {
        super();
    }

    public UserAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyExistException(String message) {
        super(message);
    }

    public UserAlreadyExistException(Throwable cause) {
        super(cause);
    }
}
