# reservation.findByRoomIdAndStatus_count
SELECT COUNT(*)
FROM reservation
WHERE room_id = :roomId AND status = :status;
# reservation.findByRoomIdAndStatus
SELECT
  res.id                   AS reservation_id,
  res.created_datetime     AS reservation_created_datetime,
  u.id                     AS user_id,
  r.id                     AS room_id,
  res.guests               AS reservation_guests,
  res.status               AS reservation_status,
  res.requested_room_class AS reservation_requested_room_class,
  res.arrival_date         AS reservation_arrival_date,
  res.departure_date       AS reservation_departure_date
FROM reservation res INNER JOIN user u ON res.user_id = u.id
  INNER JOIN room r ON res.room_id = r.id
WHERE res.room_id = :roomId AND res.status = :status;
# reservation.findByUserIdAndStatus_count
SELECT COUNT(*)
FROM reservation
WHERE user_id = :userId AND status = :status;
# reservation.findByUserIdAndStatus
SELECT
  res.id                   AS reservation_id,
  res.created_datetime     AS reservation_created_datetime,
  u.id                     AS user_id,
  r.id                     AS room_id,
  res.guests               AS reservation_guests,
  res.status               AS reservation_status,
  res.requested_room_class AS reservation_requested_room_class,
  res.arrival_date         AS reservation_arrival_date,
  res.departure_date       AS reservation_departure_date
FROM reservation res INNER JOIN user u ON res.user_id = u.id
  LEFT JOIN room r ON res.room_id = r.id
WHERE res.user_id = :userId AND res.status = :status;
# reservation.findByStatusFromDate_count
SELECT COUNT(*)
FROM reservation res
WHERE status = :status AND res.departure_date >= :fromDate;
# reservation.findByStatusFromDate
SELECT
  res.id                   AS reservation_id,
  res.created_datetime     AS reservation_created_datetime,
  u.id                     AS user_id,
  r.id                     AS room_id,
  res.guests               AS reservation_guests,
  res.status               AS reservation_status,
  res.requested_room_class AS reservation_requested_room_class,
  res.arrival_date         AS reservation_arrival_date,
  res.departure_date       AS reservation_departure_date
FROM reservation res INNER JOIN user u ON res.user_id = u.id
  LEFT JOIN room r ON res.room_id = r.id
WHERE res.status = :status AND res.departure_date >= :fromDate;
# reservation.findActiveByUserId_count
SELECT COUNT(*)
FROM reservation res
  INNER JOIN user u ON u.id = :userId
  LEFT JOIN room r ON res.room_id = r.id
WHERE res.departure_date >= CURRENT_DATE;
# reservation.findActiveByUserId
SELECT
  res.id                   AS reservation_id,
  res.created_datetime     AS reservation_created_datetime,
  u.id                     AS user_id,
  r.id                     AS room_id,
  res.guests               AS reservation_guests,
  res.status               AS reservation_status,
  res.requested_room_class AS reservation_requested_room_class,
  res.arrival_date         AS reservation_arrival_date,
  res.departure_date       AS reservation_departure_date
FROM reservation res
  INNER JOIN user u ON u.id = res.user_id
  LEFT JOIN room r ON res.room_id = r.id
WHERE res.departure_date >= CURRENT_DATE AND u.id = :userId ORDER BY res.created_datetime DESC;
# reservation.create
INSERT INTO reservation (created_datetime, user_id, guests, requested_room_class, status, arrival_date, departure_date)
VALUES (:createdDatetime, :userId, :guests, :requestedRoomClass, :status, :arrivalDate, :departureDate);
# reservation.getById
SELECT
  res.id                   AS reservation_id,
  res.created_datetime     AS reservation_created_datetime,
  res.guests               AS reservation_guests,
  res.requested_room_class AS reservation_requested_room_class,
  res.status               AS reservation_status,
  res.arrival_date         AS reservation_arrival_date,
  res.departure_date       AS reservation_departure_date,
  u.id                     AS user_id,
  u.created_datetime       AS user_created_datetime,
  u.email                  AS user_email,
  u.password               AS user_password,
  u.name                   AS user_name,
  u.role                   AS user_role,
  u.phone_number           AS user_phone_number,
  r.id                     AS room_id,
  r.created_datetime       AS room_created_datetime,
  r.number                 AS room_number,
  r.capacity               AS room_capacity,
  r.cost_per_day          AS room_cost_per_day,
  r.room_class             AS room_room_class
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
# reservation.existOverlapReservation
SELECT COUNT(*)
FROM reservation res
  INNER JOIN room r ON res.id = r.id
WHERE r.id = :roomId AND res.status = 'CONFIRMED'  AND res.departure_date > :arrivalDate AND res.arrival_date < :departureDate;