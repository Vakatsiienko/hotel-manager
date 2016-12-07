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
WHERE room_class = :roomClass AND NOT EXISTS (SELECT *
                                              FROM reservation
                                              WHERE room_id = r.id AND arrival_date < :departureDate AND
                                                    departure_date > :arrivalDate);