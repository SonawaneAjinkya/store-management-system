package com.store.management.controller;

import com.store.management.dto.PurchaseRequest;
import com.store.management.entity.Purchase;
import com.store.management.service.PurchaseService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchases")
@CrossOrigin(origins = "*")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping
    public ResponseEntity<Purchase> createPurchase(
            @Valid @RequestBody PurchaseRequest request) {

        Purchase purchase =
                purchaseService.createPurchase(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(purchase);
    }

    @GetMapping
    public ResponseEntity<List<Purchase>> getAllPurchases() {

        return ResponseEntity.ok(
                purchaseService.getAllPurchases()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Purchase> getPurchaseById(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                purchaseService.getPurchaseById(id)
        );
    }

    @GetMapping("/invoice/{invoiceNumber}")
    public ResponseEntity<Purchase> getPurchaseByInvoice(
            @PathVariable String invoiceNumber) {

        return ResponseEntity.ok(
                purchaseService.getPurchaseByInvoice(
                        invoiceNumber
                )
        );
    }
}