package com.fiap.finpath.financial;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "BANK_TRANSACTION")
public class BankTransaction {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @NotNull
    @Column(name = "bank_account_id", length = 36, nullable = false)
    private String bankAccountId;

    @NotNull
    @Column(name = "transaction_date", nullable = false)
    private long transactionDate;

    @NotNull
    @Column(name = "value", nullable = false)
    private long value; // Escolhi armazenar valor monetário em centavos usando o long.

    @NotBlank
    @Column(name = "type", length = 20, nullable = false)
    private String type;

    @NotBlank
    @Column(name = "institution_identifier", length = 255, nullable = false)
    private String institutionIdentifier;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    // Construtor padrão para JPA
    public BankTransaction() {}

    public BankTransaction(String bankAccountId, long transactionDate, long value, String type, String institutionIdentifier) {
        this.id = UUID.randomUUID().toString();
        this.bankAccountId = bankAccountId;
        this.transactionDate = transactionDate;
        this.value = value;
        this.type = type;
        this.institutionIdentifier = institutionIdentifier;
    }

    public BankTransaction(String id, String bankAccountId, long transactionDate, long value, String type, String institutionIdentifier, String name, String description) {
        this.id = id;
        this.bankAccountId = bankAccountId;
        this.transactionDate = transactionDate;
        this.value = value;
        this.type = type;
        this.institutionIdentifier = institutionIdentifier;
        this.name = name;
        this.description = description;
    }

    //* Getter Methods *//
    public String getId() {
        return id;
    }

    public String getBankAccountId() {
        return bankAccountId;
    }

    public long getTransactionDate() {
        return transactionDate;
    }

    public long getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public String getInstitutionIdentifier() {
        return institutionIdentifier;
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