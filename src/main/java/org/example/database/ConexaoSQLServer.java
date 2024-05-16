package org.example.database;

import java.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoSQLServer {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=hardware_security";
    private static final String USUARIO = "aluno1";
    private static final String SENHA = "123";

    public static Connection getConection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
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