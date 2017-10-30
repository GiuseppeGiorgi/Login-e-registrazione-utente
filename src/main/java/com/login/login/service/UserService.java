package com.login.login.service;

import com.login.login.domain.User;
import com.login.login.domain.VerificationToken;
import com.login.login.web.dto.UserDto;
import com.login.login.web.error.UserAlreadyExistException;

/**
 * Created by giuseppe on 23/08/17.
 */
public interface UserService {

    User registerNewUserAccount(UserDto accountDto) throws UserAlreadyExistException;

    void createVerificationTokenForUser(User user, String token);

    VerificationToken getVerificationToken(String verificationToken);

    void saveRegisteredUser(User user);

    User getUser(String verificationToken);

    User findUserByEmail(String email);

}