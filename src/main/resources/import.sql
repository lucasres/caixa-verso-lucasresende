-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(1, 'field-1');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;
INSERT INTO produtos
(co_nome, ic_risco, ic_tipo, nu_rentabilidade)
VALUES(
'RendaFixa Caixa 2026',
'Baixo',
'CDB',
0.12);

INSERT INTO produtos
(co_nome, ic_risco, ic_tipo, nu_rentabilidade)
VALUES(
'Debenture Empresa XPTO',
'Medio',
'Debenture',
0.18);

INSERT INTO produtos
(co_nome, ic_risco, ic_tipo, nu_rentabilidade)
VALUES(
'RendaVariavel Fundo XPTO',
'Alto',
'Ações',
0.22);

INSERT INTO users
(no_nome, co_cpf, no_password, ic_perfil)
VALUES(
'Lucas Resende',
'12345678912',
'$2a$12$nEQJJPfeXSwP6skJaW.hmOwN6VxEhdc.UEDYKnWBNoVgahTrsvpy2',
'Admin');