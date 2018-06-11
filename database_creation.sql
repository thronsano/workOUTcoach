DROP DATABASE workoutcoach;

CREATE DATABASE workoutcoach;
USE workoutcoach;

CREATE TABLE users (
  email      VARCHAR(100) PRIMARY KEY NOT NULL,
  password   VARCHAR(100)             NOT NULL,
  name       VARCHAR(50)              NOT NULL,
  surname    VARCHAR(50)              NOT NULL,
  hourlyRate INT                      NOT NULL
);

CREATE TABLE clients (
  id              INT PRIMARY KEY AUTO_INCREMENT,
  name            VARCHAR(100) NOT NULL,
  surname         VARCHAR(100) NOT NULL,
  coachEmail      VARCHAR(100) NOT NULL,
  gymName         VARCHAR(100),
  goal            VARCHAR(1000),
  goalValue       INT,
  healthCondition VARCHAR(1000),
  isActive        BIT(1),
  phoneNumber     VARCHAR(15),
  CONSTRAINT fk_clients_users FOREIGN KEY (coachEmail) REFERENCES users (email)
    ON DELETE CASCADE
);

create table authorities (
  email     VARCHAR(100) NOT NULL,
  authority VARCHAR(50)  NOT NULL,
  CONSTRAINT fk_authorities_users FOREIGN KEY (email) REFERENCES users (email)
    ON DELETE CASCADE
);

CREATE TABLE resetToken (
  email      VARCHAR(100) PRIMARY KEY,
  resetToken CHAR(36),
  date       DATETIME,
  CONSTRAINT fk_resetToken_users FOREIGN KEY (email) REFERENCES users (email)
    ON DELETE CASCADE
);

CREATE TABLE cycles (
  id       INT PRIMARY KEY AUTO_INCREMENT,
  clientID int NOT NULL,
  title    VARCHAR(100),
  CONSTRAINT fk_cycles_clients FOREIGN KEY (clientID) REFERENCES clients (id)
    ON DELETE CASCADE
);

CREATE TABLE schemes (
  id       INT PRIMARY KEY AUTO_INCREMENT,
  title    VARCHAR(100),
  cycleID  int,
  sequence int,
  CONSTRAINT fk_schemes_cycles FOREIGN KEY (cycleID) REFERENCES cycles (id)
    ON DELETE CASCADE
);

CREATE TABLE appointments (
  id          INT PRIMARY KEY AUTO_INCREMENT,
  startDate   DATETIME,
  endDate     DATETIME,
  isCancelled BIT(1),
  partOfCycle BIT(1),
  clientID    int,
  schemeID    int,
  CONSTRAINT fk_appointments_clients FOREIGN KEY (clientID) REFERENCES clients (id)
    ON DELETE CASCADE,
  CONSTRAINT fk_appointments_schemes FOREIGN KEY (schemeID) REFERENCES schemes (id)
    ON DELETE CASCADE
);

CREATE TABLE payments (
  id            INT PRIMARY KEY AUTO_INCREMENT,
  paymentDate   DATETIME,
  appointmentID INT,
  isPaid        BIT(1),
  amount        FLOAT,
  CONSTRAINT fk_payments_appointments FOREIGN KEY (appointmentID) REFERENCES appointments (id)
    ON DELETE CASCADE
);


CREATE TABLE exercises (
  id          int PRIMARY KEY AUTO_INCREMENT,
  name        VARCHAR(100),
  schemeID    int,
  repetitions int,
  CONSTRAINT fk_exercises_schemes FOREIGN KEY (schemeID) REFERENCES schemes (id)
    ON DELETE CASCADE
);

-- Sample user's password '$2a$10$9d5AC2CrUGaWSgwRHbtZV.TbKiuixWQh3EzJhZ7tHt0AeifE2AxCq' is a hashed version of password 'password'
INSERT INTO users (email, password, name, surname, hourlyRate) VALUES ('sdoe@gmail.com', '$2a$10$9d5AC2CrUGaWSgwRHbtZV.TbKiuixWQh3EzJhZ7tHt0AeifE2AxCq', 'Steve', 'Doe', 100);
INSERT INTO users (email, password, name, surname, hourlyRate) VALUES ('test@gmail.com', '$2a$10$9d5AC2CrUGa', 'Steve', 'Doe', 50);
INSERT INTO users (email, password, name, surname, hourlyRate) VALUES ('wiktoria.malawska@wp.pl', '$2a$10$9d5AC2CrUGaWSgwRHbtZV.TbKiuixWQh3EzJhZ7tHt0AeifE2AxCq', 'Wiktoria', 'Malawska', 200);
INSERT INTO users (email, password, name, surname, hourlyRate) VALUES ('tomaszewski-eryk@wp.pl', '$2a$10$9d5AC2CrUGaWSgwRHbtZV.TbKiuixWQh3EzJhZ7tHt0AeifE2AxCq', 'Eryk', 'Tomaszewski', 20);
INSERT INTO users (email, password, name, surname, hourlyRate) VALUES ('wiktoria.malawska@test.pl', '$2a$10$9d5AC2CrUGaWSgwRHbtZV.TbKiuixWQh3EzJhZ7tHt0AeifE2AxCq', 'Wiktoria', 'Malawska', 100);

