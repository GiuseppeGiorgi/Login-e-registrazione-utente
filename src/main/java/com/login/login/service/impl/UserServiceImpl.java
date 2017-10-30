package com.login.login.service.impl;

import com.login.login.domain.User;


import com.login.login.domain.VerificationToken;
import com.login.login.domain.repository.RoleRepository;
import com.login.login.domain.repository.UserRepository;
import com.login.login.domain.repository.VerificationTokenRepository;
import com.login.login.service.UserService;
import com.login.login.web.dto.UserDto;
import com.login.login.web.error.UserAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.Arrays;


/**
 * Created by giuseppe on 23/08/17.
 */

@Service
@Transactional
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;



    @Override
    public User registerNewUserAccount(UserDto accountDto) throws UserAlreadyExistException {

        if (emailExist(accountDto.getEmail())){
            throw new UserAlreadyExistException("esiste gia un account con questo indirizzo email" + accountDto.getEmail());
        }

        User user = new User();

        user.setFirstName(accountDto.getFirstName());
        user.setLastName(accountDto.getLastName());
        user.setEmail(accountDto.getEmail());
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));

        return userRepository.save(user);
    }

    @Override
    public void createVerificationTokenForUser(User user, String token) {

        VerificationToken mytoken = new VerificationToken(token,user);

        verificationTokenRepository.save(mytoken);

    }

    @Override
    public VerificationToken getVerificationToken(String verificationToken) {
        return verificationTokenRepository.findByToken(verificationToken);
    }

    @Override
    public void saveRegisteredUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User getUser(String verificationToken) {
        VerificationToken token = verificationTokenRepository.findByToken(verificationToken);
        if (token != null) {
            return token.getUser();
        }
        return null;
    }

    private boolean emailExist(String email){
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


}