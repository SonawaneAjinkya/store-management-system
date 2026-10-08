package com.store.management.repository;

import com.store.management.entity.Customer;
import com.store.management.entity.CreditPayment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditPaymentRepository
        extends JpaRepository<CreditPayment, Integer> {

    List<CreditPayment>
    findByCustomerOrderByPaymentDateDesc(Customer customer);

}