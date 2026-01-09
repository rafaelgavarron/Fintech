package com.fiap.finpath.financial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    public BankAccount connectBankAccount(String organizationId, String memberId, String bankName, String accessToken) {
        BankAccount newAccount = new BankAccount(organizationId, memberId, bankName, accessToken);
        newAccount.setConnected(true);
        newAccount.setLastSyncAt(System.currentTimeMillis() / 1000);
        System.out.println("BankAccountService: Connecting new bank account for member " + memberId + " in organization " + organizationId);
        return bankAccountRepository.save(newAccount);
    }

    public Optional<BankAccount> getBankAccountById(String bankAccountId) {
        System.out.println("BankAccountService: Retrieving bank account with ID: " + bankAccountId);
        return bankAccountRepository.findById(bankAccountId);
    }

    public List<BankAccount> getBankAccountsByMember(String memberId) {
        System.out.println("BankAccountService: Retrieving all bank accounts for member " + memberId);
        return bankAccountRepository.findByMemberId(memberId);
    }

    public List<BankAccount> getBankAccountsByOrganization(String organizationId) {
        return bankAccountRepository.findByOrganizationId(organizationId);
    }

    public BankAccount disconnectBankAccount(String bankAccountId) {
        System.out.println("BankAccountService: Disconnecting bank account with ID: " + bankAccountId);
        Optional<BankAccount> optionalAccount = bankAccountRepository.findById(bankAccountId);
        if (optionalAccount.isPresent()) {
            BankAccount account = optionalAccount.get();
            account.setConnected(false);
            account.setAccessToken(null);
            account.setRefreshToken(null);
            return bankAccountRepository.save(account);
        }
        return null;
    }

    public BankAccount refreshAccessToken(String bankAccountId, String newAccessToken, String newRefreshToken, long newExpireAt) {
        System.out.println("BankAccountService: Refreshing access token for account " + bankAccountId);
        Optional<BankAccount> optionalAccount = bankAccountRepository.findById(bankAccountId);
        if (optionalAccount.isPresent()) {
            BankAccount account = optionalAccount.get();
            account.setAccessToken(newAccessToken);
            account.setRefreshToken(newRefreshToken);
            account.setTokenExpireAt(newExpireAt);
            return bankAccountRepository.save(account);
        }
        return null;
    }

    public BankAccount syncAccountTransactions(String bankAccountId) {
        System.out.println("BankAccountService: Syncing transactions for account " + bankAccountId);
        Optional<BankAccount> optionalAccount = bankAccountRepository.findById(bankAccountId);
        if (optionalAccount.isPresent()) {
            BankAccount account = optionalAccount.get();
            account.setLastSyncAt(System.currentTimeMillis() / 1000);
            return bankAccountRepository.save(account);
        }
        return null;
    }

    public List<BankAccount> getConnectedAccounts(String organizationId) {
        return bankAccountRepository.findConnectedAccountsByOrganization(organizationId);
    }

    public List<BankAccount> getAllBankAccounts() {
        return bankAccountRepository.findAll();
    }
}