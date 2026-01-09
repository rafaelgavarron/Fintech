package com.fiap.finpath.dao;

import com.fiap.finpath.goals.Goals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GoalsDAO {
    
    /**
     * Insere uma nova meta no banco de dados
     * @param goal objeto Goals a ser inserido
     * @return true se inserção foi bem-sucedida, false caso contrário
     */
    public boolean insert(Goals goal) {
        String sql = "INSERT INTO goals (id, cd_organization_id, dt_created_at, dt_due_date, " +
                     "nm_name, nm_description, vl_desired_amount) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, goal.getId());
            stmt.setString(2, goal.getOrganizationId());
            stmt.setLong(3, goal.getCreatedAt());
            stmt.setLong(4, goal.getDueDate());
            stmt.setString(5, goal.getName());
            stmt.setString(6, goal.getDescription());
            stmt.setLong(7, goal.getDesiredAmount());
            
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Meta inserida com sucesso! ID: " + goal.getId());
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao inserir meta: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Recupera todas as metas do banco de dados
     * @return Lista de objetos Goals
     */
    public List<Goals> getAll() {
        List<Goals> goals = new ArrayList<>();
        String sql = "SELECT id, cd_organization_id, dt_created_at, dt_due_date, " +
                     "nm_name, nm_description, vl_desired_amount FROM goals ORDER BY dt_due_date";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Goals goal = new Goals(
                    rs.getString("id"),
                    rs.getString("cd_organization_id"),
                    rs.getLong("dt_created_at"),
                    rs.getLong("dt_due_date"),
                    rs.getString("nm_name"),
                    rs.getString("nm_description"),
                    rs.getLong("vl_desired_amount")
                );
                goals.add(goal);
            }
            
            System.out.println("Total de metas recuperadas: " + goals.size());
            
        } catch (SQLException e) {
            System.err.println("Erro ao recuperar metas: " + e.getMessage());
            e.printStackTrace();
        }
        
        return goals;
    }
    
    /**
     * Recupera metas por ID da organização
     * @param organizationId ID da organização
     * @return Lista de objetos Goals
     */
    public List<Goals> getByOrganization(String organizationId) {
        List<Goals> goals = new ArrayList<>();
        String sql = "SELECT id, cd_organization_id, dt_created_at, dt_due_date, " +
                     "nm_name, nm_description, vl_desired_amount " +
                     "FROM goals WHERE cd_organization_id = ? ORDER BY dt_due_date";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, organizationId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Goals goal = new Goals(
                    rs.getString("id"),
                    rs.getString("cd_organization_id"),
                    rs.getLong("dt_created_at"),
                    rs.getLong("dt_due_date"),
                    rs.getString("nm_name"),
                    rs.getString("nm_description"),
                    rs.getLong("vl_desired_amount")
                );
                goals.add(goal);
            }
            
            System.out.println("Metas da organização " + organizationId + ": " + goals.size());
            
        } catch (SQLException e) {
            System.err.println("Erro ao recuperar metas por organização: " + e.getMessage());
            e.printStackTrace();
        }
        
        return goals;
    }
}



