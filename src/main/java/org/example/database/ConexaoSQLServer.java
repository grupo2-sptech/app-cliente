package org.example.database;

import org.example.utilities.Utilitarios;

import java.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoSQLServer {
    private static final String URL = "jdbc:sqlserver://52.91.145.68:1433;database=hardware_security";
    private static final String USUARIO = "sa";
    private static final String SENHA = "urubu100";


    public static Connection getConection() {
        try {
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (SQLException e) {
            Utilitarios utilitarios = new Utilitarios();
            utilitarios.centralizaTelaVertical(2);
            utilitarios.centralizaTelaHorizontal(8);
            System.out.println("Erro ao conectar com o Servidor, verifique sua conexão com a");
            utilitarios.centralizaTelaHorizontal(8);
            System.out.println("internet ou entre em contato com o suporte técnico");
            System.out.println("Erro: " + e.getMessage());
            return null;
        }
    }

    public static void closeStatementAndResultSet(Statement st, ResultSet rt, Connection conn) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                throw new DatabaseExeption(e.getMessage());
            }
        }
        if (rt != null) {
            try {
                rt.close();
            } catch (SQLException e) {
                throw new DatabaseExeption(e.getMessage());
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                // Tratar exceção, se necessário
                e.printStackTrace();
            }
        }
    }
}