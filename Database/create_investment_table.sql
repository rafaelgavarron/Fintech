BEGIN
   EXECUTE IMMEDIATE '
   CREATE TABLE INVESTMENT (
       id VARCHAR2(36) NOT NULL,
       organization_id VARCHAR2(36) NOT NULL,
       member_id VARCHAR2(36) NOT NULL,
       name VARCHAR2(255) NOT NULL,
       category VARCHAR2(100),
       amount NUMBER(19) NOT NULL,
       purchase_date NUMBER(19) NOT NULL,
       description VARCHAR2(1000),
       CONSTRAINT pk_investment PRIMARY KEY (id)
   )';
   DBMS_OUTPUT.PUT_LINE('Tabela INVESTMENT criada com sucesso.');
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE = -955 THEN
         DBMS_OUTPUT.PUT_LINE('Tabela INVESTMENT já existe. Continuando...');
      ELSE
         RAISE;
      END IF;
END;