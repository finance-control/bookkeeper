CREATE TABLE IF NOT EXISTS transfer_category
(
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT       NOT NULL REFERENCES app_users,
    title       varchar(128) NOT NULL,
    description TEXT,
    version INT NOT NULL
);

CREATE TABLE IF NOT EXISTS spending_category
(
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT       NOT NULL REFERENCES app_users,
    title       varchar(128) NOT NULL,
    description TEXT,
    version INT NOT NULL
);

CREATE INDEX IF NOT EXISTS i_spending_category_user_id ON spending_category (user_id);
CREATE INDEX IF NOT EXISTS i_transfer_category_user_id ON transfer_category (user_id);

