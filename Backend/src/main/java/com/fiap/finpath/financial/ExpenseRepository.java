package com.fiap.finpath.financial;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, String> {

    List<Expense> findByOrganizationId(String organizationId);

    List<Expense> findByTargetMemberId(String targetMemberId);

    List<Expense> findByTargetGroupId(String targetGroupId);

    List<Expense> findByExpenseDateBetween(long startDate, long endDate);

    @Query("SELECT e FROM Expense e WHERE e.organizationId = :organizationId AND e.expenseDate BETWEEN :startDate AND :endDate")
    List<Expense> findByOrganizationIdAndDateRange(@Param("organizationId") String organizationId,
                                                   @Param("startDate") long startDate,
                                                   @Param("endDate") long endDate);

    @Query("SELECT SUM(e.expenseAmount) FROM Expense e WHERE e.organizationId = :organizationId")
    Long getTotalExpenseAmountByOrganization(@Param("organizationId") String organizationId);
}
