package com.benjaminknust.payments;

import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferService {

    private final AccountRepository repository;

    public TransferService(AccountRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        if (fromId.equals(toId)) {
            throw new InvalidTransferException("Cannot transfer to the same account");
        }
        Account from = repository.findById(fromId)
                .orElseThrow(() -> new AccountNotFoundException(fromId));
        Account to = repository.findById(toId)
                .orElseThrow(() -> new AccountNotFoundException(toId));

        if (from.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }

        from.withdraw(amount);
        to.deposit(amount);
    }
}
