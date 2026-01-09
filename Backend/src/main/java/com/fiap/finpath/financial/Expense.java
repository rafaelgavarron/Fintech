package com.fiap.finpath.financial;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "EXPENSE")
public class Expense extends FinancialTransaction {
    @NotNull
    @Column(name = "expense_date", nullable = false)
    private long expenseDate;

    @NotNull
    @Positive
    @Column(name = "expense_amount", nullable = false)
    private long expenseAmount; // Escolhi armazenar valor monetário em centavos usando o long.

    @Column(name = "category", length = 100)
    private String category;

    // Construtor padrão para JPA
    public Expense() {
        super();
    }

    public Expense(String organizationId, String targetMemberId, String targetGroupId, String bankTransactionId, long expenseDate, long expenseAmount, String name, String description, String category) {
        super(bankTransactionId, organizationId, targetMemberId, targetGroupId, name, description);
        this.expenseDate = expenseDate;
        this.expenseAmount = expenseAmount;
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public Expense(String id, String bankTransactionId, String organizationId, String targetMemberId, String targetGroupId, long expenseDate, long expenseAmount, String name, String description, String category) {
        super(id, bankTransactionId, organizationId, targetMemberId, targetGroupId, name, description);
        this.expenseDate = expenseDate;
        this.expenseAmount = expenseAmount;
        this.name = name;
        this.description = description;
        this.category = category;
    }

    //* Getter Methods *//
    public long getExpenseDate() {
        return expenseDate;
    }
    public long getExpenseAmount() {
        return expenseAmount;
    }

    //* Setter Methods *//
    public void setExpenseDate(long expenseDate) {
        this.expenseDate = expenseDate;
    }
    public void setExpenseAmount(long expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}