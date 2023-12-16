CREATE TABLE users
(
  id         uuid          NOT NULL PRIMARY KEY,
  email      VARCHAR(255)  NOT NULL UNIQUE,
  firstname  VARCHAR(255)  NOT NULL,
  lastname   VARCHAR(255)  NOT NULL,
  username   VARCHAR(255)  NOT NULL UNIQUE
);

CREATE INDEX user_details ON users (email, firstname, lastname, username)
