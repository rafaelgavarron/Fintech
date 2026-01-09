package com.fiap.finpath.financial;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@MappedSuperclass
public abstract class FinancialTransaction {
    @Id
    @Column(name = "id", length = 36)
    protected String id;

    @Column(name = "bank_transaction_id", length = 36)
    protected String bankTransactionId;

    @NotNull
    @Column(name = "organization_id", length = 36, nullable = false)
    protected String organizationId;

    @Column(name = "target_member_id", length = 36)
    protected String targetMemberId;

    @Column(name = "target_group_id", length = 36)
    protected String targetGroupId;

    @NotBlank
    @Column(name = "name", length = 255, nullable = false)
    protected String name;

    @Column(name = "description", length = 1000)
    protected String description;

    // Construtor padrão para JPA
    public FinancialTransaction() {}

    public FinancialTransaction(String bankTransactionId, String organizationId, String targetMemberId, String targetGroupId, String name, String description) {
        this.id = UUID.randomUUID().toString();
        this.bankTransactionId = bankTransactionId;
        this.organizationId = organizationId;
        this.targetMemberId = targetMemberId;
        this.targetGroupId = targetGroupId;
        this.name = name;
        this.description = description;
    }

    public FinancialTransaction(String id, String bankTransactionId, String organizationId, String targetMemberId, String targetGroupId, String name, String description) {
        this.id = id;
        this.bankTransactionId = bankTransactionId;
        this.organizationId = organizationId;
        this.targetMemberId = targetMemberId;
        this.targetGroupId = targetGroupId;
        this.name = name;
        this.description = description;
    }

    //* Getter Methods *//
    public String getId() {
        return id;
    }

    public String getBankTransactionId() {
        return bankTransactionId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public String getTargetMemberId() {
        return targetMemberId;
    }

    public String getTargetGroupId() {
        return targetGroupId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    //* Setter Methods *//
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}