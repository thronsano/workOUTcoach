create TABLE users (
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(20) UNIQUE NOT NULL,
  password VARCHAR(30) NOT NULL,
  name VARCHAR(50) NOT NULL,
  surname VARCHAR(50) NOT NULL,
  securityQuestion VARCHAR(255) NOT NULL,
  securityAnswer VARCHAR(30) NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL
);

-- Sample users password '$2a$10$9d5AC2CrUGaWSgwRHbtZV.TbKiuixWQh3EzJhZ7tHt0AeifE2AxCq' is a hashed password 'password'
INSERT into users(username, password, name, surname, securityQuestion, securityAnswer, email) VALUES ('User1', '$2a$10$9d5AC2CrUGaWSgwRHbtZV.TbKiuixWQh3EzJhZ7tHt0AeifE2AxCq', 'Steve', 'Doe', 'Whats ur name', 'Steve', 'sdoe@gmail.com');

create table authorities (
  userId INT NOT NULL,
  authority VARCHAR(50) NOT NULL ,
  CONSTRAINT fk_authorities_users FOREIGN KEY(userId) REFERENCES users(id)
);

INSERT INTO authorities(userId, authority) VALUES (1, 'ROLE_USER');