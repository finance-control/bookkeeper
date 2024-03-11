CREATE TABLE IF NOT EXISTS app_users
(
    id         BIGINT PRIMARY KEY,
    name       TEXT      NOT NULL,
    surname    TEXT      NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    version    INT       NOT NULL
);

CREATE TABLE IF NOT EXISTS credentials
(
    user_id    BIGINT       NOT NULL REFERENCES app_users,
    email      varchar(128) NOT NULL UNIQUE,
    password   TEXT         NOT NULL,
    version    INT          NOT NULL,
    updated_at TIMESTAMP    NOT NULL
);

CREATE INDEX IF NOT EXISTS i_credentials_email on credentials (email);