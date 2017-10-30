package com.login.login.domain.repository;

import com.login.login.domain.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by giuseppe on 21/10/17.
 */
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    Privilege findByName(String name);

    @Override
    void delete(Privilege privilege);

}
