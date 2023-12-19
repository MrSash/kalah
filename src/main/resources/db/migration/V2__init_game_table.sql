create table if not exists game
(
    id       uuid primary key,
    owner_id uuid          not null,
    turn     varchar(30)   not null,
    board    integer array not null,
    foreign key (owner_id) references player (id)
);