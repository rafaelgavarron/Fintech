package com.fiap.finpath.gamification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberPointsRepository extends JpaRepository<MemberPoints, String> {

    Optional<MemberPoints> findByMemberId(String memberId);

    @Query("SELECT mp FROM MemberPoints mp ORDER BY mp.totalPoints DESC")
    List<MemberPoints> findAllOrderByPointsDesc();

    @Query("SELECT mp FROM MemberPoints mp WHERE mp.totalPoints >= :minPoints ORDER BY mp.totalPoints DESC")
    List<MemberPoints> findByMinimumPoints(@Param("minPoints") long minPoints);

    @Query("SELECT SUM(mp.totalPoints) FROM MemberPoints mp")
    Long getTotalPointsAcrossAllMembers();
}
