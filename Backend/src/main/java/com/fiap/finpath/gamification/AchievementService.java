package com.fiap.finpath.gamification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AchievementService {

    @Autowired
    private AchievementsRepository achievementsRepository;

    public Achievements createAchievement(String name, String description, String iconUrl) {
        Achievements newAchievement = new Achievements(name, description, iconUrl);
        System.out.println("AchievementService: Creating new achievement '" + newAchievement.getName() + "' with ID: " + newAchievement.getId());
        return achievementsRepository.save(newAchievement);
    }

    public Optional<Achievements> getAchievementById(String achievementId) {
        System.out.println("AchievementService: Retrieving achievement with ID: " + achievementId);
        return achievementsRepository.findById(achievementId);
    }

    public Achievements updateAchievement(String achievementId, String name, String description, String iconUrl) {
        System.out.println("AchievementService: Updating achievement with ID: " + achievementId);
        Optional<Achievements> optionalAchievement = achievementsRepository.findById(achievementId);
        if (optionalAchievement.isPresent()) {
            Achievements achievement = optionalAchievement.get();
            achievement.setName(name);
            achievement.setDescription(description);
            achievement.setIconUrl(iconUrl);
            return achievementsRepository.save(achievement);
        }
        return null;
    }

    public void deleteAchievement(String achievementId) {
        System.out.println("AchievementService: Deleting achievement with ID: " + achievementId);
        achievementsRepository.deleteById(achievementId);
    }

    public List<Achievements> getAllAchievements() {
        return achievementsRepository.findAll();
    }

    public List<Achievements> searchAchievementsByName(String name) {
        return achievementsRepository.findByNameContaining(name);
    }
}