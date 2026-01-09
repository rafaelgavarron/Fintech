-- Script SQL para criar as tabelas do projeto Fintech no Oracle
-- Executar este script na instância Oracle da FIAP

-- Tabela USER_AUTH (Usuários)
CREATE TABLE USER_AUTH (
    id VARCHAR2(36) NOT NULL,
    name VARCHAR2(255) NOT NULL,
    email VARCHAR2(255) NOT NULL UNIQUE,
    password_hash VARCHAR2(255) NOT NULL,
    verified NUMBER(1) NOT NULL,
    CONSTRAINT pk_user_auth PRIMARY KEY (id)
);

-- Tabela ROLE_AUTH (Roles)
CREATE TABLE ROLE_AUTH (
    id VARCHAR2(36) NOT NULL,
    name VARCHAR2(100) NOT NULL UNIQUE,
    description VARCHAR2(1000),
    CONSTRAINT pk_role_auth PRIMARY KEY (id)
);

-- Tabela ACTION_AUTH (Ações/Permissões)
CREATE TABLE ACTION_AUTH (
    id VARCHAR2(36) NOT NULL,
    name VARCHAR2(100) NOT NULL UNIQUE,
    description VARCHAR2(1000),
    CONSTRAINT pk_action_auth PRIMARY KEY (id)
);

-- Tabela ORGANIZATION (Organizações)
CREATE TABLE ORGANIZATION (
    id VARCHAR2(36) NOT NULL,
    name VARCHAR2(255) NOT NULL,
    is_active NUMBER(1) NOT NULL,
    created_at NUMBER(19) NOT NULL,
    trial_expire_at NUMBER(19),
    CONSTRAINT pk_organization PRIMARY KEY (id)
);

-- Tabela MEMBER (Membros)
CREATE TABLE MEMBER (
    id VARCHAR2(36) NOT NULL,
    organization_id VARCHAR2(36) NOT NULL,
    user_id VARCHAR2(36) NOT NULL,
    role_id VARCHAR2(36) NOT NULL,
    CONSTRAINT pk_member PRIMARY KEY (id)
);

-- Tabela GROUPS (Grupos)
CREATE TABLE GROUPS (
    id VARCHAR2(36) NOT NULL,
    organization_id VARCHAR2(36) NOT NULL,
    name VARCHAR2(255) NOT NULL,
    description VARCHAR2(1000),
    CONSTRAINT pk_groups PRIMARY KEY (id)
);

-- Tabela GROUP_MEMBERS (Membros dos Grupos) - Chave composta
CREATE TABLE GROUP_MEMBERS (
    member_id VARCHAR2(36) NOT NULL,
    group_id VARCHAR2(36) NOT NULL,
    CONSTRAINT pk_group_members PRIMARY KEY (member_id, group_id)
);

-- Tabela ACHIEVEMENTS (Conquistas)
CREATE TABLE ACHIEVEMENTS (
    id VARCHAR2(36) NOT NULL,
    name VARCHAR2(255) NOT NULL,
    description VARCHAR2(1000),
    icon_url VARCHAR2(500),
    CONSTRAINT pk_achievements PRIMARY KEY (id)
);

-- Tabela CHALLENGES (Desafios)
CREATE TABLE CHALLENGES (
    id VARCHAR2(36) NOT NULL,
    achievement_id VARCHAR2(36),
    level VARCHAR2(20) NOT NULL,
    name VARCHAR2(255) NOT NULL,
    description VARCHAR2(1000),
    challenge_type VARCHAR2(20) NOT NULL,
    points NUMBER(19) NOT NULL,
    CONSTRAINT pk_challenges PRIMARY KEY (id)
);

-- Tabela MEMBER_ACHIEVEMENTS (Conquistas dos Membros)
CREATE TABLE MEMBER_ACHIEVEMENTS (
    id VARCHAR2(36) NOT NULL,
    member_id VARCHAR2(36) NOT NULL,
    achievement_id VARCHAR2(36) NOT NULL,
    date NUMBER(19) NOT NULL,
    CONSTRAINT pk_member_achievements PRIMARY KEY (id)
);

-- Tabela MEMBER_POINTS (Pontos dos Membros)
CREATE TABLE MEMBER_POINTS (
    member_id VARCHAR2(36) NOT NULL,
    total_points NUMBER(19) NOT NULL,
    CONSTRAINT pk_member_points PRIMARY KEY (member_id)
);

-- Tabela GOALS (Metas)
CREATE TABLE GOALS (
    id VARCHAR2(36) NOT NULL,
    organization_id VARCHAR2(36) NOT NULL,
    created_at NUMBER(19) NOT NULL,
    due_date NUMBER(19) NOT NULL,
    name VARCHAR2(255) NOT NULL,
    description VARCHAR2(1000),
    desired_amount NUMBER(19) NOT NULL,
    CONSTRAINT pk_goals PRIMARY KEY (id)
);

