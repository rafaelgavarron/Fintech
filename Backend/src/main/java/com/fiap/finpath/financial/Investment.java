package com.fiap.finpath.financial;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "INVESTMENT")
public class Investment {
    @Id
    @Column(name = "id", length = 36)
    private String id;

    @NotNull
    @Column(name = "organization_id", nullable = false, length = 36)
    private String organizationId;

    @NotNull
    @Column(name = "member_id", nullable = false, length = 36)
    private String memberId;

    @NotNull
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "category", length = 100)
    private String category;

    @NotNull
    @Positive
    @Column(name = "amount", nullable = false)
    private long amount; // Valor em centavos

    @NotNull
    @Column(name = "purchase_date", nullable = false)
    private long purchaseDate; // Timestamp Unix

    @Column(name = "description", length = 1000)
    private String description;

    // Construtor padrão para JPA
    public Investment() {
        this.id = java.util.UUID.randomUUID().toString();
    }

    public Investment(String organizationId, String memberId, String name, String category, long amount, long purchaseDate, String description) {
        this.id = java.util.UUID.randomUUID().toString();
        this.organizationId = organizationId;
        this.memberId = memberId;
        this.name = name;
        this.category = (category != null && !category.trim().isEmpty()) ? category.trim() : null;
        this.amount = amount;
        this.purchaseDate = purchaseDate;
        this.description = description;
    }

    public Investment(String id, String organizationId, String memberId, String name, String category, long amount, long purchaseDate, String description) {
        this.id = id;
        this.organizationId = organizationId;
        this.memberId = memberId;
        this.name = name;
        this.category = (category != null && !category.trim().isEmpty()) ? category.trim() : null;
        this.amount = amount;
        this.purchaseDate = purchaseDate;
        this.description = description;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public long getAmount() {
        return amount;
    }

    public long getPurchaseDate() {
        return purchaseDate;
    }

    public String getDescription() {
        return description;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = (category != null && !category.trim().isEmpty()) ? category.trim() : null;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public void setPurchaseDate(long purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

