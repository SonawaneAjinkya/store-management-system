package com.store.management.repository;

import com.store.management.entity.ShopSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopSettingsRepository
        extends JpaRepository<ShopSettings, Integer> {
}