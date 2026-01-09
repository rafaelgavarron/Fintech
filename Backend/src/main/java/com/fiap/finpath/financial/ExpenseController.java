package com.fiap.finpath.financial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "*")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    // GET - Consultar todas as despesas
    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpenses() {
        try {
            List<Expense> expenses = expenseService.getAllExpenses();
            return ResponseEntity.ok(expenses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar despesa por ID
    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable String id) {
        try {
            Optional<Expense> expense = expenseService.getExpenseById(id);
            if (expense.isPresent()) {
                return ResponseEntity.ok(expense.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar despesas por organização
    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<Expense>> getExpensesByOrganization(@PathVariable String organizationId) {
        try {
            List<Expense> expenses = expenseService.getExpensesByOrganization(organizationId);
            return ResponseEntity.ok(expenses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar despesas por membro
    @GetMapping("/member/{targetMemberId}")
    public ResponseEntity<List<Expense>> getExpensesByMember(@PathVariable String targetMemberId) {
        try {
            List<Expense> expenses = expenseService.getExpensesByMember(targetMemberId);
            return ResponseEntity.ok(expenses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar despesas por grupo
    @GetMapping("/group/{targetGroupId}")
    public ResponseEntity<List<Expense>> getExpensesByGroup(@PathVariable String targetGroupId) {
        try {
            List<Expense> expenses = expenseService.getExpensesByGroup(targetGroupId);
            return ResponseEntity.ok(expenses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar despesas por período
    @GetMapping("/date-range")
    public ResponseEntity<List<Expense>> getExpensesByDateRange(
            @RequestParam long startDate,
            @RequestParam long endDate) {
        try {
            List<Expense> expenses = expenseService.getAllExpenses().stream()
                    .filter(e -> e.getExpenseDate() >= startDate && e.getExpenseDate() <= endDate)
                    .toList();
            return ResponseEntity.ok(expenses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar despesas por organização e período
    @GetMapping("/organization/{organizationId}/date-range")
    public ResponseEntity<List<Expense>> getExpensesByOrganizationAndDateRange(
            @PathVariable String organizationId,
            @RequestParam long startDate,
            @RequestParam long endDate) {
        try {
            List<Expense> expenses = expenseService.getExpensesByOrganization(organizationId).stream()
                    .filter(e -> e.getExpenseDate() >= startDate && e.getExpenseDate() <= endDate)
                    .toList();
            return ResponseEntity.ok(expenses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar total de despesas por organização
    @GetMapping("/organization/{organizationId}/total")
    public ResponseEntity<Long> getTotalExpenseAmountByOrganization(@PathVariable String organizationId) {
        try {
            Long total = expenseService.getTotalExpenseAmountByOrganization(organizationId);
            return ResponseEntity.ok(total);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST - Criar nova despesa
    @PostMapping
    public ResponseEntity<Expense> createExpense(@RequestBody ExpenseRequest request) {
        try {
            Expense expense = expenseService.createExpense(
                request.getOrganizationId(),
                request.getTargetMemberId(),
                request.getTargetGroupId(),
                request.getBankTransactionId(),
                request.getExpenseDate(),
                request.getExpenseAmount(),
                request.getName(),
                request.getDescription(),
                request.getCategory()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(expense);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // PUT - Atualizar despesa
    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable String id, @RequestBody ExpenseUpdateRequest request) {
        try {
            Expense updatedExpense = expenseService.updateExpense(id, request.getExpenseAmount(), request.getDescription(), request.getCategory());
            if (updatedExpense != null) {
                return ResponseEntity.ok(updatedExpense);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE - Deletar despesa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable String id) {
        try {
            expenseService.deleteExpense(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Classes internas para requests
    public static class ExpenseRequest {
        private String organizationId;
        private String targetMemberId;
        private String targetGroupId;
        private String bankTransactionId;
        private long expenseDate;
        private long expenseAmount;
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
        public long getExpenseDate() { return expenseDate; }
        public void setExpenseDate(long expenseDate) { this.expenseDate = expenseDate; }
        public long getExpenseAmount() { return expenseAmount; }
        public void setExpenseAmount(long expenseAmount) { this.expenseAmount = expenseAmount; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
    }

    public static class ExpenseUpdateRequest {
        private long expenseAmount;
        private String description;
        private String category;

        // Getters e Setters
        public long getExpenseAmount() { return expenseAmount; }
        public void setExpenseAmount(long expenseAmount) { this.expenseAmount = expenseAmount; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
    }

    // GET - Consultar despesas por categoria
    @GetMapping("/organization/{organizationId}/category/{category}")
    public ResponseEntity<List<Expense>> getExpensesByCategory(
            @PathVariable String organizationId,
            @PathVariable String category) {
        try {
            List<Expense> expenses = expenseService.getExpensesByCategory(organizationId, category);
            return ResponseEntity.ok(expenses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar total de despesas por categoria
    @GetMapping("/organization/{organizationId}/category/{category}/total")
    public ResponseEntity<Long> getTotalExpenseAmountByCategory(
            @PathVariable String organizationId,
            @PathVariable String category) {
        try {
            Long total = expenseService.getTotalExpenseAmountByCategory(organizationId, category);
            return ResponseEntity.ok(total != null ? total : 0L);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
