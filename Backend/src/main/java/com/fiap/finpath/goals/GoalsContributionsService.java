package com.fiap.finpath.goals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GoalsContributionsService {

    @Autowired
    private GoalsContributionsRepository goalsContributionsRepository;

    public GoalsContributions addContribution(String goalId, long contributionDate, long value, String description) {
        GoalsContributions newContribution = new GoalsContributions(goalId, contributionDate, value, description);
        System.out.println("GoalsContributionsService: Adding a contribution of " + newContribution.getValue() + " to goal " + goalId);
        return goalsContributionsRepository.save(newContribution);
    }

    public Optional<GoalsContributions> getContributionsById(String contributionId) {
        System.out.println("GoalsContributionsService: Retrieving contribution for ID " + contributionId);
        return goalsContributionsRepository.findById(contributionId);
    }

    public List<GoalsContributions> getContributionsByGoal(String goalId) {
        System.out.println("GoalsContributionsService: Retrieving all contributions for goal " + goalId);
        return goalsContributionsRepository.findByGoalId(goalId);
    }

    public GoalsContributions updateContribution(String contributionId, long newContributionDate, long newValue, String newDescription) {
        System.out.println("GoalsContributionsService: Updating contribution with ID: " + contributionId);
        Optional<GoalsContributions> optionalContribution = goalsContributionsRepository.findById(contributionId);
        if (optionalContribution.isPresent()) {
            GoalsContributions contribution = optionalContribution.get();
            contribution.setContributionDate(newContributionDate);
            contribution.setValue(newValue);
            contribution.setDescription(newDescription);
            return goalsContributionsRepository.save(contribution);
        }
        return null;
    }

    public void deleteContribution(String contributionId) {
        System.out.println("GoalsContributionsService: Deleting contribution with ID: " + contributionId);
        goalsContributionsRepository.deleteById(contributionId);
    }

    public List<GoalsContributions> getAllContributions() {
        return goalsContributionsRepository.findAll();
    }

    public Long getTotalContributionsByGoal(String goalId) {
        Long total = goalsContributionsRepository.getTotalContributionsByGoal(goalId);
        return total != null ? total : 0L;
    }

    public Long getTotalContributionsByOrganization(String organizationId) {
        Long total = goalsContributionsRepository.getTotalContributionsByOrganization(organizationId);
        return total != null ? total : 0L;
    }
}