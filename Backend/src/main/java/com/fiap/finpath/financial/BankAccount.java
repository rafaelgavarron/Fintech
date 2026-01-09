package com.fiap.finpath.financial;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "BANK_ACCOUNT")
public class BankAccount {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @NotNull
    @Column(name = "organization_id", length = 36, nullable = false)
    private String organizationId;

    @NotNull
    @Column(name = "member_id", length = 36, nullable = false)
    private String memberId;

    @NotBlank
    @Column(name = "bank_name", length = 255, nullable = false)
    private String bankName;

    @Column(name = "access_token", length = 500)
    private String accessToken;

    @Column(name = "refresh_token", length = 500)
    private String refreshToken;

    @Column(name = "token_expire_at")
    private long tokenExpireAt;

    @NotNull
    @Column(name = "is_connected", nullable = false)
    private boolean isConnected;

    @Column(name = "last_sync_at")
    private long lastSyncAt;

    // Construtor padrão para JPA
    public BankAccount() {}

    public BankAccount(String organizationId, String memberId, String bankName, String accessToken) {
        this.id = UUID.randomUUID().toString();
        this.organizationId = organizationId;
        this.memberId = memberId;
        this.bankName = bankName;
        this.accessToken = accessToken;
        this.isConnected = false;
    }

    public BankAccount(String id, String organizationId, String memberId, String bankName, String accessToken, String refreshToken, long tokenExpireAt, boolean isConnected, long lastSyncAt) {
        this.id = id;
        this.organizationId = organizationId;
        this.memberId = memberId;
        this.bankName = bankName;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenExpireAt = tokenExpireAt;
        this.isConnected = isConnected;
        this.lastSyncAt = lastSyncAt;
    }

    //* Getter Methods * //
    public String getId() {
        return id;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getBankName() {
        return bankName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public long getTokenExpireAt() {
        return tokenExpireAt;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public long getLastSyncAt() {
        return lastSyncAt;
    }

    //* Setter Methods * //
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setTokenExpireAt(long tokenExpireAt) {
        this.tokenExpireAt = tokenExpireAt;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public void setLastSyncAt(long lastSyncAt) {
        this.lastSyncAt = lastSyncAt;
    }
}