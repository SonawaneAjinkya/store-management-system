package com.store.management.repository;

import com.store.management.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseRepository
        extends JpaRepository<Purchase, Integer> {

    Optional<Purchase> findByInvoiceNumber(String invoiceNumber);

    List<Purchase> findAllByOrderByPurchaseDateDesc();
}