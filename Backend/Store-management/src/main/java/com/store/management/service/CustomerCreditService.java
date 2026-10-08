package com.store.management.service;

import com.store.management.entity.CreditPayment;
import com.store.management.entity.Customer;
import com.store.management.entity.CustomerCredit;
import com.store.management.repository.CreditPaymentRepository;
import com.store.management.repository.CustomerCreditRepository;
import com.store.management.repository.CustomerRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomerCreditService {

    private final CustomerCreditRepository creditRepository;
    private final CustomerRepository customerRepository;
    private final CreditPaymentRepository paymentRepository;

    public CustomerCreditService(
            CustomerCreditRepository creditRepository,
            CustomerRepository customerRepository,
            CreditPaymentRepository paymentRepository) {

        this.creditRepository = creditRepository;
        this.customerRepository = customerRepository;
        this.paymentRepository = paymentRepository;
    }


    // ==========================================
    // ADD CREDIT
    // ==========================================

    @Transactional
    public CustomerCredit addCredit(
            Integer customerId,
            BigDecimal amount) {

        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new RuntimeException(
                    "Credit amount must be greater than zero");
        }

        Customer customer =
                customerRepository.findById(customerId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Customer not found"));

        CustomerCredit credit =
                creditRepository
                        .findByCustomer(customer)
                        .orElseGet(() -> {

                            CustomerCredit newCredit =
                                    new CustomerCredit();

                            newCredit.setCustomer(customer);
                            newCredit.setCreditAmount(
                                    BigDecimal.ZERO);
                            newCredit.setPaidAmount(
                                    BigDecimal.ZERO);
                            newCredit.setOutstandingAmount(
                                    BigDecimal.ZERO);
                            newCredit.setCreatedDate(
                                    LocalDateTime.now());

                            return newCredit;
                        });

        credit.setCreditAmount(
                credit.getCreditAmount()
                        .add(amount));

        credit.setOutstandingAmount(
                credit.getOutstandingAmount()
                        .add(amount));

        credit.setUpdatedDate(
                LocalDateTime.now());

        return creditRepository.save(credit);
    }


    // ==========================================
    // PAY CREDIT
    // ==========================================

    @Transactional
    public CustomerCredit payCredit(
            Integer customerId,
            BigDecimal amount,
            String paymentMethod) {

        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new RuntimeException(
                    "Payment amount must be greater than zero");
        }


        if (paymentMethod == null ||
                paymentMethod.trim().isEmpty()) {

            throw new RuntimeException(
                    "Payment method is required");
        }


        paymentMethod =
                paymentMethod.trim().toUpperCase();


        if (!paymentMethod.equals("CASH") &&
                !paymentMethod.equals("UPI") &&
                !paymentMethod.equals("CARD")) {

            throw new RuntimeException(
                    "Payment method must be CASH, UPI or CARD");
        }


        Customer customer =
                customerRepository.findById(customerId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Customer not found"));


        CustomerCredit credit =
                creditRepository
                        .findByCustomer(customer)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "No credit account found"));


        if (credit.getOutstandingAmount()
                .compareTo(amount) < 0) {

            throw new RuntimeException(
                    "Payment cannot be greater than outstanding credit");
        }


        // ==========================================
        // UPDATE CREDIT BALANCE
        // ==========================================

        credit.setPaidAmount(
                credit.getPaidAmount()
                        .add(amount));

        credit.setOutstandingAmount(
                credit.getOutstandingAmount()
                        .subtract(amount));

        credit.setUpdatedDate(
                LocalDateTime.now());


        CustomerCredit savedCredit =
                creditRepository.save(credit);


        // ==========================================
        // SAVE PAYMENT HISTORY
        // ==========================================

        CreditPayment payment =
                new CreditPayment();

        payment.setCustomer(customer);

        payment.setAmount(amount);

        payment.setPaymentDate(
                LocalDateTime.now());

        payment.setPaymentMethod(
                paymentMethod);

        payment.setNote(
                "Credit payment");

        paymentRepository.save(payment);


        return savedCredit;
    }


    // ==========================================
    // GET CUSTOMER CREDIT
    // ==========================================

    public CustomerCredit getCustomerCredit(
            Integer customerId) {

        Customer customer =
                customerRepository.findById(customerId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Customer not found"));

        return creditRepository
                .findByCustomer(customer)
                .orElseThrow(() ->
                        new RuntimeException(
                                "No credit account found"));
    }


    // ==========================================
    // GET ALL CREDITS
    // ==========================================

    public List<CustomerCredit> getAllCredits() {

        return creditRepository
                .findAllByOrderByUpdatedDateDesc();
    }


    // ==========================================
    // GET OUTSTANDING CREDITS
    // ==========================================

    public List<CustomerCredit> getOutstandingCredits() {

        return creditRepository
                .findByOutstandingAmountGreaterThanOrderByUpdatedDateDesc(
                        BigDecimal.ZERO);
    }


    // ==========================================
    // GET PAYMENT HISTORY
    // ==========================================

    public List<CreditPayment> getPaymentHistory(
            Integer customerId) {

        Customer customer =
                customerRepository.findById(customerId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Customer not found"));

        return paymentRepository
                .findByCustomerOrderByPaymentDateDesc(
                        customer);
    }
}