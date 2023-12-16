create table if not exists player
(
    id   varchar(36) primary key,
    name varchar(30) unique not null
)