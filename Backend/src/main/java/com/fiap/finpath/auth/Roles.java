package com.fiap.finpath.auth;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

@Entity
@Table(name = "ROLE_AUTH")
public class Roles {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @NotBlank
    @Column(name = "name", length = 100, nullable = false, unique = true)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    // Construtor padrão para JPA
    public Roles() {}

    public Roles(String name, String description) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
    }

    public Roles(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    //* Getter Methods *//
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }

    //* Setter Methods *//
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
