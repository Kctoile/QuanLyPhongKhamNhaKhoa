CREATE TABLE IF NOT EXISTS roles (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE
);
INSERT INTO roles (role_name) VALUES
('ADMIN'), ('DOCTOR'), ('STAFF'), ('CUSTOMER')
ON CONFLICT (role_name) DO NOTHING;

CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    role_id INT NOT NULL,
    gender VARCHAR(10),
    dob DATE,
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles(role_id)
);

CREATE TABLE IF NOT EXISTS services (
    service_id SERIAL PRIMARY KEY,
    service_name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
    duration_minutes INT CHECK (duration_minutes IS NULL OR duration_minutes > 0)
);

CREATE TABLE IF NOT EXISTS appointments (
    appointment_id SERIAL PRIMARY KEY,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'Pending'
        CHECK (status IN ('Pending','CONFIRMED','Checked In','Checked Out','Completed','Cancelled')),
    notes TEXT,
    room VARCHAR(50),
    FOREIGN KEY (patient_id) REFERENCES users(user_id),
    FOREIGN KEY (doctor_id) REFERENCES users(user_id)
);
CREATE UNIQUE INDEX IF NOT EXISTS UX_appointments_doctor_slot_active
    ON appointments (doctor_id, appointment_date, appointment_time)
    WHERE status <> 'Cancelled';
CREATE INDEX IF NOT EXISTS IX_appointments_patient_date
    ON appointments (patient_id, appointment_date DESC, appointment_time DESC);

CREATE TABLE IF NOT EXISTS appointment_services (
    appointment_id INT,
    service_id INT,
    PRIMARY KEY (appointment_id, service_id),
    FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id),
    FOREIGN KEY (service_id) REFERENCES services(service_id)
);

CREATE TABLE IF NOT EXISTS examination_results (
    result_id SERIAL PRIMARY KEY,
    appointment_id INT UNIQUE,
    result_details TEXT,
    prescription TEXT,
    doctor_notes TEXT,
    examination_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id)
);

CREATE TABLE IF NOT EXISTS prescribed_services (
    result_id INT,
    service_id INT,
    status VARCHAR(50) DEFAULT 'Pending',
    notes TEXT,
    PRIMARY KEY (result_id, service_id),
    FOREIGN KEY (result_id) REFERENCES examination_results(result_id),
    FOREIGN KEY (service_id) REFERENCES services(service_id)
);

CREATE TABLE IF NOT EXISTS prescriptions (
    prescription_id SERIAL PRIMARY KEY,
    result_id INT UNIQUE,
    instructions TEXT,
    FOREIGN KEY (result_id) REFERENCES examination_results(result_id)
);

CREATE TABLE IF NOT EXISTS medicines (
    medicine_id SERIAL PRIMARY KEY,
    medicine_name VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
    stock_quantity INT NOT NULL CHECK (stock_quantity >= 0)
);

CREATE TABLE IF NOT EXISTS prescription_details (
    prescription_id INT,
    medicine_id INT,
    prescribed_quantity INT NOT NULL CHECK (prescribed_quantity > 0),
    unit_price DECIMAL(10,2) NOT NULL CHECK (unit_price >= 0),
    PRIMARY KEY (prescription_id, medicine_id),
    FOREIGN KEY (prescription_id) REFERENCES prescriptions(prescription_id),
    FOREIGN KEY (medicine_id) REFERENCES medicines(medicine_id)
);

CREATE TABLE IF NOT EXISTS clinic_configs (
    config_id SERIAL PRIMARY KEY,
    opening_time TIME,
    closing_time TIME,
    clinic_info TEXT
);

