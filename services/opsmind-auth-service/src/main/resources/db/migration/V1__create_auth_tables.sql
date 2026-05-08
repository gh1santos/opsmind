CREATE TABLE roles
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),

    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE users
(
    id              BIGSERIAL PRIMARY KEY,

    tenant_id       UUID NOT NULL,

    first_name      VARCHAR(100) NOT NULL,
    last_name       VARCHAR(100) NOT NULL,

    email           VARCHAR(255) NOT NULL UNIQUE,

    password_hash   VARCHAR(255) NOT NULL,

    active          BOOLEAN NOT NULL DEFAULT TRUE,

    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP,

    last_login_at   TIMESTAMP
);

CREATE TABLE user_roles
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,

    PRIMARY KEY (user_id, role_id),

    CONSTRAINT fk_user_roles_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_user_roles_role
        FOREIGN KEY (role_id)
            REFERENCES roles (id)
            ON DELETE CASCADE
);

CREATE INDEX idx_users_email
    ON users(email);

CREATE INDEX idx_users_tenant
    ON users(tenant_id);