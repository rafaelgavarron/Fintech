package com.fiap.finpath.gamification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/challenges")
@CrossOrigin(origins = "*")
public class ChallengesController {

    @Autowired
    private ChallengeService challengeService;

    // GET - Consultar todos os desafios
    @GetMapping
    public ResponseEntity<List<Challenges>> getAllChallenges() {
        try {
            List<Challenges> challenges = challengeService.getAllChallenges();
            return ResponseEntity.ok(challenges);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar desafio por ID
    @GetMapping("/{id}")
    public ResponseEntity<Challenges> getChallengeById(@PathVariable String id) {
        try {
            Optional<Challenges> challenge = challengeService.getChallengeById(id);
            if (challenge.isPresent()) {
                return ResponseEntity.ok(challenge.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar desafios por achievement
    @GetMapping("/achievement/{achievementId}")
    public ResponseEntity<List<Challenges>> getChallengesByAchievement(@PathVariable String achievementId) {
        try {
            List<Challenges> challenges = challengeService.getAllChallenges().stream()
                    .filter(c -> c.getAchievementId().equals(achievementId))
                    .toList();
            return ResponseEntity.ok(challenges);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar desafios por nível
    @GetMapping("/level/{level}")
    public ResponseEntity<List<Challenges>> getChallengesByLevel(@PathVariable Challenges.DifficultyLevel level) {
        try {
            List<Challenges> challenges = challengeService.getChallengesByLevel(level);
            return ResponseEntity.ok(challenges);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar desafios por tipo
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Challenges>> getChallengesByType(@PathVariable Challenges.ChallengeType type) {
        try {
            List<Challenges> challenges = challengeService.getChallengesByType(type);
            return ResponseEntity.ok(challenges);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar desafios por nível e tipo
    @GetMapping("/level/{level}/type/{type}")
    public ResponseEntity<List<Challenges>> getChallengesByLevelAndType(
            @PathVariable Challenges.DifficultyLevel level,
            @PathVariable Challenges.ChallengeType type) {
        try {
            List<Challenges> challenges = challengeService.getChallengesByLevelAndType(level, type);
            return ResponseEntity.ok(challenges);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar total de pontos por achievement
    @GetMapping("/achievement/{achievementId}/total-points")
    public ResponseEntity<Long> getTotalPointsByAchievement(@PathVariable String achievementId) {
        try {
            Long total = challengeService.getTotalPointsByAchievement(achievementId);
            return ResponseEntity.ok(total);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST - Criar novo desafio
    @PostMapping
    public ResponseEntity<Challenges> createChallenge(@RequestBody ChallengeRequest request) {
        try {
            Challenges challenge = challengeService.createChallenge(
                request.getAchievementId(),
                request.getLevel(),
                request.getName(),
                request.getDescription(),
                request.getChallengeType(),
                request.getPoints()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(challenge);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // PUT - Atualizar desafio
    @PutMapping("/{id}")
    public ResponseEntity<Challenges> updateChallenge(@PathVariable String id, @RequestBody ChallengeUpdateRequest request) {
        try {
            Challenges updatedChallenge = challengeService.updateChallenge(
                id,
                request.getAchievementId(),
                request.getLevel(),
                request.getName(),
                request.getDescription(),
                request.getChallengeType(),
                request.getPoints()
            );
            if (updatedChallenge != null) {
                return ResponseEntity.ok(updatedChallenge);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE - Deletar desafio
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChallenge(@PathVariable String id) {
        try {
            challengeService.deleteChallenge(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Classes internas para requests
    public static class ChallengeRequest {
        private String achievementId;
        private Challenges.DifficultyLevel level;
        private String name;
        private String description;
        private Challenges.ChallengeType challengeType;
        private long points;

        // Getters e Setters
        public String getAchievementId() { return achievementId; }
        public void setAchievementId(String achievementId) { this.achievementId = achievementId; }
        public Challenges.DifficultyLevel getLevel() { return level; }
        public void setLevel(Challenges.DifficultyLevel level) { this.level = level; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Challenges.ChallengeType getChallengeType() { return challengeType; }
        public void setChallengeType(Challenges.ChallengeType challengeType) { this.challengeType = challengeType; }
        public long getPoints() { return points; }
        public void setPoints(long points) { this.points = points; }
    }

    public static class ChallengeUpdateRequest {
        private String achievementId;
        private Challenges.DifficultyLevel level;
        private String name;
        private String description;
        private Challenges.ChallengeType challengeType;
        private long points;

        // Getters e Setters
        public String getAchievementId() { return achievementId; }
        public void setAchievementId(String achievementId) { this.achievementId = achievementId; }
        public Challenges.DifficultyLevel getLevel() { return level; }
        public void setLevel(Challenges.DifficultyLevel level) { this.level = level; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Challenges.ChallengeType getChallengeType() { return challengeType; }
        public void setChallengeType(Challenges.ChallengeType challengeType) { this.challengeType = challengeType; }
        public long getPoints() { return points; }
        public void setPoints(long points) { this.points = points; }
    }
}
