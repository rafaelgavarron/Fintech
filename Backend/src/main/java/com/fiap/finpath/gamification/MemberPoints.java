package com.fiap.finpath.gamification;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "MEMBER_POINTS")
public class MemberPoints {

    @Id
    @NotBlank
    @Column(name = "member_id", length = 36, nullable = false)
    private String memberId;

    @NotNull
    @Column(name = "total_points", nullable = false)
    private long totalPoints;

    // Construtor padrão para JPA
    public MemberPoints() {}

    public MemberPoints(String memberId, long totalPoints) {
        this.memberId = memberId;
        this.totalPoints = totalPoints;
    }

    //* Getter Methods *//
    public String getMemberId() {
        return memberId;
    }
    public long getTotalPoints() {
        return totalPoints;
    }

    //* Setter Methods *//
    public void setTotalPoints(long totalPoints) {
        this.totalPoints = totalPoints;
    }
}