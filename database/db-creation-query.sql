DROP DATABASE IF EXISTS repair_agency;
CREATE SCHEMA repair_agency;

USE repair_agency;

CREATE TABLE statuses(
	id INT NOT NULL PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE
);

INSERT INTO statuses VALUES
(0, 'created'),
(1, 'waiting for payment'),
(2, 'paid'),
(3, 'canceled'),
(4, 'in work'),
(5, 'completed');

CREATE TABLE roles(
	id INT NOT NULL PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE
);

INSERT INTO roles VALUES
(0, 'manager'),
(1, 'mechanic'),
(2, 'client');

CREATE TABLE users(
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(40) NOT NULL UNIQUE,
    login VARCHAR(30) NOT NULL UNIQUE,
    pass CHAR(64) NOT NULL,
    role_id INT NOT NULL,
    FOREIGN KEY(role_id) REFERENCES roles(id)
);

CREATE TABLE persons(
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    last_name VARCHAR(16) NOT NULL,
    first_name VARCHAR(16) NOT NULL,
    middle_name VARCHAR(26) NOT NULL,
    birthdate DATE NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE clients(
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    person_id INT NOT NULL,
    cash DECIMAL(10, 2) DEFAULT 0,
    FOREIGN KEY(person_id) REFERENCES persons(id) ON DELETE CASCADE
);

CREATE TABLE employees(
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    person_id INT NOT NULL,
    FOREIGN KEY(person_id) REFERENCES persons(id) ON DELETE CASCADE
);

CREATE TABLE order_headers(
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT ,
	client_id INT NOT NULL,
    worker_id INT NULL,
    order_date DATETIME DEFAULT NOW(),
    complete_date DATETIME NULL,
    cost DECIMAL(10, 2) NULL,
    comment VARCHAR(50) NULL,
    description VARCHAR(200) NOT NULL,
    status_id INT NOT NULL DEFAULT 5, 
    FOREIGN KEY(client_id) REFERENCES clients(id),
    FOREIGN KEY(worker_id) REFERENCES employees(id),
    FOREIGN KEY(status_id) REFERENCES statuses(id)
);
