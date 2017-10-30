package com.login.login.domain.repository;

import com.login.login.domain.User;
import com.login.login.domain.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.jws.soap.SOAPBinding;
import java.util.Date;
import java.util.stream.Stream;

/**
 * Created by giuseppe on 29/08/17.
 */
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);
    VerificationToken findByUser(User user);
    Stream<VerificationToken> findByExpiryDateLessThan(Date now);
    void deleteByExpiryDateLessThan(Date now);

    @Modifying
    @Query("delete from VerificationToken t where t.expiryDate <= ?1")
    void deleteAllExpiredSince(Date now);


}
