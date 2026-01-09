package com.fiap.finpath.financial;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankTransactionRepository extends JpaRepository<BankTransaction, String> {

    List<BankTransaction> findByBankAccountId(String bankAccountId);

    List<BankTransaction> findByType(String type);

    List<BankTransaction> findByTransactionDateBetween(long startDate, long endDate);

    @Query("SELECT bt FROM BankTransaction bt WHERE bt.bankAccountId = :bankAccountId AND bt.transactionDate BETWEEN :startDate AND :endDate")
    List<BankTransaction> findByBankAccountIdAndDateRange(@Param("bankAccountId") String bankAccountId,
                                                          @Param("startDate") long startDate,
                                                          @Param("endDate") long endDate);

    @Query("SELECT SUM(bt.value) FROM BankTransaction bt WHERE bt.bankAccountId = :bankAccountId AND bt.type = :type")
    Long getTotalValueByBankAccountAndType(@Param("bankAccountId") String bankAccountId, @Param("type") String type);
}
