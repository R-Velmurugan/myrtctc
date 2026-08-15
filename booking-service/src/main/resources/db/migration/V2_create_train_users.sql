-- DROP TABLE users;+
CREATE TABLE train_users
(
    email VARCHAR PRIMARY KEY REFERENCES users(email),
    seat_preference  VARCHAR(1),
    berth_preference VARCHAR(1),
    saved_passengers JSONB
)