-- ============================
-- expeditionDB Initialization
-- ============================

\connect "expeditionDB";

-- ============================
-- Cities
-- ============================
CREATE TABLE cities (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL UNIQUE
);

-- ============================
-- Expeditions
-- ============================
CREATE TABLE expeditions (
                             id SERIAL PRIMARY KEY,

                             departure_city_id INTEGER NOT NULL,
                             arrival_city_id   INTEGER NOT NULL,

                             date_and_time TIMESTAMPTZ NOT NULL,
                             price NUMERIC(10,2) NOT NULL,
                             duration INTEGER,
                             capacity INTEGER NOT NULL,
                             number_of_booked_seats INTEGER NOT NULL,
                             profit NUMERIC(10,2) NOT NULL,

                             company_id INTEGER NOT NULL,

                             CONSTRAINT fk_expedition_departure_city
                                 FOREIGN KEY (departure_city_id)
                                     REFERENCES cities(id)
                                     ON DELETE RESTRICT,

                             CONSTRAINT fk_expedition_arrival_city
                                 FOREIGN KEY (arrival_city_id)
                                     REFERENCES cities(id)
                                     ON DELETE RESTRICT
);

-- ============================
-- Seats
-- ============================
CREATE TABLE seats (
                       id SERIAL PRIMARY KEY,

                       expedition_id INTEGER NOT NULL,
                       seat_no INTEGER NOT NULL,
                       customer_id INTEGER,
                       status VARCHAR(50) NOT NULL,

                       CONSTRAINT fk_seat_expedition
                           FOREIGN KEY (expedition_id)
                               REFERENCES expeditions(id)
                               ON DELETE CASCADE,

                       CONSTRAINT uq_seat_per_expedition
                           UNIQUE (expedition_id, seat_no)
);

-- ============================
-- Tickets
-- ============================
CREATE TABLE tickets (
                         pnr VARCHAR(50) PRIMARY KEY,

                         seat_id INTEGER NOT NULL,
                         payment_id INTEGER NOT NULL,
                         customer_id INTEGER NOT NULL,
                         created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

                         CONSTRAINT fk_ticket_seat
                             FOREIGN KEY (seat_id)
                                 REFERENCES seats(id)
                                 ON DELETE CASCADE
);
