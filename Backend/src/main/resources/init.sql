CREATE DATABASE IF NOT EXISTS oetn_portal
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE oetn_portal;

CREATE TABLE IF NOT EXISTS users (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name   VARCHAR(100) NOT NULL,
    last_name    VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20),
    username     VARCHAR(200) NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,
    role         ENUM('USER','ADMIN') NOT NULL DEFAULT 'USER',
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS support_tickets (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    status      ENUM('OPEN','IN_PROGRESS','CLOSED') NOT NULL DEFAULT 'OPEN',
    user_id     BIGINT NOT NULL,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS software_requests (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    software_name VARCHAR(255) NOT NULL,
    reason        TEXT,
    status        ENUM('PENDING','APPROVED','REJECTED') NOT NULL DEFAULT 'PENDING',
    user_id       BIGINT NOT NULL,
    created_at    DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS hardware_devices (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(200) NOT NULL,
    laptop_model  VARCHAR(255),
    serial_number VARCHAR(255) NOT NULL UNIQUE,
    user_id       BIGINT NOT NULL,
    submitted_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS guides (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    file_name   VARCHAR(255) NOT NULL,
    file_path   VARCHAR(500) NOT NULL,
    file_size   BIGINT,
    uploaded_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Compte Admin : username=admin.oetn  password=password
INSERT IGNORE INTO users (first_name, last_name, phone_number, username, password, role)
VALUES (
    'Admin', 'OETN', NULL,
    'admin.oetn',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.',
    'ADMIN'
);