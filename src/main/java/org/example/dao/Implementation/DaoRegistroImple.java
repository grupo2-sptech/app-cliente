package org.example.dao.Implementation;

import com.github.britooo.looca.api.core.Looca;
import org.example.database.ConexaoMysql;
import org.example.database.ConexaoSQLServer;
import org.example.entities.Maquina;
import org.example.entities.Usuario;
import org.example.entities.component.Registro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DaoRegistroImple implements org.example.dao.DaoRegistro {

    private Connection connMysl = null;
    private Connection connSql = null;
    private Connection conn = null;
    private PreparedStatement st = null;

    Looca looca = new Looca();
    Registro registro = new Registro();

    public void inserirRegistroTempoReal(Maquina maquina) {

        Double usoCpu = Math.round(looca.getProcessador().getUso() * 100.0) / 100.0;
        Double usoRam = registro.converterGB(looca.getMemoria().getEmUso());


        try {
            st = null;
            if (connMysl == null) {
                connMysl = ConexaoMysql.getConection();
            }
            st = connMysl.prepareStatement("INSERT INTO historico_hardware (ram_ocupada, data_hora, fk_maquina) VALUES (?,now(),?);");
            st.setDouble(1, usoRam);
            st.setInt(2, maquina.getId());
            st.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao inserir registro de RAM no banco mySql: " + e.getMessage());
            connMysl = null;
        }

        try {
            st = null;
            if (connMysl == null) {
                connMysl = ConexaoMysql.getConection();
            }
            st = connMysl.prepareStatement("INSERT INTO historico_hardware (cpu_ocupada, data_hora, fk_maquina) VALUES (?,now(),?);");
            st.setDouble(1, usoCpu);
            st.setInt(2, maquina.getId());
            st.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao inserir registro de CPU no banco mySql: " + e.getMessage());
            connMysl = null;
        }

        try {
            st = null;
            if (connSql == null) {
                connSql = ConexaoSQLServer.getConection();
            }
            st = connSql.prepareStatement("INSERT INTO historico_hardware (ram_ocupada , data_hora, fk_maquina) VALUES (?, GETDATE(), ?);");
            st.setDouble(1, usoRam);
            st.setInt(2, maquina.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao inserir registro de RAM no banco SQLServer: " + e.getMessage());
            connSql = null;
        }

        try {
            st = null;
            if (connSql == null) {
                connSql = ConexaoSQLServer.getConection();
            }
            st = connSql.prepareStatement("INSERT INTO historico_hardware (cpu_ocupada , data_hora, fk_maquina) VALUES (?, GETDATE(), ?);");
            st.setDouble(1, usoCpu);
            st.setInt(2, maquina.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao inserir registro de CPU no banco SQLServer: " + e.getMessage());
            connSql = null;
        }
    }

    public void registroEntrada(Usuario usuario, Maquina maquina) {
        conn = ConexaoSQLServer.getConection();
        try {
            st = conn.prepareStatement("""
                    INSERT INTO uso_maquina (fk_funcionario, fk_maquina, hora_data_entrada) VALUES (?, ?, GETDATE());
                    """);
            st.setInt(1, usuario.getId());
            st.setInt(2, maquina.getId());
            st.execute();
        } catch (SQLException e) {
            System.out.println("Erro ao registrar entrada: " + e.getMessage());
        }
    }
}
