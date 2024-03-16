DROP TYPE IF EXISTS currency;

CREATE TYPE currency_type as ENUM (
    'EUR',
    'USD',
    'RUB',
    'GBP',
    'GEL',
    'AED',
    'RSD'
    );

DROP TYPE IF EXISTS account_status_type;

CREATE TYPE account_status_type as ENUM (
    'IN_USE',
    'FOR_REMOVAL'
    );

CREATE TABLE IF NOT EXISTS account
(
    id        varchar(64) PRIMARY KEY,
    user_id   BIGINT              NOT NULL REFERENCES app_users,
    money     DECIMAL             NOT NULL,
    currency  currency_type       NOT NULL,
    title     varchar(128)        NOT NULL,
    opened_at DATE                NOT NULL,
    closed_at DATE,
    status    account_status_type NOT NULL,
    version   INT                 NOT NULL
);

CREATE INDEX i_account_user_id ON account (user_id);

CREATE TABLE IF NOT EXISTS spending
(
    id           BIGSERIAL PRIMARY KEY,
    user_id      BIGINT        NOT NULL REFERENCES app_users,
    date         DATE          NOT NULL,
    description  varchar(128)  NOT NULL,
    amount        DECIMAL       NOT NULL,
    currency     currency_type NOT NULL,
    category_id  BIGINT        NOT NULL REFERENCES spending_category,
    created_at   DATE          NOT NULL,
    from_account varchar(64) REFERENCES account,
    version      INT           NOT NULL
);

CREATE INDEX i_spending_user_id ON spending (user_id);
CREATE INDEX i_spending_user_id_date ON spending (user_id, date);
