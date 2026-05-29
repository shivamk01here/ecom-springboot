
# 🛒 E-Commerce Backend (Java + Spring Boot)

A scalable and modular e-commerce platform built with **Java 17** and **Spring Boot 3**.  
This is a work-in-progress project designed to evolve gradually — starting from core features like authentication and product management, and expanding towards a full production-ready system.

---

## 🚀 Features (Planned & In Progress)

- [x] Project setup with Spring Boot  
- [x] User authentication with JWT  
- [x] Product catalog CRUD (create, update, delete, search)  
- [x] Shopping cart & wishlist  
- [x] Order placement & payment integration  
- [ ] Inventory management & admin dashboard  
- [ ] Microservices architecture + Docker deployment  
- [ ] CI/CD with GitHub Actions  
- [ ] Cloud deployment (AWS RDS + EC2)

---

## 🛠 Tech Stack

- **Language:** Java 17  
- **Framework:** Spring Boot 3  
- **Database:** MySQL (via JPA/Hibernate)  
- **Build Tool:** Maven / Gradle  
- **Testing:** JUnit + Mockito  
- **API Docs:** Swagger / OpenAPI  

---

## 🏗 Architecture (Initial Plan)

```

Client → REST API → Controllers → Services → Repositories → MySQL Database

````

Later will expand to microservices + Docker.

---

## 📌 Roadmap

This project is intentionally built **step by step** to showcase progression:  

1. ✅ Initial setup with Spring Boot starter  
2. ✅ User authentication (JWT + role-based access)  
3. ✅ Product catalog with CRUD operations  
4. ✅ Shopping cart & wishlist service  
5. ✅ Order management + payments (Stripe/PayPal)  
6. 🚧 Microservices refactor + Docker  
7. 🚧 Deployment to AWS with CI/CD  

---

## ⚡ Getting Started

Clone the repo:
```bash
git clone https://github.com/yourusername/ecom-springboot.git
cd ecom-springboot
````

Run with Maven:

```bash
./mvnw spring-boot:run
```

Or with Gradle:

```bash
./gradlew bootRun
```

---

## 🔌 API Endpoints (Product Catalog)

Here are the REST API endpoints implemented for the product catalog:

| Method | Endpoint | Description | Request Body / Params |
| :--- | :--- | :--- | :--- |
| **GET** | `/api/products` | Retrieve all products | None |
| **GET** | `/api/products/{id}` | Retrieve product by ID | None |
| **GET** | `/api/products/search` | Search products by name/description | `?query={keyword}` |
| **POST** | `/api/products` | Create a new product (PermitAll) | `ProductRequest` JSON |
| **PUT** | `/api/products/{id}` | Update an existing product (PermitAll) | `ProductRequest` JSON |
| **DELETE** | `/api/products/{id}` | Delete product by ID (PermitAll) | None |

---

## 📖 Notes

This repository is not just about coding features — it's about demonstrating:

* **Clean architecture** & modular design
* **Production-ready practices** (gradually adding advanced features)

