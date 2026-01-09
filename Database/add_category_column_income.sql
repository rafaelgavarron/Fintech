-- Script para adicionar coluna category na tabela INCOME
-- Execute este script no banco de dados Oracle da FIAP

-- Adicionar coluna category (se não existir)
ALTER TABLE INCOME ADD (category VARCHAR2(100));

-- Comentário na coluna
COMMENT ON COLUMN INCOME.category IS 'Categoria da receita (Salário, Freelance, Investimentos, etc.)';

