create table tb_users (
  id uuid primary key default gen_random_uuid(),
  user_name varchar(20) not null,
  email varchar(160) not null,
  full_name varchar(255) not null,
  password varchar(255),
  auth_provider varchar(10) not null,
  provider_id varchar(255),
  account_non_expired boolean not null default true,
  account_non_locked boolean not null default true,
  credentials_non_expired boolean not null default true,
  enabled boolean not null default true,
  created_at timestamptz not null default now(),
  updated_at timestamptz not null default now(),

  constraint ck_users_auth_provider check (auth_provider in ('LOCAL', 'GOOGLE')),
  constraint ck_users_password_local check (auth_provider <> 'LOCAL' or password is not null),
  constraint uk_users_provider unique (auth_provider, provider_id)
);

create unique index uk_users_user_name on tb_users (lower(user_name));
create unique index uk_users_email on tb_users (lower(email));