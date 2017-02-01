INSERT INTO hotel_manager.user (id, created_datetime, email, password, name, role, phone_number) VALUES (11, '2016-12-06 17:27:58', 'customer@gmail.com', '$2a$10$a91uBuvBPTTjJxAa7p6t5e//k73ZGbNiyXNh7xOQpQmj0fTKMFj4O', 'Customer', 'CUSTOMER', '+3987456123');
INSERT INTO hotel_manager.user (id, created_datetime, email, password, name, role, phone_number) VALUES (12, '2016-12-06 17:28:13', 'admin@gmail.com', '$2a$10$sYGQu1L0eHfBJoZKVY6bkuZ9dkbvnwrS6kbw5HwHMoS5TYhhayeji', 'Admin', 'MANAGER', '+3123456789');

INSERT INTO hotel_manager.room_class (id, created_datetime, name) VALUES (1, '2016-12-20 14:17:55', 'Standard');
INSERT INTO hotel_manager.room_class (id, created_datetime, name) VALUES (2, '2016-12-20 14:17:55', 'Half Suite');
INSERT INTO hotel_manager.room_class (id, created_datetime, name) VALUES (3, '2016-12-20 14:17:55', 'Suite');
INSERT INTO hotel_manager.room_class (id, created_datetime, name) VALUES (4, '2016-12-20 14:17:55', 'King');

INSERT INTO hotel_manager.room (id, created_datetime, number, capacity, cost_per_day, room_class_id) VALUES (1, '2016-12-06 17:43:23', 1, 2, 10000, 1);
INSERT INTO hotel_manager.room (id, created_datetime, number, capacity, cost_per_day, room_class_id) VALUES (2, '2016-12-06 17:43:23', 2, 4, 25000, 1);
INSERT INTO hotel_manager.room (id, created_datetime, number, capacity, cost_per_day, room_class_id) VALUES (3, '2016-12-06 17:43:23', 3, 4, 25000, 1);
INSERT INTO hotel_manager.room (id, created_datetime, number, capacity, cost_per_day, room_class_id) VALUES (4, '2016-12-06 17:43:23', 11, 2, 30000, 2);
INSERT INTO hotel_manager.room (id, created_datetime, number, capacity, cost_per_day, room_class_id) VALUES (5, '2016-12-06 17:43:23', 12, 4, 50000, 2);
INSERT INTO hotel_manager.room (id, created_datetime, number, capacity, cost_per_day, room_class_id) VALUES (6, '2016-12-06 17:43:23', 13, 4, 30000, 2);
INSERT INTO hotel_manager.room (id, created_datetime, number, capacity, cost_per_day, room_class_id) VALUES (7, '2016-12-06 17:43:23', 21, 2, 60000, 3);
INSERT INTO hotel_manager.room (id, created_datetime, number, capacity, cost_per_day, room_class_id) VALUES (8, '2016-12-06 17:43:23', 22, 4, 80000, 3);
INSERT INTO hotel_manager.room (id, created_datetime, number, capacity, cost_per_day, room_class_id) VALUES (9, '2016-12-06 17:43:23', 23, 6, 90000, 3);
INSERT INTO hotel_manager.room (id, created_datetime, number, capacity, cost_per_day, room_class_id) VALUES (10, '2016-12-06 17:43:23', 31, 4, 100000, 4);
INSERT INTO hotel_manager.room (id, created_datetime, number, capacity, cost_per_day, room_class_id) VALUES (11, '2016-12-06 17:43:23', 32, 10, 150000, 4);
INSERT INTO hotel_manager.room (id, created_datetime, number, capacity, cost_per_day, room_class_id) VALUES (12, '2016-12-06 17:43:23', 33, 20, 200000, 4);

INSERT INTO hotel_manager.reservation (id, created_datetime, user_id, room_id, guests, requested_room_class_id, status, arrival_date, departure_date) VALUES (1, '2016-12-20 14:15:21', 11, null, 3, 1, 'REQUESTED', '2016-12-27', '2017-01-18');
INSERT INTO hotel_manager.reservation (id, created_datetime, user_id, room_id, guests, requested_room_class_id, status, arrival_date, departure_date) VALUES (2, '2016-12-20 14:15:33', 11, null, 2, 2, 'REQUESTED', '2016-12-22', '2017-01-04');
INSERT INTO hotel_manager.reservation (id, created_datetime, user_id, room_id, guests, requested_room_class_id, status, arrival_date, departure_date) VALUES (3, '2016-12-20 14:15:51', 11, null, 4, 3, 'REQUESTED', '2016-12-29', '2016-12-31');
INSERT INTO hotel_manager.reservation (id, created_datetime, user_id, room_id, guests, requested_room_class_id, status, arrival_date, departure_date) VALUES (4, '2016-12-20 14:17:55', 11, 11, 2, 4, 'CONFIRMED', '2017-02-08', '2017-02-10');

INSERT INTO hotel_manager.bill (id, created_datetime, reservation_id, total_cost, paid)
VALUES (1, '2016-12-20 14:17:55', 4, 300000, FALSE);

