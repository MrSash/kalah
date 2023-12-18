create table if not exists game
(
    id       uuid primary key default gen_random_uuid(),
    owner_id uuid        not null,
    turn     varchar(30) not null,
    board    integer[]   not null,
    foreign key (owner_id) references player (id)
);