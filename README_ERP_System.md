# 🚀 ERP System Backend (Spring Boot)

A robust backend system designed to manage products, inventory, orders, purchases, suppliers, and payments with secure authentication and role-based access control.

---

## 🧠 Overview

This ERP system demonstrates real-world backend architecture with:

- Modular design (Product, Inventory, Orders, Purchase, Supplier)
- JWT-based authentication
- Role-Based Access Control (RBAC)
- Transaction-safe operations
- Inventory consistency handling
- Payment failure rollback mechanism

---

## 🔐 Authentication & Security

- JWT Authentication
- Stateless session management
- Role-based access using `@PreAuthorize`
- Roles:
  - ADMIN → Full access
  - MANAGER → Operational access
  - EMPLOYEE → Read-only access

---

## 🧩 Modules

### 📦 Product
- Create, update, delete, view products

### 📊 Inventory
- Add stock
- Reduce stock
- Track stock movement history

### 🧾 Orders (Sales)
- Create orders
- Cancel orders
- Automatic stock deduction
- Payment integration with rollback

### 🛒 Purchase (Suppliers)
- Create purchase
- Update / cancel purchase
- Stock increases on purchase
- Approval-based flow (ADMIN)

### 🏢 Supplier
- Manage supplier data
- Used in purchase module

### 💳 Payment
- Internal service
- Controls final order state

---

## 🔁 Core System Flow

### Order Flow
1. Validate request  
2. Reduce stock  
3. Create order  
4. Process payment  

- ✅ Success → Order completed  
- ❌ Failure → Stock restored  

---

### Purchase Flow
1. Create purchase  
2. Validate supplier  
3. Add stock  

---

### Inventory Logic
- Purchase → Stock increases  
- Order → Stock decreases  
- Cancellation → Stock restored  

---

## 🛠 Tech Stack

- Java (Spring Boot)
- Spring Security
- JWT (Authentication)
- JPA / Hibernate
- MySQL / PostgreSQL
- Maven

---

## 📡 API Endpoints (Sample)

### Auth
POST /auth/register  
POST /auth/login  

### Product
POST /products  
GET /products  
PUT /products/{id}  
DELETE /products/{id}  

### Inventory
PATCH /inventory/{id}/add-stock  
PATCH /inventory/{id}/reduce-stock  
GET /inventory/{id}/history  

### Orders
POST /orders  
PATCH /orders/{id}/cancel  
GET /orders  

### Purchase
POST /purchases  
PATCH /purchases/{id}/approve  

### Supplier
POST /suppliers  
GET /suppliers  

---

## 🧪 How to Run

1. Clone repository  
   git clone <your-repo-url>

2. Configure database in application.properties  

3. Run application  
   mvn spring-boot:run  

4. Access APIs at  
   http://localhost:8080  

---

## 🔑 Authorization

All protected APIs require:

Authorization: Bearer <JWT_TOKEN>

---

## 📌 Key Features

- Role-based permission system
- Transaction-safe stock handling
- Payment failure recovery
- Clean modular architecture
- Scalable backend design

---

## 🚀 Future Improvements

- Refresh token mechanism
- Swagger/OpenAPI documentation
- Deployment (Render/AWS)
- Audit logging
- Concurrency handling

---

## 📈 Why This Project Stands Out

Unlike basic CRUD apps, this system includes:

- Real business logic (inventory + payment consistency)
- Secure role-based access control
- Multi-module system design
- Transaction handling

---

## 👨‍💻 Author

Divyansh jeena 
GitHub: https://github.com/divyansh-jeena
