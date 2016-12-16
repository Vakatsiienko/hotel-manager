# user.create
INSERT INTO user (created_datetime, email, password, name, role, phone_number)
VALUES (:createdDatetime, :email, :password, :name, :role, :phoneNumber);
# user.getById
SELECT
  u.id                     AS user_id,
  u.created_datetime       AS user_created_datetime,
  u.email                  AS user_email,
  u.password               AS user_password,
  u.name                   AS user_name,
  u.role                   AS user_role,
  u.phone_number           AS user_phone_number
FROM user u
WHERE id = :id;
# user.delete
DELETE FROM user
WHERE id = :id;
# user.update
UPDATE user u
SET email = :email, name = :name, password = :password, role = :role, phone_number = :phoneNumber
WHERE u.id = :id;
# user.getByEmail
SELECT
  u.id                     AS user_id,
  u.created_datetime       AS user_created_datetime,
  u.email                  AS user_email,
  u.password               AS user_password,
  u.name                   AS user_name,
  u.role                   AS user_role,
  u.phone_number           AS user_phone_number
FROM user u
WHERE email = :email;