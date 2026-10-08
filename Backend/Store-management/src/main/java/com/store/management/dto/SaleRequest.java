package com.store.management.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;
import java.util.List;

public class SaleRequest {

    private Integer customerId;

    private String customerName;

    private String customerPhone;

    // Discount amount in ₹
    private BigDecimal discount = BigDecimal.ZERO;

    // GST percentage
    private BigDecimal taxPercent = BigDecimal.ZERO;

    private String paymentMethod;

    private BigDecimal amountReceived = BigDecimal.ZERO;

    @NotEmpty(message = "Sale must contain at least one item")
    @Valid
    private List<SaleItemRequest> items;

    public SaleRequest() {
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getTaxPercent() {
        return taxPercent;
    }

    public void setTaxPercent(BigDecimal taxPercent) {
        this.taxPercent = taxPercent;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(
            BigDecimal amountReceived) {

        this.amountReceived = amountReceived;
    }

    public List<SaleItemRequest> getItems() {
        return items;
    }

    public void setItems(
            List<SaleItemRequest> items) {

        this.items = items;
    }
}