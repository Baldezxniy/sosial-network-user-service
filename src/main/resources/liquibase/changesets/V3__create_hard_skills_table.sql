--changeset Miroslav Kosiuk:subscriptions-1
CREATE TABLE schema_users.hard_skills
(
    id         bigserial primary key,
    skill_name varchar(64) not null unique
);

CREATE UNIQUE INDEX uidx_hard_skills_skill_name ON
    schema_users.hard_skills (skill_name);
