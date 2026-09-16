package com.example.demo.service;

import com.example.demo.model.Account;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountService {

    // Simulating an in-memory database
    private Map<Long, Account> accountDatabase = new HashMap<>();

    // CREATE
    public Account createAccount(Account account) {
        accountDatabase.put(account.getAccountNumber(), account);
        return account;
    }

    // READ - Single account
    public Account getAccountByNumber(Long accountNumber) {
        return accountDatabase.get(accountNumber);
    }

    // READ - All accounts
    public List<Account> getAllAccounts() {
        return new ArrayList<>(accountDatabase.values());
    }

    // DELETE
//    public boolean deleteAccount(Long accountNumber) {
//
//        if (accountDatabase.containsKey(accountNumber)) {
//            accountDatabase.remove(accountNumber);
//            return true;
//        }
//
//        return false;
//    }
}