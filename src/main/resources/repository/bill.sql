# bill.create
INSERT INTO bill (created_datetime, reservation_id, total_cost, paid)
VALUES (:createdDatetime, :reservationId, :totalCost, :paid);
# bill.getById
SELECT
  b.id                     AS bill_id,
  b.created_datetime       AS bill_created_datetime,
  b.paid                   AS bill_paid,
  b.total_cost             AS bill_total_cost,
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
SELECT
  b.id                     AS bill_id,
  b.created_datetime       AS bill_created_datetime,
  b.paid                   AS bill_paid,
  b.total_cost             AS bill_total_cost,
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
FROM bill b INNER JOIN reservation res ON b.reservation_id = res.id
  LEFT JOIN room r ON res.room_id = r.id
  INNER JOIN user u ON res.user_id = u.id
WHERE b.reservation_id = :reservationId;