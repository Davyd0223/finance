DROP TABLE IF EXISTS transactions CASCADE;
DROP TABLE IF EXISTS wallets CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users
(
    id                     SERIAL PRIMARY KEY,
    name                   VARCHAR(100) NOT NULL,
    email                  VARCHAR      NOT NULL,
    password               VARCHAR      NOT NULL,
    default_currency       VARCHAR      NOT NULL,
    default_monthly_budget numeric      NOT NULL
);

CREATE TABLE wallets
(
    id       SERIAL PRIMARY KEY,
    user_id  INTEGER NOT NULL,
    name     VARCHAR NOT NULL,
    currency VARCHAR NOT NULL,
    type     VARCHAR NOT NULL
);

CREATE TABLE transactions
(
    id             SERIAL PRIMARY KEY,
    user_id        INTEGER                                                      NOT NULL,
    date_time      TIMESTAMP WITHOUT TIME ZONE                                  NOT NULL,
    amount         numeric(15, 2)                                               NOT NULL,
    operation_kind VARCHAR(50) CHECK ( operation_kind IN ('INCOME', 'EXPENSE')) NOT NULL,
    category       VARCHAR(50)                                                  NOT NULL
);

ALTER TABLE users
    ADD CONSTRAINT users_email_key UNIQUE (email);

ALTER TABLE transactions
    ADD CONSTRAINT transactions_user_id_fkey FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE wallets
    ADD CONSTRAINT wallets_user_id_fkey FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;