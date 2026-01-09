package com.fiap.finpath.financial;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, String> {

    List<Income> findByOrganizationId(String organizationId);

    List<Income> findByTargetMemberId(String targetMemberId);

    List<Income> findByTargetGroupId(String targetGroupId);

    List<Income> findByIncomeDateBetween(long startDate, long endDate);

    @Query("SELECT i FROM Income i WHERE i.organizationId = :organizationId AND i.incomeDate BETWEEN :startDate AND :endDate")
    List<Income> findByOrganizationIdAndDateRange(@Param("organizationId") String organizationId,
                                                  @Param("startDate") long startDate,
                                                  @Param("endDate") long endDate);

    @Query("SELECT SUM(i.incomeAmount) FROM Income i WHERE i.organizationId = :organizationId")
    Long getTotalIncomeAmountByOrganization(@Param("organizationId") String organizationId);
}
