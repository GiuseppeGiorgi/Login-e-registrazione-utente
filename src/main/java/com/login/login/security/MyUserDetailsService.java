package com.login.login.security;

import com.login.login.domain.Privilege;
import com.login.login.domain.Role;
import com.login.login.domain.User;
import com.login.login.domain.repository.RoleRepository;
import com.login.login.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by giuseppe on 31/08/17.
 */

@Service
@Transactional
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private HttpServletRequest request;

    public MyUserDetailsService(){
        super();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {


        String ip = getClientIP();
        if (loginAttemptService.isBlocked(ip)) {
            throw new RuntimeException("blocked");
        }

        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        try{
            User user = userRepository.findByEmail(email);
            if (user == null) {

                throw new UsernameNotFoundException("No user found with username: " + email);
            }



            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    user.isEnabled(),
                    accountNonExpired,
                    credentialsNonExpired,
                    accountNonLocked,
                    getAuthorities(user.getRoles()));

        }
        catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    private final Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {


        return getGrantedAuthorities(getPrivileges(roles));
    }


    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges){
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges){

            authorities.add(new SimpleGrantedAuthority(privilege));
        }

        return authorities;
    }

    private List<String> getPrivileges(Collection<Role> roles){

        List<String> privileges = new ArrayList<>();
        List<Privilege> collection = new ArrayList<>();

        for (Role role: roles){
            collection.addAll(role.getPrivileges());
        }

        for (Privilege item : collection){
            privileges.add(item.getName());
        }

        return privileges;
    }



    private final String getClientIP() {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }


}
