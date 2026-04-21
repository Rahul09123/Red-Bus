-- =========================
-- securityDB Initialization
-- =========================

\connect "securityDB";

-- =========================
-- admin_sessions
-- =========================
CREATE TABLE admin_sessions
(
    id         SERIAL PRIMARY KEY,
    admin_id   INT         NOT NULL,
    code       VARCHAR(64) NOT NULL UNIQUE,
    created_at TIMESTAMP   NOT NULL,
    expires_at TIMESTAMP   NOT NULL
);

CREATE INDEX idx_admin_sessions_admin_id
    ON admin_sessions (admin_id);

CREATE INDEX idx_admin_sessions_expires_at
    ON admin_sessions (expires_at);

-- =========================
-- company_sessions
-- =========================
CREATE TABLE company_sessions
(
    id         SERIAL PRIMARY KEY,
    company_id INT         NOT NULL,
    code       VARCHAR(64) NOT NULL UNIQUE,
    created_at TIMESTAMP   NOT NULL,
    expires_at TIMESTAMP   NOT NULL
);

CREATE INDEX idx_company_sessions_company_id
    ON company_sessions (company_id);

CREATE INDEX idx_company_sessions_expires_at
    ON company_sessions (expires_at);

-- =========================
-- customer_sessions
-- =========================
CREATE TABLE customer_sessions
(
    id          SERIAL PRIMARY KEY,
    customer_id INT         NOT NULL,
    code        VARCHAR(64) NOT NULL UNIQUE,
    created_at  TIMESTAMP   NOT NULL,
    expires_at  TIMESTAMP   NOT NULL
);

CREATE INDEX idx_customer_sessions_customer_id
    ON customer_sessions (customer_id);

CREATE INDEX idx_customer_sessions_expires_at
    ON customer_sessions (expires_at);
