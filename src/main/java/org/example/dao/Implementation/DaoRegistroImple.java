package org.example.dao.Implementation;

import com.github.britooo.looca.api.core.Looca;
import org.example.database.ConexaoMysql;
import org.example.entities.Componente;
import org.example.entities.component.Registro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DaoRegistroImple implements org.example.dao.DaoRegistro {

    private Connection connMysl = null;
    private Connection connSql = null;
    private PreparedStatement st = null;
    private ResultSet rs = null;


    Looca looca = new Looca();
    Registro registro = new Registro();

    public void inserirRegistroTempoReal(Componente componente) {

        Double usoComponente = 0.0;
        String comp = "";
        Integer contartador = 1;

        if (componente.getTipo().contains("Processador")) {
            usoComponente = Math.round(looca.getProcessador().getUso() * 100.0) / 100.0;
            comp = "cpu_ocupada";
        } else if (componente.getTipo().contains("Memória Ram")) {
            usoComponente = registro.converterGB(looca.getMemoria().getEmUso());
            comp = "ram_ocupada";
        } else if (componente.getTipo().contains("Disco " + contartador)) {
            usoComponente = registro.converterGB(looca.getGrupoDeDiscos().getDiscos().get(contartador - 1).getEscritas());
            comp = "uso_disco";
        }
        try {
            st = null;

            connMysl = ConexaoMysql.getConection();
            st = connMysl.prepareStatement("INSERT INTO historico_hardware (" + comp + ", data_hora, fk_componente) VALUES (?,now(),?)");
            st.setDouble(1, usoComponente);
            st.setInt(2, componente.getIdComponente());
            st.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao inserir registro no banco mySql: " + e.getMessage());
        }

//        try {
//            st = null;
//
//            connSql = ConexaoSQLServer.getConection();
//            st = connMysl.prepareStatement("""
//                            INSERT INTO registro (uso_componente, data_hora, fk_componente) VALUES (?,?,?);
//                    """);
//
//            st.setDouble(1, usoComponente);
//            st.setString(2, "now()");
//            st.setInt(3, componente.getIdComponente());
//            st.executeUpdate();
//        } catch (SQLException e) {
//            System.out.println("Erro ao inserir registro no banco SQLServer: " + e.getMessage());
//        }
    }

}
