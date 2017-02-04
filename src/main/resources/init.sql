USE hotel_manager;
ALTER TABLE bill DROP FOREIGN KEY fk_bill_reservation;
ALTER TABLE reservation DROP FOREIGN KEY fk_reservation_room;
ALTER TABLE reservation DROP FOREIGN KEY fk_reservation_user;
ALTER TABLE reservation DROP FOREIGN KEY fk_reservation_room_class;
ALTER TABLE room DROP FOREIGN KEY fk_room_room_class;
DROP INDEX ind_bill_reservation_id ON bill;
DROP INDEX ind_reservation_user_id_departure_date_created_datetime ON reservation;
DROP INDEX ind_user_email ON user;
DROP INDEX ind_user_vk_id ON user;

DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS bill;
DROP TABLE IF EXISTS room;
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS room_class;

DROP DATABASE hotel_manager;
CREATE DATABASE hotel_manager DEFAULT CHARACTER SET utf8
  DEFAULT COLLATE utf8_general_ci;
USE hotel_manager;

CREATE TABLE user
(
  id               BIGINT(20) PRIMARY KEY AUTO_INCREMENT    NOT NULL,
  created_datetime TIMESTAMP                                NOT NULL,
  email            VARCHAR(50) UNIQUE                       NOT NULL,
  password         VARCHAR(64)                              NOT NULL,
  name             VARCHAR(40)                              NOT NULL,
  role             VARCHAR(20)                              NOT NULL,
  phone_number     VARCHAR(20)                              NOT NULL,
  vk_id       INTEGER(20) DEFAULT NULL UNIQUE
);
CREATE TABLE bill
(
  id               BIGINT(20) PRIMARY KEY AUTO_INCREMENT  NOT NULL,
  created_datetime TIMESTAMP                              NOT NULL,
  reservation_id   BIGINT(20) UNIQUE                      NOT NULL,
  total_cost       INTEGER                                NOT NULL,
  paid             BIT DEFAULT FALSE                     NOT NULL
);
CREATE TABLE room
(
  id               BIGINT(20) PRIMARY KEY AUTO_INCREMENT   NOT NULL,
  created_datetime TIMESTAMP                               NOT NULL,
  number           INTEGER UNIQUE                          NOT NULL,
  capacity         INTEGER                                 NOT NULL,
  cost_per_day     INTEGER                                 NOT NULL,
  room_class_id    BIGINT(20)                              NOT NULL
);
CREATE TABLE reservation
(
  id                      BIGINT(20) PRIMARY KEY AUTO_INCREMENT NOT NULL,
  created_datetime        TIMESTAMP                             NOT NULL,
  user_id                 BIGINT(20)                            NOT NULL,
  room_id                 BIGINT(20),
  guests                  INTEGER                               NOT NULL,
  requested_room_class_id BIGINT(20)                            NOT NULL,
  status                  VARCHAR(20)                           NOT NULL,
  arrival_date            DATE                                  NOT NULL,
  departure_date          DATE                                  NOT NULL
);
CREATE TABLE room_class
(
  id               BIGINT(20) PRIMARY KEY AUTO_INCREMENT  NOT NULL,
  created_datetime TIMESTAMP                              NOT NULL,
  name             VARCHAR(20) UNIQUE                     NOT NULL
);
ALTER TABLE bill ADD CONSTRAINT fk_bill_reservation FOREIGN KEY (reservation_id) REFERENCES reservation (id);
ALTER TABLE reservation ADD CONSTRAINT fk_reservation_room FOREIGN KEY (room_id) REFERENCES room (id);
ALTER TABLE reservation ADD CONSTRAINT fk_reservation_user FOREIGN KEY (user_id) REFERENCES user (id);
ALTER TABLE reservation ADD CONSTRAINT fk_reservation_room_class FOREIGN KEY (requested_room_class_id) REFERENCES room_class (id);
ALTER TABLE room ADD CONSTRAINT fk_room_room_class FOREIGN KEY (room_class_id) REFERENCES room_class (id);
CREATE UNIQUE INDEX ind_user_email ON user(email);
CREATE UNIQUE INDEX ind_user_vk_id ON user(vk_id);
CREATE UNIQUE INDEX ind_bill_reservation_id ON bill(reservation_id);
CREATE INDEX ind_reservation_user_id_departure_date_created_datetime ON reservation (user_id, departure_date, created_datetime);

