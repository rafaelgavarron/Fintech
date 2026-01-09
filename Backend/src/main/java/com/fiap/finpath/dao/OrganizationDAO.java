package com.fiap.finpath.dao;

import com.fiap.finpath.organization.Organization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrganizationDAO {
    
    /**
     * Insere uma nova organização no banco de dados
     * @param organization objeto Organization a ser inserido
     * @return true se inserção foi bem-sucedida, false caso contrário
     */
    public boolean insert(Organization organization) {
        String sql = "INSERT INTO organization (id, nm_name, fl_active, dt_created_at, dt_trial_expire_at) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, organization.getId());
            stmt.setString(2, organization.getName());
            stmt.setInt(3, organization.isActive() ? 1 : 0);
            stmt.setLong(4, organization.getCreatedAt());
            stmt.setLong(5, organization.getTrialExpireAt());
            
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Organização inserida com sucesso! ID: " + organization.getId());
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao inserir organização: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Recupera todas as organizações do banco de dados
     * @return Lista de objetos Organization
     */
    public List<Organization> getAll() {
        List<Organization> organizations = new ArrayList<>();
        String sql = "SELECT id, nm_name, fl_active, dt_created_at, dt_trial_expire_at " +
                     "FROM organization ORDER BY dt_created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Organization org = new Organization(
                    rs.getString("id"),
                    rs.getString("nm_name"),
                    rs.getInt("fl_active") == 1,
                    rs.getLong("dt_created_at"),
                    rs.getLong("dt_trial_expire_at")
                );
                organizations.add(org);
            }
            
            System.out.println("Total de organizações recuperadas: " + organizations.size());
            
        } catch (SQLException e) {
            System.err.println("Erro ao recuperar organizações: " + e.getMessage());
            e.printStackTrace();
        }
        
        return organizations;
    }
    
    /**
     * Recupera organizações ativas
     * @return Lista de objetos Organization ativos
     */
    public List<Organization> getActiveOrganizations() {
        List<Organization> organizations = new ArrayList<>();
        String sql = "SELECT id, nm_name, fl_active, dt_created_at, dt_trial_expire_at " +
                     "FROM organization WHERE fl_active = 1 ORDER BY dt_created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Organization org = new Organization(
                    rs.getString("id"),
                    rs.getString("nm_name"),
                    rs.getInt("fl_active") == 1,
                    rs.getLong("dt_created_at"),
                    rs.getLong("dt_trial_expire_at")
                );
                organizations.add(org);
            }
            
            System.out.println("Total de organizações ativas: " + organizations.size());
            
        } catch (SQLException e) {
            System.err.println("Erro ao recuperar organizações ativas: " + e.getMessage());
            e.printStackTrace();
        }
        
        return organizations;
    }
    
    /**
     * Busca uma organização por ID
     * @param id ID da organização
     * @return objeto Organization ou null se não encontrado
     */
    public Organization getById(String id) {
        String sql = "SELECT id, nm_name, fl_active, dt_created_at, dt_trial_expire_at " +
                     "FROM organization WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Organization(
                    rs.getString("id"),
                    rs.getString("nm_name"),
                    rs.getInt("fl_active") == 1,
                    rs.getLong("dt_created_at"),
                    rs.getLong("dt_trial_expire_at")
                );
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao buscar organização por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
}



