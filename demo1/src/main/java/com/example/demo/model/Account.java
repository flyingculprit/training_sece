package com.example.demo.model;

public class Account {
    private Long accountNumber;
    private String holderName;
    private Double balance;

    // Default constructor (Needed for JSON conversion)
    public Account() {}

    // Parameterized constructor
    public Account(Long accountNumber, String holderName, Double balance) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = balance;
    }

    // Getters and Setters
    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}


//package com.example.demo.model;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class Account {
//
//    private Long accountNumber;
//    private String holderName;
//    private Double balance;
//}