package com.fiap.finpath.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActionRepository extends JpaRepository<Action, String> {

    Optional<Action> findByName(String name);

    boolean existsByName(String name);
}
