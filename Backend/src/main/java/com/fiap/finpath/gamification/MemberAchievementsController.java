package com.fiap.finpath.gamification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/member-achievements")
@CrossOrigin(origins = "*")
public class MemberAchievementsController {

    @Autowired
    private MemberGamificationService memberGamificationService;

    // GET - Consultar conquistas de um membro
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<MemberAchievements>> getMemberAchievements(@PathVariable String memberId) {
        try {
            List<MemberAchievements> achievements = memberGamificationService.getMemberAchievements(memberId);
            return ResponseEntity.ok(achievements);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar total de conquistas de um membro
    @GetMapping("/member/{memberId}/count")
    public ResponseEntity<Long> getTotalAchievementsByMember(@PathVariable String memberId) {
        try {
            Long count = memberGamificationService.getTotalAchievementsByMember(memberId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST - Atribuir conquista a um membro
    @PostMapping("/award")
    public ResponseEntity<MemberAchievements> awardAchievement(@RequestBody AwardAchievementRequest request) {
        try {
            MemberAchievements achievement = memberGamificationService.awardAchievement(
                request.getMemberId(),
                request.getAchievementId(),
                request.getDate()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(achievement);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Classes internas para requests
    public static class AwardAchievementRequest {
        private String memberId;
        private String achievementId;
        private long date;

        // Getters e Setters
        public String getMemberId() { return memberId; }
        public void setMemberId(String memberId) { this.memberId = memberId; }
        public String getAchievementId() { return achievementId; }
        public void setAchievementId(String achievementId) { this.achievementId = achievementId; }
        public long getDate() { return date; }
        public void setDate(long date) { this.date = date; }
    }
}
