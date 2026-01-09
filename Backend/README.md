# Finpath Backend - Sistema Financeiro Colaborativo e Gamificado

## Descrição do Projeto

O Finpath é uma plataforma financeira colaborativa e gamificada desenvolvida em Java com Spring Boot, conectada ao banco de dados Oracle da FIAP. O sistema permite gerenciar despesas, receitas, membros de organizações, metas financeiras e funcionalidades de gamificação.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Spring Security** (inclui BCrypt para hash de senhas)
- **Oracle Database**
- **Maven**
- **Jakarta Validation**

## Instruções de Inicialização

### Pré-requisitos

- Java 17 ou superior
- Maven 3.6 ou superior
- Acesso à instância Oracle da FIAP

### 1. Configuração do Banco de Dados

Execute o script SQL localizado em `Database/create_tables_oracle.sql` na instância Oracle da FIAP:

```sql
-- Execute todo o conteúdo do arquivo Database/create_tables_oracle.sql
-- O script cria todas as tabelas necessárias e insere dados de teste
```

### 2. Configuração da Aplicação

As configurações do banco estão em `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL
spring.datasource.username=
spring.datasource.password=
```

### 3. Execução da Aplicação

```bash
# Navegar para o diretório Backend
cd Backend

# Compilar o projeto
mvn clean compile

# Executar a aplicação
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

### 4. Testando a Aplicação

Use os endpoints REST para testar a funcionalidade:

## Endpoints REST Disponíveis

### 📊 Despesas (Expenses) - `/api/expenses`
- `GET /api/expenses` - Listar todas as despesas
- `GET /api/expenses/{id}` - Buscar despesa por ID
- `GET /api/expenses/organization/{organizationId}` - Buscar despesas por organização
- `GET /api/expenses/member/{targetMemberId}` - Buscar despesas por membro
- `GET /api/expenses/group/{targetGroupId}` - Buscar despesas por grupo
- `GET /api/expenses/organization/{organizationId}/total` - Total de despesas por organização
- `POST /api/expenses` - Criar nova despesa
- `PUT /api/expenses/{id}` - Atualizar despesa
- `DELETE /api/expenses/{id}` - Deletar despesa

### 💰 Receitas (Incomes) - `/api/incomes`
- `GET /api/incomes` - Listar todas as receitas
- `GET /api/incomes/{id}` - Buscar receita por ID
- `GET /api/incomes/organization/{organizationId}` - Buscar receitas por organização
- `GET /api/incomes/member/{targetMemberId}` - Buscar receitas por membro
- `GET /api/incomes/group/{targetGroupId}` - Buscar receitas por grupo
- `GET /api/incomes/organization/{organizationId}/total` - Total de receitas por organização
- `POST /api/incomes` - Criar nova receita
- `PUT /api/incomes/{id}` - Atualizar receita
- `DELETE /api/incomes/{id}` - Deletar receita

### 👥 Membros (Members) - `/api/members`
- `GET /api/members` - Listar todos os membros
- `GET /api/members/{id}` - Buscar membro por ID
- `GET /api/members/organization/{organizationId}` - Buscar membros por organização
- `GET /api/members/user/{userId}` - Buscar membros por usuário
- `GET /api/members/role/{roleId}` - Buscar membros por role
- `GET /api/members/user/{userId}/organization/{organizationId}` - Buscar membro específico
- `GET /api/members/check-membership` - Verificar se usuário é membro da organização
- `POST /api/members` - Criar novo membro
- `PUT /api/members/{id}/role` - Atualizar role do membro
- `DELETE /api/members/{id}` - Deletar membro

### 🏢 Organizações (Organizations) - `/api/organizations`
- `GET /api/organizations` - Listar todas as organizações
- `GET /api/organizations/active` - Listar organizações ativas
- `GET /api/organizations/{id}` - Buscar organização por ID
- `POST /api/organizations` - Criar nova organização
- `PUT /api/organizations/{id}` - Atualizar organização
- `DELETE /api/organizations/{id}` - Deletar organização

### 🎯 Metas (Goals) - `/api/goals`
- `GET /api/goals` - Listar todas as metas
- `GET /api/goals/{id}` - Buscar meta por ID
- `GET /api/goals/organization/{organizationId}` - Buscar metas por organização
- `GET /api/goals/organization/{organizationId}/total` - Total de valor desejado por organização
- `POST /api/goals` - Criar nova meta
- `PUT /api/goals/{id}` - Atualizar meta
- `DELETE /api/goals/{id}` - Deletar meta

### 💰 Contribuições para Metas - `/api/goals-contributions`
- `GET /api/goals-contributions` - Listar todas as contribuições
- `GET /api/goals-contributions/{id}` - Buscar contribuição por ID
- `GET /api/goals-contributions/goal/{goalId}` - Buscar contribuições por meta
- `GET /api/goals-contributions/goal/{goalId}/total` - Total de contribuições por meta
- `GET /api/goals-contributions/organization/{organizationId}/total` - Total de contribuições por organização
- `POST /api/goals-contributions` - Criar nova contribuição
- `PUT /api/goals-contributions/{id}` - Atualizar contribuição
- `DELETE /api/goals-contributions/{id}` - Deletar contribuição

### 🏆 Conquistas (Achievements) - `/api/achievements`
- `GET /api/achievements` - Listar todas as conquistas
- `GET /api/achievements/{id}` - Buscar conquista por ID
- `GET /api/achievements/search?name={name}` - Buscar conquistas por nome
- `POST /api/achievements` - Criar nova conquista
- `PUT /api/achievements/{id}` - Atualizar conquista
- `DELETE /api/achievements/{id}` - Deletar conquista

### 🎮 Desafios (Challenges) - `/api/challenges`
- `GET /api/challenges` - Listar todos os desafios
- `GET /api/challenges/{id}` - Buscar desafio por ID
- `GET /api/challenges/level/{level}` - Buscar desafios por nível
- `GET /api/challenges/type/{type}` - Buscar desafios por tipo
- `GET /api/challenges/level/{level}/type/{type}` - Buscar desafios por nível e tipo
- `GET /api/challenges/achievement/{achievementId}/total-points` - Total de pontos por conquista
- `POST /api/challenges` - Criar novo desafio
- `PUT /api/challenges/{id}` - Atualizar desafio
- `DELETE /api/challenges/{id}` - Deletar desafio

### ⭐ Pontos dos Membros - `/api/member-points`
- `GET /api/member-points/member/{memberId}` - Buscar pontos de um membro
- `GET /api/member-points/leaderboard` - Ranking de membros por pontos
- `POST /api/member-points/member/{memberId}/add-points` - Adicionar pontos a um membro
- `PUT /api/member-points/member/{memberId}` - Atualizar pontos de um membro

### 🎖️ Conquistas dos Membros - `/api/member-achievements`
- `GET /api/member-achievements/member/{memberId}` - Conquistas de um membro
- `GET /api/member-achievements/member/{memberId}/count` - Total de conquistas de um membro
- `POST /api/member-achievements/award` - Atribuir conquista a um membro

### 👤 Usuários (Users) - `/api/users`
- `GET /api/users` - Listar todos os usuários
- `GET /api/users/{id}` - Buscar usuário por ID
- `GET /api/users/email/{email}` - Buscar usuário por email
- `GET /api/users/admin-token` - Obter token de admin para desenvolvimento
- `POST /api/users/register` - Registrar novo usuário (senha é hasheada com BCrypt)
- `POST /api/users/login` - Login de usuário (valida senha com BCrypt)
- `PUT /api/users/{id}` - Atualizar usuário
- `DELETE /api/users/{id}` - Deletar usuário

### 🔐 Roles - `/api/roles`
- `GET /api/roles` - Listar todos os roles
- `GET /api/roles/{id}` - Buscar role por ID
- `GET /api/roles/name/{name}` - Buscar role por nome
- `POST /api/roles` - Criar novo role
- `PUT /api/roles/{id}` - Atualizar role
- `DELETE /api/roles/{id}` - Deletar role

## Dados de Teste

### Usuário de Teste
- **ID**: `user-test-001`
- **Nome**: `Usuário Teste`
- **Email**: `teste@fiap.com.br`
- **Senha**: `hashed_password`

### Organização de Teste
- **ID**: `org-test-001`
- **Nome**: `Organização Teste`
- **Status**: `Ativa`

### Membro de Teste
- **ID**: `member-test-001`
- **Organização**: `org-test-001`
- **Usuário**: `user-test-001`
- **Role**: `role-001` (ADMIN)

### Grupo de Teste
- **ID**: `group-test-001`
- **Nome**: `Grupo Teste`
- **Organização**: `org-test-001`

### Despesa de Teste
- **ID**: `expense-test-001`
- **Valor**: `5000` (R$ 50,00)
- **Nome**: `Despesa Teste`
- **Data**: `1700000000` (Unix timestamp)

### Receita de Teste
- **ID**: `income-test-001`
- **Valor**: `10000` (R$ 100,00)
- **Nome**: `Receita Teste`
- **Data**: `1700000000` (Unix timestamp)

### Meta de Teste
- **ID**: `goal-test-001`
- **Nome**: `Viagem de Férias`
- **Valor Desejado**: `1000000` (R$ 10.000,00)
- **Contribuição**: `50000` (R$ 500,00)

## Exemplos de Uso

### Criar uma Nova Despesa

```bash
curl -X POST http://localhost:8080/api/expenses \
  -H "Content-Type: application/json" \
  -d '{
    "organizationId": "org-test-001",
    "targetMemberId": "member-test-001",
    "targetGroupId": "group-test-001",
    "bankTransactionId": "bank-tx-003",
    "expenseDate": 1700000000,
    "expenseAmount": 2500,
    "name": "Compra de Material",
    "description": "Material de escritório"
  }'
