create table tb_user_permissions (
     user_id uuid not null,
     permission_id uuid not null,
     constraint pk_user_permissions primary key (user_id, permission_id),
     constraint fk_user_permissions_user foreign key (user_id)
         references tb_users (id) on delete cascade,
     constraint fk_user_permissions_permission foreign key (permission_id)
         references tb_permissions (id)
);

create index idx_user_permissions_permission on tb_user_permissions (permission_id);