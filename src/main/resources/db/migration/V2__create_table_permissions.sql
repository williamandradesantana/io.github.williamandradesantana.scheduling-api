create table tb_permissions (
    id uuid primary key default gen_random_uuid(),
    name varchar(20) not null unique
);