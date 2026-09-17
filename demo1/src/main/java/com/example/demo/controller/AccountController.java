package com.example.demo.controller;

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

    // Welcome
    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to NextGen Bank Management System!";
    }

    // CREATE
    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        return accountService.createAccount(account);
    }

    // READ - Single account
    @GetMapping("/{accountNumber}")
    public Account getAccount(@PathVariable Long accountNumber) {
        return accountService.getAccountByNumber(accountNumber);
    }

    // READ - All accounts
    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    // DELETE
//    @DeleteMapping("/{accountNumber}")
//    public String deleteAccount(@PathVariable Long accountNumber) {
//
//        boolean deleted = accountService.deleteAccount(accountNumber);
//
//        if (deleted) {
//            return "Account deleted successfully";
//        }
//
//        return "Account not found";
//    }
}