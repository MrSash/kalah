create table if not exists player
(
    id   uuid primary key,
    name varchar(30) unique not null
);