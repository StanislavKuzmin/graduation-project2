DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS menu_item;
DROP TABLE IF EXISTS vote;
DROP TABLE IF EXISTS dish;
DROP TABLE IF EXISTS restaurant;
DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq START WITH 1;

CREATE TABLE users
(
    id         INTEGER DEFAULT nextval('global_seq') PRIMARY KEY,
    name       VARCHAR                           NOT NULL,
    email      VARCHAR                           NOT NULL,
    password   VARCHAR                           NOT NULL,
    registered TIMESTAMP           DEFAULT CURRENT_TIMESTAMP NOT NULL,
    enabled    BOOL                DEFAULT TRUE  NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE user_role
(
    user_id INTEGER NOT NULL,
    role    VARCHAR NOT NULL,
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE restaurant
(
    id      INTEGER DEFAULT nextval('global_seq') PRIMARY KEY,
    name    VARCHAR NOT NULL,
    address VARCHAR NOT NULL,
    CONSTRAINT restaurant_unique_name_address_idx UNIQUE (name, address)
);

CREATE TABLE vote
(
    id      INTEGER DEFAULT nextval('global_seq') PRIMARY KEY,
    user_id       INTEGER            NOT NULL,
    restaurant_id INTEGER            NOT NULL,
    vote_date     DATE DEFAULT CURRENT_DATE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE
);
CREATE INDEX vote_restaurant_idx ON vote (restaurant_id);

CREATE TABLE dish
(
    id            INTEGER DEFAULT nextval('global_seq') PRIMARY KEY,
    name          VARCHAR        NOT NULL,
    price         INTEGER        NOT NULL,
    restaurant_id INTEGER        NOT NULL,
    is_excluded_from_menu BOOL DEFAULT FALSE  NOT NULL,
    CONSTRAINT dish_unique_idx UNIQUE (restaurant_id, name, price, is_excluded_from_menu),
    FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE
);

CREATE TABLE menu_item
(
    id      INTEGER DEFAULT nextval('global_seq') PRIMARY KEY,
    dish_id INTEGER            NOT NULL,
    date    DATE DEFAULT CURRENT_DATE NOT NULL,
    FOREIGN KEY (dish_id) REFERENCES dish (id) ON DELETE CASCADE
);

DELETE FROM user_role;
DELETE FROM vote;
DELETE FROM menu_item;
DELETE FROM users;
DELETE FROM dish;
DELETE FROM restaurant;
ALTER SEQUENCE global_seq RESTART WITH 1;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('Guest', 'guest@gmail.com', '{noop}guest'),
       ('Stanislav', 'stasonhd2@mail.ru', '{noop}stanislav');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2),
       ('USER', 4);


INSERT INTO restaurant (name, address)
VALUES ('kwakin', 'zvenigorodskaya street, 2/44'),
       ('market_place', 'nevskii avenue, 22'),
       ('dzamiko', 'admiralteiskaya street, 2');

INSERT INTO dish (name, price, restaurant_id)
VALUES ('roast', 29900, 5),
       ('solyanka', 45005, 5),
       ('beet soup', 56002, 5),
       ('dressed herring', 32300, 6),
       ('goulash', 61002, 6),
       ('open sandwich', 21005, 6),
       ('pizza', 46400, 7),
       ('kebab', 73100, 7),
       ('hot dog', 33200, 7);

INSERT INTO vote (user_id, restaurant_id, vote_date)
VALUES (1, 5, '2024-01-29'),
       (2, 6, '2024-01-29'),
       (4, 5, '2024-01-29'),
       (1, 6, '2024-01-30'),
       (2, 6, '2024-01-30'),
       (4, 7, '2024-01-30'),
       (1, 5, CURRENT_DATE),
       (4, 7, CURRENT_DATE);


INSERT INTO menu_item (dish_id, date)
VALUES (8, '2024-01-29'),
       (9, '2024-01-29'),
       (10, '2024-01-29'),
       (11, '2024-01-29'),
       (12, '2024-01-29'),
       (13, '2024-01-29'),
       (14, '2024-01-29'),
       (9, '2024-01-30'),
       (10, '2024-01-30'),
       (12, '2024-01-30'),
       (13, '2024-01-30'),
       (15, '2024-01-30'),
       (16, '2024-01-30'),
       (8, CURRENT_DATE),
       (11, CURRENT_DATE),
       (13, CURRENT_DATE),
       (14, CURRENT_DATE),
       (15, CURRENT_DATE),
       (16, CURRENT_DATE);