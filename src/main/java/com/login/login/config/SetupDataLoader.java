package com.login.login.config;


import com.login.login.domain.Privilege;
import com.login.login.domain.Role;
import com.login.login.domain.User;
import com.login.login.domain.repository.PrivilegeRepository;
import com.login.login.domain.repository.RoleRepository;
import com.login.login.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by giuseppe on 21/10/17.
 */

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent>{

    private boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        if (alreadySetup){
            return;
        }

        Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
        Privilege passwordPrivilege = createPrivilegeIfNotFound("CHANGE_PASSWORD_PRIVILEGE");


        List<Privilege> adminPrivileges = Arrays.asList(readPrivilege,writePrivilege,passwordPrivilege);
        List<Privilege> userPrivileges = Arrays.asList(readPrivilege, passwordPrivilege);

        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", userPrivileges);

        final Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        final User user = new User();
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setPassword(passwordEncoder.encode("test"));
        user.setEmail("test@test.com");
        user.setRoles(Arrays.asList(adminRole));
        user.setEnabled(true);
        userRepository.save(user);

        alreadySetup = true;

    }


    private Privilege createPrivilegeIfNotFound(String name){
        Privilege privilege = privilegeRepository.findByName(name);

        if (privilege == null){
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }


    private Role createRoleIfNotFound(String name, Collection<Privilege> privileges){

        Role role = roleRepository.findByName(name);

        if (role == null){
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
}
