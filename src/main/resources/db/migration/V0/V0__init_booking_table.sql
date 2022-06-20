create table if not exists booking
(
    id serial primary key,
    order_id text      not null,
    item_id     text      not null,
    quantity       int       not null
);

create index if not exists booking_idx on booking (order_id);