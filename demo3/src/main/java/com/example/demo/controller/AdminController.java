package com.example.bank.controller;

import com.example.bank.model.Account;
import com.example.bank.model.Admin;
import com.example.bank.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // POST /api/admin/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String password = payload.get("password");

        Admin admin = adminService.login(username, password);
        if (admin != null) {
            return ResponseEntity.ok(admin);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Admin Username or Password");
    }

    // GET /api/admin/accounts
    @GetMapping("/accounts")
    public List<Account> getAllAccounts() {
        return adminService.getAllAccounts();
    }

    // GET /api/admin/vault
    @GetMapping("/vault")
    public Map<String, Object> getVaultMetrics() {
        return adminService.getVaultMetrics();
    }

    // DELETE /api/admin/accounts/{accountNumber}
    @DeleteMapping("/accounts/{accountNumber}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long accountNumber) {
        boolean deleted = adminService.deleteAccount(accountNumber);
        if (deleted) {
            return ResponseEntity.ok("Account #" + accountNumber + " closed successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
    }
}