-- Tabela GOALS_CONTRIBUTIONS (Contribuições para Metas)
CREATE TABLE GOALS_CONTRIBUTIONS (
    id VARCHAR2(36) NOT NULL,
    goal_id VARCHAR2(36) NOT NULL,
    contribution_date NUMBER(19) NOT NULL,
    value NUMBER(19) NOT NULL,
    description VARCHAR2(1000),
    CONSTRAINT pk_goals_contributions PRIMARY KEY (id)
);

-- Tabela EXPENSE (Despesas)
CREATE TABLE EXPENSE (
    id VARCHAR2(36) NOT NULL,
    bank_transaction_id VARCHAR2(36),
    organization_id VARCHAR2(36) NOT NULL,
    target_member_id VARCHAR2(36),
    target_group_id VARCHAR2(36),
    expense_date NUMBER(19) NOT NULL,
    expense_amount NUMBER(19) NOT NULL,
    name VARCHAR2(255) NOT NULL,
    description VARCHAR2(1000),
    CONSTRAINT pk_expense PRIMARY KEY (id)
);

-- Tabela INCOME (Receitas)
CREATE TABLE INCOME (
    id VARCHAR2(36) NOT NULL,
    bank_transaction_id VARCHAR2(36),
    organization_id VARCHAR2(36) NOT NULL,
    target_member_id VARCHAR2(36),
    target_group_id VARCHAR2(36),
    income_date NUMBER(19) NOT NULL,
    income_amount NUMBER(19) NOT NULL,
    name VARCHAR2(255) NOT NULL,
    description VARCHAR2(1000),
    CONSTRAINT pk_income PRIMARY KEY (id)
);

-- Tabela BANK_ACCOUNT (Contas Bancárias)
CREATE TABLE BANK_ACCOUNT (
    id VARCHAR2(36) NOT NULL,
    organization_id VARCHAR2(36) NOT NULL,
    member_id VARCHAR2(36) NOT NULL,
    bank_name VARCHAR2(255) NOT NULL,
    access_token VARCHAR2(500),
    refresh_token VARCHAR2(500),
    token_expire_at NUMBER(19),
    is_connected NUMBER(1) NOT NULL,
    last_sync_at NUMBER(19),
    CONSTRAINT pk_bank_account PRIMARY KEY (id)
);

-- Tabela BANK_TRANSACTION (Transações Bancárias)
CREATE TABLE BANK_TRANSACTION (
    id VARCHAR2(36) NOT NULL,
    bank_account_id VARCHAR2(36) NOT NULL,
    transaction_date NUMBER(19) NOT NULL,
    value NUMBER(19) NOT NULL,
    type VARCHAR2(20) NOT NULL,
    institution_identifier VARCHAR2(255) NOT NULL,
    name VARCHAR2(255),
    description VARCHAR2(1000),
    CONSTRAINT pk_bank_transaction PRIMARY KEY (id)
);

-- Inserir dados de teste para roles
INSERT INTO ROLE_AUTH (id, name, description) VALUES ('role-001', 'ADMIN', 'Administrador do sistema');
INSERT INTO ROLE_AUTH (id, name, description) VALUES ('role-002', 'MANAGER', 'Gerente da organização');
INSERT INTO ROLE_AUTH (id, name, description) VALUES ('role-003', 'MEMBER', 'Membro comum');

-- Inserir dados de teste para ações
INSERT INTO ACTION_AUTH (id, name, description) VALUES ('action-001', 'expense_create', 'Permissão para criar despesas');
INSERT INTO ACTION_AUTH (id, name, description) VALUES ('action-002', 'expense_read', 'Permissão para ler despesas');
INSERT INTO ACTION_AUTH (id, name, description) VALUES ('action-003', 'expense_update', 'Permissão para atualizar despesas');
INSERT INTO ACTION_AUTH (id, name, description) VALUES ('action-004', 'expense_delete', 'Permissão para deletar despesas');
INSERT INTO ACTION_AUTH (id, name, description) VALUES ('action-005', 'income_create', 'Permissão para criar receitas');
INSERT INTO ACTION_AUTH (id, name, description) VALUES ('action-006', 'member_create', 'Permissão para criar membros');

-- Inserir dados de teste para usuário
INSERT INTO USER_AUTH (id, name, email, password_hash, verified) VALUES ('user-test-001', 'Usuário Teste', 'teste@fiap.com.br', 'hashed_password', 1);

