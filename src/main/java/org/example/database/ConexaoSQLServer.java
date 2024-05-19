package org.example.database;

import org.example.utilities.Utilitarios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoSQLServer extends Conexao {

    private static final String URL = "jdbc:sqlserver://100.25.205.226:1433;database=hardware_security";
    private static final String USUARIO = "sa";
    private static final String SENHA = "urubu100";

    public static Connection getConection() {

        try {
            conn = null;
            conn = DriverManager.getConnection(URL, USUARIO, SENHA);
            return conn;
        } catch (SQLException e) {
            Utilitarios utilitarios = new Utilitarios();
            utilitarios.centralizaTelaVertical(2);
            utilitarios.centralizaTelaHorizontal(8);
            System.out.println("Erro ao conectar com o Servidor, verifique sua conexão com a");
            utilitarios.centralizaTelaHorizontal(8);
            System.out.println("internet ou entre em contato com o suporte técnico");
            System.out.println("Erro: " + e.getMessage());
            return conn;
        }
    }


}