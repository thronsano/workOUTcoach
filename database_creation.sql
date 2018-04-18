CREATE TABLE clients (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(100)
);

INSERT INTO clients (id, name, email) VALUES (1, 'Steve Stevinsky', 'superSteve@gmail.com');
INSERT INTO clients (id, name, email) VALUES (2, 'Kate Rabbit', 'katyy@gmail.com');

CREATE TABLE users (
  id INT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(20) UNIQUE NOT NULL,
  password VARCHAR(100) NOT NULL,
  name VARCHAR(50) NOT NULL,
  surname VARCHAR(50) NOT NULL,
  securityQuestion VARCHAR(255) NOT NULL,
  securityAnswer VARCHAR(30) NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL
);

-- Sample user's password '$2a$10$9d5AC2CrUGaWSgwRHbtZV.TbKiuixWQh3EzJhZ7tHt0AeifE2AxCq' is a hashed version of password 'password'
INSERT INTO users(username, password, name, surname, securityQuestion, securityAnswer, email) VALUES ('User1', '$2a$10$9d5AC2CrUGaWSgwRHbtZV.TbKiuixWQh3EzJhZ7tHt0AeifE2AxCq', 'Steve', 'Doe', 'Whats ur name', 'Steve', 'sdoe@gmail.com');

create table authorities (
  userId INT NOT NULL,
  authority VARCHAR(50) NOT NULL ,
  CONSTRAINT fk_authorities_users FOREIGN KEY(userId) REFERENCES users(id)
);

INSERT INTO authorities(userId, authority) VALUES (1, 'ROLE_USER');