CREATE TABLE roles
(
    id   SERIAL NOT NULL,
    name VARCHAR(100),
    PRIMARY KEY (id)
);


CREATE TABLE users
(
    id       SERIAL NOT NULL,
    name     VARCHAR(100),
    password VARCHAR(255),
    phone    VARCHAR(15),
    discount int8,
    balance int8,
    PRIMARY KEY (id)
);


CREATE TABLE users_roles
(
    user_id INT,
    role_id INT,
    FOREIGN KEY (user_id) references users (id) on delete cascade,
    FOREIGN KEY (role_id) references roles (id) on delete cascade
);

CREATE TABLE drivers
(
    id   SERIAL NOT NULL,
    name VARCHAR(100),
    PRIMARY KEY (id)
);

CREATE TABLE cars
(
    id            SERIAL NOT NULL,
    brand         VARCHAR(100),
    model         VARCHAR(100),
    license_plate VARCHAR(100),
    capacity      INT,
    category      VARCHAR(20),
    status        VARCHAR(20),
    driver_id     INT,
    PRIMARY KEY (id),
    FOREIGN KEY (driver_id) references drivers (id)
);


CREATE TABLE addresses
(
    id         SERIAL NOT NULL,
    street     VARCHAR(255),
    building   VARCHAR(255),
    latitude   double precision,
    longtitude double precision,
    PRIMARY KEY (id)
);

CREATE TABLE orders
(
    id                   SERIAL NOT NULL,
    departure_address_id INT,
    arrival_address_id   INT,
    passengers_count     INT,
    category             VARCHAR(100),
    car_id               INT,
    fare                 int8,
    discount             int8,
    final_fare           int8,
    status               VARCHAR(20),
    driver_id            INT,
    creation_date        TIMESTAMP,
    user_id              INT,
    waiting_seconds      INT8,
    PRIMARY KEY (id),
    FOREIGN KEY (driver_id) references drivers (id),
    FOREIGN KEY (departure_address_id) references addresses (id),
    FOREIGN KEY (arrival_address_id) references addresses (id),
    FOREIGN KEY (car_id) references cars (id),
    FOREIGN KEY (user_id) references users (id)

);
