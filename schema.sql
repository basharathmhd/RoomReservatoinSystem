-- ============================================================
-- Ocean View Resort - Room Reservation System
-- Database Schema for MySQL 8.0
-- Database: oceanview_resort
-- ============================================================

CREATE DATABASE IF NOT EXISTS oceanview_resort
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE oceanview_resort;


-- ============================================================
-- 1. room_types
-- ============================================================
CREATE TABLE room_types (
    type_id       VARCHAR(50)    PRIMARY KEY,
    type_name     VARCHAR(100)   NOT NULL,
    base_rate     DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    description   TEXT,
    max_occupancy INT            NOT NULL DEFAULT 1,
    amenities     TEXT
) ENGINE=InnoDB;


-- ============================================================
-- 2. rooms
-- ============================================================
CREATE TABLE rooms (
    room_id     VARCHAR(50)   PRIMARY KEY,
    room_number VARCHAR(20)   NOT NULL UNIQUE,
    type_id     VARCHAR(50),
    floor       INT           NOT NULL DEFAULT 0,
    capacity    INT           NOT NULL DEFAULT 1,
    status      VARCHAR(20)   NOT NULL DEFAULT 'AVAILABLE',
    amenities   TEXT,

    CONSTRAINT fk_rooms_type
        FOREIGN KEY (type_id) REFERENCES room_types(type_id)
        ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB;


-- ============================================================
-- 3. guests
-- ============================================================
CREATE TABLE guests (
    guest_id              VARCHAR(50)   PRIMARY KEY,
    first_name            VARCHAR(100)  NOT NULL,
    last_name             VARCHAR(100)  NOT NULL,
    address               TEXT,
    contact_number        VARCHAR(20),
    email                 VARCHAR(150),
    identification_number VARCHAR(50),
    date_of_birth         DATE,
    nationality           VARCHAR(50),
    created_date          DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by            VARCHAR(50),
    modified_date         DATETIME,
    modified_by           VARCHAR(50)
) ENGINE=InnoDB;


-- ============================================================
-- 4. users  (system / staff accounts)
-- ============================================================
CREATE TABLE users (
    user_id       VARCHAR(50)   PRIMARY KEY,
    username      VARCHAR(50)   NOT NULL UNIQUE,
    password      VARCHAR(255)  NOT NULL,
    full_name     VARCHAR(150)  NOT NULL,
    role          VARCHAR(20)   NOT NULL DEFAULT 'STAFF',
    is_active     BOOLEAN       NOT NULL DEFAULT TRUE,
    last_login    DATETIME,
    created_date  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by    VARCHAR(50),
    modified_date DATETIME,
    modified_by   VARCHAR(50)
) ENGINE=InnoDB;


-- ============================================================
-- 5. reservations
-- ============================================================
CREATE TABLE reservations (
    reservation_number VARCHAR(50)   PRIMARY KEY,
    guest_id           VARCHAR(50)   NOT NULL,
    room_id            VARCHAR(50)   NOT NULL,
    check_in_date      DATE          NOT NULL,
    check_out_date     DATE          NOT NULL,
    number_of_guests   INT           NOT NULL DEFAULT 1,
    status             VARCHAR(20)   NOT NULL DEFAULT 'CONFIRMED',
    special_requests   TEXT,
    created_date       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by         VARCHAR(50),
    modified_date      DATETIME,
    modified_by        VARCHAR(50),

    CONSTRAINT fk_reservations_guest
        FOREIGN KEY (guest_id) REFERENCES guests(guest_id)
        ON UPDATE CASCADE ON DELETE RESTRICT,

    CONSTRAINT fk_reservations_room
        FOREIGN KEY (room_id) REFERENCES rooms(room_id)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;


-- ============================================================
-- 6. bills
-- ============================================================
CREATE TABLE bills (
    bill_id            VARCHAR(50)    PRIMARY KEY,
    reservation_number VARCHAR(50)    NOT NULL,
    issue_date         DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    room_charges       DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    service_charges    DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    tax_amount         DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    discount_amount    DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    total_amount       DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    final_amount       DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    payment_status     VARCHAR(20)    NOT NULL DEFAULT 'PENDING',
    payment_method     VARCHAR(30),
    notes              TEXT,

    CONSTRAINT fk_bills_reservation
        FOREIGN KEY (reservation_number) REFERENCES reservations(reservation_number)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;


-- ============================================================
-- 7. payments
-- ============================================================
CREATE TABLE payments (
    payment_id     VARCHAR(50)    PRIMARY KEY,
    bill_id        VARCHAR(50)    NOT NULL,
    payment_date   DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    amount         DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    payment_method VARCHAR(30),
    transaction_id VARCHAR(100),
    status         VARCHAR(20)    NOT NULL DEFAULT 'PENDING',
    remarks        TEXT,

    CONSTRAINT fk_payments_bill
        FOREIGN KEY (bill_id) REFERENCES bills(bill_id)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;


-- ============================================================
-- 8. system_logs  (audit trail)
-- ============================================================
CREATE TABLE system_logs (
    log_id      VARCHAR(50)   PRIMARY KEY,
    timestamp   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id     VARCHAR(50),
    action      VARCHAR(50)   NOT NULL,
    description TEXT,
    ip_address  VARCHAR(45),
    entity_type VARCHAR(50),
    entity_id   VARCHAR(50),

    CONSTRAINT fk_logs_user
        FOREIGN KEY (user_id) REFERENCES users(user_id)
        ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB;
