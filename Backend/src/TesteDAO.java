import com.fiap.finpath.dao.*;
import com.fiap.finpath.financial.Expense;
import com.fiap.finpath.financial.Income;
import com.fiap.finpath.goals.Goals;
import com.fiap.finpath.organization.Organization;

import java.time.Instant;
import java.util.List;

public class TesteDAO {
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("INICIANDO TESTES DE DAO - FINPATH SYSTEM");
        System.out.println("========================================\n");
        
        // Testar conexão com banco de dados
        System.out.println("=== TESTE DE CONEXÃO ===");
        if (DatabaseConnection.testConnection()) {
            System.out.println("✓ Conexão com banco de dados OK!\n");
        } else {
            System.err.println("✗ Falha na conexão com banco de dados!");
            System.err.println("Verifique suas credenciais em DatabaseConnection.java");
            return;
        }
        
        // Testar OrganizationDAO
        testarOrganizationDAO();
        
        // Testar IncomeDAO
        testarIncomeDAO();
        
        // Testar ExpenseDAO
        testarExpenseDAO();
        
        // Testar GoalsDAO
        testarGoalsDAO();
        
        System.out.println("\n========================================");
        System.out.println("TESTES CONCLUÍDOS COM SUCESSO!");
        System.out.println("========================================");
    }
    
    /**
     * Testa as operações de OrganizationDAO
     */
    private static void testarOrganizationDAO() {
        System.out.println("\n=== TESTE: OrganizationDAO ===");
        OrganizationDAO orgDAO = new OrganizationDAO();
        
        try {
            // Inserir 5 organizações
            System.out.println("\n[1] Inserindo 5 organizações...");
            long currentTime = Instant.now().getEpochSecond();
            long trialExpire = currentTime + (90 * 24 * 60 * 60); // 90 dias
            
            Organization org1 = new Organization("Finpath Tech Solutions", true, trialExpire);
            Organization org2 = new Organization("Robert", true, trialExpire);
            Organization org3 = new Organization("Família Caetano", true, trialExpire);
            Organization org4 = new Organization("Cooperativa de Crédito ABC", true, trialExpire);
            Organization org5 = new Organization("Investimentos Horizon", true, trialExpire);
            
            orgDAO.insert(org1);
            orgDAO.insert(org2);
            orgDAO.insert(org3);
            orgDAO.insert(org4);
            orgDAO.insert(org5);
            
            // Recuperar todas as organizações
            System.out.println("\n[2] Recuperando todas as organizações...");
            List<Organization> organizations = orgDAO.getAll();
            System.out.println("Total de organizações no banco: " + organizations.size());
            
            System.out.println("\nLista de organizações:");
            for (Organization org : organizations) {
                System.out.println("  - ID: " + org.getId() + " | Nome: " + org.getName() + 
                                 " | Ativa: " + org.isActive());
            }
            
            System.out.println("\n✓ OrganizationDAO testado com sucesso!");
            
        } catch (Exception e) {
            System.err.println("✗ Erro ao testar OrganizationDAO: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Testa as operações de IncomeDAO
     */
    private static void testarIncomeDAO() {
        System.out.println("\n=== TESTE: IncomeDAO ===");
        IncomeDAO incomeDAO = new IncomeDAO();
        
        try {
            // Inserir 5 receitas
            System.out.println("\n[1] Inserindo 5 receitas...");
            long currentDate = Instant.now().getEpochSecond();
            
            // IDs fictícios para teste
            String orgId = "org-test-001";
            String memberId = "member-test-001";
            String groupId = "group-test-001";
            String transactionId = "trans-test-001";
            
            Income income1 = new Income(orgId, memberId, groupId, transactionId, 
                                       currentDate, 500000L, "Salário", 
                                       "Salário mensal de outubro");
            
            Income income2 = new Income(orgId, memberId, groupId, transactionId, 
                                       currentDate, 150000L, "Freelance", 
                                       "Projeto de consultoria");
            
            Income income3 = new Income(orgId, memberId, groupId, transactionId, 
                                       currentDate, 80000L, "Investimentos", 
                                       "Rendimento de aplicações");
            
            Income income4 = new Income(orgId, memberId, groupId, transactionId, 
                                       currentDate, 30000L, "Cashback", 
                                       "Cashback do cartão de crédito");
            
            Income income5 = new Income(orgId, memberId, groupId, transactionId, 
                                       currentDate, 120000L, "Bônus", 
                                       "Bônus trimestral");
            
            incomeDAO.insert(income1);
            incomeDAO.insert(income2);
            incomeDAO.insert(income3);
            incomeDAO.insert(income4);
            incomeDAO.insert(income5);
            
            // Recuperar todas as receitas
            System.out.println("\n[2] Recuperando todas as receitas...");
            List<Income> incomes = incomeDAO.getAll();
            System.out.println("Total de receitas no banco: " + incomes.size());
            
            System.out.println("\nLista de receitas:");
            for (Income income : incomes) {
                double amount = income.getIncomeAmount() / 100.0; // Converter de centavos para reais
                System.out.println("  - ID: " + income.getId() + " | Nome: " + income.getName() + 
                                 " | Valor: R$ " + String.format("%.2f", amount));
            }
            
            System.out.println("\n✓ IncomeDAO testado com sucesso!");
            
        } catch (Exception e) {
            System.err.println("✗ Erro ao testar IncomeDAO: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Testa as operações de ExpenseDAO
     */
    private static void testarExpenseDAO() {
        System.out.println("\n=== TESTE: ExpenseDAO ===");
        ExpenseDAO expenseDAO = new ExpenseDAO();
        
        try {
            // Inserir 5 despesas
            System.out.println("\n[1] Inserindo 5 despesas...");
            long currentDate = Instant.now().getEpochSecond();
            
            // IDs fictícios para teste
            String orgId = "org-test-001";
            String memberId = "member-test-001";
            String groupId = "group-test-001";
            String transactionId = "trans-test-002";
            
            Expense expense1 = new Expense(orgId, memberId, groupId, transactionId, 
                                          currentDate, 120000L, "Aluguel", 
                                          "Aluguel mensal do apartamento");
            
            Expense expense2 = new Expense(orgId, memberId, groupId, transactionId, 
                                          currentDate, 45000L, "Supermercado", 
                                          "Compras semanais");
            
            Expense expense3 = new Expense(orgId, memberId, groupId, transactionId, 
                                          currentDate, 25000L, "Transporte", 
                                          "Gasolina e manutenção");
            
            Expense expense4 = new Expense(orgId, memberId, groupId, transactionId, 
                                          currentDate, 15000L, "Restaurantes", 
                                          "Almoços e jantares fora");
            
            Expense expense5 = new Expense(orgId, memberId, groupId, transactionId, 
                                          currentDate, 8000L, "Streaming", 
                                          "Assinaturas Netflix, Spotify");
            
            expenseDAO.insert(expense1);
            expenseDAO.insert(expense2);
            expenseDAO.insert(expense3);
            expenseDAO.insert(expense4);
            expenseDAO.insert(expense5);
            
            // Recuperar todas as despesas
            System.out.println("\n[2] Recuperando todas as despesas...");
            List<Expense> expenses = expenseDAO.getAll();
            System.out.println("Total de despesas no banco: " + expenses.size());
            
            System.out.println("\nLista de despesas:");
            for (Expense expense : expenses) {
                double amount = expense.getExpenseAmount() / 100.0; // Converter de centavos para reais
                System.out.println("  - ID: " + expense.getId() + " | Nome: " + expense.getName() + 
                                 " | Valor: R$ " + String.format("%.2f", amount));
            }
            
            System.out.println("\n✓ ExpenseDAO testado com sucesso!");
            
        } catch (Exception e) {
            System.err.println("✗ Erro ao testar ExpenseDAO: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Testa as operações de GoalsDAO
     */
    private static void testarGoalsDAO() {
        System.out.println("\n=== TESTE: GoalsDAO ===");
        GoalsDAO goalsDAO = new GoalsDAO();
        
        try {
            // Inserir 5 metas
            System.out.println("\n[1] Inserindo 5 metas...");
            long currentTime = Instant.now().getEpochSecond();
            long dueDate1 = currentTime + (30 * 24 * 60 * 60);  // 30 dias
            long dueDate2 = currentTime + (60 * 24 * 60 * 60);  // 60 dias
            long dueDate3 = currentTime + (90 * 24 * 60 * 60);  // 90 dias
            long dueDate4 = currentTime + (180 * 24 * 60 * 60); // 180 dias
            long dueDate5 = currentTime + (365 * 24 * 60 * 60); // 365 dias
            
            String orgId = "org-test-001";
            
            Goals goal1 = new Goals(orgId, dueDate1, "Fundo de Emergência", 
                                   "Reserva de emergência para 3 meses", 1500000L);
            
            Goals goal2 = new Goals(orgId, dueDate2, "Viagem de Férias", 
                                   "Viagem para o Nordeste", 500000L);
            
            Goals goal3 = new Goals(orgId, dueDate3, "Notebook Novo", 
                                   "Comprar notebook para trabalho", 350000L);
            
            Goals goal4 = new Goals(orgId, dueDate4, "Curso de Especialização", 
                                   "Investir em educação profissional", 800000L);
            
            Goals goal5 = new Goals(orgId, dueDate5, "Entrada de Imóvel", 
                                   "Juntar para entrada de apartamento", 5000000L);
            
            goalsDAO.insert(goal1);
            goalsDAO.insert(goal2);
            goalsDAO.insert(goal3);
            goalsDAO.insert(goal4);
            goalsDAO.insert(goal5);
            
            // Recuperar todas as metas
            System.out.println("\n[2] Recuperando todas as metas...");
            List<Goals> goals = goalsDAO.getAll();
            System.out.println("Total de metas no banco: " + goals.size());
            
            System.out.println("\nLista de metas:");
            for (Goals goal : goals) {
                double amount = goal.getDesiredAmount() / 100.0; // Converter de centavos para reais
                System.out.println("  - ID: " + goal.getId() + " | Nome: " + goal.getName() + 
                                 " | Valor: R$ " + String.format("%.2f", amount));
                System.out.println("    Descrição: " + goal.getDescription());
            }
            
            System.out.println("\n✓ GoalsDAO testado com sucesso!");
            
        } catch (Exception e) {
            System.err.println("✗ Erro ao testar GoalsDAO: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

