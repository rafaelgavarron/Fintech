package com.fiap.finpath.organization;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "MEMBER")
public class Member {
    @Id
    @Column(name = "id", length = 36)
    private String id;

    @NotNull
    @Column(name = "organization_id", length = 36, nullable = false)
    private String organizationId;

    @NotNull
    @Column(name = "user_id", length = 36, nullable = false)
    private String userId;

    @NotNull
    @Column(name = "role_id", nullable = false)
    private String roleId;

    // Construtor padrão necessário para JPA
    public Member() {
    }

    public Member(String organizationId, String userId, String roleId) {
        this.id = UUID.randomUUID().toString();
        this.organizationId = organizationId;
        this.userId = userId;
        this.roleId = roleId;
    }

    public Member(String id, String organizationId, String userId, String roleId) {
        this.id = id;
        this.organizationId = organizationId;
        this.userId = userId;
        this.roleId = roleId;
    }

    //* Getter Methods *//
    public String getId() {
        return id;
    }
    public String getOrganizationId() {
        return organizationId;
    }
    public String getUserId() {
        return userId;
    }
    public String getRoleId() {
        return roleId;
    }

    //* Setter Methods *//
    public void setId(String id) {
        this.id = id;
    }
    
    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
