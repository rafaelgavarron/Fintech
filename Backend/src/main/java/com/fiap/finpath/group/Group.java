package com.fiap.finpath.group;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "GROUPS")
public class Group {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @NotNull
    @Column(name = "organization_id", length = 36, nullable = false)
    private String organizationId;

    @NotBlank
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    // Construtor padrão para JPA
    public Group() {}

    public Group(String organizationId, String name, String description) {
        this.id = UUID.randomUUID().toString();
        this.organizationId = organizationId;
        this.name = name;
    }

    public Group(String id, String organizationId, String name, String description) {
        this.id = id;
        this.organizationId = organizationId;
        this.name = name;
    }

    //* Getter Methods *//
    public String getId() {
        return id;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    //* Setter Methods *//
    public void setDescription(String description) {
        this.description = description;
    }
    public void setName(String name) {
        this.name = name;
    }
}
