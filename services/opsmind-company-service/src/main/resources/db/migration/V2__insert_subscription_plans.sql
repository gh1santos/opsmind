-- ============================================================
-- V2 — Seed subscription plans
-- ============================================================
-- price_cents: 0 = free, values in BRL cents
--   STARTER       R$ 97,00/month  →   9700
--   PROFESSIONAL  R$ 297,00/month → 29700
--   ENTERPRISE    custom / -1 (contact sales)
-- max_users:               -1 = unlimited
-- max_ai_requests_per_month: -1 = unlimited
-- max_storage_gb:          -1 = unlimited
-- ============================================================

INSERT INTO subscription_plans
    (type, name, max_users, max_ai_requests_per_month, max_storage_gb, price_cents, active)
VALUES
    ('FREE',         'Free',         3,   100,    1,      0,     TRUE),
    ('STARTER',      'Starter',      15,  1000,   10,     9700,  TRUE),
    ('PROFESSIONAL', 'Professional', 50,  5000,   50,     29700, TRUE),
    ('ENTERPRISE',   'Enterprise',   -1,  -1,     -1,     -1,    TRUE);
