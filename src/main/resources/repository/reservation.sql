# reservation.findByRoomIdAndStatus_count
SELECT COUNT(*)
FROM reservation
WHERE room_id = :roomId AND status = :status;
# reservation.findByRoomIdAndStatus
SELECT *
FROM reservation res INNER JOIN user u ON res.user_id = u.id
  INNER JOIN room r ON res.room_id = r.id
WHERE res.room_id = :roomId AND res.status = :status;
# reservation.findByUserIdAndStatus_count
SELECT COUNT(*)
FROM reservation
WHERE user_id = :userId AND status = :status;
# reservation.findByUserIdAndStatus
SELECT *
FROM reservation res INNER JOIN user u ON res.user_id = u.id
  LEFT JOIN room r ON res.room_id = r.id
WHERE res.user_id = :userId AND res.status = :status;
# reservation.findByStatus_count
SELECT COUNT(*)
FROM reservation
WHERE status = :status;
# reservation.findByStatus
SELECT *
FROM reservation res INNER JOIN user u ON res.user_id = u.id
  LEFT JOIN room r ON res.room_id = r.id
WHERE res.status = :status;
# reservation.findActiveByUserId_count
SELECT COUNT(*)
FROM reservation res
  INNER JOIN user u ON u.id = :userId
  LEFT JOIN room r ON res.room_id = r.id
WHERE res.status != :status AND res.departure_date >= CURRENT_DATE;
# reservation.findActiveByUserId
SELECT *
FROM reservation res
  INNER JOIN user u ON u.id = :userId
  LEFT JOIN room r ON res.room_id = r.id
WHERE res.status != :status AND res.departure_date >= CURRENT_DATE ORDER BY res.id;
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