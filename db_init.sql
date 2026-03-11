CREATE DATABASE [Dental];
GO

USE [Dental];
GO

-- 1. Table: roles
CREATE TABLE roles (
    role_id INT PRIMARY KEY IDENTITY(1,1),
    role_name NVARCHAR(50) NOT NULL
);

-- 2. Table: users
CREATE TABLE users (
    user_id INT PRIMARY KEY IDENTITY(1,1),
    full_name NVARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL, -- email usually doesn't need N, but just to be safe or leave as VARCHAR
    password NVARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    role_id INT,
    gender NVARCHAR(10),
    dob DATE,
    address NVARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    display_order INT,
    FOREIGN KEY (role_id) REFERENCES roles(role_id)
);

-- Fix email MVARCHAR typo, email is VARCHAR
-- 3. Table: services
CREATE TABLE services (
    service_id INT PRIMARY KEY IDENTITY(1,1),
    service_name NVARCHAR(100) NOT NULL,
    description NVARCHAR(MAX),
    price DECIMAL(10, 2) NOT NULL,
    duration_minutes INT 
);

-- 4. Table: appointments
CREATE TABLE appointments (
    appointment_id INT PRIMARY KEY IDENTITY(1,1),
    patient_id INT, 
    doctor_id INT,  
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    status NVARCHAR(50) DEFAULT 'Pending', 
    notes NVARCHAR(MAX),
    room NVARCHAR(50), 
    FOREIGN KEY (patient_id) REFERENCES users(user_id),
    FOREIGN KEY (doctor_id) REFERENCES users(user_id)
);

-- 5. Table: appointment_services
CREATE TABLE appointment_services (
    appointment_id INT,
    service_id INT,
    PRIMARY KEY (appointment_id, service_id),
    FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id),
    FOREIGN KEY (service_id) REFERENCES services(service_id)
);

-- 6. Table: examination_results
CREATE TABLE examination_results (
    result_id INT PRIMARY KEY IDENTITY(1,1),
    appointment_id INT UNIQUE,
    result_details NVARCHAR(MAX), 
    examination_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id)
);

-- 7. Table: prescribed_services
CREATE TABLE prescribed_services (
    result_id INT,
    service_id INT,
    status NVARCHAR(50) DEFAULT 'Pending', 
    notes NVARCHAR(MAX),
    PRIMARY KEY (result_id, service_id),
    FOREIGN KEY (result_id) REFERENCES examination_results(result_id),
    FOREIGN KEY (service_id) REFERENCES services(service_id)
);

-- 8. Table: prescriptions
CREATE TABLE prescriptions (
    prescription_id INT PRIMARY KEY IDENTITY(1,1),
    result_id INT UNIQUE,
    instructions NVARCHAR(MAX), 
    FOREIGN KEY (result_id) REFERENCES examination_results(result_id)
);

-- 9. Table: medicines
CREATE TABLE medicines (
    medicine_id INT PRIMARY KEY IDENTITY(1,1),
    medicine_name NVARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    stock_quantity INT NOT NULL 
);

-- 10. Table: prescription_details
CREATE TABLE prescription_details (
    prescription_id INT,
    medicine_id INT,
    prescribed_quantity INT NOT NULL, 
    purchased_quantity INT DEFAULT 0, 
    unit_price DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (prescription_id, medicine_id),
    FOREIGN KEY (prescription_id) REFERENCES prescriptions(prescription_id),
    FOREIGN KEY (medicine_id) REFERENCES medicines(medicine_id)
);

-- 11. Table: clinic_configs
CREATE TABLE clinic_configs (
    config_id INT PRIMARY KEY IDENTITY(1,1),
    opening_time TIME,
    closing_time TIME,
    clinic_info NVARCHAR(MAX)
);
GO
