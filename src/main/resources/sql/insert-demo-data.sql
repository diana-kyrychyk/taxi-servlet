INSERT INTO roles(id, name)
VALUES (1, 'USER'),
       (2, 'ADMIN');

INSERT INTO users(id, name, phone, password)
VALUES (1, 'ADMIN', '+380630000000', '73acd9a5972130b75066c82595a1fae3');

INSERT INTO users_roles (user_id, role_id)
VALUES (1, 2);

INSERT INTO drivers(id, name)
VALUES (1, 'Petro'),
       (2, 'Maryna'),
       (3, 'Viacheslav');

INSERT INTO cars(id, brand, model, license_plate, capacity, category, status, driver_id)
VALUES (1, 'Hyundai', 'Accent', 'AA0001AA', 4, 'PREMIUM', 'FREE', 3),
       (2, 'Renault', 'Logan', 'AA0050AA', 3, 'STANDARD', 'FREE', 2),
       (3, 'Citroen', 'C-Elysee', 'AA0700AA', 4, 'STANDARD', 'FREE', 1);


INSERT INTO addresses(id, street, building, latitude, longtitude)
VALUES (1, 'Vasylkivska', '4', '50.793', '30.890'),
       (2, 'Orlova', '75', '50.234', '30.911'),
       (3, 'Demiivska', '5', '50.275', '30.242'),
       (4, 'Khreschatyk', '18', '50.087', '30.012');
