package com.fiap.finpath.goals;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalsRepository extends JpaRepository<Goals, String> {

    List<Goals> findByOrganizationId(String organizationId);

    List<Goals> findByDueDateBetween(long startDate, long endDate);

    @Query("SELECT g FROM Goals g WHERE g.organizationId = :organizationId AND g.dueDate BETWEEN :startDate AND :endDate")
    List<Goals> findByOrganizationIdAndDueDateRange(@Param("organizationId") String organizationId,
                                                   @Param("startDate") long startDate,
                                                   @Param("endDate") long endDate);

    @Query("SELECT SUM(g.desiredAmount) FROM Goals g WHERE g.organizationId = :organizationId")
    Long getTotalDesiredAmountByOrganization(@Param("organizationId") String organizationId);
}
