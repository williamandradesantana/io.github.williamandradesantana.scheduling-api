insert into tb_users
(user_name, email, full_name, password, auth_provider, provider_id, account_non_locked, enabled)
values
    ('admin', 'admin@example.com', 'Administrador do Sistema', '{pbkdf2}cfd576caa4f14155fbf85cee12152049ebf72e54e835e5b040188bedcc82497aa55aa5b0776e1ed3', 'LOCAL', null, true, true),
    ('maria', 'maria@example.com', 'Maria Silva', '{pbkdf2}2e08b2a30168fca5ce83407cd5fcd967f8bbc2372512b22cd5bbe5ccd07d972490009f11f5e3ef54', 'LOCAL', null, true, true),
    ('joao.pro', 'joao.pro@example.com', 'Joao Souza', '{pbkdf2}d3762db029cd31c629009d12fb035e4b8ca5bd26efcea83bc6b8b9c10fcd262d7834ff1b552e8543', 'LOCAL', null, true, true),
    ('bloqueado', 'bloqueado@example.com', 'Conta Bloqueada', '{pbkdf2}ef20479e3d297cc12d666124e0c4dd89db1cd660b5ad16a49d39634823c1ebaaee39ee5602e3a4c6', 'LOCAL', null, false, true),
    ('inativo', 'inativo@example.com', 'Conta Desabilitada', '{pbkdf2}3ecd368189e557a3ab8f524724ce9e372b7afd12a97e7aa5e63979a0a6d77db2373e25a0292deab1', 'LOCAL', null, true, false),
    ('ana.google', 'ana@gmail.com', 'Ana Costa', null, 'GOOGLE', '110248495921238986420', true, true);

insert into tb_user_permissions (user_id, permission_id)
select u.id, p.id
from (
    values
    ('admin', 'ADMIN'),
    ('maria', 'CUSTOMER'),
    ('joao.pro', 'PROFESSIONAL'),
    ('joao.pro', 'CUSTOMER'),
    ('bloqueado', 'CUSTOMER'),
    ('inativo', 'CUSTOMER'),
    ('ana.google', 'CUSTOMER')
 ) as v(user_name, permission_name)
  join tb_users u on u.user_name = v.user_name
  join tb_permissions p on p.name = v.permission_name;