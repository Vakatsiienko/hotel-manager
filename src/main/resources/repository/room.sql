# room.create
INSERT INTO room (created_datetime, number, capacity, cost_per_day, room_class)
VALUES (:createdDatetime, :number, :capacity, :costPerDay, :roomClass);
# room.getById
SELECT
  r.id                     AS room_id,
  r.created_datetime       AS room_created_datetime,
  r.number                 AS room_number,
  r.capacity               AS room_capacity,
  r.cost_per_day          AS room_cost_per_day,
  r.room_class             AS room_room_class
FROM room r
WHERE id = :id;
# room.delete
DELETE FROM room
WHERE id = :id;
# room.update
UPDATE room
SET number    = :number, capacity = :capacity, cost_per_day = :costPerDay, room_class = :roomClass
WHERE id = :id;
# room.findAvailableForReservation
SELECT
  r.id                     AS room_id,
  r.created_datetime       AS room_created_datetime,
  r.number                 AS room_number,
  r.capacity               AS room_capacity,
  r.cost_per_day          AS room_cost_per_day,
  r.room_class             AS room_room_class
FROM room r
WHERE room_class = :roomClass AND NOT EXISTS (SELECT 1
                                              FROM reservation
                                              WHERE room_id = r.id AND arrival_date < :departureDate AND
                                                    departure_date > :arrivalDate);
# room.findAvailableForReservation_count
SELECT COUNT(*)
FROM room r
WHERE room_class = :roomClass AND NOT EXISTS (SELECT 1
                                              FROM reservation
                                              WHERE room_id = r.id AND arrival_date < :departureDate AND
                                                    departure_date > :arrivalDate);
# room.findAll
SELECT
  r.id                     AS room_id,
  r.created_datetime       AS room_created_datetime,
  r.number                 AS room_number,
  r.capacity               AS room_capacity,
  r.cost_per_day          AS room_cost_per_day,
  r.room_class             AS room_room_class
FROM room r;
# room.findAll_count
SELECT COUNT(*) FROM room;