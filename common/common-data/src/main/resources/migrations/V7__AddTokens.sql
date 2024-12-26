create extension if not exists pgcrypto;

CREATE TABLE IF NOT EXISTS app_tokens
(
    user_id    BIGINT       NOT NULL REFERENCES app_users,
    client_id  varchar(128) NOT NULL,
    ip_address varchar(64),
    created_at  TIMESTAMP    NOT NULL,
    expired_at TIMESTAMP    NOT NULL,
    token      TEXT NOT NULL,
    version    INT          NOT NULL,
    UNIQUE (token, client_id)
);

CREATE INDEX IF NOT EXISTS i_app_tokens_client_id_token ON app_tokens (client_id, token);