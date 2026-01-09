package com.fiap.finpath.financial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/incomes")
@CrossOrigin(origins = "*")
public class IncomeController {

    @Autowired
    private IncomeService incomeService;

    // GET - Consultar todas as receitas
    @GetMapping
    public ResponseEntity<List<Income>> getAllIncomes() {
        try {
            List<Income> incomes = incomeService.getAllIncomes();
            return ResponseEntity.ok(incomes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar receita por ID
    @GetMapping("/{id}")
    public ResponseEntity<Income> getIncomeById(@PathVariable String id) {
        try {
            Optional<Income> income = incomeService.getIncomeById(id);
            if (income.isPresent()) {
                return ResponseEntity.ok(income.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar receitas por organização
    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<Income>> getIncomesByOrganization(@PathVariable String organizationId) {
        try {
            List<Income> incomes = incomeService.getIncomesByOrganization(organizationId);
            return ResponseEntity.ok(incomes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar receitas por membro
    @GetMapping("/member/{targetMemberId}")
    public ResponseEntity<List<Income>> getIncomesByMember(@PathVariable String targetMemberId) {
        try {
            List<Income> incomes = incomeService.getIncomesByMember(targetMemberId);
            return ResponseEntity.ok(incomes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar receitas por grupo
    @GetMapping("/group/{targetGroupId}")
    public ResponseEntity<List<Income>> getIncomesByGroup(@PathVariable String targetGroupId) {
        try {
            List<Income> incomes = incomeService.getIncomesByGroup(targetGroupId);
            return ResponseEntity.ok(incomes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar receitas por período
    @GetMapping("/date-range")
    public ResponseEntity<List<Income>> getIncomesByDateRange(
            @RequestParam long startDate,
            @RequestParam long endDate) {
        try {
            List<Income> incomes = incomeService.getAllIncomes().stream()
                    .filter(i -> i.getIncomeDate() >= startDate && i.getIncomeDate() <= endDate)
                    .toList();
            return ResponseEntity.ok(incomes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar receitas por organização e período
    @GetMapping("/organization/{organizationId}/date-range")
    public ResponseEntity<List<Income>> getIncomesByOrganizationAndDateRange(
            @PathVariable String organizationId,
            @RequestParam long startDate,
            @RequestParam long endDate) {
        try {
            List<Income> incomes = incomeService.getIncomesByOrganization(organizationId).stream()
                    .filter(i -> i.getIncomeDate() >= startDate && i.getIncomeDate() <= endDate)
                    .toList();
            return ResponseEntity.ok(incomes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar total de receitas por organização
    @GetMapping("/organization/{organizationId}/total")
    public ResponseEntity<Long> getTotalIncomeAmountByOrganization(@PathVariable String organizationId) {
        try {
            Long total = incomeService.getTotalIncomeAmountByOrganization(organizationId);
            return ResponseEntity.ok(total);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST - Criar nova receita
    @PostMapping
    public ResponseEntity<Income> createIncome(@RequestBody IncomeRequest request) {
        try {
            Income income = incomeService.createIncome(
                request.getOrganizationId(),
                request.getTargetMemberId(),
                request.getTargetGroupId(),
                request.getBankTransactionId(),
                request.getIncomeDate(),
                request.getIncomeAmount(),
                request.getName(),
                request.getDescription(),
                request.getCategory()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(income);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // PUT - Atualizar receita
    @PutMapping("/{id}")
    public ResponseEntity<Income> updateIncome(@PathVariable String id, @RequestBody IncomeUpdateRequest request) {
        try {
            Income updatedIncome = incomeService.updateIncome(id, request.getIncomeAmount(), request.getDescription(), request.getCategory());
            if (updatedIncome != null) {
                return ResponseEntity.ok(updatedIncome);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE - Deletar receita
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable String id) {
        try {
            incomeService.deleteIncome(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Classes internas para requests
    public static class IncomeRequest {
        private String organizationId;
        private String targetMemberId;
        private String targetGroupId;
        private String bankTransactionId;
        private long incomeDate;
        private long incomeAmount;
        private String name;
        private String description;
        private String category;

        // Getters e Setters
        public String getOrganizationId() { return organizationId; }
        public void setOrganizationId(String organizationId) { this.organizationId = organizationId; }
        public String getTargetMemberId() { return targetMemberId; }
        public void setTargetMemberId(String targetMemberId) { this.targetMemberId = targetMemberId; }
        public String getTargetGroupId() { return targetGroupId; }
        public void setTargetGroupId(String targetGroupId) { this.targetGroupId = targetGroupId; }
        public String getBankTransactionId() { return bankTransactionId; }
        public void setBankTransactionId(String bankTransactionId) { this.bankTransactionId = bankTransactionId; }
        public long getIncomeDate() { return incomeDate; }
        public void setIncomeDate(long incomeDate) { this.incomeDate = incomeDate; }
        public long getIncomeAmount() { return incomeAmount; }
        public void setIncomeAmount(long incomeAmount) { this.incomeAmount = incomeAmount; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
    }

    public static class IncomeUpdateRequest {
        private long incomeAmount;
        private String description;
        private String category;

        // Getters e Setters
        public long getIncomeAmount() { return incomeAmount; }
        public void setIncomeAmount(long incomeAmount) { this.incomeAmount = incomeAmount; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
    }
}
