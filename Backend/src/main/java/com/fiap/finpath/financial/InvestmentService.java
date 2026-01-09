package com.fiap.finpath.financial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvestmentService {

    @Autowired
    private InvestmentRepository investmentRepository;

    public Investment createInvestment(String organizationId, String memberId, String name, String category, long amount, long purchaseDate, String description) {
        // Normalizar categoria
        String normalizedCategory = (category != null && !category.trim().isEmpty()) ? category.trim() : null;
        Investment newInvestment = new Investment(organizationId, memberId, name, normalizedCategory, amount, purchaseDate, description);
        System.out.println("InvestmentService: Creating new investment record for organization " + organizationId + " with amount: " + amount + " and category: " + normalizedCategory);
        return investmentRepository.save(newInvestment);
    }

    public Optional<Investment> getInvestmentById(String investmentId) {
        System.out.println("InvestmentService: Retrieving investment record with ID: " + investmentId);
        return investmentRepository.findById(investmentId);
    }

    public List<Investment> getInvestmentsByOrganization(String organizationId) {
        System.out.println("InvestmentService: Retrieving all investment records for organization " + organizationId);
        return investmentRepository.findByOrganizationId(organizationId);
    }

    public List<Investment> getInvestmentsByMember(String memberId) {
        return investmentRepository.findByMemberId(memberId);
    }

    public Investment updateInvestment(String investmentId, String name, String category, long amount, long purchaseDate, String description) {
        System.out.println("InvestmentService: Updating investment record " + investmentId);
        Optional<Investment> optionalInvestment = investmentRepository.findById(investmentId);
        if (optionalInvestment.isPresent()) {
            Investment investment = optionalInvestment.get();
            if (name != null) {
                investment.setName(name);
            }
            // Normalizar categoria
            if (category != null && !category.trim().isEmpty()) {
                investment.setCategory(category.trim());
            } else {
                investment.setCategory(null);
            }
            if (amount > 0) {
                investment.setAmount(amount);
            }
            if (purchaseDate > 0) {
                investment.setPurchaseDate(purchaseDate);
            }
            if (description != null) {
                investment.setDescription(description);
            }
            return investmentRepository.save(investment);
        }
        return null;
    }

    public void deleteInvestment(String investmentId) {
        System.out.println("InvestmentService: Deleting investment record with ID: " + investmentId);
        investmentRepository.deleteById(investmentId);
    }

    public List<Investment> getAllInvestments() {
        return investmentRepository.findAll();
    }

    public Long getTotalAmountByOrganization(String organizationId) {
        Long total = investmentRepository.getTotalAmountByOrganization(organizationId);
        return total != null ? total : 0L;
    }
}

