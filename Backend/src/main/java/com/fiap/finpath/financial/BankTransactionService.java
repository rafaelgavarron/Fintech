package com.fiap.finpath.financial;

import java.util.List;

public class BankTransactionService {

    public BankTransaction createBankTransaction(String bankAccountId, long transactionDate, long value, String type, String institutionIdentifier) {
        BankTransaction newTransaction = new BankTransaction(bankAccountId, transactionDate, value, type, institutionIdentifier);
        System.out.println("BankTransactionService: Creating new bank transaction for account " + bankAccountId + " of type " + type + " with value: " + value);
        return newTransaction;
    }

    public BankTransaction getBankTransactionById(String transactionId) {
        System.out.println("BankTransactionService: Retrieving bank transaction with ID: " + transactionId);
        return new BankTransaction("transactionId", "AccountId", 21235900L, 120200L, "DEBIT", "BankX", "TESTE", "Transacão de teste");
    }

    public List<BankTransaction> getBankTransactionsByAccount(String bankAccountId) {
        System.out.println("BankTransactionService: Retrieving all bank transactions for account " + bankAccountId);
        return List.of();
    }

    public void updateBankTransactionDescription(String transactionId, String description) {
        System.out.println("BankTransactionService: Updating description for transaction " + transactionId);
    }
}