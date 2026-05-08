INSERT INTO users(name, email, password, default_currency, default_monthly_budget)
VALUES ('Danil', 'danil@yandex.com', 'password', 'BYN', 3000);

INSERT INTO wallets(user_id, owner, name, currency, type)
VALUES ((SELECT id FROM users WHERE email = 'danil@yandex.com'),'Danil', 'main', 'BYN', 'cash');

INSERT INTO operation_categories(user_id, name, kind)
VALUES ((SELECT id FROM users WHERE email = 'danil@yandex.com'),'Payment for transportation', 'EXPENSE');

INSERT INTO transactions(user_id, wallet_id, operation_category_id, date_time , amount, operation_kind, description)
VALUES ((SELECT id FROM users WHERE email = 'danil@yandex.com'),
        (SELECT id FROM wallets WHERE name = 'main' AND user_id = (SELECT id FROM users WHERE email = 'danil@yandex.com')),
        (SELECT id FROM operation_categories WHERE name = 'Payment for transportation' AND user_id = (SELECT id FROM users WHERE email = 'danil@yandex.com')),
        '2025-05-07 10:00:00', 460, 'EXPENSE', 'Standard payment');