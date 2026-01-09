package com.fiap.finpath.financial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IncomeService {

    @Autowired
    private IncomeRepository incomeRepository;

    public Income createIncome(String organizationId, String targetMemberId, String targetGroupId, String bankTransactionId, long incomeDate, long incomeAmount, String name, String description, String category) {
        // Normalizar categoria: null, vazio ou "Customizada" sem valor customizado vira null
        String normalizedCategory = (category != null && !category.trim().isEmpty() && !category.equals("Customizada")) ? category.trim() : null;
        Income newIncome = new Income(organizationId, targetMemberId, targetGroupId, bankTransactionId, incomeDate, incomeAmount, name, description, normalizedCategory);
        System.out.println("IncomeService: Creating new income record for organization " + organizationId + " with amount: " + incomeAmount + " and category: " + normalizedCategory);
        return incomeRepository.save(newIncome);
    }

    public Optional<Income> getIncomeById(String incomeId) {
        System.out.println("IncomeService: Retrieving income record with ID: " + incomeId);
        return incomeRepository.findById(incomeId);
    }

    public List<Income> getIncomesByOrganization(String organizationId) {
        System.out.println("IncomeService: Retrieving all income records for organization " + organizationId);
        return incomeRepository.findByOrganizationId(organizationId);
    }

    public Income updateIncome(String incomeId, long newIncomeAmount, String newDescription, String newCategory) {
        System.out.println("IncomeService: Updating income record " + incomeId + ". New amount: " + newIncomeAmount);
        Optional<Income> optionalIncome = incomeRepository.findById(incomeId);
        if (optionalIncome.isPresent()) {
            Income income = optionalIncome.get();
            income.setIncomeAmount(newIncomeAmount);
            if (newDescription != null) {
                income.setDescription(newDescription);
            }
            // Normalizar categoria: null, vazio ou "Customizada" sem valor customizado vira null
            if (newCategory != null && !newCategory.trim().isEmpty() && !newCategory.equals("Customizada")) {
                income.setCategory(newCategory.trim());
            } else {
                income.setCategory(null);
            }
            return incomeRepository.save(income);
        }
        return null;
    }

    public void deleteIncome(String incomeId) {
        System.out.println("IncomeService: Deleting income record with ID: " + incomeId);
        incomeRepository.deleteById(incomeId);
    }

    public List<Income> getAllIncomes() {
        return incomeRepository.findAll();
    }

    public List<Income> getIncomesByMember(String targetMemberId) {
        return incomeRepository.findByTargetMemberId(targetMemberId);
    }

    public List<Income> getIncomesByGroup(String targetGroupId) {
        return incomeRepository.findByTargetGroupId(targetGroupId);
    }

    public Long getTotalIncomeAmountByOrganization(String organizationId) {
        Long total = incomeRepository.getTotalIncomeAmountByOrganization(organizationId);
        return total != null ? total : 0L;
    }
}