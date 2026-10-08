package com.store.management.service;

import com.store.management.dto.DashboardResponse;
import com.store.management.entity.Product;
import com.store.management.entity.Purchase;
import com.store.management.entity.Sale;
import com.store.management.entity.SaleItem;
import com.store.management.repository.CustomerRepository;
import com.store.management.repository.ProductRepository;
import com.store.management.repository.PurchaseRepository;
import com.store.management.repository.SaleRepository;
import com.store.management.repository.SupplierRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DashboardService {

    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final SupplierRepository supplierRepository;
    private final SaleRepository saleRepository;
    private final PurchaseRepository purchaseRepository;

    public DashboardService(
            ProductRepository productRepository,
            CustomerRepository customerRepository,
            SupplierRepository supplierRepository,
            SaleRepository saleRepository,
            PurchaseRepository purchaseRepository) {

        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.supplierRepository = supplierRepository;
        this.saleRepository = saleRepository;
        this.purchaseRepository = purchaseRepository;
    }

    // ==========================================
    // DASHBOARD
    // ==========================================

    public DashboardResponse getDashboard() {

        DashboardResponse response =
                new DashboardResponse();

        LocalDate today =
                LocalDate.now();

        LocalDateTime startOfDay =
                today.atStartOfDay();

        LocalDateTime startOfTomorrow =
                today.plusDays(1).atStartOfDay();

        // ==========================================
        // TODAY'S SALES
        // ==========================================

        List<Sale> sales =
                saleRepository
                        .findAllByOrderBySaleDateDesc();

        BigDecimal todaySales =
                BigDecimal.ZERO;

        BigDecimal todayProfit =
                BigDecimal.ZERO;

        for (Sale sale : sales) {

            if (sale.getSaleDate() == null) {
                continue;
            }

            if (sale.getSaleDate()
                    .isBefore(startOfDay)) {

                continue;
            }

            if (!sale.getSaleDate()
                    .isBefore(startOfTomorrow)) {

                continue;
            }

            // ------------------------------------------
            // SALES TOTAL
            // ------------------------------------------

            if (sale.getTotalAmount() != null) {

                todaySales =
                        todaySales.add(
                                sale.getTotalAmount());
            }

            // ------------------------------------------
            // PROFIT
            // ------------------------------------------

            if (sale.getItems() == null) {
                continue;
            }

            for (SaleItem item :
                    sale.getItems()) {

                if (item.getPrice() == null ||
                        item.getQuantity() == null) {

                    continue;
                }

                BigDecimal purchasePrice =
                        item.getPurchasePrice();

                /*
                 * New sales have their original
                 * purchase price saved directly
                 * inside SaleItem.
                 *
                 * Old sales may have NULL because
                 * this field did not exist before.
                 *
                 * For those old records, use the
                 * current product purchase price
                 * as a fallback.
                 */

                if (purchasePrice == null &&
                        item.getProduct() != null) {

                    purchasePrice =
                            item.getProduct()
                                    .getPurchasePrice();
                }

                if (purchasePrice == null) {
                    continue;
                }

                BigDecimal sellingPrice =
                        item.getPrice();

                BigDecimal quantity =
                        BigDecimal.valueOf(
                                item.getQuantity());

                BigDecimal itemProfit =
                        sellingPrice
                                .subtract(
                                        purchasePrice)
                                .multiply(quantity);

                todayProfit =
                        todayProfit.add(
                                itemProfit);
            }
        }

        // ==========================================
        // TODAY'S PURCHASES
        // ==========================================

        List<Purchase> purchases =
                purchaseRepository
                        .findAllByOrderByPurchaseDateDesc();

        BigDecimal todayPurchases =
                BigDecimal.ZERO;

        for (Purchase purchase :
                purchases) {

            if (purchase.getPurchaseDate() == null) {
                continue;
            }

            if (purchase.getPurchaseDate()
                    .isBefore(startOfDay)) {

                continue;
            }

            if (!purchase.getPurchaseDate()
                    .isBefore(startOfTomorrow)) {

                continue;
            }

            if (purchase.getTotalAmount() != null) {

                todayPurchases =
                        todayPurchases.add(
                                purchase.getTotalAmount());
            }
        }

        // ==========================================
        // TOTAL PRODUCTS
        // ==========================================

        long totalProducts =
                productRepository.count();

        // ==========================================
        // LOW STOCK PRODUCTS
        // ==========================================

        List<Product> products =
                productRepository.findAll();

        long lowStockProducts =
                products.stream()
                        .filter(product -> {

                            Float quantity =
                                    product.getQuantity();

                            Float minimumStock =
                                    product.getMinimumStock();

                            if (quantity == null) {
                                quantity = 0f;
                            }

                            if (minimumStock == null) {
                                return false;
                            }

                            return quantity <= minimumStock;
                        })
                        .count();

        // ==========================================
        // TOTAL CUSTOMERS
        // ==========================================

        long totalCustomers =
                customerRepository.count();

        // ==========================================
        // TOTAL SUPPLIERS
        // ==========================================

        long totalSuppliers =
                supplierRepository.count();

        // ==========================================
        // RESPONSE
        // ==========================================

        response.setTodaySales(
                todaySales.setScale(
                        2,
                        RoundingMode.HALF_UP));

        response.setTodayPurchases(
                todayPurchases.setScale(
                        2,
                        RoundingMode.HALF_UP));

        response.setTodayProfit(
                todayProfit.setScale(
                        2,
                        RoundingMode.HALF_UP));

        response.setTotalProducts(
                totalProducts);

        response.setLowStockProducts(
                lowStockProducts);

        response.setTotalCustomers(
                totalCustomers);

        response.setTotalSuppliers(
                totalSuppliers);

        return response;
    }
}