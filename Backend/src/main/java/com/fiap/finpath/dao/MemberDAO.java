package com.fiap.finpath.dao;

import com.fiap.finpath.organization.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para gerenciar operações de Membros no banco de dados
 */
public class MemberDAO {
    
    /**
     * Insere um novo membro no banco de dados
     * @param member objeto Member a ser inserido
     * @return true se inserção foi bem-sucedida, false caso contrário
     */
    public boolean insert(Member member) {
        String sql = "INSERT INTO member (id, cd_organization_id, cd_user_id, cd_role_id) " +
                     "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, member.getId());
            stmt.setString(2, member.getOrganizationId());
            stmt.setString(3, member.getUserId());
            stmt.setString(4, member.getRoleId());
            
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Membro inserido com sucesso! ID: " + member.getId());
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao inserir membro: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Recupera todos os membros do banco de dados
     * @return Lista de objetos Member
     */
    public List<Member> getAll() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT id, cd_organization_id, cd_user_id, cd_role_id FROM member";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Member member = new Member(
                    rs.getString("id"),
                    rs.getString("cd_organization_id"),
                    rs.getString("cd_user_id"),
                    rs.getString("cd_role_id")
                );
                members.add(member);
            }
            
            System.out.println("Total de membros recuperados: " + members.size());
            
        } catch (SQLException e) {
            System.err.println("Erro ao recuperar membros: " + e.getMessage());
            e.printStackTrace();
        }
        
        return members;
    }
    
    /**
     * Recupera membros por ID da organização
     * @param organizationId ID da organização
     * @return Lista de objetos Member
     */
    public List<Member> getByOrganization(String organizationId) {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT id, cd_organization_id, cd_user_id, cd_role_id " +
                     "FROM member WHERE cd_organization_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, organizationId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Member member = new Member(
                    rs.getString("id"),
                    rs.getString("cd_organization_id"),
                    rs.getString("cd_user_id"),
                    rs.getString("cd_role_id")
                );
                members.add(member);
            }
            
            System.out.println("Membros da organização " + organizationId + ": " + members.size());
            
        } catch (SQLException e) {
            System.err.println("Erro ao recuperar membros por organização: " + e.getMessage());
            e.printStackTrace();
        }
        
        return members;
    }
    
    /**
     * Busca um membro por ID
     * @param id ID do membro
     * @return objeto Member ou null se não encontrado
     */
    public Member getById(String id) {
        String sql = "SELECT id, cd_organization_id, cd_user_id, cd_role_id " +
                     "FROM member WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Member(
                    rs.getString("id"),
                    rs.getString("cd_organization_id"),
                    rs.getString("cd_user_id"),
                    rs.getString("cd_role_id")
                );
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao buscar membro por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
}



