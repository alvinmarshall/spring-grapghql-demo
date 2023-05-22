CREATE TABLE IF NOT EXISTS countries
(
    country_iso3_code VARCHAR(10) NOT NULL,
    country_iso2_code VARCHAR(10),
    name              VARCHAR(100),
    phone_code        VARCHAR(50),
    country_id        VARCHAR(100),
    currency          jsonb,
    CONSTRAINT pk_countries PRIMARY KEY (country_iso3_code)
);

CREATE TABLE IF NOT EXISTS states
(
    id         VARCHAR(100) NOT NULL,
    name       VARCHAR(255),
    code       VARCHAR(50),
    country_id VARCHAR(10),
    CONSTRAINT pk_states PRIMARY KEY (id)
);

ALTER TABLE states
    ADD CONSTRAINT FK_STATES_ON_COUNTRY FOREIGN KEY (country_id) REFERENCES countries (country_iso3_code);
