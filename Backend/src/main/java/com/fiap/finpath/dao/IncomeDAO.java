package com.fiap.finpath.dao;

import com.fiap.finpath.financial.Income;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IncomeDAO {
    
    /**
     * Insere uma nova receita no banco de dados
     * @param income objeto Income a ser inserido
     * @return true se inserção foi bem-sucedida, false caso contrário
     */
    public boolean insert(Income income) {
        String sql = "INSERT INTO income (id, cd_bank_transaction_id, cd_organization_id, " +
                     "cd_target_member_id, cd_target_group_id, dt_income_date, vl_income_amount, " +
                     "nm_name, nm_description, category) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, income.getId());
            stmt.setString(2, income.getBankTransactionId());
            stmt.setString(3, income.getOrganizationId());
            stmt.setString(4, income.getTargetMemberId());
            stmt.setString(5, income.getTargetGroupId());
            stmt.setLong(6, income.getIncomeDate());
            stmt.setLong(7, income.getIncomeAmount());
            stmt.setString(8, income.getName());
            stmt.setString(9, income.getDescription());
            stmt.setString(10, income.getCategory());
            
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Receita inserida com sucesso! ID: " + income.getId());
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao inserir receita: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Recupera todas as receitas do banco de dados
     * @return Lista de objetos Income
     */
    public List<Income> getAll() {
        List<Income> incomes = new ArrayList<>();
        String sql = "SELECT id, cd_bank_transaction_id, cd_organization_id, cd_target_member_id, " +
                     "cd_target_group_id, dt_income_date, vl_income_amount, nm_name, nm_description, category " +
                     "FROM income ORDER BY dt_income_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Income income = new Income(
                    rs.getString("id"),
                    rs.getString("cd_bank_transaction_id"),
                    rs.getString("cd_organization_id"),
                    rs.getString("cd_target_member_id"),
                    rs.getString("cd_target_group_id"),
                    rs.getLong("dt_income_date"),
                    rs.getLong("vl_income_amount"),
                    rs.getString("nm_name"),
                    rs.getString("nm_description"),
                    rs.getString("category")
                );
                incomes.add(income);
            }
            
            System.out.println("Total de receitas recuperadas: " + incomes.size());
            
        } catch (SQLException e) {
            System.err.println("Erro ao recuperar receitas: " + e.getMessage());
            e.printStackTrace();
        }
        
        return incomes;
    }
    
    /**
     * Recupera receitas por ID da organização
     * @param organizationId ID da organização
     * @return Lista de objetos Income
     */
    public List<Income> getByOrganization(String organizationId) {
        List<Income> incomes = new ArrayList<>();
        String sql = "SELECT id, cd_bank_transaction_id, cd_organization_id, cd_target_member_id, " +
                     "cd_target_group_id, dt_income_date, vl_income_amount, nm_name, nm_description, category " +
                     "FROM income WHERE cd_organization_id = ? ORDER BY dt_income_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, organizationId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Income income = new Income(
                    rs.getString("id"),
                    rs.getString("cd_bank_transaction_id"),
                    rs.getString("cd_organization_id"),
                    rs.getString("cd_target_member_id"),
                    rs.getString("cd_target_group_id"),
                    rs.getLong("dt_income_date"),
                    rs.getLong("vl_income_amount"),
                    rs.getString("nm_name"),
                    rs.getString("nm_description"),
                    rs.getString("category")
                );
                incomes.add(income);
            }
            
            System.out.println("Receitas da organização " + organizationId + ": " + incomes.size());
            
        } catch (SQLException e) {
            System.err.println("Erro ao recuperar receitas por organização: " + e.getMessage());
            e.printStackTrace();
        }
        
        return incomes;
    }
}



