package com.store.management.service;

import com.store.management.entity.ShopSettings;
import com.store.management.repository.ShopSettingsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopSettingsService {

    private final ShopSettingsRepository shopSettingsRepository;

    public ShopSettingsService(
            ShopSettingsRepository shopSettingsRepository) {

        this.shopSettingsRepository =
                shopSettingsRepository;
    }

    public ShopSettings getSettings() {

        List<ShopSettings> settings =
                shopSettingsRepository.findAll();

        if (settings.isEmpty()) {

            ShopSettings defaultSettings =
                    new ShopSettings();

            defaultSettings.setShopName("My Shop");
            defaultSettings.setTagline(
                    "Your Trusted Store"
            );
            defaultSettings.setInvoicePrefix("INV-");
            defaultSettings.setPurchaseInvoicePrefix(
                    "PUR-"
            );
            defaultSettings.setCurrencySymbol("₹");
            defaultSettings.setReceiptFooter(
                    "Thank you for shopping with us!"
            );

            return shopSettingsRepository.save(
                    defaultSettings
            );
        }

        return settings.get(0);
    }

    public ShopSettings updateSettings(
            ShopSettings updatedSettings) {

        ShopSettings existingSettings =
                getSettings();

        existingSettings.setShopName(
                updatedSettings.getShopName()
        );

        existingSettings.setTagline(
                updatedSettings.getTagline()
        );

        existingSettings.setAddress(
                updatedSettings.getAddress()
        );

        existingSettings.setPhone(
                updatedSettings.getPhone()
        );

        existingSettings.setEmail(
                updatedSettings.getEmail()
        );

        existingSettings.setGstin(
                updatedSettings.getGstin()
        );

        if (updatedSettings.getInvoicePrefix() != null &&
                !updatedSettings.getInvoicePrefix().isBlank()) {

            existingSettings.setInvoicePrefix(
                    updatedSettings.getInvoicePrefix()
            );
        }

        if (updatedSettings.getPurchaseInvoicePrefix() != null &&
                !updatedSettings.getPurchaseInvoicePrefix().isBlank()) {

            existingSettings.setPurchaseInvoicePrefix(
                    updatedSettings.getPurchaseInvoicePrefix()
            );
        }

        if (updatedSettings.getCurrencySymbol() != null &&
                !updatedSettings.getCurrencySymbol().isBlank()) {

            existingSettings.setCurrencySymbol(
                    updatedSettings.getCurrencySymbol()
            );
        }

        existingSettings.setReceiptFooter(
                updatedSettings.getReceiptFooter()
        );

        return shopSettingsRepository.save(
                existingSettings
        );
    }
}