INSERT INTO users (name, email, password, default_currency, default_monthly_budget)
VALUES ('Danil', 'danil@yandex.com', 'password', 'USD', 3000);

INSERT INTO wallets (user_id, name, currency, type)
VALUES ((SELECT id FROM users WHERE email = 'danil@yandex.com'),
        'main',
        'USD',
        'CASH');

INSERT INTO transactions (user_id, wallet_id, date_time, amount, operation_kind, category)
VALUES ((SELECT id FROM users WHERE email = 'danil@yandex.com'),
        (SELECT id FROM wallets WHERE user_id = (SELECT id FROM users WHERE email = 'danil@yandex.com')),
        '2025-05-07 10:00:00',
        460,
        'EXPENSE',
        'TRANSPORT');