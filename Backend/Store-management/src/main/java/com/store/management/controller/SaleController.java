package com.store.management.controller;

import com.store.management.dto.SaleRequest;
import com.store.management.entity.Sale;
import com.store.management.service.SaleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
@CrossOrigin(origins = "*")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    // Create a new sale
    @PostMapping
    public ResponseEntity<Sale> createSale(
            @Valid @RequestBody SaleRequest request) {

        Sale sale = saleService.createSale(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(sale);
    }

    // Get all sales
    @GetMapping
    public ResponseEntity<List<Sale>> getAllSales() {

        return ResponseEntity.ok(
                saleService.getAllSales()
        );
    }

    // Get sale by ID
    @GetMapping("/{id}")
    public ResponseEntity<Sale> getSaleById(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                saleService.getSaleById(id)
        );
    }

    // Get sale by invoice number
    @GetMapping("/invoice/{invoiceNumber}")
    public ResponseEntity<Sale> getSaleByInvoice(
            @PathVariable String invoiceNumber) {

        return ResponseEntity.ok(
                saleService.getSaleByInvoice(invoiceNumber)
        );
    }
}