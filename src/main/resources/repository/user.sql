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