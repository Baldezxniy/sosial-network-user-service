--changeset Miroslav Kosiuk:subscriptions-1
CREATE TABLE schema_users.users_hard_skills
(
    user_id  BIGINT NOT NULL REFERENCES schema_users.users (id)
    ON DELETE CASCADE,
    skill_id BIGINT NOT NULL REFERENCES schema_users.hard_skills (id)
    ON DELETE CASCADE,

    primary key (user_id, skill_id)
);