--changeset Miroslav Kosiuk:users-1
CREATE TABLE schema_users.users
(
    id           BIGSERIAL PRIMARY KEY,

    email        varchar(128) unique not null,
    username     varchar(64) unique,

    first_name   VARCHAR(32)         NOT NULL,
    second_name  VARCHAR(32)         NOT NULL,
    last_name    VARCHAR(32)         NOT NULL,

    sex          varchar(6),

    bio          varchar(128),
    mobile_phone varchar(16) unique  not null,

    birthday_at  timestamp,

    city         varchar(64),

    avatar_url   varchar(256),

    is_deleted   BOOLEAN             NOT NULL DEFAULT false

);

CREATE UNIQUE INDEX u_idx_users_email ON schema_users.users (email);
CREATE UNIQUE INDEX u_idx_users_username ON schema_users.users (username);
CREATE UNIQUE INDEX u_idx_users_mobile_phone ON schema_users.users (mobile_phone);