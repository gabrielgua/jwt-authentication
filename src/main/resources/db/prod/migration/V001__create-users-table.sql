CREATE TABLE users(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NUll,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);