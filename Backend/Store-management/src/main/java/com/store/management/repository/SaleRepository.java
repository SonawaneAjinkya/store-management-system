package com.store.management.repository;

import com.store.management.entity.Sale;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SaleRepository
        extends JpaRepository<Sale, Integer> {

    Optional<Sale> findByInvoiceNumber(
            String invoiceNumber);

    boolean existsByInvoiceNumber(
            String invoiceNumber);

    List<Sale> findAllByOrderBySaleDateDesc();
}