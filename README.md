# 🚦 Traffic Police Management System

<div align="center">

![Java](https://img.shields.io/badge/Java-8-orange?style=for-the-badge&logo=java)
![Hibernate](https://img.shields.io/badge/Hibernate-3.6-blue?style=for-the-badge)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18-blue?style=for-the-badge&logo=postgresql)
![RMI](https://img.shields.io/badge/Java-RMI-red?style=for-the-badge)
![Swing](https://img.shields.io/badge/Java-Swing-green?style=for-the-badge)

**A Distributed Java Application for Traffic Law Enforcement**

*Adventist University of Central Africa (AUCA)*
*Faculty of Information Technology*
*Course: INSY 7312 — Java Programming*
*Academic Year: 2025-2026*

</div>

---

## 👨‍💻 Author

| Field | Details |
|-------|---------|
| **Name** | NTWARI EMERY |
| **Institution** | AUCA — Gishushu Campus |
| **Instructor** | Dr. SEBAGENZI Jason & Jeremie U. Tuyisenge |
| **Submission Date** | May 2026 |

---

## 📋 Table of Contents

- [About the Project](#about-the-project)
- [System Architecture](#system-architecture)
- [Technologies Used](#technologies-used)
- [Features](#features)
- [Database Design](#database-design)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [How to Run](#how-to-run)
- [Login Credentials](#login-credentials)
- [Validation Rules](#validation-rules)
- [Screenshots](#screenshots)
- [Contact](#contact)

---

## 📌 About the Project

The **Traffic Police Management System** is a distributed Java application
developed to assist Rwanda National Police in managing traffic violations,
fines, penalties, and related enforcement activities.

The system replaces manual paper-based processes with a centralized digital
solution that improves accuracy, speed, and consistency in traffic law
enforcement across Rwanda.

### 🎯 Key Goals
- Digitize traffic violation recording and ticket issuance
- Automate fine calculation including repeat offender penalties
- Send real-time email notifications to drivers
- Generate professional PDF/Excel reports
- Provide live statistics and analytics dashboard

---

## 🏗️ System Architecture

---

## 🛠️ Technologies Used

| Technology | Version | Purpose |
|-----------|---------|---------|
| **Java** | JDK 8 | Core programming language |
| **Java RMI** | Built-in | Client-Server communication |
| **Java Swing** | Built-in | Graphical User Interface |
| **Hibernate** | 3.6.10 | ORM / Database persistence |
| **PostgreSQL** | 18 | Relational Database |
| **Apache ActiveMQ** | 5.16.6 | Message broker notifications |
| **iText PDF** | 5.5.13 | PDF report generation |
| **Apache POI** | 5.2.3 | Excel report generation |
| **JavaMail** | 1.4.7 | Email notifications |
| **Google Charts API** | Online | QR code generation |

### Design Patterns Used
- **MVC** — Model View Controller
- **DAO** — Data Access Object
- **RMI** — Remote Method Invocation

---

## ✨ Features

### 🔐 Authentication
- Two-step login with OTP verification
- Admin OTP sent via **Gmail email**
- Officer OTP sent via **email**
- Role-based access control (ADMIN / OFFICER)

### 👮 Officer Management *(Admin only)*
- Create, update, deactivate officer accounts
- Assign roles (ADMIN / OFFICER)
- Badge number management

### 🚗 Driver Management
- Register drivers with full details
- Vehicle registration tracking
- Search by license number or plate

### ⚠️ Violation Type Management *(Admin only)*
- Define violation categories with fine amounts
- Set penalty multipliers for repeat offenders
- Severity levels (LOW / MEDIUM / HIGH / CRITICAL)

### 🎫 Ticket Management
- Issue traffic tickets with unique ticket numbers
- Auto-calculate fines (with repeat offender penalty)
- Track ticket lifecycle: ISSUED → PAID / OVERDUE / ESCALATED
- **Email notification** sent to driver on ticket issue
- Print professional ticket receipt
- Escalate unpaid tickets to legal action

### 💳 Payment Processing
- Process fine payments (CASH, BANK, MOBILE MONEY, ONLINE)
- Auto-mark tickets as PAID
- Delete payment and revert ticket status
- Email receipt notification

### 📊 Statistics & Analytics
- Live bar charts for violations by type
- Pie chart for ticket status distribution
- Officer performance chart
- Collection rate summary

### 🔍 Advanced Search & Filter
- Search by driver name, plate, ticket number
- Filter by status, officer, violation type
- Date range filtering
- Export search results to CSV
- Print filtered tickets

### 📈 Reports & Export
- 6 report types
- Export to **PDF** (iText)
- Export to **Excel** (Apache POI)

### 📧 Email Notifications
- Driver receives email when ticket is issued
- Driver receives overdue reminder
- Driver receives escalation alert
- Each email includes:
  - Full ticket details table
  - **QR code for payment**
  - Rwanda National Police contacts
  - Payment instructions

### 🖨️ Print Receipt
- Print professional A5 ticket receipt
- Includes officer signature line
- Police headquarters details

---

## 🗄️ Database Design

### Entity Relationships

### Tables

| Table | Description |
|-------|-------------|
| `officer` | Traffic officers and admins |
| `driver` | Registered drivers |
| `violation_type` | Categories of traffic violations |
| `ticket` | Issued traffic tickets |
| `ticket_violation` | Many-to-many join table |
| `payment` | Fine payment records |

---

## 📁 Project Structure

---

## 📦 Prerequisites

Install these before running:

1. **Java JDK 8** — https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html
2. **NetBeans IDE 8.2** — https://netbeans.apache.org/download/archive/
3. **PostgreSQL 18** — https://www.postgresql.org/download/
4. **pgAdmin 4** — Included with PostgreSQL installer

### Required JAR Files

---

## ⚙️ Installation & Setup

### Step 1 — Create Database

Open **pgAdmin 4** and run:

```sql
CREATE DATABASE traffic_police_management_system_db;
```

### Step 2 — Create Tables

```sql
CREATE TABLE IF NOT EXISTS officer (
    id SERIAL PRIMARY KEY,
    badge_number VARCHAR(50) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    active BOOLEAN DEFAULT true
);

CREATE TABLE IF NOT EXISTS driver (
    id SERIAL PRIMARY KEY,
    license_number VARCHAR(50) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    national_id VARCHAR(20) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    email VARCHAR(100),
    vehicle_plate VARCHAR(20) NOT NULL,
    vehicle_model VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS violation_type (
    id SERIAL PRIMARY KEY,
    code VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    fine_amount DOUBLE PRECISION NOT NULL,
    penalty_multiplier DOUBLE PRECISION,
    severity_level VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS ticket (
    id SERIAL PRIMARY KEY,
    ticket_number VARCHAR(50) UNIQUE NOT NULL,
    issue_date DATE NOT NULL,
    due_date DATE,
    location VARCHAR(200) NOT NULL,
    total_fine_amount DOUBLE PRECISION,
    status VARCHAR(20) NOT NULL,
    notes TEXT,
    officer_id INTEGER REFERENCES officer(id),
    driver_id INTEGER REFERENCES driver(id)
);

CREATE TABLE IF NOT EXISTS ticket_violation (
    ticket_id INTEGER REFERENCES ticket(id),
    violation_type_id INTEGER REFERENCES violation_type(id),
    PRIMARY KEY (ticket_id, violation_type_id)
);

CREATE TABLE IF NOT EXISTS payment (
    id SERIAL PRIMARY KEY,
    payment_reference VARCHAR(50) UNIQUE NOT NULL,
    amount_paid DOUBLE PRECISION NOT NULL,
    payment_date DATE NOT NULL,
    payment_method VARCHAR(20) NOT NULL,
    received_by VARCHAR(100),
    ticket_id INTEGER UNIQUE REFERENCES ticket(id)
);
```

### Step 3 — Seed Initial Data

```sql
INSERT INTO officer (badge_number, full_name, email,
    phone_number, username, password, role, active)
VALUES
('ADMIN-001', 'System Administrator',
    'admin@traffic.rw', '0780000001',
    'admin', 'admin123', 'ADMIN', true),
('OFF-001', 'Jean Pierre Habimana',
    'jp@traffic.rw', '0780000002',
    'officer1', 'officer123', 'OFFICER', true),
('OFF-002', 'Marie Claire Uwera',
    'mc@traffic.rw', '0780000003',
    'officer2', 'officer123', 'OFFICER', true);

INSERT INTO violation_type (code, name, description,
    fine_amount, penalty_multiplier, severity_level)
VALUES
('VT001','Speeding','Driving above speed limit',50000,1.5,'HIGH'),
('VT002','Running Red Light','Passing red signal',80000,2.0,'CRITICAL'),
('VT003','Illegal Parking','Parking in restricted zone',20000,1.2,'LOW'),
('VT004','No Seatbelt','Not wearing seatbelt',15000,1.2,'MEDIUM'),
('VT005','Driving Without License','No valid license',100000,2.5,'CRITICAL'),
('VT006','Reckless Driving','Endangering road users',120000,3.0,'CRITICAL'),
('VT007','Drunk Driving','Under alcohol influence',150000,3.0,'CRITICAL'),
('VT008','Using Phone While Driving','Phone use while driving',30000,1.5,'HIGH'),
('VT009','Expired Registration','Expired vehicle registration',40000,1.3,'MEDIUM'),
('VT010','Wrong Way Driving','Against traffic lanes',90000,2.0,'CRITICAL');
```

### Step 4 — Configure hibernate.cfg.xml

Update the password in `hibernate.cfg.xml`:

```xml
<property name="hibernate.connection.password">YOUR_PASSWORD</property>
```

---

## ▶️ How to Run

> ⚠️ **IMPORTANT:** Always start the Server BEFORE the Client!

### 1. Start the Server
Starting Hibernate...
Hibernate ready!

### 2. Start the Client

### 3. Login
The login screen appears. Enter credentials and check email for OTP.

---

## 🔑 Login Credentials

| Role | Username | Password | OTP Delivery |
|------|----------|----------|--------------|
| **ADMIN** | `admin` | `admin123` | 📧 Email |
| **OFFICER** | `officer1` | `officer123` | 📧 Email |
| **OFFICER** | `officer2` | `officer123` | 📧 Email |

---

## ✅ Validation Rules

### Business Validation Rules (5)
| # | Rule | Location |
|---|------|----------|
| BVR-01 | Repeat offender penalty multiplier applied after 3 tickets | TicketForm |
| BVR-02 | Cannot delete a PAID ticket | TicketForm |
| BVR-03 | Cannot pay an ESCALATED ticket | PaymentForm |
| BVR-04 | Ticket due date must be a future date | TicketForm |
| BVR-05 | Fine amount must be a positive number | ViolationTypeForm |

### Technical Validation Rules (5)
| # | Rule | Location |
|---|------|----------|
| TVR-01 | Email must match regex format | OfficerForm |
| TVR-02 | Username minimum 4 characters | OfficerForm |
| TVR-03 | Password minimum 6 characters | OfficerForm |
| TVR-04 | Vehicle plate format validation | DriverForm |
| TVR-05 | National ID must be exactly 16 digits | DriverForm |

---

## 🏛️ Rwanda National Police Contacts

| | |
|-|-|
| 🏛️ **Headquarters** | KN 4 Ave, Kigali, Rwanda |
| 📞 **Traffic Hotline** | +250 788 311 155 |
| 🚨 **Emergency** | 113 / 112 |
| 📧 **General Email** | info@police.gov.rw |
| 📧 **Traffic Email** | traffic@police.gov.rw |
| 🌐 **Website** | www.police.gov.rw |

---

## 📸 Screenshots

> Add screenshots of your running application here after uploading!

---

## 📄 License

This project was developed as a final-term academic project for
**INSY 7312 — Java Programming** at
**Adventist University of Central Africa (AUCA)**.

---

<div align="center">

**Made with ❤️ by NTWARI EMERY **

*AUCA — Gishushu Campus | May 2026*

</div>

