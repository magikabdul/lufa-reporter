-- Migration V3: Rename column `data` back to `created_at` and make it the 2nd column
-- Note: PostgreSQL does not support reordering columns directly; we recreate the table
-- with the desired column order and copy data over in a single transaction.

BEGIN;

-- 1) Create new table with the desired column order and types
--    id (1st), created_at (2nd), then the rest
CREATE TABLE tre_reordered (
    id BIGSERIAL PRIMARY KEY,
    created_at DATE NOT NULL DEFAULT NOW(),
    customer_name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    hours_spent NUMERIC(6,2) NOT NULL CHECK (hours_spent >= 0),
    salesperson_last_name VARCHAR(100) NOT NULL,
    salesperson_first_name VARCHAR(100) NOT NULL,
    notes TEXT
);

-- 2) Copy data from existing table `tre` into `tre_reordered`
--    Prefer existing created_at if present; otherwise derive from `data` (DATE -> TIMESTAMPTZ)
INSERT INTO tre_reordered (
    id, created_at, customer_name, description, hours_spent,
    salesperson_last_name, salesperson_first_name, notes
)
SELECT 
    t.id,
    COALESCE(
        -- if column created_at exists and is not null
--         (CASE WHEN to_regclass('public.tre') IS NOT NULL AND EXISTS (
--             SELECT 1 FROM information_schema.columns
--             WHERE table_schema = 'public' AND table_name = 'tre' AND column_name = 'created_at'
--         ) THEN t.created_at END),
        -- else, fallback to `data` casted to timestamptz
        (CASE WHEN to_regclass('public.tre') IS NOT NULL AND EXISTS (
            SELECT 1 FROM information_schema.columns 
            WHERE table_schema = 'public' AND table_name = 'tre' AND column_name = 'data'
        ) THEN t.data END),
        NOW()
    ) AS created_at,
    t.customer_name,
    t.description,
    t.hours_spent,
    t.salesperson_last_name,
    t.salesperson_first_name,
    t.notes
FROM tre t;

-- 3) Drop old table and replace with the reordered one
DROP TABLE tre;
ALTER TABLE tre_reordered RENAME TO tre;

-- 4) Recreate index on created_at
CREATE INDEX IF NOT EXISTS idx_tre_created_at ON tre (created_at);

-- 5) Reset sequence for `id` to avoid future PK conflicts
SELECT setval(
    pg_get_serial_sequence('tre', 'id'),
    COALESCE((SELECT MAX(id) FROM tre), 0)
);

COMMIT;
