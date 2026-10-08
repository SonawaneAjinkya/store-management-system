package com.store.management.repository;

import com.store.management.entity.Customer;
import com.store.management.entity.CustomerCredit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerCreditRepository
        extends JpaRepository<CustomerCredit, Integer> {

    Optional<CustomerCredit>
    findByCustomer(Customer customer);

    List<CustomerCredit>
    findAllByOrderByUpdatedDateDesc();

    List<CustomerCredit>
    findByOutstandingAmountGreaterThanOrderByUpdatedDateDesc(
            java.math.BigDecimal amount);
}