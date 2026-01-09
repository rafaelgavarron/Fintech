package com.fiap.finpath.auth;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "VERIFICATION_CODE")
public class VerificationCode {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @NotBlank
    @Email
    @Column(name = "user_email", length = 255, nullable = false)
    private String userEmail;

    @NotNull
    @Column(name = "expire_at", nullable = false)
    private long expireAt;

    @NotBlank
    @Column(name = "code_text", length = 10, nullable = false)
    private String codeText;

    @NotNull
    @Column(name = "is_used", nullable = false)
    private boolean isUsed;

    // Construtor padrão para JPA
    public VerificationCode() {}

    public VerificationCode(String userEmail) {
        this.id = UUID.randomUUID().toString();
        this.userEmail = userEmail;

        // Obtém o timestamp Unix atual em segundos
        long currentTimestamp = Instant.now().getEpochSecond();
        // Adiciona 1 hora (3600 segundos) ao timestamp atual
        this.expireAt = currentTimestamp + 3600;

        this.codeText = generateSecureCode(6);
        this.isUsed = false;
    }

    public VerificationCode(String id, String userEmail, long expireAt, String codeText, boolean isUsed) {
        this.id = id;
        this.userEmail = userEmail;
        this.expireAt = expireAt;
        this.codeText = codeText;
        this.isUsed = isUsed;
    }

    //* Getter Methods * //
    public String getId() {
        return id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public long getExpireAt() {
        return expireAt;
    }

    public String getCodeText() {
        return codeText;
    }

    public boolean isUsed() {
        return isUsed;
    }

    //* Setter Methods *//
    public void setUsed(boolean used) {
        isUsed = used;
    }

    // CodeText Generation Algorithm //
    private String generateSecureCode(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder(length);
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        for (int i = 0; i < 6; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }
        return code.toString();
    }
}
