package com.fiap.finpath.goals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/goals-contributions")
@CrossOrigin(origins = "*")
public class GoalsContributionsController {

    @Autowired
    private GoalsContributionsService goalsContributionsService;

    // GET - Consultar todas as contribuições
    @GetMapping
    public ResponseEntity<List<GoalsContributions>> getAllContributions() {
        try {
            List<GoalsContributions> contributions = goalsContributionsService.getAllContributions();
            return ResponseEntity.ok(contributions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar contribuição por ID
    @GetMapping("/{id}")
    public ResponseEntity<GoalsContributions> getContributionById(@PathVariable String id) {
        try {
            Optional<GoalsContributions> contribution = goalsContributionsService.getContributionsById(id);
            if (contribution.isPresent()) {
                return ResponseEntity.ok(contribution.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar contribuições por meta
    @GetMapping("/goal/{goalId}")
    public ResponseEntity<List<GoalsContributions>> getContributionsByGoal(@PathVariable String goalId) {
        try {
            List<GoalsContributions> contributions = goalsContributionsService.getContributionsByGoal(goalId);
            return ResponseEntity.ok(contributions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar contribuições por período
    @GetMapping("/date-range")
    public ResponseEntity<List<GoalsContributions>> getContributionsByDateRange(
            @RequestParam long startDate,
            @RequestParam long endDate) {
        try {
            List<GoalsContributions> contributions = goalsContributionsService.getAllContributions().stream()
                    .filter(c -> c.getContributionDate() >= startDate && c.getContributionDate() <= endDate)
                    .toList();
            return ResponseEntity.ok(contributions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar total de contribuições por meta
    @GetMapping("/goal/{goalId}/total")
    public ResponseEntity<Long> getTotalContributionsByGoal(@PathVariable String goalId) {
        try {
            Long total = goalsContributionsService.getTotalContributionsByGoal(goalId);
            return ResponseEntity.ok(total);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar total de contribuições por organização
    @GetMapping("/organization/{organizationId}/total")
    public ResponseEntity<Long> getTotalContributionsByOrganization(@PathVariable String organizationId) {
        try {
            Long total = goalsContributionsService.getTotalContributionsByOrganization(organizationId);
            return ResponseEntity.ok(total);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST - Criar nova contribuição
    @PostMapping
    public ResponseEntity<GoalsContributions> createContribution(@RequestBody ContributionRequest request) {
        try {
            GoalsContributions contribution = goalsContributionsService.addContribution(
                request.getGoalId(),
                request.getContributionDate(),
                request.getValue(),
                request.getDescription()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(contribution);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // PUT - Atualizar contribuição
    @PutMapping("/{id}")
    public ResponseEntity<GoalsContributions> updateContribution(@PathVariable String id, @RequestBody ContributionUpdateRequest request) {
        try {
            GoalsContributions updatedContribution = goalsContributionsService.updateContribution(
                id,
                request.getContributionDate(),
                request.getValue(),
                request.getDescription()
            );
            if (updatedContribution != null) {
                return ResponseEntity.ok(updatedContribution);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE - Deletar contribuição
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContribution(@PathVariable String id) {
        try {
            goalsContributionsService.deleteContribution(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Classes internas para requests
    public static class ContributionRequest {
        private String goalId;
        private long contributionDate;
        private long value;
        private String description;

        // Getters e Setters
        public String getGoalId() { return goalId; }
        public void setGoalId(String goalId) { this.goalId = goalId; }
        public long getContributionDate() { return contributionDate; }
        public void setContributionDate(long contributionDate) { this.contributionDate = contributionDate; }
        public long getValue() { return value; }
        public void setValue(long value) { this.value = value; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public static class ContributionUpdateRequest {
        private long contributionDate;
        private long value;
        private String description;

        // Getters e Setters
        public long getContributionDate() { return contributionDate; }
        public void setContributionDate(long contributionDate) { this.contributionDate = contributionDate; }
        public long getValue() { return value; }
        public void setValue(long value) { this.value = value; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}
