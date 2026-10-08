# 🏪 Store Management & POS System

> A modern full-stack retail management and Point of Sale (POS) system designed for small and medium-sized shops.

**Store Management & POS System** is a complete retail management application built with **Java, Spring Boot, MySQL, HTML, CSS and JavaScript**.

It provides a centralized system for managing products, inventory, sales, purchases, customers, suppliers, customer credit, billing and shop configuration.

---

## 📸 Screenshots

### Dashboard

![Dashboard](screenshots/dashboard.png)

The dashboard provides a quick overview of:

- Today's sales
- Today's purchases
- Today's profit
- Total products
- Low-stock products
- Customers
- Suppliers

---

### Point of Sale

![POS](screenshots/pos.png)

The POS interface allows shop staff to:

- Search products
- Add products to cart
- Change quantities
- Apply discounts
- Calculate GST
- Select payment method
- Handle cash/change
- Handle credit sales
- Generate invoices

---

### Product Management

![Products](screenshots/products.png)

Product management includes:

- Product information
- Purchase price
- Selling price
- Stock quantity
- Minimum stock level
- Category
- Unit
- Barcode

---

### Customer Management

![Customers](screenshots/customers.png)

Customer management provides:

- Customer profiles
- Contact information
- Credit balance
- Credit payments
- Payment history

---

### Billing

![Invoice](screenshots/invoice.png)

Invoices include:

- Shop information
- Invoice number
- Customer information
- Product details
- Quantity
- Price
- Discount
- GST
- Total
- Amount received
- Change
- Credit amount

---

# ✨ Features

## 📊 Dashboard

A centralized dashboard for monitoring shop activity.

- Today's sales
- Today's purchases
- Today's profit
- Total products
- Low-stock products
- Total customers
- Total suppliers

---

## 🛒 Point of Sale

A complete retail billing workflow.

- Product search
- Barcode lookup
- Shopping cart
- Quantity controls
- Automatic subtotal calculation
- Discount calculation
- GST calculation
- Final bill calculation
- Cash payment
- UPI payment
- Card payment
- Credit payment
- Automatic change calculation
- Automatic credit calculation
- Invoice generation

---

## 📦 Product Management

Manage the complete product catalog.

- Add products
- Update products
- Delete products
- Search products
- Barcode support
- Categories
- Units
- Purchase price
- Selling price
- Current stock
- Minimum stock level
- Low-stock detection

---

## 🔄 Inventory Management

Inventory automatically changes when business transactions occur.

### Stock In

Stock increases when:

- New stock is purchased
- Products are restocked

### Stock Out

Stock decreases when:

- A customer purchases a product
- Stock is manually removed

The system also maintains inventory transaction history.

---

## 💰 Sales Management

Every sale stores:

- Invoice number
- Customer information
- Products sold
- Quantity
- Selling price
- Historical purchase price
- Subtotal
- Discount
- Tax
- Final total
- Payment method
- Amount received
- Change
- Credit amount
- Sale date

Historical purchase price is stored with each sale item so that profit calculations remain accurate even if the product's purchase price changes later.

---

## 🚚 Purchase & Restocking

Manage purchases from suppliers.

- Supplier selection
- Product selection
- Purchase quantity
- Purchase price
- Discount
- GST/tax
- Purchase invoice
- Automatic stock increase
- Purchase history

---

## 👥 Customer Management

Manage customer information.

- Name
- Phone
- Email
- Address
- Customer history
- Credit balance
- Credit payments

---

## 💳 Customer Credit

The system supports shop credit management.

Features include:

- Add credit
- Track outstanding credit
- Record payments
- Cash credit payments
- UPI credit payments
- Card credit payments
- Payment history
- Customer credit balance

---

## 🚛 Supplier Management

Manage supplier information.

- Supplier name
- Phone
- Email
- Address
- GSTIN
- Purchase history

---

## 🧾 Invoice & Billing

Professional invoice generation with:

- Shop branding
- Invoice number
- Customer information
- Product list
- Quantity
- Price
- Subtotal
- Discount
- GST
- Total
- Payment information
- Change
- Credit due
- Printable receipt

---

## ⚙️ Shop Settings

Shop owners can configure:

- Shop name
- Tagline
- Address
- Phone
- Email
- GSTIN
- Sales invoice prefix
- Purchase invoice prefix
- Currency symbol
- Receipt footer

---

# 🧮 Business Calculations

## Inventory Value

```text
Inventory Value = Product Cost × Quantity
