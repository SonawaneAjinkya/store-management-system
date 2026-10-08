# Store Management Frontend

A polished, responsive frontend for the Spring Boot Store-management backend.

## Folder structure

- `html/index.html` — Dashboard
- `html/sell.html` — POS / New Sale
- `html/products.html` — Product management
- `html/restock.html` — Purchases / restocking
- `html/customers.html` — Customer management
- `html/transactions.html` — Sales + inventory history
- `html/settings.html` — Shop settings
- `html/bill.html` — Printable invoice
- `css/style.css` — Complete theme
- `js/app.js` — Shared API utilities
- `js/sell.js` — POS logic

## Backend

The frontend expects:

http://localhost:8080/api

## Run

1. Start the Spring Boot backend in Eclipse.
2. Open the `Store-management-frontend` folder in VS Code.
3. Install/use Live Server.
4. Open:
   `html/index.html`

## Navigation

Dashboard -> `index.html`
New Sale -> `sell.html`
Products -> `products.html`
Restock -> `restock.html`
Customers -> `customers.html`
Transactions -> `transactions.html`
Settings -> `settings.html`

All HTML pages are intentionally kept inside the `html` folder so relative navigation is consistent.
