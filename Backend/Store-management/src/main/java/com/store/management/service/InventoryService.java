package com.store.management.service;

import com.store.management.entity.InventoryTransaction;
import com.store.management.entity.Product;
import com.store.management.repository.InventoryTransactionRepository;
import com.store.management.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class InventoryService {

    private final ProductRepository productRepository;
    private final InventoryTransactionRepository transactionRepository;

    public InventoryService(
            ProductRepository productRepository,
            InventoryTransactionRepository transactionRepository) {

        this.productRepository = productRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Product stockIn(Integer productId, Float quantity) {

        validateQuantity(quantity);

        Product product = getProduct(productId);

        Float currentQuantity = product.getQuantity();

        if (currentQuantity == null) {
            currentQuantity = 0f;
        }

        product.setQuantity(currentQuantity + quantity);

        Product updatedProduct = productRepository.save(product);

        InventoryTransaction transaction =
                new InventoryTransaction();

        transaction.setProduct(product);
        transaction.setTransactionType("STOCK_IN");
        transaction.setQuantity(quantity);

        BigDecimal amount = product.getPurchasePrice()
                .multiply(BigDecimal.valueOf(quantity));

        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDateTime.now());

        transactionRepository.save(transaction);

        return updatedProduct;
    }

    @Transactional
    public Product stockOut(Integer productId, Float quantity) {

        validateQuantity(quantity);

        Product product = getProduct(productId);

        Float currentQuantity = product.getQuantity();

        if (currentQuantity == null) {
            currentQuantity = 0f;
        }

        if (quantity > currentQuantity) {
            throw new RuntimeException(
                    "Insufficient stock. Available stock: "
                            + currentQuantity
            );
        }

        product.setQuantity(currentQuantity - quantity);

        Product updatedProduct = productRepository.save(product);

        InventoryTransaction transaction =
                new InventoryTransaction();

        transaction.setProduct(product);
        transaction.setTransactionType("STOCK_OUT");
        transaction.setQuantity(quantity);

        BigDecimal amount = product.getSellingPrice()
                .multiply(BigDecimal.valueOf(quantity));

        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDateTime.now());

        transactionRepository.save(transaction);

        return updatedProduct;
    }

    public List<InventoryTransaction> getAllTransactions() {

        return transactionRepository
                .findAllByOrderByTransactionDateDesc();
    }

    public List<InventoryTransaction> getProductTransactions(
            Integer productId) {

        return transactionRepository
                .findByProductIdOrderByTransactionDateDesc(productId);
    }

    private Product getProduct(Integer productId) {

        return productRepository.findById(productId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product not found with id: "
                                        + productId));
    }

    private void validateQuantity(Float quantity) {

        if (quantity == null || quantity <= 0) {
            throw new RuntimeException(
                    "Quantity must be greater than 0");
        }
    }
}