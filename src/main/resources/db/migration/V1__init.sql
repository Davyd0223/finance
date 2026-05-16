CREATE TABLE users
(
    id                     SERIAL PRIMARY KEY,
    name                   VARCHAR(100)   NOT NULL,
    email                  VARCHAR        NOT NULL UNIQUE,
    password               VARCHAR        NOT NULL,
    default_currency       VARCHAR        NOT NULL,
    default_monthly_budget NUMERIC(15, 2) NOT NULL
);

CREATE TABLE wallets
(
    id       SERIAL PRIMARY KEY,
    user_id  INTEGER NOT NULL,
    name     VARCHAR NOT NULL,
    currency VARCHAR NOT NULL,
    type     VARCHAR NOT NULL,
    CONSTRAINT wallets_user_id_fkey
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE transactions
(
    id             SERIAL PRIMARY KEY,
    user_id        INTEGER        NOT NULL,
    wallet_id      INTEGER        NOT NULL,
    date_time      TIMESTAMP      NOT NULL,
    amount         NUMERIC(15, 2) NOT NULL,
    operation_kind VARCHAR(50)    NOT NULL
        CHECK (operation_kind IN ('INCOME', 'EXPENSE')),
    category       VARCHAR(50)    NOT NULL,
    CONSTRAINT transactions_user_id_fkey
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT transactions_wallet_id_fkey
        FOREIGN KEY (wallet_id) REFERENCES wallets (id) ON DELETE CASCADE
);