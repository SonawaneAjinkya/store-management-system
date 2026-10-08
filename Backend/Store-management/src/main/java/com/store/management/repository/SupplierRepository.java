package com.store.management.repository;

import com.store.management.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository
        extends JpaRepository<Supplier, Integer> {

    Optional<Supplier> findByPhone(String phone);

    List<Supplier> findByNameContainingIgnoreCase(String name);

    List<Supplier> findByPhoneContaining(String phone);
}