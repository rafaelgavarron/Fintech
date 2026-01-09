package com.fiap.finpath.goals;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;

@Entity
@Table(name = "GOALS_CONTRIBUTIONS")
public class GoalsContributions {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @NotNull
    @Column(name = "goal_id", length = 36, nullable = false)
    private String goalId;

    @NotNull
    @Column(name = "contribution_date", nullable = false)
    private long contributionDate;

    @NotNull
    @Positive
    @Column(name = "value", nullable = false)
    private long value;

    @Column(name = "description", length = 1000)
    private String description;

    // Construtor padrão para JPA
    public GoalsContributions() {}

    public GoalsContributions(String goalId, long contributionDate, long value, String description) {
        this.id = UUID.randomUUID().toString();
        this.goalId = goalId;
        this.contributionDate = contributionDate;
        this.value = value;
        this.description = description;
    }

    public GoalsContributions(String id, String goalId, long contributionDate, long value, String description) {
        this.id = id;
        this.goalId = goalId;
        this.contributionDate = contributionDate;
        this.value = value;
        this.description = description;
    }

    //* Getter Methods *//
    public String getId() {
        return id;
    }
    public String getGoalId() {
        return goalId;
    }
    public long getContributionDate() {
        return contributionDate;
    }
    public long getValue() {
        return value;
    }
    public String getDescription() {
        return description;
    }

    //* Setter Methods *//
    public void setContributionDate(long contributionDate) {
        this.contributionDate = contributionDate;
    }
    public void setValue(long value) {
        this.value = value;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
