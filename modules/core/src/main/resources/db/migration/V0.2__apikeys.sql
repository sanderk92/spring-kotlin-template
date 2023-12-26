CREATE TABLE apikeys(
  id          uuid          NOT NULL PRIMARY KEY,
  owner       uuid          NOT NULL REFERENCES users,
  hashed_key  VARCHAR(255)  NOT NULL UNIQUE,
  name        VARCHAR(255)  NOT NULL
);

CREATE TABLE apikey_authorities(
  api_key_entity_id  uuid  NOT NULL REFERENCES apikeys,
  authorities        TEXT  NOT NULL CHECK (authorities IN ('READ', 'WRITE', 'DELETE', 'ADMIN'))
);

CREATE UNIQUE INDEX hashed_apikey_index ON apikeys (hashed_key)
