INSERT INTO users (name, email, password, default_currency, default_monthly_budget, role)
VALUES ('Admin', 'admin@gmail.com', '{noop}admin', 'USD', 5000.00, 'ADMIN'),
       ('User', 'user@gmail.com', '{noop}user', 'USD', 1000.00, 'USER');

INSERT INTO wallets (name, currency, type, user_id)
VALUES ('Main Wallet', 'USD', 'CASH', 1),
       ('Card', 'USD', 'CARD', 1),
       ('My Wallet', 'USD', 'CASH', 2);

INSERT INTO transactions (user_id, wallet_id, category, date_time, amount, operation_kind)
VALUES (1, 1, 'SALARY',        '2025-01-10 10:00:00', 3000.00, 'INCOME'),
       (1, 1, 'CAFE',          '2025-01-11 12:00:00',  150.00, 'EXPENSE'),
       (1, 1, 'TRANSPORT',     '2025-01-12 09:00:00',   50.00, 'EXPENSE'),
       (1, 2, 'ENTERTAINMENT', '2025-01-13 18:00:00',  200.00, 'EXPENSE'),
       (2, 3, 'SALARY',        '2025-01-10 10:00:00', 2000.00, 'INCOME'),
       (2, 3, 'UTILITIES',     '2025-01-14 15:00:00',  100.00, 'EXPENSE');