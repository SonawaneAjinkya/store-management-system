
package com.store.management.service;

import com.store.management.dto.SaleItemRequest;
import com.store.management.dto.SaleRequest;
import com.store.management.entity.Customer;
import com.store.management.entity.CustomerCredit;
import com.store.management.entity.InventoryTransaction;
import com.store.management.entity.Product;
import com.store.management.entity.Sale;
import com.store.management.entity.SaleItem;
import com.store.management.entity.ShopSettings;
import com.store.management.repository.CustomerCreditRepository;
import com.store.management.repository.CustomerRepository;
import com.store.management.repository.InventoryTransactionRepository;
import com.store.management.repository.ProductRepository;
import com.store.management.repository.SaleRepository;
import com.store.management.repository.ShopSettingsRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final InventoryTransactionRepository inventoryTransactionRepository;
    private final ShopSettingsRepository shopSettingsRepository;
    private final CustomerCreditRepository customerCreditRepository;

    public SaleService(
            SaleRepository saleRepository,
            ProductRepository productRepository,
            CustomerRepository customerRepository,
            InventoryTransactionRepository inventoryTransactionRepository,
            ShopSettingsRepository shopSettingsRepository,
            CustomerCreditRepository customerCreditRepository) {

        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.inventoryTransactionRepository =
                inventoryTransactionRepository;
        this.shopSettingsRepository =
                shopSettingsRepository;
        this.customerCreditRepository =
                customerCreditRepository;
    }

    @Transactional
    public Sale createSale(SaleRequest request) {

        if (request.getItems() == null ||
                request.getItems().isEmpty()) {

            throw new RuntimeException(
                    "Sale must contain at least one item");
        }

        Customer customer = null;

        if (request.getCustomerId() != null) {

            customer = customerRepository.findById(
                    request.getCustomerId()
            ).orElseThrow(() ->
                    new RuntimeException(
                            "Customer not found"));
        }

        Sale sale = new Sale();

        sale.setCustomerName(
                request.getCustomerName());

        sale.setCustomerPhone(
                request.getCustomerPhone());

        sale.setDiscount(
                request.getDiscount() == null
                        ? BigDecimal.ZERO
                        : request.getDiscount());

        sale.setTax(BigDecimal.ZERO);

        sale.setPaymentMethod(
                request.getPaymentMethod());

        sale.setAmountReceived(
                request.getAmountReceived() == null
                        ? BigDecimal.ZERO
                        : request.getAmountReceived());

        sale.setChangeAmount(
                BigDecimal.ZERO);

        sale.setCreditAmount(
                BigDecimal.ZERO);

        sale.setSaleDate(
                LocalDateTime.now());


        // ==========================================
        // INVOICE NUMBER
        // ==========================================

        ShopSettings settings =
                shopSettingsRepository
                        .findAll()
                        .stream()
                        .findFirst()
                        .orElse(null);

        String invoicePrefix = "INV-";

        if (settings != null &&
                settings.getInvoicePrefix() != null &&
                !settings.getInvoicePrefix()
                        .trim()
                        .isEmpty()) {

            invoicePrefix =
                    settings.getInvoicePrefix().trim();
        }


        /*
         * Generate invoice number.
         *
         * The timestamp contains milliseconds.
         * We also check the database before using it.
         */

        String invoiceNumber;

        int invoiceAttempt = 0;

        do {

            invoiceNumber =
                    invoicePrefix +
                    LocalDateTime.now()
                            .format(
                                    DateTimeFormatter.ofPattern(
                                            "yyyyMMddHHmmssSSS"
                                    )
                            );

            /*
             * In the extremely unlikely event that the same
             * millisecond is used, wait for the next timestamp.
             */
            if (saleRepository.existsByInvoiceNumber(
                    invoiceNumber)) {

                invoiceAttempt++;

                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {

                    Thread.currentThread().interrupt();

                    throw new RuntimeException(
                            "Unable to generate unique invoice number",
                            e);
                }

            } else {

                break;
            }

        } while (invoiceAttempt < 10);


        if (saleRepository.existsByInvoiceNumber(
                invoiceNumber)) {

            throw new RuntimeException(
                    "Unable to generate unique invoice number");
        }


        sale.setInvoiceNumber(
                invoiceNumber);


        // ==========================================
        // SALE ITEMS
        // ==========================================

        BigDecimal subtotal =
                BigDecimal.ZERO;


        for (SaleItemRequest itemRequest :
                request.getItems()) {

            if (itemRequest.getQuantity() <= 0) {

                throw new RuntimeException(
                        "Quantity must be greater than zero");
            }


            /*
             * Pessimistic locking:
             *
             * The product row is locked while this
             * transaction is processing.
             *
             * This prevents simultaneous sales from
             * overselling the same stock.
             */

            Product product =
                    productRepository.findByIdForUpdate(
                            itemRequest.getProductId()
                    ).orElseThrow(() ->
                            new RuntimeException(
                                    "Product not found: "
                                            + itemRequest.getProductId()
                            ));


            BigDecimal quantity =
                    BigDecimal.valueOf(
                            itemRequest.getQuantity());


            BigDecimal sellingPrice =
                    product.getSellingPrice();

            BigDecimal purchasePrice =
                    product.getPurchasePrice();


            if (sellingPrice == null) {

                throw new RuntimeException(
                        "Selling price is not set for product: "
                                + product.getName());
            }


            if (purchasePrice == null) {

                throw new RuntimeException(
                        "Purchase price is not set for product: "
                                + product.getName());
            }


            BigDecimal itemTotal =
                    sellingPrice.multiply(quantity);


            BigDecimal availableStock =
                    BigDecimal.valueOf(
                            product.getQuantity());


            // ==========================================
            // STOCK CHECK
            // ==========================================

            if (availableStock.compareTo(quantity) < 0) {

                throw new RuntimeException(
                        "Insufficient stock for product: "
                                + product.getName()
                                + ". Available: "
                                + product.getQuantity()
                                + ", Requested: "
                                + itemRequest.getQuantity());
            }


            // ==========================================
            // SALE ITEM
            // ==========================================

            SaleItem saleItem =
                    new SaleItem();

            saleItem.setSale(sale);

            saleItem.setProduct(product);

            saleItem.setQuantity(
                    itemRequest.getQuantity());

            saleItem.setPrice(
                    sellingPrice);

            /*
             * Store the historical purchase price.
             */
            saleItem.setPurchasePrice(
                    purchasePrice);

            saleItem.setTotal(
                    itemTotal);

            sale.getItems().add(
                    saleItem);


            subtotal =
                    subtotal.add(
                            itemTotal);


            // ==========================================
            // REDUCE STOCK
            // ==========================================

            product.setQuantity(
                    product.getQuantity()
                            - itemRequest.getQuantity());

            productRepository.save(
                    product);


            // ==========================================
            // INVENTORY TRANSACTION
            // ==========================================

            InventoryTransaction transaction =
                    new InventoryTransaction();

            transaction.setProduct(
                    product);

            transaction.setTransactionType(
                    "SALE");

            transaction.setQuantity(
                    itemRequest.getQuantity());

            transaction.setAmount(
                    itemTotal);

            transaction.setTransactionDate(
                    LocalDateTime.now());

            inventoryTransactionRepository.save(
                    transaction);
        }


        // ==========================================
        // DISCOUNT
        // ==========================================

        BigDecimal discount =
                request.getDiscount() == null
                        ? BigDecimal.ZERO
                        : request.getDiscount();


        if (discount.compareTo(
                BigDecimal.ZERO) < 0) {

            throw new RuntimeException(
                    "Discount cannot be negative");
        }


        if (discount.compareTo(
                subtotal) > 0) {

            throw new RuntimeException(
                    "Discount cannot be greater than subtotal");
        }


        BigDecimal taxableAmount =
                subtotal.subtract(
                        discount);


        // ==========================================
        // TAX
        // ==========================================

        BigDecimal taxPercent =
                request.getTaxPercent() == null
                        ? BigDecimal.ZERO
                        : request.getTaxPercent();


        if (taxPercent.compareTo(
                BigDecimal.ZERO) < 0) {

            throw new RuntimeException(
                    "Tax percentage cannot be negative");
        }


        BigDecimal taxAmount =
                taxableAmount
                        .multiply(taxPercent)
                        .divide(
                                BigDecimal.valueOf(100),
                                2,
                                RoundingMode.HALF_UP);


        // ==========================================
        // FINAL TOTAL
        // ==========================================

        BigDecimal total =
                taxableAmount
                        .add(taxAmount)
                        .setScale(
                                2,
                                RoundingMode.HALF_UP);


        sale.setSubtotal(
                subtotal.setScale(
                        2,
                        RoundingMode.HALF_UP));

        sale.setDiscount(
                discount.setScale(
                        2,
                        RoundingMode.HALF_UP));

        sale.setTax(
                taxAmount);

        sale.setTotalAmount(
                total);


        // ==========================================
        // PAYMENT METHOD
        // ==========================================

        String paymentMethod =
                request.getPaymentMethod();


        if (paymentMethod == null ||
                paymentMethod.trim().isEmpty()) {

            throw new RuntimeException(
                    "Payment method is required");
        }


        paymentMethod =
                paymentMethod
                        .trim()
                        .toUpperCase();


        if (!paymentMethod.equals("CASH") &&
                !paymentMethod.equals("UPI") &&
                !paymentMethod.equals("CARD") &&
                !paymentMethod.equals("CREDIT")) {

            throw new RuntimeException(
                    "Payment method must be CASH, UPI, CARD or CREDIT");
        }


        sale.setPaymentMethod(
                paymentMethod);


        // ==========================================
        // AMOUNT RECEIVED
        // ==========================================

        BigDecimal amountReceived =
                request.getAmountReceived() == null
                        ? BigDecimal.ZERO
                        : request.getAmountReceived();


        if (amountReceived.compareTo(
                BigDecimal.ZERO) < 0) {

            throw new RuntimeException(
                    "Amount received cannot be negative");
        }


        // ==========================================
        // CREDIT SALE
        // ==========================================

        if (paymentMethod.equals("CREDIT")) {

            if (customer == null) {

                throw new RuntimeException(
                        "Customer is required for credit sale");
            }


            if (amountReceived.compareTo(
                    total) > 0) {

                throw new RuntimeException(
                        "Amount received cannot be greater than total amount");
            }


            BigDecimal creditDue =
                    total.subtract(
                            amountReceived)
                            .setScale(
                                    2,
                                    RoundingMode.HALF_UP);


            sale.setAmountReceived(
                    amountReceived);

            sale.setChangeAmount(
                    BigDecimal.ZERO);

            sale.setCreditAmount(
                    creditDue);


            CustomerCredit credit =
                    customerCreditRepository
                            .findByCustomer(
                                    customer)
                            .orElse(null);


            if (credit == null) {

                credit =
                        new CustomerCredit();

                credit.setCustomer(
                        customer);

                credit.setCreditAmount(
                        BigDecimal.ZERO);

                credit.setPaidAmount(
                        BigDecimal.ZERO);

                credit.setOutstandingAmount(
                        BigDecimal.ZERO);

                credit.setCreatedDate(
                        LocalDateTime.now());
            }


            // ==========================================
            // CREDIT ACCOUNTING
            // ==========================================

            credit.setCreditAmount(
                    credit.getCreditAmount()
                            .add(total));

            credit.setPaidAmount(
                    credit.getPaidAmount()
                            .add(amountReceived));

            credit.setOutstandingAmount(
                    credit.getOutstandingAmount()
                            .add(creditDue));

            credit.setUpdatedDate(
                    LocalDateTime.now());


            customerCreditRepository.save(
                    credit);
        }


        // ==========================================
        // CASH / UPI / CARD
        // ==========================================

        else {

            if (amountReceived.compareTo(
                    total) < 0) {

                throw new RuntimeException(
                        "Amount received is less than total amount");
            }


            BigDecimal change =
                    amountReceived
                            .subtract(total)
                            .setScale(
                                    2,
                                    RoundingMode.HALF_UP);


            sale.setAmountReceived(
                    amountReceived);

            sale.setChangeAmount(
                    change);

            sale.setCreditAmount(
                    BigDecimal.ZERO);
        }


        // ==========================================
        // SAVE SALE
        // ==========================================

        return saleRepository.save(
                sale);
    }


    // ==========================================
    // GET ALL SALES
    // ==========================================

    public List<Sale> getAllSales() {

        return saleRepository.findAll();
    }


    // ==========================================
    // GET SALE BY ID
    // ==========================================

    public Sale getSaleById(
            Integer id) {

        return saleRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Sale not found"));
    }


    // ==========================================
    // GET SALE BY INVOICE
    // ==========================================

    public Sale getSaleByInvoice(
            String invoiceNumber) {

        return saleRepository
                .findByInvoiceNumber(
                        invoiceNumber)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Sale not found"));
    }
}

