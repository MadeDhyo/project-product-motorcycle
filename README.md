# 🏍️ project-product-motorcycle

A Spring Boot RESTful API designed to manage a relational database of motorcycles and brands. This system features complex filtering by name or category and supports bulk data operations.

## 🚀 Key Features
* **Relational Database**: Managed Many-to-One relationships between `Items` (Motorcycles) and `Brands`.
* **Advanced Filtering**: Optimized `/filter` endpoint that supports searching by:
    * **Partial Name**: Finds items containing specific strings (e.g., "BMW") using `.trim()` to handle whitespace.
    * **Category**: Filters items based on nested string collections.
    * **Brand ID**: Filters items belonging to a specific manufacturer.
* **Bulk Operations**: Specialized endpoints to **Create** or **Delete** multiple items and brands in a single transaction.
* **Database Integrity**: Implemented `@OnDelete(action = OnDeleteAction.CASCADE)` to ensure all orphan categories are automatically wiped when a motorcycle is deleted.
* **Modern UUIDs**: Uses Hibernate 6 `GenerationType.UUID` for standard-compliant identifiers, removing deprecated strategy warnings.



## 🛠️ Tech Stack
* **Java 17**
* **Spring Boot 3.x**
* **Spring Data JPA** (Hibernate 6)
* **MySQL**
* **Maven**

---

## ⚙️ Setup & Configuration

### 1. Database Setup
Create a MySQL database named `db_motor` and update your `src/main/resources/application.properties`:
``properties
spring.datasource.url=jdbc:mysql://localhost:3306/db_motor
spring.datasource.username=YOUR_USER
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update``


### 2. ID Reset (For Testing)
If you need your numeric Brand IDs to start from 1 again (for example, after deleting all data), run the following SQL script in your MySQL client to truncate existing data and reset the auto-increment counters:



``sql
-- Disable foreign key checks to allow truncation
SET FOREIGN_KEY_CHECKS = 0;

-- Wipe data and reset ID counters to 1
TRUNCATE TABLE item_categories;
TRUNCATE TABLE items;
TRUNCATE TABLE brands;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

# 📖 API Documentation

This API provides comprehensive endpoints for managing a motorcycle inventory, including products (items) and manufacturers (brands). It supports advanced filtering and high-performance bulk operations.

**Base URL**: `http://localhost:8080`

---

## 🏍️ Product API (`/api/v1/products`)

The Product API manages individual motorcycles, their pricing, stock levels, and associated categories.

### 🟢 Retrieval & Filtering
| Method | Endpoint | Query Parameters | Description |
| :--- | :--- | :--- | :--- |
| **GET** | `/` | `page`, `size`, `category`, `brandId` | Fetch all products with pagination support. Can be filtered by category or brand. |
| **GET** | `/filter` | `name`, `category`, `brandId` | **Advanced Search**: Filter by partial name (case-insensitive), category, or brand ID. |
| **GET** | `/{id}` | N/A | Fetch a single product by its unique UUID. |

### 🟡 Mutations (Create/Update)
| Method | Endpoint | Body | Description |
| :--- | :--- | :--- | :--- |
| **POST** | `/` | `Item Object` | Create a single product entry. |
| **POST** | `/bulk` | `Array of Items` | Upload multiple products in a single transaction. |
| **PUT** | `/{id}` | `{"name": "New Name"}` | Partially update a product's name by its UUID. |

### 🔴 Deletion
| Method | Endpoint | Body | Description |
| :--- | :--- | :--- | :--- |
| **DELETE** | `/{id}` | N/A | Permanently remove a single product by its UUID. |
| **DELETE** | `/bulk` | `["uuid1", "uuid2"]` | Remove multiple products at once by providing an array of UUIDs. |

---

## 🏷️ Brand API (`/api/brands`)

The Brand API manages motorcycle manufacturers. Note that deleting a brand may affect items associated with it depending on constraints.

| Method | Endpoint | Body | Description |
| :--- | :--- | :--- | :--- |
| **GET** | `/` | N/A | List all registered brands in the system. |
| **POST** | `/` | `Brand Object` | Register a new motorcycle manufacturer. |
| **DELETE** | `/{id}` | N/A | Delete a single brand by its numeric ID. |
| **DELETE** | `/bulk` | `[1, 2, 3]` | Bulk delete brands using an array of numeric IDs. |

---

## 📝 Sample Data (Bulk Motor Create)
**URL**: `POST /api/v1/products/bulk`

```json
[
  {
    "name": "BMW S1000RR",
    "price": 780000000.0,
    "stock": 2,
    "categories": ["Superbike", "999cc"],
    "brand": { "id": 1 }
  },
  {
    "name": "KTM 1290 Super Duke R",
    "price": 480000000.0,
    "stock": 3,
    "categories": ["Naked", "1301cc"],
    "brand": { "id": 2 }
  },
  {
    "name": "Honda CBR1000RR-R",
    "price": 1100000000.0,
    "stock": 1,
    "categories": ["Superbike", "999cc"],
    "brand": { "id": 6 }
  }
]
