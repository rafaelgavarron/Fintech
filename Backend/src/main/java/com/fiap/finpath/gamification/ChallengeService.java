package com.fiap.finpath.gamification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChallengeService {

    @Autowired
    private ChallengesRepository challengesRepository;

    public Challenges createChallenge(String achievementId, Challenges.DifficultyLevel level, String name, String description, Challenges.ChallengeType type, long points) {
        Challenges newChallenge = new Challenges(achievementId, level, name, description, type, points);
        System.out.println("ChallengeService: Creating new " + newChallenge.getLevel() + " challenge named '" + newChallenge.getName() + "'");
        return challengesRepository.save(newChallenge);
    }

    public Optional<Challenges> getChallengeById(String challengeId) {
        System.out.println("ChallengeService: Retrieving challenge with ID: " + challengeId);
        return challengesRepository.findById(challengeId);
    }

    public Challenges updateChallenge(String challengeId, String achievementId, Challenges.DifficultyLevel level, String name, String description, Challenges.ChallengeType type, long points) {
        System.out.println("ChallengeService: Updating challenge with ID: " + challengeId);
        Optional<Challenges> optionalChallenge = challengesRepository.findById(challengeId);
        if (optionalChallenge.isPresent()) {
            Challenges challenge = optionalChallenge.get();
            challenge.setAchievementId(achievementId);
            challenge.setLevel(level);
            challenge.setName(name);
            challenge.setDescription(description);
            challenge.setChallengeType(type);
            challenge.setPoints(points);
            return challengesRepository.save(challenge);
        }
        return null;
    }

    public void deleteChallenge(String challengeId) {
        System.out.println("ChallengeService: Deleting challenge with ID: " + challengeId);
        challengesRepository.deleteById(challengeId);
    }

    public List<Challenges> getAllChallenges() {
        return challengesRepository.findAll();
    }

    public List<Challenges> getChallengesByType(Challenges.ChallengeType type) {
        System.out.println("ChallengeService: Retrieving all challenges of type " + type);
        return challengesRepository.findByChallengeType(type);
    }

    public List<Challenges> getChallengesByLevel(Challenges.DifficultyLevel level) {
        return challengesRepository.findByLevel(level);
    }

    public List<Challenges> getChallengesByLevelAndType(Challenges.DifficultyLevel level, Challenges.ChallengeType type) {
        return challengesRepository.findByLevelAndType(level, type);
    }

    public Long getTotalPointsByAchievement(String achievementId) {
        return challengesRepository.getTotalPointsByAchievement(achievementId);
    }
}