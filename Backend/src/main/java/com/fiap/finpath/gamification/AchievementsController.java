package com.fiap.finpath.gamification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/achievements")
@CrossOrigin(origins = "*")
public class AchievementsController {

    @Autowired
    private AchievementService achievementService;

    // GET - Consultar todas as conquistas
    @GetMapping
    public ResponseEntity<List<Achievements>> getAllAchievements() {
        try {
            List<Achievements> achievements = achievementService.getAllAchievements();
            return ResponseEntity.ok(achievements);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar conquista por ID
    @GetMapping("/{id}")
    public ResponseEntity<Achievements> getAchievementById(@PathVariable String id) {
        try {
            Optional<Achievements> achievement = achievementService.getAchievementById(id);
            if (achievement.isPresent()) {
                return ResponseEntity.ok(achievement.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Buscar conquistas por nome
    @GetMapping("/search")
    public ResponseEntity<List<Achievements>> searchAchievementsByName(@RequestParam String name) {
        try {
            List<Achievements> achievements = achievementService.searchAchievementsByName(name);
            return ResponseEntity.ok(achievements);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST - Criar nova conquista
    @PostMapping
    public ResponseEntity<Achievements> createAchievement(@RequestBody AchievementRequest request) {
        try {
            Achievements achievement = achievementService.createAchievement(
                request.getName(),
                request.getDescription(),
                request.getIconUrl()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(achievement);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // PUT - Atualizar conquista
    @PutMapping("/{id}")
    public ResponseEntity<Achievements> updateAchievement(@PathVariable String id, @RequestBody AchievementUpdateRequest request) {
        try {
            Achievements updatedAchievement = achievementService.updateAchievement(
                id,
                request.getName(),
                request.getDescription(),
                request.getIconUrl()
            );
            if (updatedAchievement != null) {
                return ResponseEntity.ok(updatedAchievement);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE - Deletar conquista
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAchievement(@PathVariable String id) {
        try {
            achievementService.deleteAchievement(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Classes internas para requests
    public static class AchievementRequest {
        private String name;
        private String description;
        private String iconUrl;

        // Getters e Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getIconUrl() { return iconUrl; }
        public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }
    }

    public static class AchievementUpdateRequest {
        private String name;
        private String description;
        private String iconUrl;

        // Getters e Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getIconUrl() { return iconUrl; }
        public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }
    }
}
