INSERT INTO users(email, login, pass, role_id) VALUES('furna@gmail.com', 'furna', '123321', 2);
INSERT INTO users(email, login, pass, role_id) VALUES('manager@gmail.com', 'manager', '5554444', 0);
INSERT INTO users(email, login, pass, role_id) VALUES('repairer@gmail.com', 'repairer', '777888', 1);

INSERT INTO persons(last_name, first_name, middle_name, birthdate, user_id)
VALUES('Filiaiev', 'Vladyslav', 'Dmytrovych', '2002-02-20', 1);
INSERT INTO persons(last_name, first_name, middle_name, birthdate, user_id)
VALUES('Петренко', 'Петро', 'Петрович', '1990-03-17', 2);
INSERT INTO persons(last_name, first_name, middle_name, birthdate, user_id)
VALUES('Максименко', 'Максим', 'Максимович', '1988-11-05', 3);

INSERT INTO clients(person_id, preferred_locale) VALUES(1, 'uk');
INSERT INTO employees(person_id) VALUES(2), (3);

INSERT INTO order_headers(client_id, worker_id, order_date, complete_date, cost, comment, description, status_id)
VALUES(1, 2, '2021-06-05 17:37:00', '2021-06-08 17:37:00', 500, 'Thanks!', 'Test order', 5);
