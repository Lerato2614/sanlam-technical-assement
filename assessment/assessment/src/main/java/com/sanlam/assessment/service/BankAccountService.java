package com.sanlam.assessment.service;

import com.sanlam.assessment.dao.Account;
import com.sanlam.assessment.dto.WithdrawalRequest;
import com.sanlam.assessment.dto.WithdrawalResponse;
import com.sanlam.assessment.repository.BankAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Service
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final SnsPublisher snsPublisher;

    public BankAccountService(final BankAccountRepository bankAccountRepository,
                              final SnsPublisher snsPublisher) {
        this.bankAccountRepository = bankAccountRepository;
        this.snsPublisher = snsPublisher;
    }

    @Transactional
    public WithdrawalResponse withdraw(final WithdrawalRequest request) {
        final Optional<Account> account = bankAccountRepository.findById(request.getAccountId());
        BigDecimal balance = null;
        if (account.isPresent()) {
            log.info("Found account balance for account id {}", request.getAccountId());
            balance = account.get().getBalance();
        }

        if (balance.compareTo(request.getAmount()) < 0) {
            log.error("Insufficient funds for account {}", request.getAccountId());
            return new WithdrawalResponse("FAILED", "Insufficient funds");
        }

        final boolean success = updateBalance(request.getAccountId(), request.getAmount());
        if (!success) {
            log.error("Failed to update account for account {}.", request.getAmount());
            throw new RuntimeException("Failed to update account");
        }

        snsPublisher.publishEvent(request);  // Abstracted publisher
        return new WithdrawalResponse("SUCCESS", "Withdrawal completed");
    }

    private boolean updateBalance(final Long accountId, final BigDecimal balance) {
        final int rowsUpdated = bankAccountRepository.updateBankAccountBalance(accountId, balance);
        log.info("Bank account balance for account id {} has been updated.", rowsUpdated);
        return rowsUpdated > 0;
    }

}
