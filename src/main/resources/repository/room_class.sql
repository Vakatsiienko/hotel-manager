# roomClass.create
INSERT INTO room_class (created_datetime, name) VALUES (:roomClassCreatedDatetime, :roomClassName);
# roomClass.update
UPDATE room_class rc
SET rc.name = :roomClassName WHERE rc.id = :id;
# roomClass.delete
DELETE FROM room_class
WHERE id = :id;
# roomClass.getById
SELECT
  rc.id               AS room_class_id,
  rc.created_datetime AS room_class_created_datetime,
  rc.name             AS room_class_name
FROM room_class rc
WHERE rc.id = :id;
# roomClass.getByName
SELECT
  rc.id               AS room_class_id,
  rc.created_datetime AS room_class_created_datetime,
  rc.name             AS room_class_name
FROM room_class rc
WHERE rc.name = :name;
# roomClass.findAll
SELECT
  rc.id               AS room_class_id,
  rc.created_datetime AS room_class_created_datetime,
  rc.name             AS room_class_name
FROM room_class rc;