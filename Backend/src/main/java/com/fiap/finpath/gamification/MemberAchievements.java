package com.fiap.finpath.gamification;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "MEMBER_ACHIEVEMENTS")
public class MemberAchievements {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @NotBlank
    @Column(name = "member_id", length = 36, nullable = false)
    private String memberId;

    @NotBlank
    @Column(name = "achievement_id", length = 36, nullable = false)
    private String achievementId;

    @NotNull
    @Column(name = "date", nullable = false)
    private long date;

    // Construtor padrão para JPA
    public MemberAchievements() {}

    public MemberAchievements(String memberId, String achievementId, long date) {
        this.id = UUID.randomUUID().toString();
        this.memberId = memberId;
        this.achievementId = achievementId;
        this.date = date;
    }

    public MemberAchievements(String id, String memberId, String achievementId, long date) {
        this.id = id;
        this.memberId = memberId;
        this.achievementId = achievementId;
        this.date = date;
    }

    //* Getter Methods *//
    public String getId() {
        return id;
    }
    public String getMemberId() {
        return memberId;
    }
    public String getAchievementId() {
        return achievementId;
    }
    public long getDate() {
        return date;
    }
}