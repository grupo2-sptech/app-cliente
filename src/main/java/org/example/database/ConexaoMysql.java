package org.example.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class ConexaoMysql {

    private static Connection conn = null;

    public static Connection getConection() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hardware_security", "aluno1", "123");
            } catch (SQLException e) {
                throw new DatabaseExeption(e.getMessage());
            }
        }
        return conn;
    }

    public static void cloneConection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new DatabaseExeption(e.getMessage());
            }
        }
    }


    private static Properties loadProperties() {
        try (FileInputStream fs = new FileInputStream("db.propities")) {
            Properties properties = new Properties();
            properties.load(fs);
            return properties;
        } catch (IOException e) {
            throw new DatabaseExeption(e.getMessage());
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
