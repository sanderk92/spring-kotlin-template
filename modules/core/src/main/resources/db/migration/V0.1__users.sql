CREATE TABLE users(
  id        uuid         NOT NULL PRIMARY KEY,
  email     VARCHAR(255) NOT NULL UNIQUE,
  firstname VARCHAR(255) NOT NULL,
  lastname  VARCHAR(255) NOT NULL,
  username  VARCHAR(255) NOT NULL UNIQUE
);

CREATE UNIQUE INDEX user_email_index ON users (email);
CREATE UNIQUE INDEX user_name_index ON users (username);
CREATE INDEX user_details_index ON users (email, firstname, lastname, username)
