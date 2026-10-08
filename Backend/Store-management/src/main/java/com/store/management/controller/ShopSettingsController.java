package com.store.management.controller;

import com.store.management.entity.ShopSettings;
import com.store.management.service.ShopSettingsService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
@CrossOrigin(origins = "*")
public class ShopSettingsController {

    private final ShopSettingsService shopSettingsService;

    public ShopSettingsController(
            ShopSettingsService shopSettingsService) {

        this.shopSettingsService = shopSettingsService;
    }

    @GetMapping
    public ResponseEntity<ShopSettings> getSettings() {

        return ResponseEntity.ok(
                shopSettingsService.getSettings()
        );
    }

    @PutMapping
    public ResponseEntity<ShopSettings> updateSettings(
            @RequestBody ShopSettings settings) {

        return ResponseEntity.ok(
                shopSettingsService.updateSettings(settings)
        );
    }
}