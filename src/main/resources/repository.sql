# room.create
INSERT INTO room (created_datetime, number, capacity, cost_per_day, room_class, description)
VALUES (:createdDatetime, :number, :capacity, :costPerDay, :roomClass, :description);
# room.getById
SELECT *
FROM room
WHERE id = :id;
# room.delete
DELETE FROM room
WHERE id = :id;
# room.update
UPDATE room
SET number    = :number, capacity = :capacity, cost_per_day = :costPerDay, room_class = :roomClass,
  description = :description
WHERE id = :id;
# room.findAvailableForReservation
SELECT *
FROM room r
WHERE room_class = :roomClass AND NOT EXISTS(SELECT *
                                             FROM reservation
                                             WHERE room_id = r.id AND arrival_date < :departureDate AND
                                                   departure_date > :arrivalDate);
# bill.create
INSERT INTO bill (created_datetime, reservation_id, total_cost, paid)
VALUES (:createdDatetime, :reservationId, :totalCost, :paid);
# bill.getById
SELECT *
FROM bill b INNER JOIN reservation res ON b.reservation_id = res.id
  LEFT JOIN room r ON res.room_id = r.id
  INNER JOIN user u ON res.user_id = u.id
WHERE b.id = :id;
# bill.delete
DELETE FROM bill
WHERE id = :id;
# bill.update
UPDATE bill
SET reservation_id = :reservationId, total_cost = :totalCost, paid = :paid
WHERE id = :id;
# bill.getByReservationId
SELECT *
FROM bill b INNER JOIN reservation res ON b.reservation_id = res.id
  LEFT JOIN room r ON res.room_id = r.id
  INNER JOIN user u ON res.user_id = u.id
WHERE b.reservation_id = :reservationId;
# user.create
INSERT INTO user (created_datetime, email, password, name, role, phone_number)
VALUES (:createdDatetime, :email, :password, :name, :role, :phoneNumber);
# user.getById
SELECT *
FROM user
WHERE id = :id;
# user.delete
DELETE FROM user
WHERE id = :id;
# user.update
UPDATE user u
SET email = :email, name = :name, password = :password, role = :role, phone_number = :phoneNumber
WHERE u.id = :id;
# user.getByEmail
SELECT *
FROM user
WHERE email = :email;
# reservation.findByRoomIdAndStatus_count
SELECT COUNT(*)
FROM reservation
WHERE room_id = :roomId AND status = :status;
# reservation.findByRoomByIdAndStatus
SELECT *
FROM reservation res INNER JOIN user u ON res.user_id = u.id
  INNER JOIN room r ON res.room_id = r.id
WHERE res.room_id = :roomId AND res.status = :status;
# reservation.findByStatus_count
SELECT COUNT(*)
FROM reservation
WHERE status = :status;
# reservation.findByStatus
SELECT *
FROM reservation res INNER JOIN user u ON res.user_id = u.id
  LEFT JOIN room r ON res.room_id = r.id
WHERE res.status = :status;
# reservation.create
INSERT INTO reservation (created_datetime, user_id, guests, requested_room_class, status, arrival_date, departure_date)
VALUES (:createdDatetime, :userId, :guests, :requestedRoomClass, :status, :arrivalDate, :departureDate);
# reservation.getById
SELECT *
FROM reservation res INNER JOIN user u ON res.user_id = u.id
  LEFT JOIN room r ON res.room_id = r.id
WHERE res.id = :id;
# reservation.delete
DELETE FROM reservation
WHERE id = :id;
# reservation.update_withRoom
UPDATE reservation
SET user_id = :userId, room_id = :roomId, guests = :guests, requested_room_class = :requestedRoomClass,
  status    = :status, arrival_date = :arrivalDate, departure_date = :departureDate
WHERE id = :id;
# reservation.update_withoutRoom
UPDATE reservation
SET user_id    = :userId, guests = :guests, requested_room_class = :requestedRoomClass, status = :status,
  arrival_date = :arrivalDate, departure_date = :departureDate
WHERE id = :id;

