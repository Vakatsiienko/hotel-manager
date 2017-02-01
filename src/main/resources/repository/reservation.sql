# reservation.findByRoomIdAndStatus
SELECT
  res.id               AS reservation_id,
  res.created_datetime AS reservation_created_datetime,
  u.id                 AS user_id,
  r.id                 AS room_id,
  res.guests           AS reservation_guests,
  res.status           AS reservation_status,
  res.arrival_date     AS reservation_arrival_date,
  res.departure_date   AS reservation_departure_date,
  res_rc.name          AS requested_room_class_name,
  rc.name              AS room_class_name
FROM reservation res INNER JOIN user u ON res.user_id = u.id
  INNER JOIN room_class res_rc ON res.requested_room_class_id = res_rc.id
  LEFT JOIN room r ON res.room_id = r.id
  LEFT JOIN room_class rc ON r.room_class_id = rc.id
WHERE res.room_id = :roomId AND res.status = :status;
# reservation.findByUserIdAndStatus
SELECT
  res.id                   AS reservation_id,
  res.created_datetime     AS reservation_created_datetime,
  u.id                     AS user_id,
  r.id                     AS room_id,
  res.guests               AS reservation_guests,
  res.status               AS reservation_status,
  res.arrival_date         AS reservation_arrival_date,
  res.departure_date       AS reservation_departure_date,
  res_rc.name              AS requested_room_class_name,
  rc.name                  AS room_class_name
FROM reservation res INNER JOIN user u ON res.user_id = u.id
  INNER JOIN room_class res_rc ON res.requested_room_class_id = res_rc.id
  LEFT JOIN room r ON res.room_id = r.id
  LEFT JOIN room_class rc ON r.room_class_id = rc.id
WHERE res.user_id = :userId AND res.status = :status;
# reservation.findPageByStatusFromDate
SELECT
  res.id               AS reservation_id,
  res.created_datetime AS reservation_created_datetime,
  u.id                 AS user_id,
  r.id                 AS room_id,
  res.guests           AS reservation_guests,
  res.status           AS reservation_status,
  res.arrival_date     AS reservation_arrival_date,
  res.departure_date   AS reservation_departure_date,
  res_rc.name          AS requested_room_class_name,
  rc.name              AS room_class_name
FROM reservation res INNER JOIN user u ON res.user_id = u.id
  INNER JOIN room_class res_rc ON res.requested_room_class_id = res_rc.id
  LEFT JOIN room r ON res.room_id = r.id
  LEFT JOIN room_class rc ON r.room_class_id = rc.id
WHERE res.status = :status AND res.departure_date >= :fromDate LIMIT :offset,:size;
# reservation.findPageByStatusFromDateCount
SELECT COUNT(1) FROM reservation res WHERE res.status = :status AND res.departure_date >= :fromDate;
# reservation.findActiveByUserId
SELECT
  res.id               AS reservation_id,
  res.created_datetime AS reservation_created_datetime,
  u.id                 AS user_id,
  r.id                 AS room_id,
  res.guests           AS reservation_guests,
  res.status           AS reservation_status,
  res.arrival_date     AS reservation_arrival_date,
  res.departure_date   AS reservation_departure_date,
  res_rc.name          AS requested_room_class_name,
  rc.name              AS room_class_name
FROM reservation res
  INNER JOIN user u ON u.id = res.user_id
  INNER JOIN room_class res_rc ON res.requested_room_class_id = res_rc.id
  LEFT JOIN room r ON res.room_id = r.id
  LEFT JOIN room_class rc ON r.room_class_id = rc.id
WHERE res.departure_date >= CURRENT_DATE AND u.id = :userId ORDER BY res.created_datetime DESC;
# reservation.create
INSERT INTO reservation (created_datetime, user_id, guests, requested_room_class_id, status, arrival_date, departure_date)
VALUES (:reservationCreatedDatetime, :reservationUserId, :reservationGuests, :reservationRequestedRoomClassId, :reservationStatus, :reservationArrivalDate,
        :reservationDepartureDate);
# reservation.getById
SELECT
  res.id                  AS reservation_id,
  res.created_datetime    AS reservation_created_datetime,
  res.guests              AS reservation_guests,
  res.status              AS reservation_status,
  res.arrival_date        AS reservation_arrival_date,
  res.departure_date      AS reservation_departure_date,
  u.id                    AS user_id,
  u.created_datetime      AS user_created_datetime,
  u.email                 AS user_email,
  u.password              AS user_password,
  u.name                  AS user_name,
  u.role                  AS user_role,
  u.phone_number          AS user_phone_number,
  u.vk_id            AS user_vk_id,
  r.id                    AS room_id,
  r.created_datetime      AS room_created_datetime,
  r.number                AS room_number,
  r.capacity              AS room_capacity,
  r.cost_per_day          AS room_cost_per_day,
  req_rc.id               AS requested_room_class_id,
  req_rc.created_datetime AS requested_room_class_created_datetime,
  req_rc.name             AS requested_room_class_name,
  rc.id                   AS room_class_id,
  rc.created_datetime     AS room_class_created_datetime,
  rc.name                 AS room_class_name
FROM reservation res INNER JOIN user u ON res.user_id = u.id
  INNER JOIN room_class req_rc ON res.requested_room_class_id = req_rc.id
  LEFT JOIN room r ON res.room_id = r.id
  LEFT JOIN room_class rc ON r.room_class_id = rc.id
WHERE res.id = :id;
# reservation.delete
DELETE FROM reservation
WHERE id = :id;
# reservation.update_withRoom
UPDATE reservation
SET user_id               = :reservationUserId, room_id = :reservationRoomId, guests = :reservationGuests,
  requested_room_class_id = :reservationRequestedRoomClassId,
  status                  = :reservationStatus, arrival_date = :reservationArrivalDate,
  departure_date          = :reservationDepartureDate
WHERE id = :id;
# reservation.update_withoutRoom
UPDATE reservation
SET user_id    = :reservationUserId, guests = :reservationGuests, requested_room_class_id = :reservationRequestedRoomClassId,
  status       = :reservationStatus,
  arrival_date = :reservationArrivalDate, departure_date = :reservationDepartureDate
WHERE id = :id;
# reservation.existOverlapReservation
SELECT EXISTS(SELECT 1
FROM reservation res
  INNER JOIN room r ON res.id = r.id
WHERE r.id = :roomId AND res.status = 'CONFIRMED'  AND res.departure_date > :arrivalDate AND res.arrival_date < :departureDate);