package com.fiap.finpath.goals;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "GOALS")
public class Goals {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @NotNull
    @Column(name = "organization_id", length = 36, nullable = false)
    private String organizationId;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private long createdAt;

    @NotNull
    @Column(name = "due_date", nullable = false)
    private long dueDate;

    @NotBlank
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @NotNull
    @Positive
    @Column(name = "desired_amount", nullable = false)
    private long desiredAmount;

    // Construtor padrão para JPA
    public Goals() {}

    public Goals(String organizationId, long dueDate, String name, String description, long desiredAmount) {
        this.id = UUID.randomUUID().toString();
        this.organizationId = organizationId;
        this.createdAt = Instant.now().getEpochSecond();
        this.dueDate = dueDate;
        this.name = name;
        this.description = description;
        this.desiredAmount = desiredAmount;
    }

    public Goals(String id, String organizationId, long createdAt, long dueDate, String name, String description, long desiredAmount) {
        this.id = id;
        this.organizationId = organizationId;
        this.createdAt = createdAt;
        this.dueDate = dueDate;
        this.name = name;
        this.description = description;
        this.desiredAmount = desiredAmount;
    }

    //* Getter Methods *//
    public String getId() {
        return id;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getDueDate() {
        return dueDate;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public long getDesiredAmount() {
        return desiredAmount;
    }

    //* Setter Methods *//
    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDesiredAmount(long desiredAmount) {
        this.desiredAmount = desiredAmount;
    }
}