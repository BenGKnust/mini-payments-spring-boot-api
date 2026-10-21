package com.benjaminknust.payments;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    AccountRepository repository;

    @InjectMocks
    TransferService service;

    @Test
    void transfersMoneyBetweenAccounts() {
        Account anna = new Account("Anna", new BigDecimal("100"));
        Account ben = new Account("Ben", new BigDecimal("50"));
        when(repository.findById(1L)).thenReturn(Optional.of(anna));
        when(repository.findById(2L)).thenReturn(Optional.of(ben));

        service.transfer(1L, 2L, new BigDecimal("30"));

        assertThat(anna.getBalance()).isEqualByComparingTo("70");
        assertThat(ben.getBalance()).isEqualByComparingTo("80");
    }

    @Test
    void rejectsTransferWithInsufficientFunds() {
        Account anna = new Account("Anna", new BigDecimal("100"));
        Account ben = new Account("Ben", new BigDecimal("50"));
        when(repository.findById(1L)).thenReturn(Optional.of(anna));
        when(repository.findById(2L)).thenReturn(Optional.of(ben));

        assertThatThrownBy(() -> service.transfer(1L, 2L, new BigDecimal("500")))
                .isInstanceOf(InsufficientFundsException.class);

        assertThat(anna.getBalance()).isEqualByComparingTo("100");
        assertThat(ben.getBalance()).isEqualByComparingTo("50");
    }

    @Test
    void rejectsTransferToUnknownAccount() {
        Account anna = new Account("Anna", new BigDecimal("100"));
        when(repository.findById(1L)).thenReturn(Optional.of(anna));
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.transfer(1L, 99L, new BigDecimal("10")))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void rejectsTransferToSameAccount() {
        assertThatThrownBy(() -> service.transfer(1L, 1L, new BigDecimal("10")))
                .isInstanceOf(InvalidTransferException.class);
    }
}
