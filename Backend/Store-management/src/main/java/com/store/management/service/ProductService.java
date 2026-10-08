package com.store.management.service;

import com.store.management.entity.Product;
import com.store.management.repository.ProductRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // ==========================================
    // ADD PRODUCT
    // ==========================================

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    // ==========================================
    // GET ALL PRODUCTS
    // ==========================================

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // ==========================================
    // GET PRODUCT BY ID
    // ==========================================

    public Product getProductById(Integer id) {

        return productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product not found with id: " + id));
    }

    // ==========================================
    // UPDATE PRODUCT
    // ==========================================

    public Product updateProduct(
            Integer id,
            Product updatedProduct) {

        Product existingProduct =
                getProductById(id);

        existingProduct.setName(
                updatedProduct.getName());

        existingProduct.setPurchasePrice(
                updatedProduct.getPurchasePrice());

        existingProduct.setSellingPrice(
                updatedProduct.getSellingPrice());

        existingProduct.setQuantity(
                updatedProduct.getQuantity());

        existingProduct.setMinimumStock(
                updatedProduct.getMinimumStock());

        existingProduct.setUnit(
                updatedProduct.getUnit());

        existingProduct.setBarcode(
                updatedProduct.getBarcode());

        existingProduct.setCategory(
                updatedProduct.getCategory());

        return productRepository.save(
                existingProduct);
    }

    // ==========================================
    // DELETE PRODUCT
    // ==========================================

    public void deleteProduct(Integer id) {

        if (!productRepository.existsById(id)) {

            throw new RuntimeException(
                    "Product not found with id: " + id);
        }

        productRepository.deleteById(id);
    }

    // ==========================================
    // SEARCH PRODUCTS
    // ==========================================

    public List<Product> searchProducts(
            String name) {

        return productRepository
                .findByNameContainingIgnoreCase(name);
    }

    // ==========================================
    // GET PRODUCT BY BARCODE
    // ==========================================

    public Product getProductByBarcode(
            String barcode) {

        return productRepository
                .findByBarcode(barcode)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product not found with barcode: "
                                        + barcode));
    }

    // ==========================================
    // GET LOW STOCK PRODUCTS
    // ==========================================
    //
    // A product is low stock when:
    //
    // quantity <= minimumStock
    //
    // Each product can therefore have
    // its own minimum stock level.
    // ==========================================

    public List<Product> getLowStockProducts() {

        List<Product> products =
                productRepository.findAll();

        return products.stream()
                .filter(product ->
                        product.getMinimumStock() != null
                                &&
                        product.getQuantity() != null
                                &&
                        product.getQuantity()
                                <= product.getMinimumStock()
                )
                .collect(Collectors.toList());
    }
}