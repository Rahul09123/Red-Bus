-- ============================
-- memberDB Initialization
-- ============================
\connect "memberDB";
-- admins, companies, customers, favorites


-- ============================
-- Admins
-- ============================
CREATE TABLE admins
(
    id           SERIAL PRIMARY KEY,

    name         VARCHAR(150) NOT NULL,
    surname      VARCHAR(50)  NOT NULL,
    email        VARCHAR(100) NOT NULL UNIQUE,
    password     TEXT         NOT NULL,

    ref_admin_id INTEGER,

    created_at   TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMPTZ  NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_admin_ref_admin
        FOREIGN KEY (ref_admin_id)
            REFERENCES admins (id)
            ON DELETE SET NULL
);

-- ============================
-- Companies
-- ============================
CREATE TABLE companies
(
    id           SERIAL PRIMARY KEY,

    title        VARCHAR(150) NOT NULL,
    email        VARCHAR(100) NOT NULL UNIQUE,
    password     TEXT         NOT NULL,

    is_verified  BOOLEAN      NOT NULL DEFAULT FALSE,

    ref_admin_id INTEGER,

    created_at   TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    verified_at  TIMESTAMPTZ,

    CONSTRAINT fk_company_admin
        FOREIGN KEY (ref_admin_id)
            REFERENCES admins (id)
            ON DELETE SET NULL
);

-- ============================
-- Customers
-- ============================
CREATE TABLE customers
(
    id         SERIAL PRIMARY KEY,

    name       VARCHAR(150) NOT NULL,
    surname    VARCHAR(50)  NOT NULL,
    gender     VARCHAR(20)  NOT NULL,

    email      VARCHAR(100) NOT NULL UNIQUE,
    password   TEXT         NOT NULL,

    created_at TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

-- ============================
-- Favorite Companies
-- ============================
CREATE TABLE favorite_companies
(
    id          SERIAL PRIMARY KEY,

    customer_id INTEGER NOT NULL,
    company_id  INTEGER NOT NULL,

    CONSTRAINT fk_favorite_customer
        FOREIGN KEY (customer_id)
            REFERENCES customers (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_favorite_company
        FOREIGN KEY (company_id)
            REFERENCES companies (id)
            ON DELETE CASCADE,

    CONSTRAINT uq_customer_company_favorite
        UNIQUE (customer_id, company_id)
);
