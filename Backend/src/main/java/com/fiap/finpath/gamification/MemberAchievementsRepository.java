package com.fiap.finpath.gamification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberAchievementsRepository extends JpaRepository<MemberAchievements, String> {

    List<MemberAchievements> findByMemberId(String memberId);

    List<MemberAchievements> findByAchievementId(String achievementId);

    List<MemberAchievements> findByDateBetween(long startDate, long endDate);

    @Query("SELECT ma FROM MemberAchievements ma WHERE ma.memberId = :memberId AND ma.achievementId = :achievementId")
    List<MemberAchievements> findByMemberIdAndAchievementId(@Param("memberId") String memberId,
                                                            @Param("achievementId") String achievementId);

    @Query("SELECT COUNT(ma) FROM MemberAchievements ma WHERE ma.memberId = :memberId")
    Long countAchievementsByMember(@Param("memberId") String memberId);
}
