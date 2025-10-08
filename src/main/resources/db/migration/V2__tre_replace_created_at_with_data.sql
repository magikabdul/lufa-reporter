-- Migration: Replace created_at timestamp with data date column
-- 1) Add new column `data` (DATE)
ALTER TABLE tre ADD COLUMN IF NOT EXISTS data DATE;

-- 2) Backfill `data` from `created_at` (TIMESTAMPTZ -> DATE)
UPDATE tre SET data = created_at::date WHERE data IS NULL;

-- 3) Enforce NOT NULL on `data`
ALTER TABLE tre ALTER COLUMN data SET NOT NULL;

-- 4) Drop index that depends on `created_at` (if present)
DROP INDEX IF EXISTS idx_tre_created_at;

-- 5) Remove old column
ALTER TABLE tre DROP COLUMN IF EXISTS created_at;