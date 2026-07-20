CREATE TABLE IF NOT EXISTS Train (
    train_number INT PRIMARY KEY,
    total_seats INT
);
CREATE TABLE IF NOT EXISTS Station (
    station_code VARCHAR(4) PRIMARY KEY,
    station_name VARCHAR(100)
);
CREATE TABLE IF NOT EXISTS TrainStation (
    train_number INT REFERENCES Train(train_number),
    station_code VARCHAR(4) REFERENCES Station(station_code),
    CONSTRAINT train_station_id PRIMARY KEY(train_number, station_code),
    sequence_number INT,
    arrival_day_offset INT,
    arrival_time TIME,
    departure_time TIME
);
CREATE TABLE IF NOT EXISTS Seat (
    seat_number INT,
    train_number INT REFERENCES Train(train_number),
    CONSTRAINT seat_id PRIMARY KEY(seat_number, train_number),
    seat_type VARCHAR(1),
    coach_number VARCHAR(2) -- TODO: should be converted to Coach's FK
);
CREATE TABLE IF NOT EXISTS Journey (
    journey_id VARCHAR(10) PRIMARY KEY,
    train_number INT REFERENCES Train(train_number),
    journey_date DATE
);
CREATE TABLE IF NOT EXISTS Users (
    user_id VARCHAR(20) PRIMARY KEY,
    email VARCHAR,
    password VARCHAR,
    age INT,
    seat_preference VARCHAR(1),
    berth_preference VARCHAR(1),
    saved_passengers JSONB
);
CREATE TABLE IF NOT EXISTS booking (
    pnr VARCHAR(20) PRIMARY KEY,
    journey_id VARCHAR(10) REFERENCES Journey(journey_id),
    booked_by VARCHAR(20) REFERENCES Users(user_id),
    source_station VARCHAR(4) REFERENCES Station(station_code)
);