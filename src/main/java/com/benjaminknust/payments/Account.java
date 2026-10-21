package com.benjaminknust.payments;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Account {

    @Id
    @GeneratedValue
    private Long id;
    private String owner;
    private BigDecimal balance;

    protected Account() {}

    public Account(String owner, BigDecimal balance) {
        this.owner = owner;
        this.balance = balance;
    }

    public Long getId() { return id; }
    public String getOwner() { return owner; }
    public BigDecimal getBalance() { return balance; }
}