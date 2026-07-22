CREATE TABLE IF NOT EXISTS platform_bootstrap (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    note VARCHAR(120) NOT NULL
);

INSERT INTO platform_bootstrap (note)
SELECT 'initial-baseline'
WHERE NOT EXISTS (
    SELECT 1
    FROM platform_bootstrap
    WHERE note = 'initial-baseline'
);