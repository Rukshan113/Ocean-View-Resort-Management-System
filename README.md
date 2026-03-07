# Ocean View Resort Management System

The **Ocean View Resort Management System** is a web-based reservation management system designed for a popular beachside hotel in **Galle, Sri Lanka**.

It automates **room reservations, guest registration, billing, and reporting**, replacing the traditional manual process to improve efficiency, reduce errors, and enhance the overall guest experience.

This project is implemented using **Java (JSP/Servlets)** with a **MySQL database** and follows **MVC Architecture** and the **DAO Design Pattern**.

---

# Features

## User Authentication
- Secure login for **Admin** and **Receptionist** roles  
- **Role-Based Access Control (RBAC)**  
- **First-time login requires password change**

## User Management (Admin)
- Create staff accounts  
- Search staff accounts  
- Update staff accounts  
- Delete staff accounts  

## Guest & Reservation Management
- Add reservations  
- Search reservations  
- Cancel reservations  
- Assign rooms based on availability  

## Billing
- Automated calculation of total bill based on **room type and duration**

## Reports
- Show reservation information

---

# System Architecture

## Three-Tier Architecture

**Presentation Layer**
- JSP  
- HTML / CSS  

**Business Logic Layer**
- Java Servlets  

**Data Access Layer**
- DAO classes connected to MySQL  

Additional Design Principles:

- Follows **MVC Design Pattern** for maintainability and scalability  

---

# Installation / Setup

## 1. Clone the Repository

```bash
git clone https://github.com/Rukshan113/Ocean-View-Resort-Management-System.git
```

## 2. Import the Project
Import the project into **NetBeans** or your preferred **Java IDE**.

## 3. Setup the Database
Run the SQL scripts provided in the **database folder** to create the required tables:

- Users  
- Rooms  
- Reservations  

## 4. Deploy the Application
Deploy the project on **Apache Tomcat Server**.

## 5. Run the System

Open your browser and access:

```
http://localhost:8080/Ocean-View-Resort-Management-System
```

---

# Default Login Credentials

## Admin
- **Email:** admin@gmail.com  
- **Password:** 123  

## Receptionist
- **Email:** r1@gmail.com  
- **Password:** 123  

---

# Technologies Used

- Java (JSP / Servlets)
- MySQL
- HTML
- CSS
- Apache Tomcat
- MVC Architecture
- DAO Design Pattern
