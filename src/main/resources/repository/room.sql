# room.create
INSERT INTO room (created_datetime, number, capacity, cost_per_day, room_class_id)
VALUES (:roomCreatedDatetime, :roomNumber, :roomCapacity, :roomCostPerDay, :roomRoomClassId);
# room.getById
SELECT
  r.id                AS room_id,
  r.created_datetime  AS room_created_datetime,
  r.number            AS room_number,
  r.capacity          AS room_capacity,
  r.cost_per_day      AS room_cost_per_day,
  rc.id               AS room_class_id,
  rc.created_datetime AS room_class_created_datetime,
  rc.name             AS room_class_name
FROM room r
  INNER JOIN room_class rc ON r.room_class_id = rc.id
WHERE r.id = :id;
# room.delete
DELETE FROM room
WHERE id = :id;
# room.update
UPDATE room
SET number      = :roomNumber, capacity = :roomCapacity, cost_per_day = :roomCostPerDay,
  room_class_id = :roomRoomClassId
WHERE id = :id;
# room.findAvailableForReservation
SELECT
  r.id                AS room_id,
  r.created_datetime  AS room_created_datetime,
  r.number            AS room_number,
  r.capacity          AS room_capacity,
  r.cost_per_day      AS room_cost_per_day,
  rc.id               AS room_class_id,
  rc.created_datetime AS room_class_created_datetime,
  rc.name             AS room_class_name
FROM room r INNER JOIN room_class rc ON r.room_class_id = rc.id
WHERE rc.name = :roomClassName AND NOT EXISTS(SELECT 1
                                              FROM reservation
                                              WHERE room_id = r.id AND arrival_date < :departureDate AND
                                                    departure_date > :arrivalDate);
# room.findAll
SELECT
  r.id                AS room_id,
  r.created_datetime  AS room_created_datetime,
  r.number            AS room_number,
  r.capacity          AS room_capacity,
  r.cost_per_day      AS room_cost_per_day,
  rc.id               AS room_class_id,
  rc.created_datetime AS room_class_created_datetime,
  rc.name             AS room_class_name
FROM room r INNER JOIN room_class rc ON r.room_class_id = rc.id;
# room.existsRoomByRoomClass
SELECT exists(SELECT 1
              FROM room r
              WHERE r.room_class_id = :roomClassId);
# room.getByNumber
SELECT
  r.id                AS room_id,
  r.created_datetime  AS room_created_datetime,
  r.number            AS room_number,
  r.capacity          AS room_capacity,
  r.cost_per_day      AS room_cost_per_day,
  rc.id               AS room_class_id,
  rc.created_datetime AS room_class_created_datetime,
  rc.name             AS room_class_name
FROM room r INNER JOIN room_class rc ON r.room_class_id = rc.id
WHERE r.number = :number;
# room.findPage
SELECT
  r.id                AS room_id,
  r.created_datetime  AS room_created_datetime,
  r.number            AS room_number,
  r.capacity          AS room_capacity,
  r.cost_per_day      AS room_cost_per_day,
  rc.id               AS room_class_id,
  rc.created_datetime AS room_class_created_datetime,
  rc.name             AS room_class_name
FROM room r INNER JOIN room_class rc ON r.room_class_id = rc.id
ORDER BY r.id ASC LIMIT :offset,:size;
# room.totalCount
SELECT COUNT(1) FROM room;