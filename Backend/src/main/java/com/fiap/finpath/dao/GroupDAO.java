package com.fiap.finpath.dao;

import com.fiap.finpath.group.Group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupDAO {
    
    /**
     * Insere um novo grupo no banco de dados
     * @param group objeto Group a ser inserido
     * @return true se inserção foi bem-sucedida, false caso contrário
     */
    public boolean insert(Group group) {
        String sql = "INSERT INTO group_table (id, cd_organization_id, nm_name, nm_description) " +
                     "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, group.getId());
            stmt.setString(2, group.getOrganizationId());
            stmt.setString(3, group.getName());
            stmt.setString(4, group.getDescription());
            
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Grupo inserido com sucesso! ID: " + group.getId());
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao inserir grupo: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Recupera todos os grupos do banco de dados
     * @return Lista de objetos Group
     */
    public List<Group> getAll() {
        List<Group> groups = new ArrayList<>();
        String sql = "SELECT id, cd_organization_id, nm_name, nm_description FROM group_table";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Group group = new Group(
                    rs.getString("id"),
                    rs.getString("cd_organization_id"),
                    rs.getString("nm_name"),
                    rs.getString("nm_description")
                );
                groups.add(group);
            }
            
            System.out.println("Total de grupos recuperados: " + groups.size());
            
        } catch (SQLException e) {
            System.err.println("Erro ao recuperar grupos: " + e.getMessage());
            e.printStackTrace();
        }
        
        return groups;
    }
    
    /**
     * Recupera grupos por ID da organização
     * @param organizationId ID da organização
     * @return Lista de objetos Group
     */
    public List<Group> getByOrganization(String organizationId) {
        List<Group> groups = new ArrayList<>();
        String sql = "SELECT id, cd_organization_id, nm_name, nm_description " +
                     "FROM group_table WHERE cd_organization_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, organizationId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Group group = new Group(
                    rs.getString("id"),
                    rs.getString("cd_organization_id"),
                    rs.getString("nm_name"),
                    rs.getString("nm_description")
                );
                groups.add(group);
            }
            
            System.out.println("Grupos da organização " + organizationId + ": " + groups.size());
            
        } catch (SQLException e) {
            System.err.println("Erro ao recuperar grupos por organização: " + e.getMessage());
            e.printStackTrace();
        }
        
        return groups;
    }
    
    /**
     * Busca um grupo por ID
     * @param id ID do grupo
     * @return objeto Group ou null se não encontrado
     */
    public Group getById(String id) {
        String sql = "SELECT id, cd_organization_id, nm_name, nm_description " +
                     "FROM group_table WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Group(
                    rs.getString("id"),
                    rs.getString("cd_organization_id"),
                    rs.getString("nm_name"),
                    rs.getString("nm_description")
                );
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao buscar grupo por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
}



