DECLARE
    column_exists NUMBER;
BEGIN
    SELECT COUNT(*) INTO column_exists
    FROM user_tab_columns
    WHERE table_name = 'EXPENSE'
    AND column_name = 'CATEGORY';
    
    IF column_exists = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE EXPENSE ADD category VARCHAR2(100)';
        DBMS_OUTPUT.PUT_LINE('Coluna category adicionada com sucesso!');
    ELSE
        DBMS_OUTPUT.PUT_LINE('Coluna category já existe na tabela EXPENSE');
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Erro ao adicionar coluna: ' || SQLERRM);
        RAISE;
END;