package com.fiap.finpath.ai;

import java.util.UUID;

public class AiRecommendations {
    public enum Status {
        ACTIVE, IGNORED, RESOLVED
    }
    private final String id;
    private final String organizationId;
    private final String recommendation;
    private Status status;

    public AiRecommendations(String organizationId, String recommendation) {
        this.id = UUID.randomUUID().toString();
        this.organizationId = organizationId;
        this.recommendation = recommendation;
        this.status = Status.ACTIVE;
    }

    public AiRecommendations(String id, String organizationId, String recommendation, Status status) {
        this.id = id;
        this.organizationId = organizationId;
        this.recommendation = recommendation;
        this.status = status;
    }

    //* Getter Methods *//
    public String getId() {
        return id;
    }
    public String getOrganizationId() {
        return organizationId;
    }
    public String getRecommendation() {
        return recommendation;
    }
    public Status getStatus() {
        return status;
    }

    //* Setter Methods *//
    public void setStatus(Status status) {
        this.status = status;
    }
}
