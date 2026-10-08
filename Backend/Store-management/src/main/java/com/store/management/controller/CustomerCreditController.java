package com.store.management.controller;

import com.store.management.entity.CreditPayment;
import com.store.management.entity.CustomerCredit;
import com.store.management.service.CustomerCreditService;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/credits")
@CrossOrigin("*")
public class CustomerCreditController {

    private final CustomerCreditService service;

    public CustomerCreditController(
            CustomerCreditService service) {

        this.service = service;
    }


    // ==========================================
    // ADD CREDIT
    // ==========================================

    @PostMapping("/add/{customerId}")
    public CustomerCredit addCredit(
            @PathVariable Integer customerId,
            @RequestParam BigDecimal amount) {

        return service.addCredit(
                customerId,
                amount
        );
    }


    // ==========================================
    // PAY CREDIT
    // ==========================================

    @PostMapping("/pay/{customerId}")
    public CustomerCredit payCredit(
            @PathVariable Integer customerId,
            @RequestParam BigDecimal amount,
            @RequestParam String paymentMethod) {

        return service.payCredit(
                customerId,
                amount,
                paymentMethod
        );
    }


    // ==========================================
    // GET CUSTOMER CREDIT
    // ==========================================

    @GetMapping("/{customerId}")
    public CustomerCredit getCustomerCredit(
            @PathVariable Integer customerId) {

        return service.getCustomerCredit(
                customerId
        );
    }


    // ==========================================
    // GET ALL CREDITS
    // ==========================================

    @GetMapping
    public List<CustomerCredit> getAllCredits() {

        return service.getAllCredits();
    }


    // ==========================================
    // GET OUTSTANDING CREDITS
    // ==========================================

    @GetMapping("/outstanding")
    public List<CustomerCredit> getOutstandingCredits() {

        return service.getOutstandingCredits();
    }


    // ==========================================
    // GET CREDIT PAYMENT HISTORY
    // ==========================================

    @GetMapping("/payments/{customerId}")
    public List<CreditPayment> getPaymentHistory(
            @PathVariable Integer customerId) {

        return service.getPaymentHistory(
                customerId
        );
    }
}