drop table IF EXISTS posts CASCADE;

create TABLE IF NOT EXISTS posts
(
    p_id    uuid not null,
    p_post  VARCHAR(255) not null
);

