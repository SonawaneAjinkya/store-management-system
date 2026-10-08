package com.store.management.repository;

import com.store.management.entity.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryTransactionRepository
        extends JpaRepository<InventoryTransaction, Integer> {

    List<InventoryTransaction>
    findAllByOrderByTransactionDateDesc();

    List<InventoryTransaction>
    findByProductIdOrderByTransactionDateDesc(Integer productId);
}