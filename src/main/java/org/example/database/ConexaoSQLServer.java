package org.example.database;

import org.example.utilities.Utilitarios;
import org.example.utilities.console.FucionalidadeConsole;
import org.example.utilities.log.Log;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConexaoSQLServer extends Conexao {

    static Log logTeste = new Log();
    static LocalDateTime currentDateTime = LocalDateTime.now();
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");
    static String formattedDateTime = currentDateTime.format(formatter);
    private static final String URL = "jdbc:sqlserver://52.91.135.84:1433;database=hardware_security2;encrypt=true;trustServerCertificate=true";
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
                logTeste.geradorLog("[" + formattedDateTime + "] ERRO: " + e.getMessage(), "ProcessoInovação");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return conn;
    }


}