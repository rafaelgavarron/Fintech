package com.fiap.finpath.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, String> {

    Optional<VerificationCode> findByUserEmailAndCodeText(String userEmail, String codeText);

    List<VerificationCode> findByUserEmail(String userEmail);

    List<VerificationCode> findByIsUsed(boolean isUsed);

    @Query("SELECT vc FROM VerificationCode vc WHERE vc.expireAt < :currentTime")
    List<VerificationCode> findExpiredCodes(@Param("currentTime") long currentTime);
}
