package com.fiap.finpath.goals;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalsContributionsRepository extends JpaRepository<GoalsContributions, String> {

    List<GoalsContributions> findByGoalId(String goalId);

    List<GoalsContributions> findByContributionDateBetween(long startDate, long endDate);

    @Query("SELECT gc FROM GoalsContributions gc WHERE gc.goalId = :goalId AND gc.contributionDate BETWEEN :startDate AND :endDate")
    List<GoalsContributions> findByGoalIdAndDateRange(@Param("goalId") String goalId,
                                                      @Param("startDate") long startDate,
                                                      @Param("endDate") long endDate);

    @Query("SELECT SUM(gc.value) FROM GoalsContributions gc WHERE gc.goalId = :goalId")
    Long getTotalContributionsByGoal(@Param("goalId") String goalId);

    @Query("SELECT SUM(gc.value) FROM GoalsContributions gc WHERE gc.goalId IN (SELECT g.id FROM Goals g WHERE g.organizationId = :organizationId)")
    Long getTotalContributionsByOrganization(@Param("organizationId") String organizationId);
}
