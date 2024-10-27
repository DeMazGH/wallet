-- liquibase formatted sql

-- changeset demaz:1
create table if not exists wallet
(
    wallet_id uuid    not null,
    amount    numeric not null default 0
);