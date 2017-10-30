package com.login.login.domain.repository;

import com.login.login.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by giuseppe on 22/08/17.
 */
public interface UserRepository extends JpaRepository<User, Long>{

    User findByEmail(String email);



    @Override
    void delete(User user);

}