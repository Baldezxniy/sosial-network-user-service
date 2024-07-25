--changeset Miroslav Kosiuk:subscriptions-1
CREATE TABLE schema_users.subscriptions
(
    creator_id    BIGINT  NOT NULL REFERENCES schema_users.users (id)
        ON DELETE CASCADE,
    subscriber_id BIGINT  NOT NULL REFERENCES schema_users.users (id)
        ON DELETE CASCADE,
    is_deleted    BOOLEAN NOT NULL DEFAULT false,
    PRIMARY KEY (creator_id, subscriber_id)
);
