package com.fiap.finpath.gamification;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;

@Entity
@Table(name = "CHALLENGES")
public class Challenges {
    public enum DifficultyLevel {
        VERY_EASY,
        EASY,
        MEDIUM,
        HARD,
        ULTRA_HARD
    }
    public enum ChallengeType {
        SAVINGS,
        EXTRA_INCOME,
        LEARNING
    }

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "achievement_id", length = 36)
    private String achievementId;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    private DifficultyLevel level;

    @NotBlank
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "challenge_type", nullable = false)
    private ChallengeType challengeType;

    @NotNull
    @Positive
    @Column(name = "points", nullable = false)
    private long points;

    // Construtor padrão para JPA
    public Challenges() {}

    public Challenges(String achievementId, DifficultyLevel level, String name, String description, ChallengeType challengeType, long points) {
        this.id = UUID.randomUUID().toString();
        this.achievementId = achievementId;
        this.level = level;
        this.name = name;
        this.description = description;
        this.challengeType = challengeType;
        this.points = points;
    }

    public Challenges(String id, String achievementId, DifficultyLevel level, String name, String description, ChallengeType challengeType, long points) {
        this.id = id;
        this.achievementId = achievementId;
        this.level = level;
        this.name = name;
        this.description = description;
        this.challengeType = challengeType;
        this.points = points;
    }

    //* Getter Methods *//
    public String getId() {
        return id;
    }

    public String getAchievementId() {
        return achievementId;
    }

    public DifficultyLevel getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ChallengeType getChallengeType() {
        return challengeType;
    }

    public long getPoints() {
        return points;
    }

    //* Setter Methods *//

    public void setAchievementId(String achievementId) {
        this.achievementId = achievementId;
    }

    public void setLevel(DifficultyLevel level) {
        this.level = level;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setChallengeType(ChallengeType challengeType) {
        this.challengeType = challengeType;
    }

    public void setPoints(long points) {
        this.points = points;
    }
}
