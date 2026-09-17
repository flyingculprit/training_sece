package com.example.demo.service;

import com.example.demo.model.Account;
import com.example.demo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    // 1. Customer Signup (Check for duplicate account ID)
    public Account signup(Account account) {
        if (account.getAccountNumber() == null || accountRepository.existsById(account.getAccountNumber())) {
            return null; // Duplicate or invalid account ID
        }
        if (account.getBalance() == null || account.getBalance() < 0) {
            account.setBalance(0.0);
        }
        return accountRepository.save(account);
    }

    // 2. Customer Login Verification
    public Account login(Long accountNumber, String password) {
        Optional<Account> accOpt = accountRepository.findById(accountNumber);
        if (accOpt.isPresent() && accOpt.get().getPassword().equals(password)) {
            return accOpt.get();
        }
        return null;
    }

    // 3. Get Account by ID
    public Account getAccount(Long accountNumber) {
        return accountRepository.findById(accountNumber).orElse(null);
    }

    // 4. Deposit Money
    public Account deposit(Long accountNumber, Double amount) {
        Account account = getAccount(accountNumber);
        if (account != null && amount != null && amount > 0) {
            account.setBalance(account.getBalance() + amount);
            return accountRepository.save(account);
        }
        return null;
    }

    // 5. Withdraw Money (With Overdraft Guard)
    public Account withdraw(Long accountNumber, Double amount) {
        Account account = getAccount(accountNumber);
        if (account != null && amount != null && amount > 0 && account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);
            return accountRepository.save(account);
        }
        return null;
    }
}
