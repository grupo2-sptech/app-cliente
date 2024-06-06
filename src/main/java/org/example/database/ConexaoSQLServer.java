package org.example.database;

import org.example.utilities.Utilitarios;
import org.example.utilities.console.FucionalidadeConsole;
import org.example.utilities.log.Log;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoSQLServer extends Conexao {

    static Log logTeste = new Log();
    private static final String URL = "jdbc:sqlserver://44.213.9.204:1433;database=hardware_security2;encrypt=true;trustServerCertificate=true";
    private static final String USUARIO = "sa";
    private static final String SENHA = "urubu100";

    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConection() {

        try {
            conn = null;
            conn = DriverManager.getConnection(URL, USUARIO, SENHA);
            return conn;
        } catch (SQLException e) {
            FucionalidadeConsole.limparConsole();
            Utilitarios utilitarios = new Utilitarios();
            utilitarios.centralizaTelaVertical(2);
            utilitarios.problemaConexao();
            try {
                logTeste.geradorLog("[" + logTeste.fomatarHora() + "] Erro: " + e.getMessage(), "erro conexao banco de dados");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        }
        return conn;
    }


}