CREATE TABLE refresh_tokens (

                                id BIGSERIAL PRIMARY KEY,

                                token VARCHAR(500) NOT NULL UNIQUE,

                                user_id BIGINT NOT NULL,

                                revoked BOOLEAN NOT NULL DEFAULT FALSE,

                                expires_at TIMESTAMP NOT NULL,

                                created_at TIMESTAMP NOT NULL
);