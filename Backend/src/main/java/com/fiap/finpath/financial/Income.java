package com.fiap.finpath.financial;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "INCOME")
public class Income extends FinancialTransaction {
    @NotNull
    @Column(name = "income_date", nullable = false)
    private long incomeDate;

    @NotNull
    @Positive
    @Column(name = "income_amount", nullable = false)
    private long incomeAmount; // Escolhi armazenar valor monetário em centavos usando o long.

    @Column(name = "category", length = 100)
    private String category;

    // Construtor padrão para JPA
    public Income() {
        super();
    }

    public Income(String organizationId, String targetMemberId, String targetGroupId, String bankTransactionId, long incomeDate, long incomeAmount, String name, String description, String category) {
        super(bankTransactionId, organizationId, targetMemberId, targetGroupId, name, description);
        this.incomeDate = incomeDate;
        this.incomeAmount = incomeAmount;
        this.name = name;
        this.description = description;
        this.category = (category != null && !category.trim().isEmpty()) ? category.trim() : null;
    }

    public Income(String id, String bankTransactionId, String organizationId, String targetMemberId, String targetGroupId, long incomeDate, long incomeAmount, String name, String description, String category) {
        super(id, bankTransactionId, organizationId, targetMemberId, targetGroupId, name, description);
        this.incomeDate = incomeDate;
        this.incomeAmount = incomeAmount;
        this.name = name;
        this.description = description;
        this.category = (category != null && !category.trim().isEmpty()) ? category.trim() : null;
    }

    //* Getter Methods *//
    public long getIncomeDate() {
        return incomeDate;
    }
    public long getIncomeAmount() {
        return incomeAmount;
    }

    //* Setter Methods *//
    public void setIncomeDate(long incomeDate) {
        this.incomeDate = incomeDate;
    }
    public void setIncomeAmount(long incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}