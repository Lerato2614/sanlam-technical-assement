package com.sanlam.assessment.repository;

import com.sanlam.assessment.dao.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface BankAccountRepository extends JpaRepository<Account, Long> {

    @Modifying
    @Query("UPDATE Account a SET a.balance = :balance WHERE a.id = :accountId")
    int updateBankAccountBalance(@Param("accountId") final Long accountId, @Param("balance") final BigDecimal balance);
}

