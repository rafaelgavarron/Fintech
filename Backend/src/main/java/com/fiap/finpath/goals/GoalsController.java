package com.fiap.finpath.goals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/goals")
@CrossOrigin(origins = "*")
public class GoalsController {

    @Autowired
    private GoalsService goalsService;

    // GET - Consultar todas as metas
    @GetMapping
    public ResponseEntity<List<Goals>> getAllGoals() {
        try {
            List<Goals> goals = goalsService.getAllGoals();
            return ResponseEntity.ok(goals);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar meta por ID
    @GetMapping("/{id}")
    public ResponseEntity<Goals> getGoalById(@PathVariable String id) {
        try {
            Optional<Goals> goal = goalsService.getGoalById(id);
            if (goal.isPresent()) {
                return ResponseEntity.ok(goal.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar metas por organização
    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<Goals>> getGoalsByOrganization(@PathVariable String organizationId) {
        try {
            List<Goals> goals = goalsService.getGoalsByOrganization(organizationId);
            return ResponseEntity.ok(goals);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar metas por período de vencimento
    @GetMapping("/due-date-range")
    public ResponseEntity<List<Goals>> getGoalsByDueDateRange(
            @RequestParam long startDate,
            @RequestParam long endDate) {
        try {
            List<Goals> goals = goalsService.getGoalsByDueDateRange(startDate, endDate);
            return ResponseEntity.ok(goals);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar total de valor desejado por organização
    @GetMapping("/organization/{organizationId}/total")
    public ResponseEntity<Long> getTotalDesiredAmountByOrganization(@PathVariable String organizationId) {
        try {
            Long total = goalsService.getTotalDesiredAmountByOrganization(organizationId);
            return ResponseEntity.ok(total);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST - Criar nova meta
    @PostMapping
    public ResponseEntity<Goals> createGoal(@RequestBody GoalsRequest request) {
        try {
            Goals goal = goalsService.createGoal(
                request.getOrganizationId(),
                request.getDueDate(),
                request.getName(),
                request.getDescription(),
                request.getDesiredAmount()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(goal);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // PUT - Atualizar meta
    @PutMapping("/{id}")
    public ResponseEntity<Goals> updateGoal(@PathVariable String id, @RequestBody GoalsUpdateRequest request) {
        try {
            Goals updatedGoal = goalsService.updateGoal(
                id,
                request.getDueDate(),
                request.getName(),
                request.getDescription(),
                request.getDesiredAmount()
            );
            if (updatedGoal != null) {
                return ResponseEntity.ok(updatedGoal);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE - Deletar meta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable String id) {
        try {
            goalsService.deleteGoal(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Classes internas para requests
    public static class GoalsRequest {
        private String organizationId;
        private long dueDate;
        private String name;
        private String description;
        private long desiredAmount;

        // Getters e Setters
        public String getOrganizationId() { return organizationId; }
        public void setOrganizationId(String organizationId) { this.organizationId = organizationId; }
        public long getDueDate() { return dueDate; }
        public void setDueDate(long dueDate) { this.dueDate = dueDate; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public long getDesiredAmount() { return desiredAmount; }
        public void setDesiredAmount(long desiredAmount) { this.desiredAmount = desiredAmount; }
    }

    public static class GoalsUpdateRequest {
        private long dueDate;
        private String name;
        private String description;
        private long desiredAmount;

        // Getters e Setters
        public long getDueDate() { return dueDate; }
        public void setDueDate(long dueDate) { this.dueDate = dueDate; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public long getDesiredAmount() { return desiredAmount; }
        public void setDesiredAmount(long desiredAmount) { this.desiredAmount = desiredAmount; }
    }
}
