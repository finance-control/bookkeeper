DROP TABLE IF EXISTS app_tokens;

CREATE TABLE app_tokens
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT       NOT NULL REFERENCES app_users,
    client_id  varchar(128) NOT NULL,
    ip_address varchar(64),
    created_at TIMESTAMP    NOT NULL,
    expired_at TIMESTAMP    NOT NULL,
    token      TEXT         NOT NULL,
    version    INT          NOT NULL,
    UNIQUE (token, client_id)
);

CREATE INDEX IF NOT EXISTS i_app_tokens_user_id_client_id_token ON app_tokens (user_id, client_id, token);
ALTER SEQUENCE app_tokens_id_seq INCREMENT BY 1000;