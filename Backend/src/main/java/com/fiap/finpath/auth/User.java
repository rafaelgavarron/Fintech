package com.fiap.finpath.auth;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

@Entity
@Table(name = "USER_AUTH")
public class User {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @NotBlank
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @NotBlank
    @Email
    @Column(name = "email", length = 255, nullable = false, unique = true)
    private String email;

    @NotBlank
    @Size(max = 255)
    @Column(name = "password_hash", length = 255, nullable = false)
    private String passwordHash;

    @Column(name = "verified", nullable = false)
    private boolean isVerified;

    // Construtor padrão para JPA
    public User() {}

    public User(String name, String email, String passwordHash) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.isVerified = false;
    }

    public User(String id, String name, String email, String passwordHash, boolean isVerified) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.isVerified = isVerified;
    }

    //* Getter Methods *//
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public boolean isVerified() {
        return isVerified;
    }

    //* Setter Methods *//
    public void setName(String name) {
        this.name = name;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }
}
