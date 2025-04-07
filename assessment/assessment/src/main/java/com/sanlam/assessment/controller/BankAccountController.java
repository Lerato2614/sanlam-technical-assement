package com.sanlam.assessment.controller;

import com.sanlam.assessment.dto.WithdrawalRequest;
import com.sanlam.assessment.dto.WithdrawalResponse;
import com.sanlam.assessment.service.BankAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/bank")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    public BankAccountController(final BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @PostMapping("/withdraw")
    public ResponseEntity<WithdrawalResponse> withdraw(@RequestBody final WithdrawalRequest request) {
        return ResponseEntity.ok(bankAccountService.withdraw(request));
    }
}
