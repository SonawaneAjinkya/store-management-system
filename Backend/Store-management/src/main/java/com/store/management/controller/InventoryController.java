package com.store.management.controller;

import com.store.management.entity.InventoryTransaction;
import com.store.management.entity.Product;
import com.store.management.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // Stock In
    @PostMapping("/in/{productId}")
    public ResponseEntity<Product> stockIn(
            @PathVariable Integer productId,
            @RequestParam Float quantity) {

        Product product =
                inventoryService.stockIn(productId, quantity);

        return ResponseEntity.ok(product);
    }

    // Stock Out
    @PostMapping("/out/{productId}")
    public ResponseEntity<Product> stockOut(
            @PathVariable Integer productId,
            @RequestParam Float quantity) {

        Product product =
                inventoryService.stockOut(productId, quantity);

        return ResponseEntity.ok(product);
    }

    // Get all inventory transactions
    @GetMapping("/transactions")
    public ResponseEntity<List<InventoryTransaction>>
    getAllTransactions() {

        return ResponseEntity.ok(
                inventoryService.getAllTransactions()
        );
    }

    // Get transactions for one product
    @GetMapping("/transactions/{productId}")
    public ResponseEntity<List<InventoryTransaction>>
    getProductTransactions(
            @PathVariable Integer productId) {

        return ResponseEntity.ok(
                inventoryService.getProductTransactions(productId)
        );
    }
}