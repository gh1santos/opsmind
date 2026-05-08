-- ============================================================
-- V1 — OpsMind Company Service — Initial Schema
-- ============================================================

-- ----------------------------------------------------------------
-- subscription_plans
-- ----------------------------------------------------------------
CREATE TABLE subscription_plans (
    id                      BIGSERIAL       PRIMARY KEY,
    type                    VARCHAR(50)     NOT NULL UNIQUE,
    name                    VARCHAR(100)    NOT NULL,
    max_users               INT             NOT NULL DEFAULT -1,   -- -1 = unlimited
    max_ai_requests_per_month INT           NOT NULL DEFAULT -1,
    max_storage_gb          INT             NOT NULL DEFAULT -1,
    price_cents             BIGINT          NOT NULL DEFAULT 0,     -- 0 = free
    active                  BOOLEAN         NOT NULL DEFAULT TRUE,
    created_at              TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_plans_active ON subscription_plans(active);
CREATE INDEX idx_plans_type   ON subscription_plans(type);

-- ----------------------------------------------------------------
-- companies
-- ----------------------------------------------------------------
CREATE TABLE companies (
    id              BIGSERIAL       PRIMARY KEY,
    tenant_id       UUID            NOT NULL UNIQUE,
    name            VARCHAR(255)    NOT NULL,
    slug            VARCHAR(100)    NOT NULL UNIQUE,
    industry        VARCHAR(100),
    status          VARCHAR(50)     NOT NULL DEFAULT 'ACTIVE',
    plan_id         BIGINT          REFERENCES subscription_plans(id),
    trial_ends_at   TIMESTAMP,
    owner_email     VARCHAR(255)    NOT NULL,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_companies_tenant_id   ON companies(tenant_id);
CREATE INDEX idx_companies_slug        ON companies(slug);
CREATE INDEX idx_companies_status      ON companies(status);
CREATE INDEX idx_companies_owner_email ON companies(owner_email);
CREATE INDEX idx_companies_plan_id     ON companies(plan_id);

-- ----------------------------------------------------------------
-- memberships
-- ----------------------------------------------------------------
CREATE TABLE memberships (
    id              BIGSERIAL       PRIMARY KEY,
    company_id      BIGINT          NOT NULL REFERENCES companies(id),
    tenant_id       UUID            NOT NULL,
    user_email      VARCHAR(255)    NOT NULL,
    role            VARCHAR(50)     NOT NULL,   -- OWNER | ADMIN | MEMBER | VIEWER
    active          BOOLEAN         NOT NULL DEFAULT TRUE,
    joined_at       TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    left_at         TIMESTAMP,

    CONSTRAINT uq_membership_company_user UNIQUE (company_id, user_email)
);

CREATE INDEX idx_memberships_company_id  ON memberships(company_id);
CREATE INDEX idx_memberships_user_email  ON memberships(user_email);
CREATE INDEX idx_memberships_tenant_id   ON memberships(tenant_id);
CREATE INDEX idx_memberships_active      ON memberships(active);
CREATE INDEX idx_memberships_role        ON memberships(role);

-- ----------------------------------------------------------------
-- company_invites
-- ----------------------------------------------------------------
CREATE TABLE company_invites (
    id                  BIGSERIAL       PRIMARY KEY,
    company_id          BIGINT          NOT NULL REFERENCES companies(id),
    tenant_id           UUID            NOT NULL,
    invited_email       VARCHAR(255)    NOT NULL,
    invited_by_email    VARCHAR(255)    NOT NULL,
    role                VARCHAR(50)     NOT NULL,   -- role to assign on acceptance
    token               VARCHAR(500)    NOT NULL UNIQUE,
    status              VARCHAR(50)     NOT NULL DEFAULT 'PENDING',  -- PENDING | ACCEPTED | REVOKED | EXPIRED
    expires_at          TIMESTAMP       NOT NULL,
    created_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_invites_company_id     ON company_invites(company_id);
CREATE INDEX idx_invites_invited_email  ON company_invites(invited_email);
CREATE INDEX idx_invites_token          ON company_invites(token);
CREATE INDEX idx_invites_status         ON company_invites(status);
CREATE INDEX idx_invites_tenant_id      ON company_invites(tenant_id);
