package com.fiap.finpath.gamification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengesRepository extends JpaRepository<Challenges, String> {

    List<Challenges> findByAchievementId(String achievementId);

    List<Challenges> findByLevel(Challenges.DifficultyLevel level);

    List<Challenges> findByChallengeType(Challenges.ChallengeType challengeType);

    List<Challenges> findByNameContaining(String name);

    @Query("SELECT c FROM Challenges c WHERE c.level = :level AND c.challengeType = :type")
    List<Challenges> findByLevelAndType(@Param("level") Challenges.DifficultyLevel level,
                                       @Param("type") Challenges.ChallengeType type);

    @Query("SELECT SUM(c.points) FROM Challenges c WHERE c.achievementId = :achievementId")
    Long getTotalPointsByAchievement(@Param("achievementId") String achievementId);
}
