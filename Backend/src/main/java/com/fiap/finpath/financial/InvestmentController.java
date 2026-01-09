package com.fiap.finpath.financial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/investments")
@CrossOrigin(origins = "*")
public class InvestmentController {

    @Autowired
    private InvestmentService investmentService;

    // GET - Consultar todos os investimentos
    @GetMapping
    public ResponseEntity<List<Investment>> getAllInvestments() {
        try {
            List<Investment> investments = investmentService.getAllInvestments();
            return ResponseEntity.ok(investments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar investimento por ID
    @GetMapping("/{id}")
    public ResponseEntity<Investment> getInvestmentById(@PathVariable String id) {
        try {
            Optional<Investment> investment = investmentService.getInvestmentById(id);
            if (investment.isPresent()) {
                return ResponseEntity.ok(investment.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar investimentos por organização
    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<Investment>> getInvestmentsByOrganization(@PathVariable String organizationId) {
        try {
            List<Investment> investments = investmentService.getInvestmentsByOrganization(organizationId);
            return ResponseEntity.ok(investments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar investimentos por membro
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Investment>> getInvestmentsByMember(@PathVariable String memberId) {
        try {
            List<Investment> investments = investmentService.getInvestmentsByMember(memberId);
            return ResponseEntity.ok(investments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar total de investimentos por organização
    @GetMapping("/organization/{organizationId}/total")
    public ResponseEntity<Long> getTotalAmountByOrganization(@PathVariable String organizationId) {
        try {
            Long total = investmentService.getTotalAmountByOrganization(organizationId);
            return ResponseEntity.ok(total);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST - Criar novo investimento
    @PostMapping
    public ResponseEntity<Investment> createInvestment(@RequestBody InvestmentRequest request) {
        try {
            Investment investment = investmentService.createInvestment(
                request.getOrganizationId(),
                request.getMemberId(),
                request.getName(),
                request.getCategory(),
                request.getAmount(),
                request.getPurchaseDate(),
                request.getDescription()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(investment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // PUT - Atualizar investimento
    @PutMapping("/{id}")
    public ResponseEntity<Investment> updateInvestment(@PathVariable String id, @RequestBody InvestmentUpdateRequest request) {
        try {
            Investment updatedInvestment = investmentService.updateInvestment(
                id,
                request.getName(),
                request.getCategory(),
                request.getAmount(),
                request.getPurchaseDate(),
                request.getDescription()
            );
            if (updatedInvestment != null) {
                return ResponseEntity.ok(updatedInvestment);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE - Deletar investimento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvestment(@PathVariable String id) {
        try {
            investmentService.deleteInvestment(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Classes internas para requests
    public static class InvestmentRequest {
        private String organizationId;
        private String memberId;
        private String name;
        private String category;
        private long amount;
        private long purchaseDate;
        private String description;

        // Getters e Setters
        public String getOrganizationId() { return organizationId; }
        public void setOrganizationId(String organizationId) { this.organizationId = organizationId; }
        public String getMemberId() { return memberId; }
        public void setMemberId(String memberId) { this.memberId = memberId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public long getAmount() { return amount; }
        public void setAmount(long amount) { this.amount = amount; }
        public long getPurchaseDate() { return purchaseDate; }
        public void setPurchaseDate(long purchaseDate) { this.purchaseDate = purchaseDate; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public static class InvestmentUpdateRequest {
        private String name;
        private String category;
        private long amount;
        private long purchaseDate;
        private String description;

        // Getters e Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public long getAmount() { return amount; }
        public void setAmount(long amount) { this.amount = amount; }
        public long getPurchaseDate() { return purchaseDate; }
        public void setPurchaseDate(long purchaseDate) { this.purchaseDate = purchaseDate; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}