INSERT INTO authorities (email, authority) VALUES ('sdoe@gmail.com', 'ROLE_USER');
INSERT INTO authorities (email, authority) VALUES ('wiktoria.malawska@wp.pl', 'ROLE_USER');
INSERT INTO authorities (email, authority) VALUES ('tomaszewski-eryk@wp.pl', 'ROLE_USER');
INSERT INTO authorities (email, authority) VALUES ('wiktoria.malawska@test.pl', 'ROLE_USER');

INSERT INTO clients (name, surname, coachEmail, gymName, goal, goalValue, healthCondition, isActive) VALUES ('Steve', 'Stevinsky', 'sdoe@gmail.com', 'Jatomi', 'Muscle gain',0, 'Healthy', true);
INSERT INTO clients (name, surname, coachEmail, gymName, goal,goalValue, healthCondition, isActive) VALUES ('Kate', 'Rabbit', 'sdoe@gmail.com', 'Platinium', 'To be fit!',0, 'healthy', true);
INSERT INTO clients (name, surname, coachEmail, isActive) VALUES ('Paul', 'Old', 'sdoe@gmail.com', false);
INSERT INTO clients (name, surname, coachEmail, isActive) VALUES ('William', 'McDonald', 'wiktoria.malawska@wp.pl', true);
INSERT INTO clients (name, surname, coachEmail, isActive) VALUES ('Ann', 'McDonald2', 'wiktoria.malawska@wp.pl', true);
INSERT INTO clients (name, surname, coachEmail, isActive) VALUES ('Mary', 'Unused', 'wiktoria.malawska@wp.pl', false);
INSERT INTO clients (name, surname, coachEmail, isActive) VALUES ('Tom', 'Unused2', 'wiktoria.malawska@wp.pl', false);

INSERT INTO cycles (clientID, title) VALUES (1, 'Rzezba');
INSERT INTO cycles (clientID, title) VALUES (2, 'Figura');
INSERT INTO cycles (clientID, title) VALUES (3, 'Rzezba');
INSERT INTO cycles (clientID, title) VALUES (4, 'Figura');
INSERT INTO cycles (clientID, title) VALUES (5, 'Rzezba');
INSERT INTO cycles (clientID, title) VALUES (6, 'Figura');
INSERT INTO cycles (clientID, title) VALUES (7, 'Rzezba');

INSERT INTO schemes (title, cycleID, sequence) VALUES ('Nogi seq1', 1, 1);
INSERT INTO schemes (title, cycleID, sequence) VALUES ('Barki seq2', 1, 2);
INSERT INTO schemes (title, cycleID, sequence) VALUES ('Rece seq3', 1, 3);
INSERT INTO schemes (title, cycleID, sequence) VALUES ('Brzuch seq4', 1, 4);
INSERT INTO schemes (title, cycleID, sequence) VALUES ('Nogi dla leniwych', 2, 1);
INSERT INTO schemes (title, cycleID, sequence) VALUES ('Rece dla zaawansowanych', 2, 2);
INSERT INTO schemes (title, cycleID, sequence) VALUES ('Brzuch dla leniwych', 2, 0);
INSERT INTO schemes (title, cycleID, sequence) VALUES ('Rece dla zaawansowanych', 3, 1);

insert into exercises (name, schemeID, repetitions) VALUES ('przysiady', 1, 10);
insert into exercises (name, schemeID, repetitions) VALUES ('przysiady na jednej nodze', 2, 10);

INSERT INTO appointments (id, startDate, endDate, isCancelled, partOfCycle, clientID, schemeID) values (1, '2018-06-10 11:00:00', '2018-06-10 11:40:00', 0, 0, 2, 1);
INSERT INTO appointments (id, startDate, endDate, isCancelled, partOfCycle, clientID, schemeID) values (2, '2018-06-12 10:00:00', '2018-06-12 11:00:00', 0, 0, 1, 1);

INSERT INTO payments (appointmentID, isPaid, amount) VALUES (1, 0, 50);
INSERT INTO payments (appointmentID, isPaid, amount) VALUES (2, 0, 25);