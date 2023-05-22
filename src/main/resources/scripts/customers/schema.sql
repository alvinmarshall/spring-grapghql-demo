create TABLE if not exists users
(
    id                              VARCHAR(255) NOT NULL,
    address_line1                   VARCHAR(255),
    city                            VARCHAR(100),
    email                           VARCHAR(255),
    middle_name                     VARCHAR(100),
    first_name                      VARCHAR(100),
    gender                          VARCHAR(30),
    last_name                       VARCHAR(100),
    mobile_phone                    VARCHAR(20),
    mobile_country_code             VARCHAR(15),
    state                           VARCHAR(100),
    zipcode                         VARCHAR(20),
    external_provider_id            VARCHAR(255),
    nationality                     VARCHAR(50),
    kyc_status                      VARCHAR(50),
    tier                            VARCHAR(50),
    events                          jsonb,
    dob                             date,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

create TABLE if not exists recipients
(
    id                       VARCHAR(255) NOT NULL,
    address_line1            VARCHAR(255),
    city                     VARCHAR(100),
    email                    VARCHAR(255),
    middle_name              VARCHAR(100),
    first_name               VARCHAR(100),
    last_name                VARCHAR(100),
    mobile_phone             VARCHAR(30),
    province                 VARCHAR(100),
    status                   VARCHAR(100),
    zip_code                 VARCHAR(50),
    country                  VARCHAR(50),
    occupation               VARCHAR(100),
    sender_relationship      VARCHAR(100),
    available_payout_methods jsonb,
    user_id                  VARCHAR(255),
    CONSTRAINT pk_recipients PRIMARY KEY (id)
);

alter table recipients
    add CONSTRAINT FK_RECIPIENTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

