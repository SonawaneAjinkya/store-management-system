package com.store.management.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "shop_settings")
public class ShopSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String shopName;

    private String tagline;

    private String address;

    private String phone;

    private String email;

    private String gstin;

    @Column(nullable = false)
    private String invoicePrefix = "INV-";

    @Column(nullable = false)
    private String purchaseInvoicePrefix = "PUR-";

    @Column(nullable = false)
    private String currencySymbol = "₹";

    private String receiptFooter;

    public ShopSettings() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getInvoicePrefix() {
        return invoicePrefix;
    }

    public void setInvoicePrefix(String invoicePrefix) {
        this.invoicePrefix = invoicePrefix;
    }

    public String getPurchaseInvoicePrefix() {
        return purchaseInvoicePrefix;
    }

    public void setPurchaseInvoicePrefix(
            String purchaseInvoicePrefix) {

        this.purchaseInvoicePrefix =
                purchaseInvoicePrefix;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getReceiptFooter() {
        return receiptFooter;
    }

    public void setReceiptFooter(String receiptFooter) {
        this.receiptFooter = receiptFooter;
    }
}