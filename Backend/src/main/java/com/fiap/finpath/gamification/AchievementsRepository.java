package com.fiap.finpath.gamification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchievementsRepository extends JpaRepository<Achievements, String> {

    List<Achievements> findByNameContaining(String name);

    List<Achievements> findByDescriptionContaining(String description);
}
