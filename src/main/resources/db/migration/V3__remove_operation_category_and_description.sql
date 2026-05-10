-- Existing DB upgrade: remove OperationCategory and description, move to enum Category stored in transactions.category

ALTER TABLE IF EXISTS transactions DROP CONSTRAINT IF EXISTS transactions_operation_category_id_fkey;
ALTER TABLE IF EXISTS transactions DROP CONSTRAINT IF EXISTS transactions_wallet_id_fkey;

ALTER TABLE IF EXISTS transactions DROP COLUMN IF EXISTS operation_category_id;
ALTER TABLE IF EXISTS transactions DROP COLUMN IF EXISTS wallet_id;
ALTER TABLE IF EXISTS transactions DROP COLUMN IF EXISTS description;

ALTER TABLE IF EXISTS transactions ADD COLUMN IF NOT EXISTS category VARCHAR(50);

UPDATE transactions
SET category = COALESCE(category, 'OTHER')
WHERE category IS NULL;

ALTER TABLE IF EXISTS transactions ALTER COLUMN category SET NOT NULL;

DROP TABLE IF EXISTS operation_categories CASCADE;

ALTER TABLE IF EXISTS transactions DROP CONSTRAINT IF EXISTS transactions_operation_category_id_fkey;
ALTER TABLE IF EXISTS transactions DROP CONSTRAINT IF EXISTS transactions_wallet_id_fkey;

ALTER TABLE IF EXISTS transactions DROP COLUMN IF EXISTS operation_category_id;
ALTER TABLE IF EXISTS transactions DROP COLUMN IF EXISTS wallet_id;
ALTER TABLE IF EXISTS transactions DROP COLUMN IF EXISTS description;

ALTER TABLE IF EXISTS transactions ADD COLUMN IF NOT EXISTS category VARCHAR(50);

UPDATE transactions
SET category = COALESCE(category, 'OTHER')
WHERE category IS NULL;

ALTER TABLE IF EXISTS transactions ALTER COLUMN category SET NOT NULL;

DROP TABLE IF EXISTS operation_categories CASCADE;

