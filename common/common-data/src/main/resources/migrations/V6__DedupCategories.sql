CREATE TABLE IF NOT EXISTS user_category
(
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT       NOT NULL REFERENCES app_users,
    title       varchar(128) NOT NULL,
    description TEXT,
    version     INT          NOT NULL,
    UNIQUE (title, user_id)
);

CREATE INDEX IF NOT EXISTS i_category_user_id ON user_category (user_id);

ALTER SEQUENCE user_category_id_seq INCREMENT BY 1000;

DELETE FROM spending;
DELETE FROM transfer;


ALTER TABLE spending DROP CONSTRAINT spending_category_id_fkey;
ALTER TABLE transfer DROP CONSTRAINT transfer_category_id_fkey;
ALTER TABLE spending ADD FOREIGN KEY (category_id) REFERENCES user_category;
ALTER TABLE transfer ADD FOREIGN KEY (category_id) REFERENCES user_category;

DROP TABLE transfer_category;
DROP TABLE spending_category;

-- TODO: change references
