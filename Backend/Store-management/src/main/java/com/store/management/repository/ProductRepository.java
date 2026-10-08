
package com.store.management.repository;

import com.store.management.entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.Optional;

public interface ProductRepository
        extends JpaRepository<Product, Integer> {

    Optional<Product> findByBarcode(String barcode);

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByCategoryIgnoreCase(String category);

    List<Product> findByQuantityLessThanEqual(Float quantity);

    /*
     * Locks the product row while a sale/purchase is processing.
     *
     * This prevents two simultaneous transactions from reading
     * the same stock quantity and both selling it.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT p
            FROM Product p
            WHERE p.id = :id
            """)
    Optional<Product> findByIdForUpdate(
            @Param("id") Integer id
    );
}

