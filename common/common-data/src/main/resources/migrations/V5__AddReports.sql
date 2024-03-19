CREATE TABLE IF NOT EXISTS daily_report
(
    user_id   BIGINT NOT NULL REFERENCES app_users,
    date      DATE   NOT NULL,
    expenses  JSONB  NOT NULL,
    earnings  JSONB  NOT NULL,
    transfers JSONB  NOT NULL,
    total     JSONB  NOT NULL,
    version   INT    NOT NULL,
    PRIMARY KEY (user_id, date)
);

CREATE TABLE IF NOT EXISTS monthly_report
(
    user_id    BIGINT NOT NULL REFERENCES app_users,
    year_month DATE   NOT NULL,
    expenses   JSONB  NOT NULL,
    earnings   JSONB  NOT NULL,
    transfers  JSONB  NOT NULL,
    total      JSONB  NOT NULL,
    version    INT    NOT NULL,
    PRIMARY KEY (user_id, year_month)
);

CREATE TABLE IF NOT EXISTS yearly_report
(
    user_id   BIGINT NOT NULL REFERENCES app_users,
    year      INT    NOT NULL,
    expenses  JSONB  NOT NULL,
    earnings  JSONB  NOT NULL,
    transfers JSONB  NOT NULL,
    total     JSONB  NOT NULL,
    version   INT    NOT NULL,
    PRIMARY KEY (user_id, year)
);

CREATE INDEX IF NOT EXISTS i_daily_report_user_id ON daily_report (user_id);
CREATE INDEX IF NOT EXISTS i_monthly_report_user_id ON monthly_report (user_id);
