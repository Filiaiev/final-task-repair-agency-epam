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
(1, 'repairer'),
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
    last_name VARCHAR(26) NOT NULL,
    first_name VARCHAR(26) NOT NULL,
    middle_name VARCHAR(26) NOT NULL,
    birthdate DATE NOT NULL,
    user_id INT NOT NULL UNIQUE,
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE clients(
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    person_id INT NOT NULL UNIQUE,
    cash DECIMAL(10, 2) DEFAULT 0,
    preferred_locale CHAR(2) NOT NULL DEFAULT 'uk',
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
    status_id INT NOT NULL DEFAULT 0, 
    FOREIGN KEY(client_id) REFERENCES clients(id) ON DELETE CASCADE,
    FOREIGN KEY(worker_id) REFERENCES employees(id),
    FOREIGN KEY(status_id) REFERENCES statuses(id)
);

CREATE TABLE locales(
	id INT AUTO_INCREMENT NOT NULL,
    locale CHAR(2) NOT NULL,
    PRIMARY KEY(id)
);

INSERT INTO locales(locale) VALUES('en'), ('uk');

CREATE TABLE statuses_localized(
	status_id INT NOT NULL,
    locale_id INT NOT NULL,
    status_name CHAR(20) NOT NULL,
    PRIMARY KEY(status_id, locale_id),
    FOREIGN KEY(status_id) REFERENCES statuses(id) ON DELETE CASCADE,
    FOREIGN KEY(locale_id) REFERENCES locales(id) ON DELETE CASCADE
);

INSERT INTO statuses_localized(status_id, locale_id, status_name) VALUES
(0, 1, 'Created'), 
(1, 1, 'Waiting for payment'),
(2, 1, 'Paid'),
(3, 1, 'Canceled'),
(4, 1, 'In work'),
(5, 1, 'Completed'),
(0, 2, 'Створено'),
(1, 2, 'Очікування оплати'),
(2, 2, 'Оплачено'),
(3, 2, 'Відмінено'),
(4, 2, 'В роботі'),
(5, 2, 'Виконано');

-- Adding employees 
INSERT INTO users(email, login, pass, role_id)
VALUES
('manager@ukraine.com', 'manager1', 'manager1', 0),
('repairer1@ukraine.com', 'luzkovo', 'luzkov123', 1),
('repairer2@ukraine.com', 'kozak', 'kozak234', 1);

use repair_agency;
SELECT * FROM users;
SELECT * FROM employees;
SELECT * FROM persons;

INSERT INTO persons(last_name, first_name, middle_name, birthdate, user_id)
VALUES
('Дрин', 'Валентин', 'Миколайович', '1982-05-12', 1),
('Лужков', 'Олександр', 'Петрович', '1976-10-24', 2),
('Козак', 'Петро', 'Олексійович', '1987-03-16', 3);

INSERT INTO employees(person_id)
VALUES
(1), (2), (3);