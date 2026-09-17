package com.example.demo.controller;

import com.example.demo.model.Account;
import com.example.demo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/account")
@CrossOrigin(origins = "*")
public class AccountController {

    @Autowired
    private AccountService accountService;

    // POST /api/account/signup
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Account account) {
        if (account.getAccountNumber() == null || account.getHolderName() == null || account.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Account Number, Name, and Password are required!");
        }

        if (accountService.getAccount(account.getAccountNumber()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Account #" + account.getAccountNumber() + " already exists. Please choose a different number.");
        }

        Account created = accountService.signup(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // POST /api/account/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, Object> payload) {
        try {
            Long accountNumber = Long.parseLong(payload.get("accountNumber").toString());
            String password = payload.get("password").toString();

            Account account = accountService.login(accountNumber, password);
            if (account != null) {
                return ResponseEntity.ok(account);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Account Number or Password");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid login payload");
        }
    }

    // GET /api/account/{accountNumber}/balance
    @GetMapping("/{accountNumber}/balance")
    public ResponseEntity<?> getBalance(@PathVariable Long accountNumber) {
        Account account = accountService.getAccount(accountNumber);
        if (account != null) {
            return ResponseEntity.ok(account);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
    }

    // PUT /api/account/{accountNumber}/deposit?amount=5000
    @PutMapping("/{accountNumber}/deposit")
    public ResponseEntity<?> deposit(@PathVariable Long accountNumber, @RequestParam Double amount) {
        Account account = accountService.deposit(accountNumber, amount);
        if (account != null) {
            return ResponseEntity.ok(account);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Deposit failed. Enter a valid positive amount.");
    }

    // PUT /api/account/{accountNumber}/withdraw?amount=2000
    @PutMapping("/{accountNumber}/withdraw")
    public ResponseEntity<?> withdraw(@PathVariable Long accountNumber, @RequestParam Double amount) {
        Account account = accountService.withdraw(accountNumber, amount);
        if (account != null) {
            return ResponseEntity.ok(account);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Withdrawal failed. Insufficient funds or invalid amount.");
    }
}
