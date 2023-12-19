DROP DATABASE IF EXISTS clevertec_user;
CREATE DATABASE clevertec_user;

CREATE TABLE users(
    id SERIAL PRIMARY KEY,
    firstname VARCHAR(31) NOT NULL,
    lastname VARCHAR(31) NOT NULL,
    date_of_birth DATE NOT NULL,
    email VARCHAR(31) NOT NULL
);
