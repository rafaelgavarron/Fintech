# 📬 Como Usar a Collection do Postman

## 📥 Importar a Collection

1. Abra o Postman
2. Clique em **Import** (canto superior esquerdo)
3. Selecione o arquivo `Finpath_API.postman_collection.json`
4. A collection será importada com todos os endpoints organizados

## 🔧 Configurar Variáveis

A collection já vem com variáveis pré-configuradas, mas você pode ajustá-las:

1. Clique na collection **Finpath API - Complete Collection**
2. Vá na aba **Variables**
3. Configure as variáveis:

| Variável | Valor Padrão | Descrição |
|----------|--------------|-----------|
| `baseUrl` | `http://localhost:8080` | URL base da API |
| `organizationId` | `org-test-001` | ID da organização de teste |
| `memberId` | `member-test-001` | ID do membro de teste |
| `userId` | `user-test-001` | ID do usuário de teste |

## 🎯 Estrutura da Collection

A collection está organizada em **12 pastas** principais:

### 📊 Expenses (8 endpoints)
- GET All Expenses
- GET Expense By ID
- GET Expenses By Organization
- GET Expenses By Member
- GET Total Expenses
- POST Create Expense
- PUT Update Expense
- DELETE Expense

### 💰 Incomes (7 endpoints)
- GET All Incomes
- GET Income By ID
- GET Incomes By Organization
- GET Total Incomes
- POST Create Income
- PUT Update Income
- DELETE Income

### 👥 Members (7 endpoints)
- GET All Members
- GET Member By ID
- GET Members By Organization
- GET Check Membership
- POST Create Member
- PUT Update Member Role
- DELETE Member

### 🏢 Organizations (6 endpoints)
- GET All Organizations
- GET Active Organizations
- GET Organization By ID
- POST Create Organization
- PUT Update Organization
- DELETE Organization

### 🎯 Goals (7 endpoints)
- GET All Goals
- GET Goal By ID
- GET Goals By Organization
- GET Total Desired Amount
- POST Create Goal
- PUT Update Goal
- DELETE Goal

### 💰 Goals Contributions (6 endpoints)
- GET All Contributions
- GET Contributions By Goal
- GET Total Contributions
- POST Create Contribution
- PUT Update Contribution
- DELETE Contribution

### 🏆 Achievements (6 endpoints)
- GET All Achievements
- GET Achievement By ID
- GET Search By Name
- POST Create Achievement
- PUT Update Achievement
- DELETE Achievement

### 🎮 Challenges (7 endpoints)
- GET All Challenges
- GET Challenge By ID
- GET By Level
- GET By Type
- POST Create Challenge
- PUT Update Challenge
- DELETE Challenge

### ⭐ Member Points (3 endpoints)
- GET Member Points
- GET Leaderboard
- POST Add Points

### 🎖️ Member Achievements (3 endpoints)
- GET Member Achievements
- GET Total Count
- POST Award Achievement

### 👤 Users (7 endpoints)
- GET All Users
- GET User By ID
- GET User By Email
- POST Register User
- POST Login User
- PUT Update User
- DELETE User

### 🔐 Roles (6 endpoints)
- GET All Roles
- GET Role By ID
- GET Role By Name
- POST Create Role
- PUT Update Role
- DELETE Role

### 🏦 Bank Accounts (7 endpoints)
- GET All Bank Accounts
- GET Bank Account By ID
- GET By Organization
- POST Connect Account
- PUT Disconnect Account
- PUT Sync Transactions
- DELETE Bank Account

## 🚀 Como Testar

### 1. Inicie a API

Certifique-se de que a API está rodando:
- Com Docker: `docker-compose up`
- Com Maven: `mvn spring-boot:run`

### 2. Execute os Testes

#### Teste Básico
1. Abra a collection **Organizations**
2. Execute **Get All Organizations**
3. Deve retornar lista de organizações

#### Fluxo Completo
1. **Create Organization** - Criar organização
2. **Create User** - Criar usuário
3. **Register User** - Registrar usuário
4. **Create Member** - Adicionar membro à organização
5. **Create Expense** - Criar despesa
6. **Create Income** - Criar receita
7. **Create Goal** - Criar meta
8. **Create Contribution** - Contribuir para meta

### 3. Usar Variáveis Dinâmicas

Alguns endpoints retornam IDs que você pode usar em outros requests:

1. Execute **Create Organization**
2. Copie o `id` da resposta
3. Cole no campo `organizationId` nas variáveis da collection
4. Todos os próximos requests usarão esse ID

## ✅ Checklist de Testes

- [ ] GET All Organizations
- [ ] Create Organization
- [ ] Register User
- [ ] Login User
- [ ] Create Member
- [ ] Create Expense
- [ ] Create Income
- [ ] Create Goal
- [ ] Add Contribution to Goal
- [ ] Create Achievement
- [ ] Create Challenge
- [ ] Award Achievement to Member
- [ ] Add Points to Member
- [ ] Check Leaderboard

## 🐛 Troubleshooting

### Erro 404
- Verifique se a API está rodando
- Confirme que a `baseUrl` está correta
- Verifique se o endpoint existe

### Erro 500
- Verifique os logs da aplicação
- Confirme que o banco de dados está acessível
- Verifique se os dados enviados são válidos

### Erro de Conexão
- Verifique se a porta 8080 está disponível
- Confirme que não há firewall bloqueando
- Teste com `curl http://localhost:8080/api/organizations`
