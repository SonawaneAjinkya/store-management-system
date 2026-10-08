package com.store.management.controller;

import com.store.management.entity.Supplier;
import com.store.management.service.SupplierService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@CrossOrigin(origins = "*")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @PostMapping
    public ResponseEntity<Supplier> addSupplier(
            @RequestBody Supplier supplier) {

        Supplier savedSupplier =
                supplierService.addSupplier(supplier);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedSupplier);
    }

    @GetMapping
    public ResponseEntity<List<Supplier>> getAllSuppliers() {

        return ResponseEntity.ok(
                supplierService.getAllSuppliers()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Supplier> getSupplierById(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                supplierService.getSupplierById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Supplier> updateSupplier(
            @PathVariable Integer id,
            @RequestBody Supplier supplier) {

        return ResponseEntity.ok(
                supplierService.updateSupplier(id, supplier)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSupplier(
            @PathVariable Integer id) {

        supplierService.deleteSupplier(id);

        return ResponseEntity.ok(
                "Supplier deleted successfully"
        );
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<Supplier> getSupplierByPhone(
            @PathVariable String phone) {

        return ResponseEntity.ok(
                supplierService.getSupplierByPhone(phone)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<List<Supplier>> searchSuppliers(
            @RequestParam String name) {

        return ResponseEntity.ok(
                supplierService.searchSuppliers(name)
        );
    }

    @GetMapping("/search-phone")
    public ResponseEntity<List<Supplier>> searchSuppliersByPhone(
            @RequestParam String phone) {

        return ResponseEntity.ok(
                supplierService.searchSuppliersByPhone(phone)
        );
    }
}