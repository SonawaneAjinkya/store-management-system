package com.store.management.service;

import com.store.management.entity.Supplier;
import com.store.management.repository.SupplierRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public Supplier addSupplier(Supplier supplier) {

        if (supplier.getCreatedDate() == null) {
            supplier.setCreatedDate(LocalDateTime.now());
        }

        return supplierRepository.save(supplier);
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Supplier getSupplierById(Integer id) {
        return supplierRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Supplier not found with id: " + id));
    }

    public Supplier updateSupplier(
            Integer id,
            Supplier updatedSupplier) {

        Supplier existingSupplier = getSupplierById(id);

        existingSupplier.setName(updatedSupplier.getName());
        existingSupplier.setPhone(updatedSupplier.getPhone());
        existingSupplier.setEmail(updatedSupplier.getEmail());
        existingSupplier.setAddress(updatedSupplier.getAddress());
        existingSupplier.setGstin(updatedSupplier.getGstin());

        return supplierRepository.save(existingSupplier);
    }

    public void deleteSupplier(Integer id) {

        if (!supplierRepository.existsById(id)) {
            throw new RuntimeException(
                    "Supplier not found with id: " + id);
        }

        supplierRepository.deleteById(id);
    }

    public Supplier getSupplierByPhone(String phone) {
        return supplierRepository.findByPhone(phone)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Supplier not found with phone: "
                                        + phone));
    }

    public List<Supplier> searchSuppliers(String name) {
        return supplierRepository
                .findByNameContainingIgnoreCase(name);
    }

    public List<Supplier> searchSuppliersByPhone(String phone) {
        return supplierRepository
                .findByPhoneContaining(phone);
    }
}