create table if not exists player
(
    id   uuid primary key default gen_random_uuid(),
    name varchar(30) unique not null
);