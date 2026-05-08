-- Refresh Token Rotation: novos campos para rastrear rotação e contexto do usuário
ALTER TABLE refresh_tokens
    ADD COLUMN user_email  VARCHAR(255),
    ADD COLUMN user_role   VARCHAR(100),
    ADD COLUMN tenant_id   UUID,
    ADD COLUMN replaced_by_token VARCHAR(500);

-- Índices para performance em consultas frequentes
CREATE INDEX idx_refresh_tokens_user_id  ON refresh_tokens(user_id);
CREATE INDEX idx_refresh_tokens_revoked  ON refresh_tokens(revoked);

-- Brute Force Protection: tabela de tentativas de login (fallback persistente)
CREATE TABLE login_attempts (
    id          BIGSERIAL PRIMARY KEY,
    identifier  VARCHAR(255) NOT NULL,
    attempts    INT          NOT NULL DEFAULT 0,
    blocked_until TIMESTAMP,
    last_attempt_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX idx_login_attempts_identifier ON login_attempts(identifier);
