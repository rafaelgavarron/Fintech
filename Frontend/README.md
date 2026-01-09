# Finpath Frontend - Sistema Financeiro Colaborativo e Gamificado

## 📋 Descrição do Projeto

O Finpath Frontend é uma aplicação web moderna desenvolvida em React com TypeScript que oferece uma interface intuitiva para gerenciamento financeiro pessoal e colaborativo. A aplicação permite aos usuários gerenciar despesas, receitas, investimentos, metas financeiras e organizações, tudo com uma interface elegante e responsiva em tema dark.

## 🚀 Tecnologias Utilizadas

- **React 19.1.1** - Biblioteca JavaScript para construção de interfaces
- **TypeScript 5.9.3** - Superset do JavaScript com tipagem estática
- **Vite 7.1.7** - Build tool e servidor de desenvolvimento rápido
- **React Router DOM 7.9.4** - Roteamento para aplicações React
- **Bootstrap Icons** - Biblioteca de ícones
- **CSS Modules** - Estilos modulares e isolados

## 📦 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- **Node.js** 18.0 ou superior
- **npm** 9.0 ou superior (ou **yarn** / **pnpm**)
- **Backend do Finpath** rodando em `http://localhost:8080`

## 🔧 Instalação

1. **Clone o repositório** (se ainda não tiver feito):
   ```bash
   git clone <repository-url>
   cd Fintech
   ```

2. **Navegue para o diretório Frontend**:
   ```bash
   cd Frontend
   ```

3. **Instale as dependências**:
   ```bash
   npm install
   ```
   ou
   ```bash
   yarn install
   ```

## ⚙️ Configuração

### Configuração da API

O arquivo de configuração da API está localizado em `src/config/api.ts`. Por padrão, a aplicação está configurada para se conectar ao backend em:

```
http://localhost:8080/api
```

Se necessário, você pode alterar a URL base da API neste arquivo:

```typescript
export const API_BASE_URL = 'http://localhost:8080/api';
```

### Token de Admin

O token de admin é configurado no mesmo arquivo. Certifique-se de que corresponde ao token configurado no backend:

```typescript
export const ADMIN_TOKEN = 'admin-secure-token-2024-finpath';
```

## 🏃 Executando a Aplicação

### Modo de Desenvolvimento

```bash
npm run dev
```

A aplicação estará disponível em: `http://localhost:5173`

O Vite fornece Hot Module Replacement (HMR), então as alterações no código são refletidas automaticamente no navegador.

### Build de Produção

```bash
npm run build
```

Isso gera os arquivos otimizados na pasta `dist/`.

### Preview da Build de Produção

```bash
npm run preview
```

Isso inicia um servidor local para testar a build de produção antes de fazer deploy.


## 🎯 Funcionalidades Principais

### 🔐 Autenticação
- **Login** com email e senha
- **Registro** de novos usuários
- **Recuperação de senha**
- Autenticação persistente com Context API

### 📊 Dashboard
- Visão geral financeira com cards de resumo
- Saldo atual, receitas e despesas do mês
- Gráfico de timeseries (estilo Datadog) com filtros por:
  - Data
  - Categoria
  - Conta bancária
- Visualização de despesas por categoria
- Visualização de receitas por categoria
- Lista de metas com progresso visual

### 💰 Despesas e Receitas
- **Listagem** de todas as transações
- **Criação** e **edição** de despesas e receitas
- **Categorização** com categorias pré-definidas e customizadas
- **Formatação monetária** automática em inputs
- Filtros e busca

### 📈 Investimentos
- Listagem de investimentos por organização
- Criação e edição de investimentos
- Categorias: Renda Fixa, Ações, FIIs, Criptomoedas, etc.
- Visualização do valor total investido

### 🎯 Metas Financeiras
- Criação e gerenciamento de metas
- Acompanhamento de progresso com barras visuais
- Contribuições para metas
- Visualização de progresso percentual

### 🏢 Organizações
- Criação e gerenciamento de organizações
- Troca de organização ativa
- Configurações de organização
- Lista de organizações do usuário

### 🏦 Contas Bancárias
- Listagem de contas bancárias conectadas
- Visualização de contas por organização

## 🎨 Design e UX

- **Tema Dark** moderno e elegante
- **Layout responsivo** para desktop, tablet e mobile
- **Componentes Bootstrap** customizados
- **Ícones Bootstrap Icons** para melhor UX
- **Formatação monetária** brasileira (R$)
- **Tabelas estilizadas** com tema dark consistente
- **Cards** com bordas arredondadas e sombras
- **Gráficos SVG** customizados estilo Datadog

## 🔌 Integração com Backend

O frontend se comunica com o backend através de serviços RESTful:

- **Expenses**: `/api/expenses`
- **Incomes**: `/api/incomes`
- **Investments**: `/api/investments`
- **Goals**: `/api/goals`
- **Users**: `/api/users`
- **Organizations**: `/api/organizations`
- **Members**: `/api/members`
- **Bank Accounts**: `/api/bank-accounts`

Todos os serviços estão localizados em `src/services/` e utilizam o token de admin configurado para autenticação.

## 🛠️ Scripts Disponíveis

| Script | Descrição |
|--------|-----------|
| `npm run dev` | Inicia o servidor de desenvolvimento |
| `npm run build` | Cria uma build de produção |
| `npm run preview` | Preview da build de produção |
| `npm run lint` | Executa o linter ESLint |

## 📝 Convenções de Código

- **TypeScript**: Todo o código utiliza TypeScript para type safety
- **Componentes Funcionais**: Uso de React Hooks (useState, useEffect, useContext)
- **Context API**: Autenticação e estado global gerenciados via Context
- **Services**: Lógica de comunicação com API isolada em services
- **CSS Modules**: Estilos modulares para componentes específicos
- **Formatação de Moeda**: Utilitário `currencyFormatter.ts` para formatação consistente

## 🐛 Solução de Problemas

### Porta 5173 já em uso

Se a porta padrão do Vite estiver em uso, você pode alterar no `vite.config.ts`:

```typescript
export default defineConfig({
  server: {
    port: 3000  // ou outra porta disponível
  }
})
```

### Erro de CORS

Certifique-se de que o backend está configurado para aceitar requisições de `http://localhost:5173`. Verifique o arquivo `SecurityConfig.java` no backend.

### Erro de Conexão com API

Verifique se:
1. O backend está rodando em `http://localhost:8080`
2. A URL da API está correta em `src/config/api.ts`
3. O token de admin está correto

## 📚 Recursos Adicionais

- [Documentação do React](https://react.dev/)
- [Documentação do TypeScript](https://www.typescriptlang.org/docs/)
- [Documentação do Vite](https://vite.dev/)
- [React Router DOM](https://reactrouter.com/)
- [Bootstrap Icons](https://icons.getbootstrap.com/)

## 📄 Licença

Este projeto é parte do sistema Finpath desenvolvido para a FIAP.

