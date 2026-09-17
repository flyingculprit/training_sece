package com.example.bank.controller;

import com.example.demo.model.Account;
import com.example.demo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    // 1. Welcome / Health Check
    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to NextGen Bank Management System!";
    }

    // 2. CREATE - Open Account
    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        return accountService.createAccount(account);
    }

    // 3. READ - Single Account by Number
    @GetMapping("/{accountNumber}")
    public Account getAccount(@PathVariable Long accountNumber) {
        return accountService.getAccountByNumber(accountNumber);
    }

    // 4. READ - All Accounts
    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    // 5. UPDATE - Deposit Money
    @PutMapping("/{accountNumber}/deposit")
    public Account deposit(@PathVariable Long accountNumber, @RequestParam Double amount) {
        return accountService.deposit(accountNumber, amount);
    }

    // 6. UPDATE - Withdraw Money
    @PutMapping("/{accountNumber}/withdraw")
    public Account withdraw(@PathVariable Long accountNumber, @RequestParam Double amount) {
        return accountService.withdraw(accountNumber, amount);
    }

    // 7. DELETE - Close Account
    @DeleteMapping("/{accountNumber}")
    public String deleteAccount(@PathVariable Long accountNumber) {
        boolean deleted = accountService.deleteAccount(accountNumber);
        if (deleted) {
            return "Account " + accountNumber + " deleted successfully";
        }
        return "Account not found";
    }
}