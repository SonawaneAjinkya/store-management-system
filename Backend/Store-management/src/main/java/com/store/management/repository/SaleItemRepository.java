package com.store.management.repository;

import com.store.management.entity.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleItemRepository
        extends JpaRepository<SaleItem, Integer> {

    List<SaleItem> findByProductId(Integer productId);
}