DROP DATABASE workoutcoach;

CREATE DATABASE workoutcoach;
USE workoutcoach;

CREATE TABLE users (
  email    VARCHAR(100) PRIMARY KEY NOT NULL,
  password VARCHAR(100)             NOT NULL,
  name     VARCHAR(50)              NOT NULL,
  surname  VARCHAR(50)              NOT NULL
);

-- Sample user's password '$2a$10$9d5AC2CrUGaWSgwRHbtZV.TbKiuixWQh3EzJhZ7tHt0AeifE2AxCq' is a hashed version of password 'password'
INSERT INTO users (email, password, name, surname) VALUES ('sdoe@gmail.com', '$2a$10$9d5AC2CrUGaWSgwRHbtZV.TbKiuixWQh3EzJhZ7tHt0AeifE2AxCq', 'Steve', 'Doe');
INSERT INTO users (email, password, name, surname) VALUES ('test@gmail.com', '$2a$10$9d5AC2CrUGa', 'Steve', 'Doe');
INSERT INTO users (email, password, name, surname) VALUES ('wiktoria.malawska@wp.pl', '$2a$10$9d5AC2CrUGaWSgwRHbtZV.TbKiuixWQh3EzJhZ7tHt0AeifE2AxCq', 'Wiktoria', 'Malawska');
INSERT INTO users (email, password, name, surname) VALUES ('tomaszewski-eryk@wp.pl', '$2a$10$9d5AC2CrUGaWSgwRHbtZV.TbKiuixWQh3EzJhZ7tHt0AeifE2AxCq', 'Eryk', 'Tomaszewski');
INSERT INTO users (email, password, name, surname) VALUES ('wiktoria.malawska@test.pl', '$2a$10$9d5AC2CrUGaWSgwRHbtZV.TbKiuixWQh3EzJhZ7tHt0AeifE2AxCq', 'Wiktoria', 'Malawska');

CREATE TABLE clients (
  id              INT PRIMARY KEY AUTO_INCREMENT,
  name            VARCHAR(100) NOT NULL,
  surname         VARCHAR(100) NOT NULL,
  coachEmail      VARCHAR(100) NOT NULL,
  gymName         VARCHAR(100),
  goal            VARCHAR(1000),
  healthCondition VARCHAR(1000),
  isActive        BIT(1),
  phoneNumber     VARCHAR(15),
  FOREIGN KEY (coachEmail) REFERENCES users (email)
);

INSERT INTO clients (name, surname, coachEmail, isActive) VALUES ('Steve', 'Stevinsky', 'sdoe@gmail.com', true);
INSERT INTO clients (name, surname, coachEmail, isActive) VALUES ('Kate', 'Rabbit', 'sdoe@gmail.com', true);
INSERT INTO clients (name, surname, coachEmail, isActive) VALUES ('Paul', 'Old', 'sdoe@gmail.com', false);
INSERT INTO clients (name, surname, coachEmail, isActive) VALUES ('William', 'McDonald', 'wiktoria.malawska@wp.pl', true);
INSERT INTO clients (name, surname, coachEmail, isActive) VALUES ('Mary', 'Unused', 'wiktoria.malawska@wp.pl', false);

create table authorities (
  email     VARCHAR(100) NOT NULL,
  authority VARCHAR(50)  NOT NULL,
  CONSTRAINT fk_authorities_users FOREIGN KEY (email) REFERENCES users (email)
);

INSERT INTO authorities (email, authority) VALUES ('sdoe@gmail.com', 'ROLE_USER');
INSERT INTO authorities (email, authority) VALUES ('wiktoria.malawska@wp.pl', 'ROLE_USER');
INSERT INTO authorities (email, authority) VALUES ('tomaszewski-eryk@wp.pl', 'ROLE_USER');
INSERT INTO authorities (email, authority) VALUES ('wiktoria.malawska@test.pl', 'ROLE_USER');

CREATE TABLE resetToken (
  email      VARCHAR(100) PRIMARY KEY,
  resetToken CHAR(36),
  date       date,
  CONSTRAINT fk_resetToken_users FOREIGN KEY (email) REFERENCES users (email)
);

CREATE TABLE cycles (
  id       INT PRIMARY KEY AUTO_INCREMENT,
  clientID int,
  title    VARCHAR(100),
  CONSTRAINT fk_cycles_clients FOREIGN KEY (clientID) REFERENCES clients (id)
);

CREATE TABLE schemes (
  id       INT PRIMARY KEY AUTO_INCREMENT,
  title    VARCHAR(100),
  cycleID  int,
  sequence int,
  CONSTRAINT fk_schemes_cycles FOREIGN KEY (cycleID) REFERENCES cycles (id)
);

CREATE TABLE appointments (
  id        INT PRIMARY KEY AUTO_INCREMENT,
  startDate DATE,
  endDate   DATE,
  clientID  int,
  schemeID  int,
  CONSTRAINT fk_appointments_clients FOREIGN KEY (clientID) REFERENCES clients (id),
  CONSTRAINT fk_appointments_schemes FOREIGN KEY (schemeID) REFERENCES schemes (id)
);