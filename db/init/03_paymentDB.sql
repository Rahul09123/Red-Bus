-- =========================
-- paymentDB Initialization
-- =========================
\connect "paymentDB";

CREATE TABLE cards
(
    id              SERIAL PRIMARY KEY,

    card_no         VARCHAR(16)  NOT NULL,
    expiration_date VARCHAR(5)   NOT NULL,
    cvc             VARCHAR(3)   NOT NULL,

    name            VARCHAR(100) NOT NULL,
    surname         VARCHAR(100) NOT NULL,

    customer_id     INTEGER      NOT NULL,

    is_active       BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE payments
(
    id      SERIAL PRIMARY KEY,

    card_id INTEGER        NOT NULL,
    amount  NUMERIC(10, 2) NOT NULL CHECK (amount >= 0),
    date    TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_payments_card
        FOREIGN KEY (card_id)
            REFERENCES cards (id)
            ON DELETE RESTRICT
            ON UPDATE CASCADE
);
