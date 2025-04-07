package com.sanlam.assessment.dao;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class Account {
    private Long accountId;
    private BigDecimal balance;
}
