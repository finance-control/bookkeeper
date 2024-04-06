ALTER TABLE transfer_category
    ADD CONSTRAINT UNIQUE (title, user_id);

ALTER TABLE spending_category
    ADD CONSTRAINT UNIQUE (title, user_id);
