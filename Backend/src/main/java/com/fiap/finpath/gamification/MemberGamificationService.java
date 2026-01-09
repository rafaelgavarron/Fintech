package com.fiap.finpath.gamification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberGamificationService {

    @Autowired
    private MemberAchievementsRepository memberAchievementsRepository;

    @Autowired
    private MemberPointsRepository memberPointsRepository;

    public MemberAchievements awardAchievement(String memberId, String achievementId, long date) {
        MemberAchievements newAchievement = new MemberAchievements(memberId, achievementId, date);
        System.out.println("MemberGamificationService: Awarding achievement " + achievementId + " to member " + memberId);
        return memberAchievementsRepository.save(newAchievement);
    }

    public void addPointsToMember(String memberId, long pointsToAdd) {
        System.out.println("MemberGamificationService: Adding " + pointsToAdd + " points to member " + memberId);

        Optional<MemberPoints> optionalMemberPoints = memberPointsRepository.findByMemberId(memberId);
        if (optionalMemberPoints.isPresent()) {
            MemberPoints memberPoints = optionalMemberPoints.get();
            memberPoints.setTotalPoints(memberPoints.getTotalPoints() + pointsToAdd);
            memberPointsRepository.save(memberPoints);
        } else {
            MemberPoints newMemberPoints = new MemberPoints(memberId, pointsToAdd);
            memberPointsRepository.save(newMemberPoints);
        }
    }

    public List<MemberAchievements> getMemberAchievements(String memberId) {
        return memberAchievementsRepository.findByMemberId(memberId);
    }

    public Optional<MemberPoints> getMemberPoints(String memberId) {
        return memberPointsRepository.findByMemberId(memberId);
    }

    public List<MemberPoints> getTopMembersByPoints() {
        return memberPointsRepository.findAllOrderByPointsDesc();
    }

    public Long getTotalAchievementsByMember(String memberId) {
        return memberAchievementsRepository.countAchievementsByMember(memberId);
    }
}