```

### Consultar Despesas por Organização

```bash
curl http://localhost:8080/api/expenses/organization/org-test-001
```

### Atualizar uma Receita

```bash
curl -X PUT http://localhost:8080/api/incomes/income-test-001 \
  -H "Content-Type: application/json" \
  -d '{
    "incomeAmount": 15000,
    "description": "Receita atualizada"
  }'
```

### Registrar um Novo Usuário

```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao.silva@example.com",
    "password": "senha123"
  }'
```

### Login de Usuário

```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "teste@fiap.com.br",
    "password": "hashed_password"
  }'
```

### Criar uma Nova Meta

```bash
curl -X POST http://localhost:8080/api/goals \
  -H "Content-Type: application/json" \
  -d '{
    "organizationId": "org-test-001",
    "dueDate": 1735689600,
    "name": "Casa Própria",
    "description": "Economizar para entrada da casa",
    "desiredAmount": 5000000
  }'
```

### Adicionar Contribuição à Meta

```bash
curl -X POST http://localhost:8080/api/goals-contributions \
  -H "Content-Type: application/json" \
  -d '{
    "goalId": "goal-test-001",
    "contributionDate": 1700000000,
    "value": 100000,
    "description": "Contribuição mensal"
  }'
```

## Regras de Negócio Implementadas

### Despesas
- Valor deve ser maior que zero
- Data não pode ser futura
- Campos obrigatórios: organização, data, valor, nome

### Receitas
- Valor deve ser maior que zero
- Data não pode ser futura
- Campos obrigatórios: organização, data, valor, nome

### Membros
- Usuário não pode ser membro da mesma organização duas vezes
- Role ID deve ser válido
- Campos obrigatórios: organização, usuário, role

### Metas
- Valor desejado deve ser maior que zero
- Data de vencimento deve ser futura
- Campos obrigatórios: organização, data, nome, valor

### Contribuições
- Valor deve ser maior que zero
- Data não pode ser futura
- Campos obrigatórios: meta, data, valor

### Usuários
- Email deve ser único
- Email deve ser válido
- Senha obrigatória
- Nome obrigatório

## Segurança

### Hash de Senhas (BCrypt)
O sistema utiliza **BCrypt** (biblioteca nativa do Spring Security) para hashear senhas antes de armazenar no banco de dados:
- Senhas são automaticamente hasheadas com BCrypt ao registrar novo usuário
- Senhas são hasheadas ao atualizar senha existente
- Login valida senha usando comparação BCrypt

### Token de Admin (Desenvolvimento)
Para desenvolvimento e testes, existe um token de admin que permite acesso a todas as rotas:
- **Endpoint**: `GET /api/users/admin-token`
- **Token**: `admin-secure-token-2024-finpath`
- **Uso**: Adicione o header `X-Admin-Token` com o valor do token nas requisições

**⚠️ IMPORTANTE**: Este token é apenas para desenvolvimento. Em produção, implemente autenticação JWT ou similar.

### Bibliotecas Utilizadas
Todas as bibliotecas utilizadas são oficiais do Spring Boot:
- `spring-boot-starter-security` - Inclui BCrypt Password Encoder
- `spring-boot-starter-web` - REST Controllers
- `spring-boot-starter-data-jpa` - Integração com banco de dados
- `spring-boot-starter-validation` - Validação de dados

**Nenhuma biblioteca externa ou JWT é necessária** para as funcionalidades de segurança básica.

## Status HTTP

- `200 OK` - Operação realizada com sucesso
- `201 Created` - Recurso criado com sucesso
- `204 No Content` - Recurso deletado com sucesso
- `400 Bad Request` - Dados inválidos
- `404 Not Found` - Recurso não encontrado
- `500 Internal Server Error` - Erro interno do servidor

## Arquitetura

O projeto segue a arquitetura em camadas:

1. **Controller Layer** - Recebe as requisições REST e valida os dados
2. **Service Layer** - Contém as regras de negócio e orquestra as operações
3. **Repository Layer** - Interface com o banco de dados usando JPA
4. **Entity Layer** - Mapeamento das tabelas do banco de dados
