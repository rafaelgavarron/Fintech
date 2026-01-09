package com.fiap.finpath.financial;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, String> {

    List<BankAccount> findByOrganizationId(String organizationId);

    List<BankAccount> findByMemberId(String memberId);

    List<BankAccount> findByBankName(String bankName);

    List<BankAccount> findByIsConnected(boolean isConnected);

    @Query("SELECT ba FROM BankAccount ba WHERE ba.organizationId = :organizationId AND ba.isConnected = true")
    List<BankAccount> findConnectedAccountsByOrganization(@Param("organizationId") String organizationId);
}
