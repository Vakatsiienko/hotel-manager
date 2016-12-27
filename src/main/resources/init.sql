USE hotel_manager;
ALTER TABLE bill DROP FOREIGN KEY fk_bill_reservation;
ALTER TABLE reservation DROP FOREIGN KEY fk_reservation_room;
ALTER TABLE reservation DROP FOREIGN KEY fk_reservation_user;

DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS bill;
DROP TABLE IF EXISTS room;
DROP TABLE IF EXISTS reservation;

DROP DATABASE hotel_manager;
CREATE DATABASE hotel_manager DEFAULT CHARACTER SET utf8
  DEFAULT COLLATE utf8_general_ci;
USE hotel_manager;

CREATE TABLE user
(
  id               BIGINT(20) PRIMARY KEY AUTO_INCREMENT    NOT NULL,
  created_datetime TIMESTAMP                                NOT NULL,
  email            VARCHAR(32) UNIQUE                       NOT NULL,
  password         VARCHAR(64)                              NOT NULL,
  name             VARCHAR(20)                              NOT NULL,
  role             VARCHAR(20)                              NOT NULL,
  phone_number     VARCHAR(20)                              NOT NULL
);
CREATE TABLE bill
(
  id               BIGINT(20) PRIMARY KEY AUTO_INCREMENT NOT NULL,
  created_datetime TIMESTAMP                             NOT NULL,
  reservation_id   BIGINT(20)                            NOT NULL,
  total_cost       INTEGER                               NOT NULL,
  paid             BIT DEFAULT FALSE                     NOT NULL
);
CREATE TABLE room
(
  id               BIGINT(20) PRIMARY KEY AUTO_INCREMENT NOT NULL,
  created_datetime TIMESTAMP                             NOT NULL,
  number           INTEGER                               NOT NULL,
  capacity         INTEGER                               NOT NULL,
  cost_per_day     INTEGER                               NOT NULL,
  room_class       VARCHAR(20)                           NOT NULL
);
CREATE TABLE reservation
(
  id                   BIGINT(20) PRIMARY KEY AUTO_INCREMENT NOT NULL,
  created_datetime     TIMESTAMP                             NOT NULL,
  user_id              BIGINT(20)                            NOT NULL,
  room_id              BIGINT(20),
  guests               INTEGER                               NOT NULL,
  requested_room_class VARCHAR(20)                           NOT NULL,
  status               VARCHAR(20)                           NOT NULL,
  arrival_date         DATE                                  NOT NULL,
  departure_date       DATE                                  NOT NULL
);
ALTER TABLE bill ADD CONSTRAINT fk_bill_reservation FOREIGN KEY (reservation_id) REFERENCES reservation (id);
ALTER TABLE reservation ADD CONSTRAINT fk_reservation_room FOREIGN KEY (room_id) REFERENCES room (id);
ALTER TABLE reservation ADD CONSTRAINT fk_reservation_user FOREIGN KEY (user_id) REFERENCES user (id);

