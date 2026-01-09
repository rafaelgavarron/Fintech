package com.fiap.finpath.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Classe para gerenciar conexões com o banco de dados Oracle
public class DatabaseConnection {
    
    // Configurações de conexão com o banco de dados
    private static final String URL = "jdbc:oracle:thin:@//localhost:57668/FREEPDB1";
    private static final String USER = "system";
    private static final String PASSWORD = "YourStrongPasswordHere";
    
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC do Oracle não encontrado!", e);
        }
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexão fechada com sucesso.");
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
    
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("Conexão com o banco de dados estabelecida com sucesso!");
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Erro ao conectar com o banco de dados: " + e.getMessage());
            return false;
        }
    }
}



