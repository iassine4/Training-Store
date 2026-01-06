-- ======================================================================
--   Base de données : Training Store
--   Script de création conforme au modèle (Category, Training, Cart, Order…)
-- ======================================================================

DROP DATABASE IF EXISTS training_store;

CREATE DATABASE IF NOT EXISTS training_store
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE training_store;

-- =====================================================
-- UTILISATEUR SQL RESTREINT (optionnel mais recommandé)
-- =====================================================
DROP USER IF EXISTS 'training_app'@'localhost';
CREATE USER 'training_app'@'localhost' IDENTIFIED BY 'training_app_pwd';
GRANT SELECT, INSERT, UPDATE, DELETE ON training_store.* TO 'training_app'@'localhost';
FLUSH PRIVILEGES;

-- =====================================================
-- TABLE : CATEGORY
-- Une catégorie peut contenir plusieurs formations
-- =====================================================
CREATE TABLE category (
    id_category INT AUTO_INCREMENT PRIMARY KEY,
    label VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- =====================================================
-- TABLE : TRAINING
-- Une formation appartient (optionnellement) à une catégorie
-- =====================================================
CREATE TABLE training (
    id_training INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT NOT NULL,
    duration_days INT NOT NULL,
    modality ENUM('ONSITE', 'REMOTE') NOT NULL,
    price DECIMAL(10,2) NOT NULL,

    -- Foreign key
    fk_id_category INT NULL,

    CONSTRAINT chk_training_duration CHECK (duration_days > 0),
    CONSTRAINT chk_training_price CHECK (price >= 0),

    CONSTRAINT fk_training_category
        FOREIGN KEY (fk_id_category) REFERENCES category(id_category)
        ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE INDEX idx_training_category ON training(fk_id_category);
CREATE INDEX idx_training_modality ON training(modality);
CREATE INDEX idx_training_name ON training(name);

-- =====================================================
-- TABLE : USER_ACCOUNT
-- Compte applicatif (login/password)
-- =====================================================
CREATE TABLE user_account (
    id_user INT AUTO_INCREMENT PRIMARY KEY,
    login VARCHAR(80) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
) ENGINE=InnoDB;

-- =====================================================
-- TABLE : CART
-- Un utilisateur a 0..1 panier courant (UNIQUE)
-- =====================================================
CREATE TABLE cart (
    id_cart INT AUTO_INCREMENT PRIMARY KEY,

    -- Foreign key
    fk_id_user INT NOT NULL UNIQUE,

    CONSTRAINT fk_cart_user
        FOREIGN KEY (fk_id_user) REFERENCES user_account(id_user)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =====================================================
-- TABLE D'ASSOCIATION : CART_LINE (N:N avec quantité)
-- Un panier contient plusieurs formations
-- =====================================================
CREATE TABLE cart_line (
    fk_id_cart INT NOT NULL,
    fk_id_training INT NOT NULL,
    quantity INT NOT NULL,

    PRIMARY KEY (fk_id_cart, fk_id_training),

    CONSTRAINT chk_cart_line_qty CHECK (quantity > 0),

    CONSTRAINT fk_cartline_cart
        FOREIGN KEY (fk_id_cart) REFERENCES cart(id_cart)
        ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT fk_cartline_training
        FOREIGN KEY (fk_id_training) REFERENCES training(id_training)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =====================================================
-- TABLE : CUSTOMER
-- Un client peut être associé à plusieurs commandes
-- =====================================================
CREATE TABLE customer (
    id_customer INT AUTO_INCREMENT PRIMARY KEY,
    last_name VARCHAR(100) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    address VARCHAR(255) NOT NULL,
    phone VARCHAR(30) NOT NULL
) ENGINE=InnoDB;

CREATE INDEX idx_customer_email ON customer(email);

-- =====================================================
-- TABLE : CUSTOMER_ORDER
-- Une commande appartient à un utilisateur et à un client
-- =====================================================
CREATE TABLE customer_order (
    id_order INT AUTO_INCREMENT PRIMARY KEY,
    order_date DATETIME NOT NULL,

    -- Foreign keys
    fk_id_user INT NOT NULL,
    fk_id_customer INT NOT NULL,

    CONSTRAINT fk_order_user
        FOREIGN KEY (fk_id_user) REFERENCES user_account(id_user)
        ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT fk_order_customer
        FOREIGN KEY (fk_id_customer) REFERENCES customer(id_customer)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE INDEX idx_order_user ON customer_order(fk_id_user);
CREATE INDEX idx_order_customer ON customer_order(fk_id_customer);

-- =====================================================
-- TABLE D'ASSOCIATION : ORDER_LINE (N:N avec quantité + prix)
-- Une commande contient plusieurs formations
-- =====================================================
CREATE TABLE order_line (
    fk_id_order INT NOT NULL,
    fk_id_training INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,

    PRIMARY KEY (fk_id_order, fk_id_training),

    CONSTRAINT chk_order_line_qty CHECK (quantity > 0),
    CONSTRAINT chk_order_line_price CHECK (unit_price >= 0),

    CONSTRAINT fk_orderline_order
        FOREIGN KEY (fk_id_order) REFERENCES customer_order(id_order)
        ON DELETE CASCADE ON UPDATE CASCADE,

    CONSTRAINT fk_orderline_training
        FOREIGN KEY (fk_id_training) REFERENCES training(id_training)
        ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =====================================================
-- DONNÉES MINIMALES (facultatif)
-- =====================================================
INSERT INTO category(label) VALUES ('Java'), ('Database'), ('DevOps');

INSERT INTO training(name, description, duration_days, modality, price, fk_id_category) VALUES
('Java Fundamentals', 'Core Java syntax and OOP basics', 5, 'ONSITE', 1200.00, (SELECT id_category FROM category WHERE label='Java')),
('SQL Bootcamp', 'Queries, joins, constraints, indexing', 3, 'REMOTE', 750.00, (SELECT id_category FROM category WHERE label='Database')),
('Docker Essentials', 'Containers, images, volumes, networks', 2, 'REMOTE', 500.00, (SELECT id_category FROM category WHERE label='DevOps'));
