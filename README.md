## Library Management System — SOLID Architecture (Assignment 4)
- Project Overview

This project is a refactored version of Assignment 3. The goal of this milestone is to redesign the application using SOLID principles, layered architecture, and advanced Java OOP features. The system demonstrates clean separation of responsibilities, better scalability, and maintainable code structure.

The application manages a small library database with two main entities:

Authors

Books (PrintedBook and EBook)

## Architecture

The project strictly follows layered architecture:

Controller → Service → Repository → Database

Each layer has a clear responsibility.

Controller

Handles user input and output, prints results to the console, and delegates all work to services. It contains no business logic.

Service

Contains business logic and validation rules. Throws custom exceptions when input is invalid or data is missing. Depends only on repository interfaces (Dependency Inversion Principle).

Repository

Works only with JDBC and SQL queries. Performs CRUD operations. Contains no business logic.

Database

PostgreSQL database with tables, foreign keys, and constraints.

## OOP Design
Abstract Base Class — BookBase

The system uses an abstract base class to demonstrate polymorphism and inheritance.

Fields:

- id

- title

- price

- author

Methods:

- abstract getFormat()

- abstract getLoanPeriodDays()

- concrete printInfo()

This allows all book types to be handled through the common BookBase reference.

Subclasses (LSP compliant)
PrintedBook
pages shelf location EBook file size
download link

Both override abstract methods and behave correctly when used as BookBase. This follows the Liskov Substitution Principle.

Composition

Book → Author

Each book contains a reference to an author. This relationship is implemented both in Java (object reference) and in the database using a foreign key.

# SOLID Principles Implementation
SRP (Single Responsibility Principle)

- Each class has only one responsibility. Controllers handle input/output, services handle logic, repositories handle database access.

OCP (Open/Closed Principle)

- New book types can be added without modifying existing code. We only create new subclasses.

LSP (Liskov Substitution Principle)

- PrintedBook and EBook can replace BookBase anywhere in the program.

ISP (Interface Segregation Principle)

- Small, focused interfaces such as Validatable and PricedItem are used instead of large ones.

DIP (Dependency Inversion Principle)

- Services depend on repository interfaces rather than concrete JDBC implementations.

# Advanced Java Features
Generics

- Implemented in:
- CrudRepository<T, ID>

This allows reusable CRUD logic for any entity type and reduces code duplication.

- Lambdas

Used in SortingUtils for sorting collections. Example:
books.sort(Comparator.comparingDouble(BookBase::getPrice));

This demonstrates modern functional programming in Java.

Reflection (RTTI)

ReflectionUtils prints:

- class name

- fields

- methods

It demonstrates runtime type inspection and metadata analysis.

Interface default/static methods

Implemented in the Validatable interface with:

- default require()

- static notBlank()

These methods provide shared reusable validation logic.

# Database Schema
- authors

- author_id (Primary Key)

- full_name

- country

- books

- book_id (Primary Key)

- title

- price

- author_id (Foreign Key)

# Relationship:
- books.author_id → authors.author_id

This ensures referential integrity between books and authors.

# Features Demonstrated

The application demonstrates:

- Creating entities

- Reading all records

- Updating records

- Deleting records

- Validation errors

- Custom exceptions

- Lambda sorting

- Reflection output

- Polymorphism

Generic repository usage

All features are shown in Main.java through console output.

# Project Structure
src/

├── controller/

├── service/

│    ├── interfaces/

├── repository /

│    ├── interfaces/

├── model/

├── utils/

├── exception/

├── DatabaseConnection.java

└── Main.java

# How to Run

Requirements:

Java 17+

PostgreSQL

Steps:

Create database

Run schema.sql

Configure DatabaseConnection

Run Main.java

# What I Learned

During this assignment I learned how to:

Design layered architecture

Apply SOLID principles in real projects

Use generics and lambdas

Work with reflection

Write cleaner and more maintainable Java code

Separate business logic from database logic