-- Inserir dados de teste para organização
INSERT INTO ORGANIZATION (id, name, is_active, created_at, trial_expire_at) VALUES ('org-test-001', 'Organização Teste', 1, 1700000000, 1735689600);

-- Inserir dados de teste para membro
INSERT INTO MEMBER (id, organization_id, user_id, role_id) VALUES ('member-test-001', 'org-test-001', 'user-test-001', 'role-001');

-- Inserir dados de teste para grupo
INSERT INTO GROUPS (id, organization_id, name, description) VALUES ('group-test-001', 'org-test-001', 'Grupo Teste', 'Grupo para testes');

-- Inserir dados de teste para membro do grupo
INSERT INTO GROUP_MEMBERS (member_id, group_id) VALUES ('member-test-001', 'group-test-001');

-- Inserir dados de teste para conquistas
INSERT INTO ACHIEVEMENTS (id, name, description, icon_url) VALUES ('achievement-001', 'Primeira Despesa', 'Você registrou sua primeira despesa', 'icon1.png');
INSERT INTO ACHIEVEMENTS (id, name, description, icon_url) VALUES ('achievement-002', 'Primeira Receita', 'Você registrou sua primeira receita', 'icon2.png');
INSERT INTO ACHIEVEMENTS (id, name, description, icon_url) VALUES ('achievement-003', 'Meta Atingida', 'Você atingiu sua primeira meta', 'icon3.png');

-- Inserir dados de teste para desafios
INSERT INTO CHALLENGES (id, achievement_id, level, name, description, challenge_type, points) VALUES ('challenge-001', 'achievement-001', 'EASY', 'Registrar Primeira Despesa', 'Registre sua primeira despesa no sistema', 'SAVINGS', 100);
INSERT INTO CHALLENGES (id, achievement_id, level, name, description, challenge_type, points) VALUES ('challenge-002', 'achievement-002', 'EASY', 'Registrar Primeira Receita', 'Registre sua primeira receita no sistema', 'EXTRA_INCOME', 100);

-- Inserir dados de teste para pontos do membro
INSERT INTO MEMBER_POINTS (member_id, total_points) VALUES ('member-test-001', 0);

-- Inserir dados de teste para meta
INSERT INTO GOALS (id, organization_id, created_at, due_date, name, description, desired_amount) VALUES ('goal-test-001', 'org-test-001', 1700000000, 1735689600, 'Viagem de Férias', 'Economizar para viagem', 1000000);

-- Inserir dados de teste para contribuição na meta
INSERT INTO GOALS_CONTRIBUTIONS (id, goal_id, contribution_date, value, description) VALUES ('contribution-001', 'goal-test-001', 1700000000, 50000, 'Contribuição inicial');

-- Inserir dados de teste para despesa
INSERT INTO EXPENSE (id, bank_transaction_id, organization_id, target_member_id, target_group_id, expense_date, expense_amount, name, description) VALUES ('expense-test-001', 'bank-tx-001', 'org-test-001', 'member-test-001', 'group-test-001', 1700000000, 5000, 'Despesa Teste', 'Despesa para testes do sistema');

-- Inserir dados de teste para receita
INSERT INTO INCOME (id, bank_transaction_id, organization_id, target_member_id, target_group_id, income_date, income_amount, name, description) VALUES ('income-test-001', 'bank-tx-002', 'org-test-001', 'member-test-001', 'group-test-001', 1700000000, 10000, 'Receita Teste', 'Receita para testes do sistema');

-- Inserir dados de teste para conta bancária
INSERT INTO BANK_ACCOUNT (id, organization_id, member_id, bank_name, access_token, is_connected, last_sync_at) VALUES ('bank-account-001', 'org-test-001', 'member-test-001', 'Sicredi', 'access-token-test', 1, 1700000000);

-- Inserir dados de teste para transação bancária
INSERT INTO BANK_TRANSACTION (id, bank_account_id, transaction_date, value, type, institution_identifier, name, description) VALUES ('bank-tx-001', 'bank-account-001', 1700000000, 5000, 'DEBIT', 'SICREDI', 'Despesa Teste', 'Despesa para testes');

-- Commit das alterações
COMMIT;

-- Verificar se as tabelas foram criadas
SELECT table_name FROM user_tables WHERE table_name IN (
    'USER_AUTH', 'ROLE_AUTH', 'ACTION_AUTH', 'ORGANIZATION', 'MEMBER',
    'GROUPS', 'GROUP_MEMBERS', 'ACHIEVEMENTS', 'CHALLENGES',
    'MEMBER_ACHIEVEMENTS', 'MEMBER_POINTS', 'GOALS', 'GOALS_CONTRIBUTIONS',
    'EXPENSE', 'INCOME', 'BANK_ACCOUNT', 'BANK_TRANSACTION'
);
