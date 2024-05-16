package org.example.dao.Implementation;

import org.example.database.ConexaoMysql;
import org.example.database.ConexaoSQLServer;
import org.example.entities.Maquina;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DaoMaquinaImple implements org.example.dao.DaoMaquina {

    public Maquina validarMaquinaMysql(String idProcessador) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        conn = ConexaoMysql.getConection();

        Maquina maquina = new Maquina();
        try {
            st = conn.prepareStatement("SELECT * FROM maquina WHERE processador_id = ?");
            st.setString(1, idProcessador);
            rs = st.executeQuery();
            if (rs.next()) {
                maquina.setId(rs.getInt("maquina_id"));
                maquina.setIdPorcessador(rs.getString("processador_id"));
                maquina.setSistemaOperacional(rs.getString("sistema_operacional"));
                maquina.setMemorialTotal(rs.getDouble("memoria_total_maquina"));
                maquina.setArquitetura(rs.getInt("arquitetura"));
                maquina.setIdSetor(rs.getInt("fk_setor"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao validar maquina: " + e.getMessage());
        } finally {
            // ConexaoMysql.closeStatementAndResultSet(st, rs, conn);
        }
        return maquina;
    }

    public void cadastrarMaquinaMysql(Integer id_cadastro, Maquina maquina) throws SQLException {
        Connection connMysql = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        connMysql = ConexaoMysql.getConection();
        try {
            st = connMysql.prepareStatement("""
                    UPDATE maquina SET processador_id = ?, sistema_operacional = ?, memoria_total_maquina = ?, arquitetura = ? WHERE maquina_id = ?;
                    """);
            st.setString(1, maquina.getIdPorcessador());
            st.setString(2, maquina.getSistemaOperacional());
            st.setDouble(3, maquina.getMemorialTotal());
            st.setInt(4, maquina.getArquitetura());
            st.setInt(5, id_cadastro);
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar maquina: " + e.getMessage());
        } finally {
            //  ConexaoMysql.closeStatementAndResultSet(st, rs, conn);
        }
    }

    public void cadastrarMaquinaSqlServer(Integer id_cadastro, Maquina maquina) throws SQLException {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        conn = ConexaoSQLServer.getConection();
        try {
            st = conn.prepareStatement("""
                    UPDATE maquina SET processador_id = ?, sistema_operacional = ?, memoria_total_maquina = ?, arquitetura = ? WHERE maquina_id = ?;
                    """);
            st.setString(1, maquina.getIdPorcessador());
            st.setString(2, maquina.getSistemaOperacional());
            st.setDouble(3, maquina.getMemorialTotal());
            st.setInt(4, maquina.getArquitetura());
            st.setInt(5, id_cadastro);
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar maquina: " + e.getMessage());
        } finally {
            //  ConexaoMysql.closeStatementAndResultSet(st, rs, conn);
        }
    }

    public Integer buscarSetorMaquina(Integer idMaquina) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        conn = ConexaoMysql.getConection();
        try {
            st = conn.prepareStatement("SELECT fk_setor FROM maquina WHERE maquina_id = ?;");
            st.setInt(1, idMaquina);
            rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt("fk_setor");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao capturar dados do setor: " + e.getMessage());
        } finally {
            // ConexaoMysql.closeStatementAndResultSet(st, rs, conn);
        }
        return null;
    }

    public Integer buscarSetorMaquinaSqlServer(Integer idMaquina) throws SQLException {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        conn = ConexaoSQLServer.getConection();
        try {
            st = conn.prepareStatement("SELECT fk_setor FROM maquina WHERE maquina_id = ?;");
            st.setInt(1, idMaquina);
            rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt("fk_setor");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao capturar dados do setor: " + e.getMessage());
        } finally {
            // ConexaoMysql.closeStatementAndResultSet(st, rs, conn);
        }
        return null;
    }
}