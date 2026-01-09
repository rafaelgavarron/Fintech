package com.fiap.finpath.financial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    public Expense createExpense(String organizationId, String targetMemberId, String targetGroupId, String bankTransactionId, long expenseDate, long expenseAmount, String name, String description, String category) {
        // Normalizar categoria: null, vazio ou "Customizada" sem valor customizado vira null
        String normalizedCategory = (category != null && !category.trim().isEmpty() && !category.equals("Customizada")) ? category.trim() : null;
        Expense newExpense = new Expense(organizationId, targetMemberId, targetGroupId, bankTransactionId, expenseDate, expenseAmount, name, description, normalizedCategory);
        System.out.println("ExpenseService: Creating new expense record for organization " + organizationId + " with amount: " + expenseAmount + " and category: " + normalizedCategory);
        return expenseRepository.save(newExpense);
    }

    public Optional<Expense> getExpenseById(String expenseId) {
        System.out.println("ExpenseService: Retrieving expense record with ID: " + expenseId);
        return expenseRepository.findById(expenseId);
    }

    public List<Expense> getExpensesByOrganization(String organizationId) {
        System.out.println("ExpenseService: Retrieving all expense records for organization " + organizationId);
        return expenseRepository.findByOrganizationId(organizationId);
    }

    public Expense updateExpense(String expenseId, long newExpenseAmount, String newDescription, String newCategory) {
        System.out.println("ExpenseService: Updating expense record " + expenseId + ". New amount: " + newExpenseAmount);
        Optional<Expense> optionalExpense = expenseRepository.findById(expenseId);
        if (optionalExpense.isPresent()) {
            Expense expense = optionalExpense.get();
            expense.setExpenseAmount(newExpenseAmount);
            if (newDescription != null) {
                expense.setDescription(newDescription);
            }
            // Normalizar categoria: null, vazio ou "Customizada" sem valor customizado vira null
            if (newCategory != null && !newCategory.trim().isEmpty() && !newCategory.equals("Customizada")) {
                expense.setCategory(newCategory.trim());
            } else {
                expense.setCategory(null);
            }
            return expenseRepository.save(expense);
        }
        return null;
    }

    public List<Expense> getExpensesByCategory(String organizationId, String category) {
        return expenseRepository.findByOrganizationId(organizationId).stream()
                .filter(e -> category == null || category.equals(e.getCategory()))
                .toList();
    }

    public Long getTotalExpenseAmountByCategory(String organizationId, String category) {
        return expenseRepository.findByOrganizationId(organizationId).stream()
                .filter(e -> category == null || category.equals(e.getCategory()))
                .mapToLong(Expense::getExpenseAmount)
                .sum();
    }

    public void deleteExpense(String expenseId) {
        System.out.println("ExpenseService: Deleting expense record with ID: " + expenseId);
        expenseRepository.deleteById(expenseId);
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public List<Expense> getExpensesByMember(String targetMemberId) {
        return expenseRepository.findByTargetMemberId(targetMemberId);
    }

    public List<Expense> getExpensesByGroup(String targetGroupId) {
        return expenseRepository.findByTargetGroupId(targetGroupId);
    }

    public Long getTotalExpenseAmountByOrganization(String organizationId) {
        Long total = expenseRepository.getTotalExpenseAmountByOrganization(organizationId);
        return total != null ? total : 0L;
    }
}