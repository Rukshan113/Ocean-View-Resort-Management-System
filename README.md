Ocean View Resort Management System

The Ocean View Resort Management System is a web-based reservation management system designed for a popular beachside hotel in Galle, Sri Lanka.
It automates room reservations, guest registration, billing, and reporting, replacing the traditional manual process to improve efficiency, reduce errors,
and enhance the overall guest experience.

This project is implemented using Java (JSP/Servlets) with a MySQL database and follows MVC architecture and DAO design pattern. 

Features

User Authentication

  Secure login for Admin and Receptionist roles
  
  Role-based access control (RBAC)
  
  First-time login requires password change

User Management (Admin)

  Create, search, update, and delete staff accounts

Guest & Reservation Management

  Add, search and cancel reservations
  
  Assign rooms based on availability

Billing

  Automated calculation of total bill based on room type and duration

Reports

System Architecture

Three-Tier Architecture

  Presentation Layer: JSP + HTML/CSS
  
  Business Logic Layer: Java Servlets
  
  Data Access Layer: DAO classes connecting to MySQL

Follows MVC Design Pattern for maintainability and scalability

Implements DAO Pattern for database interaction


Installation / Setup
  Clone the repository,
  git clone https://github.com/Rukshan113/Ocean-View-Resort-Management-System.git
  
  Import the project into NetBeans or your preferred Java IDE.

  Setup the MySQL database,
  Run the provided SQL scripts in the database folder to create tables (Users, Rooms, Reservations)

  Deploy the project on Apache Tomcat.

  Access the system via your browser:
    http://localhost:8080/Ocean-View-Resort-Management-System

Default login credentials:

Admin: email - admin@gmail.com
       password - 123

Receptionist: email - r1@gmail.com 
              password - 123
