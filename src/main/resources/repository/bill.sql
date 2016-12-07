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