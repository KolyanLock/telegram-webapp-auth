CREATE TABLE users
(
    id                 BIGINT PRIMARY KEY,
    first_name         VARCHAR(100),
    last_name          VARCHAR(100),
    username           VARCHAR(100),
    language_code      VARCHAR(2),
    allows_write_to_pm BOOLEAN     NOT NULL,
    photo_url          VARCHAR(255),
    created_at         TIMESTAMPTZ NOT NULL,
    updated_at         TIMESTAMPTZ NOT NULL
);