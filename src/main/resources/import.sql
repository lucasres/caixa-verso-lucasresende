-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(1, 'field-1');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;
INSERT INTO caixaverso.dbo.produtos
(co_id, co_nome, ic_risco, ic_tipo, nu_rentabilidade)
VALUES('4f7b6a02-d7fb-4fa0-bef4-dfeacce42ed7',
'RendaFixa Caixa 2026',
'Baixo',
'CDB',
0.12);

INSERT INTO caixaverso.dbo.produtos
(co_id, co_nome, ic_risco, ic_tipo, nu_rentabilidade)
VALUES('86db1bd2-c800-4771-9505-96083c781af0',
'Debenture Empresa XPTO',
'Medio',
'Debenture',
0.18);

INSERT INTO caixaverso.dbo.produtos
(co_id, co_nome, ic_risco, ic_tipo, nu_rentabilidade)
VALUES('6ab8a43a-0b9b-4a09-b843-8f27f26d0a97',
'RendaVariavel Fundo XPTO',
'Alto',
'Ações',
0.22);