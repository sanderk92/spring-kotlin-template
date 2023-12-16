CREATE TABLE apikeys
(
  id          uuid          NOT NULL PRIMARY KEY,
  owner       uuid          REFERENCES users,
  hashed_key  VARCHAR(255)  NOT NULL UNIQUE,
  name        VARCHAR(255)  NOT NULL UNIQUE
);

CREATE TABLE apikey_authorities
(
  api_key_entity_id  uuid  NOT NULL REFERENCES apikeys,
  authorities        TEXT  CHECK (authorities IN ('READ', 'WRITE', 'DELETE', 'ADMIN'))
);

CREATE INDEX hashed_apikey ON apikeys (hashed_key)
