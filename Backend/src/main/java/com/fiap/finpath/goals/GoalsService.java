package com.fiap.finpath.goals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GoalsService {

    @Autowired
    private GoalsRepository goalsRepository;

    @Autowired
    private GoalsContributionsService goalsContributionsService;

    public Goals createGoal(String organizationId, long dueDate, String name, String description, long desiredAmount) {
        Goals newGoal = new Goals(organizationId, dueDate, name, description, desiredAmount);
        System.out.println("GoalsService: Creating new goal '" + newGoal.getName() + "' for organization " + organizationId);
        return goalsRepository.save(newGoal);
    }

    public Optional<Goals> getGoalById(String goalId) {
        System.out.println("GoalsService: Retrieving goal with ID: " + goalId);
        return goalsRepository.findById(goalId);
    }

    public Goals updateGoal(String goalId, long newDueDate, String newName, String newDescription, long newDesiredAmount) {
        System.out.println("GoalsService: Updating goal with ID: " + goalId + ". New name: " + newName);
        Optional<Goals> optionalGoal = goalsRepository.findById(goalId);
        if (optionalGoal.isPresent()) {
            Goals goal = optionalGoal.get();
            goal.setDueDate(newDueDate);
            goal.setName(newName);
            goal.setDescription(newDescription);
            goal.setDesiredAmount(newDesiredAmount);
            return goalsRepository.save(goal);
        }
        return null;
    }

    public void deleteGoal(String goalId) {
        System.out.println("GoalsService: Deleting goal with ID: " + goalId);
        goalsRepository.deleteById(goalId);
    }

    public List<Goals> getGoalsByOrganization(String organizationId) {
        System.out.println("GoalsService: Retrieving all goals for organization " + organizationId);
        return goalsRepository.findByOrganizationId(organizationId);
    }

    public List<Goals> getAllGoals() {
        return goalsRepository.findAll();
    }

    public List<Goals> getGoalsByDueDateRange(long startDate, long endDate) {
        return goalsRepository.findByDueDateBetween(startDate, endDate);
    }

    public Long getTotalDesiredAmountByOrganization(String organizationId) {
        Long total = goalsRepository.getTotalDesiredAmountByOrganization(organizationId);
        return total != null ? total : 0L;
    }

    public double getGoalProgress(String goalId) {
        Optional<Goals> optionalGoal = goalsRepository.findById(goalId);
        if (optionalGoal.isPresent()) {
            Goals goal = optionalGoal.get();
            Long contributions = goalsContributionsService.getTotalContributionsByGoal(goalId);
            return contributions != null ? (contributions.doubleValue() / goal.getDesiredAmount()) * 100 : 0.0;
        }
        return 0.0;
    }
}