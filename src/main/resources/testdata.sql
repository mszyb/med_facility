insert into roles (role_name) value ('ROLE_ADMIN'), ('ROLE_USER'), ('ROLE_PHYSICIAN');
insert into user (email, first_name, last_name, password, phone_number, role_id, created) VALUE ('marcin@ge.com', 'Marcin', 'Szybki', '$2a$10$5BOlqiOpoUn330wSQXL4CexYd0Y53k5cyzcjm60OQUR0e1KO0QcN.', '123456789', 1, now());
insert into user (email, first_name, last_name, password, phone_number, role_id, created) VALUE ('kasia@ge.com', 'Kasia', 'Marcza', '$2a$10$W.pLkYfU1Cf36OmUmxT4defY3nSx1d8TytVy2RYhgfDP9K6NntzPC', '123456789', 2, now());
insert into user (email, first_name, last_name, password, phone_number, role_id, created) VALUE ('maria@ge.com', 'Maria', 'Domininska', '$2a$10$2N7cly0z1jF81VG/syyuQuTtGVb18AUMxfOKqTPkAQtdUlH9894i6', '123456789', 3, now());
insert into user (email, first_name, last_name, password, phone_number, role_id, created) VALUE ('basia@ge.com', 'Basia', 'Kowalska', '$2a$10$W.pLkYfU1Cf36OmUmxT4defY3nSx1d8TytVy2RYhgfDP9K6NntzPC', '123456789', 2, now());
insert into user (email, first_name, last_name, password, phone_number, role_id, created) VALUE ('leszek@ge.com', 'Leszek', 'Leszkowski', '$2a$10$2N7cly0z1jF81VG/syyuQuTtGVb18AUMxfOKqTPkAQtdUlH9894i6', '123456789', 3, now());
insert into user (email, first_name, last_name, password, phone_number, role_id, created) VALUE ('artur@ge.com', 'Artur', 'Wojewodzki', '$2a$10$W.pLkYfU1Cf36OmUmxT4defY3nSx1d8TytVy2RYhgfDP9K6NntzPC', '123456789', 2, now());
insert into user (email, first_name, last_name, password, phone_number, role_id, created) VALUE ('piotr@ge.com', 'Piotr', 'Cebulski', '$2a$10$2N7cly0z1jF81VG/syyuQuTtGVb18AUMxfOKqTPkAQtdUlH9894i6', '123456789', 3, now());
insert into user (email, first_name, last_name, password, phone_number, role_id, created) VALUE ('albert@ge.com', 'Albert', 'Kaczkowski', '$2a$10$W.pLkYfU1Cf36OmUmxT4defY3nSx1d8TytVy2RYhgfDP9K6NntzPC', '123456789', 2, now());
insert into user (email, first_name, last_name, password, phone_number, role_id, created) VALUE ('andrze@ge.com', 'Andrzej', 'Gudel', '$2a$10$2N7cly0z1jF81VG/syyuQuTtGVb18AUMxfOKqTPkAQtdUlH9894i6', '123456789', 3, now());
insert into specialization (name) values ('laryngologia'), ('onkologia'), ('kardiologia'), ('interna'), ('pediatria');
insert into service_type (name) values ('USG'),('skierowanie'),('kontrola'),('pierwsza wizyta'),('kontynuacja leczenia'),('audiometr'),('kolonoskopia');
insert into user_specialization_service(service_id, specialization_id, user_id) value (3,1,3),(4,1,3),(5,1,3),(6,1,3),(2,4,3),(4,4,3),(5,4,3);
insert into physician_schedule (start_time, end_time, physician_id) values (TIMESTAMP('2022-08-15T09:00'), TIMESTAMP('2022-08-15T17:00'), 3),(TIMESTAMP('2022-08-16T09:00'), TIMESTAMP('2022-08-16T17:00'), 3),(TIMESTAMP('2022-08-17T15:00'), TIMESTAMP('2022-08-17T20:00'), 3),(TIMESTAMP('2022-08-18T09:00'), TIMESTAMP('2022-08-18T15:00'), 3);