CREATE DATABASE workoutcoach;

CREATE TABLE clients (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(100)
);

INSERT INTO clients (id, name, email) VALUES (1, 'Steve Stevinsky', 'superSteve@gmail.com');
INSERT INTO clients (id, name, email) VALUES (2, 'Kate Rabbit', 'katyy@gmail.com');

CREATE TABLE users (
  email VARCHAR(100) PRIMARY KEY NOT NULL,
  password VARCHAR(100) NOT NULL,
  name VARCHAR(50) NOT NULL,
  surname VARCHAR(50) NOT NULL,
  securityQuestion VARCHAR(255) NOT NULL,
  securityAnswer VARCHAR(30) NOT NULL,
  resetToken CHAR(36)
);

-- Sample user's password '$2a$10$9d5AC2CrUGaWSgwRHbtZV.TbKiuixWQh3EzJhZ7tHt0AeifE2AxCq' is a hashed version of password 'password'
INSERT INTO users(email, password, name, surname, securityQuestion, securityAnswer,resetToken ) VALUES ('sdoe@gmail.com', '$2a$10$9d5AC2CrUGaWSgwRHbtZV.TbKiuixWQh3EzJhZ7tHt0AeifE2AxCq', 'Steve', 'Doe', 'Whats ur name', 'Steve',null);

create table authorities (
  email VARCHAR(100) NOT NULL,
  authority VARCHAR(50) NOT NULL ,
  CONSTRAINT fk_authorities_users FOREIGN KEY(email) REFERENCES users(email)
);

INSERT INTO authorities(email, authority) VALUES ('sdoe@gmail.com', 'ROLE_USER');