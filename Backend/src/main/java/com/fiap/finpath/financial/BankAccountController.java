package com.fiap.finpath.financial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bank-accounts")
@CrossOrigin(origins = "*")
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    // GET - Consultar todas as contas bancárias
    @GetMapping
    public ResponseEntity<List<BankAccount>> getAllBankAccounts() {
        try {
            List<BankAccount> accounts = bankAccountService.getAllBankAccounts();
            return ResponseEntity.ok(accounts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar conta bancária por ID
    @GetMapping("/{id}")
    public ResponseEntity<BankAccount> getBankAccountById(@PathVariable String id) {
        try {
            Optional<BankAccount> account = bankAccountService.getBankAccountById(id);
            if (account.isPresent()) {
                return ResponseEntity.ok(account.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar contas bancárias por organização
    @GetMapping("/organization/{organizationId}")
    public ResponseEntity<List<BankAccount>> getBankAccountsByOrganization(@PathVariable String organizationId) {
        try {
            List<BankAccount> accounts = bankAccountService.getBankAccountsByOrganization(organizationId);
            return ResponseEntity.ok(accounts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar contas bancárias por membro
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<BankAccount>> getBankAccountsByMember(@PathVariable String memberId) {
        try {
            List<BankAccount> accounts = bankAccountService.getBankAccountsByMember(memberId);
            return ResponseEntity.ok(accounts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET - Consultar contas conectadas por organização
    @GetMapping("/organization/{organizationId}/connected")
    public ResponseEntity<List<BankAccount>> getConnectedAccounts(@PathVariable String organizationId) {
        try {
            List<BankAccount> accounts = bankAccountService.getConnectedAccounts(organizationId);
            return ResponseEntity.ok(accounts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST - Conectar nova conta bancária
    @PostMapping("/connect")
    public ResponseEntity<BankAccount> connectBankAccount(@RequestBody ConnectAccountRequest request) {
        try {
            BankAccount account = bankAccountService.connectBankAccount(
                request.getOrganizationId(),
                request.getMemberId(),
                request.getBankName(),
                request.getAccessToken()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(account);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // PUT - Desconectar conta bancária
    @PutMapping("/{id}/disconnect")
    public ResponseEntity<BankAccount> disconnectBankAccount(@PathVariable String id) {
        try {
            BankAccount account = bankAccountService.disconnectBankAccount(id);
            if (account != null) {
                return ResponseEntity.ok(account);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // PUT - Atualizar tokens de acesso
    @PutMapping("/{id}/refresh-token")
    public ResponseEntity<BankAccount> refreshAccessToken(@PathVariable String id, @RequestBody RefreshTokenRequest request) {
        try {
            BankAccount account = bankAccountService.refreshAccessToken(
                id,
                request.getAccessToken(),
                request.getRefreshToken(),
                request.getExpireAt()
            );
            if (account != null) {
                return ResponseEntity.ok(account);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // PUT - Sincronizar transações
    @PutMapping("/{id}/sync")
    public ResponseEntity<BankAccount> syncAccountTransactions(@PathVariable String id) {
        try {
            BankAccount account = bankAccountService.syncAccountTransactions(id);
            if (account != null) {
                return ResponseEntity.ok(account);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE - Deletar conta bancária
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBankAccount(@PathVariable String id) {
        try {
            bankAccountService.disconnectBankAccount(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Classes internas para requests
    public static class ConnectAccountRequest {
        private String organizationId;
        private String memberId;
        private String bankName;
        private String accessToken;

        // Getters e Setters
        public String getOrganizationId() { return organizationId; }
        public void setOrganizationId(String organizationId) { this.organizationId = organizationId; }
        public String getMemberId() { return memberId; }
        public void setMemberId(String memberId) { this.memberId = memberId; }
        public String getBankName() { return bankName; }
        public void setBankName(String bankName) { this.bankName = bankName; }
        public String getAccessToken() { return accessToken; }
        public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    }

    public static class RefreshTokenRequest {
        private String accessToken;
        private String refreshToken;
        private long expireAt;

        // Getters e Setters
        public String getAccessToken() { return accessToken; }
        public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
        public String getRefreshToken() { return refreshToken; }
        public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
        public long getExpireAt() { return expireAt; }
        public void setExpireAt(long expireAt) { this.expireAt = expireAt; }
    }
}
