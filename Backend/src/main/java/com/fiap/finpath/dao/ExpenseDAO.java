package com.fiap.finpath.dao;

import com.fiap.finpath.financial.Expense;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para gerenciar operações de Despesas no banco de dados
 */
public class ExpenseDAO {
    
    /**
     * Insere uma nova despesa no banco de dados
     * @param expense objeto Expense a ser inserido
     * @return true se inserção foi bem-sucedida, false caso contrário
     */
    public boolean insert(Expense expense) {
        String sql = "INSERT INTO expense (id, cd_bank_transaction_id, cd_organization_id, " +
                     "cd_target_member_id, cd_target_group_id, dt_expense_date, vl_expense_amount, " +
                     "nm_name, nm_description, category) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, expense.getId());
            stmt.setString(2, expense.getBankTransactionId());
            stmt.setString(3, expense.getOrganizationId());
            stmt.setString(4, expense.getTargetMemberId());
            stmt.setString(5, expense.getTargetGroupId());
            stmt.setLong(6, expense.getExpenseDate());
            stmt.setLong(7, expense.getExpenseAmount());
            stmt.setString(8, expense.getName());
            stmt.setString(9, expense.getDescription());
            stmt.setString(10, expense.getCategory());
            
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Despesa inserida com sucesso! ID: " + expense.getId());
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao inserir despesa: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Recupera todas as despesas do banco de dados
     * @return Lista de objetos Expense
     */
    public List<Expense> getAll() {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT id, cd_bank_transaction_id, cd_organization_id, cd_target_member_id, " +
                     "cd_target_group_id, dt_expense_date, vl_expense_amount, nm_name, nm_description, category " +
                     "FROM expense ORDER BY dt_expense_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Expense expense = new Expense(
                    rs.getString("id"),
                    rs.getString("cd_bank_transaction_id"),
                    rs.getString("cd_organization_id"),
                    rs.getString("cd_target_member_id"),
                    rs.getString("cd_target_group_id"),
                    rs.getLong("dt_expense_date"),
                    rs.getLong("vl_expense_amount"),
                    rs.getString("nm_name"),
                    rs.getString("nm_description"),
                    rs.getString("category")
                );
                expenses.add(expense);
            }
            
            System.out.println("Total de despesas recuperadas: " + expenses.size());
            
        } catch (SQLException e) {
            System.err.println("Erro ao recuperar despesas: " + e.getMessage());
            e.printStackTrace();
        }
        
        return expenses;
    }
    
    /**
     * Recupera despesas por ID da organização
     * @param organizationId ID da organização
     * @return Lista de objetos Expense
     */
    public List<Expense> getByOrganization(String organizationId) {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT id, cd_bank_transaction_id, cd_organization_id, cd_target_member_id, " +
                     "cd_target_group_id, dt_expense_date, vl_expense_amount, nm_name, nm_description, category " +
                     "FROM expense WHERE cd_organization_id = ? ORDER BY dt_expense_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, organizationId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Expense expense = new Expense(
                    rs.getString("id"),
                    rs.getString("cd_bank_transaction_id"),
                    rs.getString("cd_organization_id"),
                    rs.getString("cd_target_member_id"),
                    rs.getString("cd_target_group_id"),
                    rs.getLong("dt_expense_date"),
                    rs.getLong("vl_expense_amount"),
                    rs.getString("nm_name"),
                    rs.getString("nm_description"),
                    rs.getString("category")
                );
                expenses.add(expense);
            }
            
            System.out.println("Despesas da organização " + organizationId + ": " + expenses.size());
            
        } catch (SQLException e) {
            System.err.println("Erro ao recuperar despesas por organização: " + e.getMessage());
            e.printStackTrace();
        }
        
        return expenses;
    }
}



