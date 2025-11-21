-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(1, 'field-1');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;
INSERT INTO produtos
(co_nome, ic_risco, ic_tipo, nu_rentabilidade, dt_criacao,de_liquidez)
VALUES(
'RendaFixa Caixa 2026',
'Baixo',
'CDB',
0.12,
'2025-11-01',
'30 Dias');

INSERT INTO produtos
(co_nome, ic_risco, ic_tipo, nu_rentabilidade, dt_criacao,de_liquidez)
VALUES(
'Poupançudo Caixa 2026',
'Baixo',
'Poupanca',
0.06,
'2025-11-01',
'1 Dia Útil');

INSERT INTO produtos
(co_nome, ic_risco, ic_tipo, nu_rentabilidade, dt_criacao, de_liquidez)
VALUES(
'Debenture Empresa XPTO',
'Medio',
'Debenture',
0.18,
'2025-11-10',
'3 meses');

INSERT INTO produtos
(co_nome, ic_risco, ic_tipo, nu_rentabilidade, dt_criacao, de_liquidez)
VALUES(
'ETF IBOV',
'Medio',
'ETF',
0.15,
'2025-11-18',
'1 Dia Útil');

INSERT INTO produtos
(co_nome, ic_risco, ic_tipo, nu_rentabilidade, dt_criacao, de_liquidez)
VALUES(
'FII Shopping',
'Alto',
'FII',
0.20,
'2025-11-08',
'1 Dia Útil');

INSERT INTO produtos
(co_nome, ic_risco, ic_tipo, nu_rentabilidade, dt_criacao, de_liquidez)
VALUES(
'RendaVariavel Fundo XPTO',
'Alto',
'Ações',
0.22,
'2025-11-08',
'1 Dia Útil');

INSERT INTO users
(no_nome, co_cpf, no_password, ic_perfil)
VALUES(
'Lucas Resende',
'12345678912',
'$2a$12$nEQJJPfeXSwP6skJaW.hmOwN6VxEhdc.UEDYKnWBNoVgahTrsvpy2',
'Admin');