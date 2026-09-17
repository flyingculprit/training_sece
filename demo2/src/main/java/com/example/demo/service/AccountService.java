package com.example.demo.service;

import com.example.demo.model.Account;
import com.example.demo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    // CREATE (Section 12)
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    // READ - Single account (Section 13)
    public Account getAccountByNumber(Long accountNumber) {
        Optional<Account> optionalAccount = accountRepository.findById(accountNumber);
        return optionalAccount.orElse(null);
    }

    // READ - All accounts (Section 13)
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    // UPDATE - Deposit money (Section 14)
    public Account deposit(Long accountNumber, Double amount) {
        Account account = getAccountByNumber(accountNumber);
        if (account != null && amount > 0) {
            account.setBalance(account.getBalance() + amount);
            return accountRepository.save(account);
        }
        return null;
    }

    // UPDATE - Withdraw money (Section 14)
    public Account withdraw(Long accountNumber, Double amount) {
        Account account = getAccountByNumber(accountNumber);
        if (account != null && account.getBalance() >= amount && amount > 0) {
            account.setBalance(account.getBalance() - amount);
            return accountRepository.save(account);
        }
        return null;
    }

    // DELETE (Section 14)
    public boolean deleteAccount(Long accountNumber) {
        if (accountRepository.existsById(accountNumber)) {
            accountRepository.deleteById(accountNumber);
            return true;
        }
        return false;
    }
}