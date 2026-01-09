package com.fiap.finpath.organization;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ORGANIZATION")
public class Organization {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @NotBlank
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private long createdAt;

    @Column(name = "trial_expire_at")
    private long trialExpireAt;

    // Construtor padrão para JPA
    public Organization() {}

    public Organization(String name, boolean isActive, long trialExpireAt) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.isActive = isActive;
        this.createdAt = Instant.now().getEpochSecond(); // timestamp Unix atual em segundos
        this.trialExpireAt = trialExpireAt;
    }

    public Organization(String id, String name, boolean isActive, long createdAt, long trialExpireAt) {
        this.id = id;
        this.name = name;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.trialExpireAt = trialExpireAt;
    }

    //* Getter Methods *//
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return isActive;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getTrialExpireAt() {
        return trialExpireAt;
    }

    //* Setter Methods *//

    public void setName(String name) {
        this.name = name;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setTrialExpireAt(long trialExpireAt) {
        this.trialExpireAt = trialExpireAt;
    }
}
