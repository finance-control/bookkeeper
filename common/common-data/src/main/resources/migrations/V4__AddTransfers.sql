CREATE TABLE IF NOT EXISTS transfer
(
    id                BIGSERIAL PRIMARY KEY,
    user_id           BIGINT        NOT NULL REFERENCES app_users,
    date              DATE          NOT NULL,
    description       varchar(128)  NOT NULL,
    send_amount       DECIMAL,
    received_amount   DECIMAL       NOT NULL,
    send_currency     currency_type,
    received_currency currency_type NOT NULL,
    from_account      varchar(64) REFERENCES account,
    to_account        varchar(64) REFERENCES account,
    category_id       BIGINT        NOT NULL REFERENCES spending_category,
    created_at        DATE          NOT NULL,
    version           INT           NOT NULL
);

CREATE INDEX i_transfer_user_id ON transfer (user_id);
CREATE INDEX i_transfer_user_id_date ON transfer (user_id, date);

ALTER SEQUENCE transfer_id_seq INCREMENT BY 1000;
