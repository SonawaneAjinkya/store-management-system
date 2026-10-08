package com.store.management.dto;

import java.math.BigDecimal;

public class DashboardResponse {

    private BigDecimal todaySales;
    private BigDecimal todayPurchases;
    private BigDecimal todayProfit;

    private Long totalProducts;
    private Long lowStockProducts;
    private Long totalCustomers;
    private Long totalSuppliers;

    public DashboardResponse() {
    }

    public BigDecimal getTodaySales() {
        return todaySales;
    }

    public void setTodaySales(BigDecimal todaySales) {
        this.todaySales = todaySales;
    }

    public BigDecimal getTodayPurchases() {
        return todayPurchases;
    }

    public void setTodayPurchases(BigDecimal todayPurchases) {
        this.todayPurchases = todayPurchases;
    }

    public BigDecimal getTodayProfit() {
        return todayProfit;
    }

    public void setTodayProfit(BigDecimal todayProfit) {
        this.todayProfit = todayProfit;
    }

    public Long getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(Long totalProducts) {
        this.totalProducts = totalProducts;
    }

    public Long getLowStockProducts() {
        return lowStockProducts;
    }

    public void setLowStockProducts(Long lowStockProducts) {
        this.lowStockProducts = lowStockProducts;
    }

    public Long getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(Long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public Long getTotalSuppliers() {
        return totalSuppliers;
    }

    public void setTotalSuppliers(Long totalSuppliers) {
        this.totalSuppliers = totalSuppliers;
    }
}