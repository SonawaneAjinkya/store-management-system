package com.store.management.repository;

import com.store.management.entity.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseItemRepository
        extends JpaRepository<PurchaseItem, Integer> {

    List<PurchaseItem> findByProductId(Integer productId);

    List<PurchaseItem> findByPurchaseId(Integer purchaseId);
}