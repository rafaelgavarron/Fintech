package com.fiap.finpath.gamification;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

@Entity
@Table(name = "ACHIEVEMENTS")
public class Achievements {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @NotBlank
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "icon_url", length = 500)
    private String iconUrl;

    // Construtor padrão para JPA
    public Achievements() {}

    public Achievements(String name, String description, String iconUrl) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.iconUrl = iconUrl;
    }

    public Achievements(String id, String name, String description, String iconUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.iconUrl = iconUrl;
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
    public String getIconUrl() {
        return iconUrl;
    }

    //* Setter Methods *//
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
