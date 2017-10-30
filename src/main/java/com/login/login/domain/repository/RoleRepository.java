package com.login.login.domain.repository;


import com.login.login.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by giuseppe on 21/10/17.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

    @Override
    void delete(Role role);
}
