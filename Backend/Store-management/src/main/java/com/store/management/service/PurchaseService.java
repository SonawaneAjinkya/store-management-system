package com.store.management.service;

import com.store.management.dto.PurchaseItemRequest;
import com.store.management.dto.PurchaseRequest;
import com.store.management.entity.InventoryTransaction;
import com.store.management.entity.Product;
import com.store.management.entity.Purchase;
import com.store.management.entity.PurchaseItem;
import com.store.management.entity.ShopSettings;
import com.store.management.entity.Supplier;
import com.store.management.repository.InventoryTransactionRepository;
import com.store.management.repository.ProductRepository;
import com.store.management.repository.PurchaseRepository;
import com.store.management.repository.ShopSettingsRepository;
import com.store.management.repository.SupplierRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final InventoryTransactionRepository transactionRepository;
    private final ShopSettingsRepository shopSettingsRepository;

    public PurchaseService(
            PurchaseRepository purchaseRepository,
            ProductRepository productRepository,
            SupplierRepository supplierRepository,
            InventoryTransactionRepository transactionRepository,
            ShopSettingsRepository shopSettingsRepository) {

        this.purchaseRepository = purchaseRepository;
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
        this.transactionRepository = transactionRepository;
        this.shopSettingsRepository = shopSettingsRepository;
    }

    @Transactional
    public Purchase createPurchase(PurchaseRequest request) {

        if (request == null ||
                request.getItems() == null ||
                request.getItems().isEmpty()) {

            throw new RuntimeException(
                    "Purchase must contain at least one item");
        }

        Supplier supplier = supplierRepository
                .findById(request.getSupplierId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Supplier not found: "
                                        + request.getSupplierId()));

        Purchase purchase = new Purchase();

        purchase.setInvoiceNumber(
                generateInvoiceNumber());

        purchase.setSupplier(supplier);

        purchase.setPurchaseDate(
                LocalDateTime.now());

        BigDecimal subtotal =
                BigDecimal.ZERO;

        BigDecimal discount =
                request.getDiscount();

        if (discount == null ||
                discount.compareTo(
                        BigDecimal.ZERO) < 0) {

            discount = BigDecimal.ZERO;
        }

        BigDecimal tax =
                request.getTax();

        if (tax == null ||
                tax.compareTo(
                        BigDecimal.ZERO) < 0) {

            tax = BigDecimal.ZERO;
        }

        purchase.setDiscount(discount);
        purchase.setTax(tax);

        for (PurchaseItemRequest itemRequest :
                request.getItems()) {

            if (itemRequest.getQuantity() == null ||
                    itemRequest.getQuantity() <= 0) {

                throw new RuntimeException(
                        "Quantity must be greater than 0");
            }

            if (itemRequest.getPrice() == null ||
                    itemRequest.getPrice()
                            .compareTo(
                                    BigDecimal.ZERO) <= 0) {

                throw new RuntimeException(
                        "Purchase price must be greater than 0");
            }

            Product product = productRepository
                    .findById(itemRequest.getProductId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Product not found: "
                                            + itemRequest
                                            .getProductId()));

            BigDecimal quantity =
                    BigDecimal.valueOf(
                            itemRequest.getQuantity());

            BigDecimal price =
                    itemRequest.getPrice();

            BigDecimal itemTotal =
                    price.multiply(quantity)
                            .setScale(
                                    2,
                                    RoundingMode.HALF_UP);

            subtotal =
                    subtotal.add(itemTotal);

            PurchaseItem purchaseItem =
                    new PurchaseItem();

            purchaseItem.setPurchase(purchase);

            purchaseItem.setProduct(product);

            purchaseItem.setQuantity(
                    itemRequest.getQuantity());

            purchaseItem.setPrice(price);

            purchaseItem.setTotal(itemTotal);

            purchase.getItems().add(
                    purchaseItem);

            Float currentStock =
                    product.getQuantity();

            if (currentStock == null) {
                currentStock = 0f;
            }

            product.setQuantity(
                    currentStock +
                            itemRequest.getQuantity());

            productRepository.save(product);

            InventoryTransaction transaction =
                    new InventoryTransaction();

            transaction.setProduct(product);

            transaction.setTransactionType(
                    "STOCK_IN");

            transaction.setQuantity(
                    itemRequest.getQuantity());

            transaction.setAmount(itemTotal);

            transaction.setTransactionDate(
                    LocalDateTime.now());

            transactionRepository.save(
                    transaction);
        }

        BigDecimal total =
                subtotal
                        .subtract(discount)
                        .add(tax)
                        .setScale(
                                2,
                                RoundingMode.HALF_UP);

        if (total.compareTo(
                BigDecimal.ZERO) < 0) {

            throw new RuntimeException(
                    "Discount cannot be greater than subtotal");
        }

        purchase.setSubtotal(
                subtotal.setScale(
                        2,
                        RoundingMode.HALF_UP));

        purchase.setTotalAmount(total);

        return purchaseRepository.save(
                purchase);
    }

    public List<Purchase> getAllPurchases() {

        return purchaseRepository
                .findAllByOrderByPurchaseDateDesc();
    }

    public Purchase getPurchaseById(
            Integer id) {

        return purchaseRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Purchase not found with id: "
                                        + id));
    }

    public Purchase getPurchaseByInvoice(
            String invoiceNumber) {

        return purchaseRepository
                .findByInvoiceNumber(
                        invoiceNumber)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Purchase invoice not found: "
                                        + invoiceNumber));
    }

    private String generateInvoiceNumber() {

        String prefix = "PUR-";

        List<ShopSettings> settings =
                shopSettingsRepository.findAll();

        if (!settings.isEmpty()) {

            ShopSettings shopSettings =
                    settings.get(0);

            if (shopSettings
                    .getPurchaseInvoicePrefix() != null &&
                    !shopSettings
                            .getPurchaseInvoicePrefix()
                            .isBlank()) {

                prefix =
                        shopSettings
                                .getPurchaseInvoicePrefix();
            }
        }

        String timestamp =
                LocalDateTime.now()
                        .format(
                                DateTimeFormatter
                                        .ofPattern(
                                                "yyyyMMddHHmmssSSS"));

        return prefix + timestamp;
    }
}