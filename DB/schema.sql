CREATE TABLE Train COLUMN (
    train_number INT PRIMARY KEY,
    total_seats INT
)
CREATE TABLE Station COLUMN (
    station_code VARCHAR(4),
    station_name VARCHAR(100)
)
CREATE TABLE TrainStation COLUMN (
    train_number REFERENCES Train(train_number),
    station_code REFERENCES Station(station_code),
    CONSTRAINT train_station_id PRIMARY KEY(train_number, station_code),
    sequence_number INT,
    arrival_day_offset INT,
    arrival_time TIME,
    departure_time TIME
)
CREATE TABLE Seat COLUMN(
    seat_number INT,
    train_number REFERENCES Train(train_number),
    CONSTRAINT seat_id PRIMARY KEY(seat_number, train_number),
    seat_type VARCHAR(1),
    coach_number VARCHAR(2) -- TODO: should be converted to Coach's FK
),
CREATE TABLE Journey COLUMN(
    journey_id VARCHAR(10) PRIMARY KEY,
    train_number REFERENCES Train(train_number),
    journey_date DATE
),
CREATE TABLE User COLUMN(
    user_id VARCHAR(20) PRIMARY KEY,
    email CITEXT,
    password VARCHAR,
    age INT,
    seat_preference VARCHAR(1),
    berth_preference VARCHAR(1),
    saved_passengers JSONB
),
CREATE TABLE booking COLUMN(
    pnr VARCHAR(20) PRIMARY KEY,
    journey_id REFERENCES Journey(journey_id),
    booked_by REFERENCES User(user_id),
    source_station REFERENCES Station(station_code)
)