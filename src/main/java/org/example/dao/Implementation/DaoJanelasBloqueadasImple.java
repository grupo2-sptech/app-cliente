package org.example.dao.Implementation;

import org.example.database.ConexaoMysql;
import org.example.database.ConexaoSQLServer;
import org.example.entities.JanelasBloqueadas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DaoJanelasBloqueadasImple implements org.example.dao.DaoJanelasBloqueadas {

    public List<String> buscarJanelasBloqueadasMysql(List<Integer> idCard) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;

        JanelasBloqueadas janelasBloqueadas = new JanelasBloqueadas();

        for (Integer idCardVez : idCard) {
            try {
                conn = ConexaoMysql.getConection();

                ps = conn.prepareStatement("""
                        SELECT p.titulo_processo
                        FROM processos_janelas AS p
                        JOIN card_tem_processo AS card ON p.id_processo = card.fk_processo_card
                        WHERE card.ativo = 1 AND card.fk_card = ?;
                        """);
                ps.setInt(1, idCardVez);
                rs = ps.executeQuery();
                while (rs.next()) {
                    janelasBloqueadas.addBloqueioNaLista(rs.getString("titulo_processo"));
                }
            } catch (Exception e) {
                System.out.println("Erro ao buscar janelas bloqueadas SQLSERVER: " + e.getMessage());
            }
        }
        return janelasBloqueadas.exibirLista();
    }

    public List<String> buscarJanelasBloqueadasSqlServer(List<Integer> idCard) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;

        JanelasBloqueadas janelasBloqueadas = new JanelasBloqueadas();

        for (Integer idCardVez : idCard) {
            try {
                conn = ConexaoSQLServer.getConection();

                ps = conn.prepareStatement("""
                        SELECT p.titulo_processo
                        FROM processos_janelas AS p
                        JOIN card_tem_processo AS card ON p.id_processo = card.fk_processo_card
                        WHERE card.ativo = 1 AND card.fk_card = ?;
                        """);
                ps.setInt(1, idCardVez);
                rs = ps.executeQuery();
                while (rs.next()) {
                    janelasBloqueadas.addBloqueioNaLista(rs.getString("titulo_processo"));
                }
            } catch (Exception e) {
                System.out.println("Erro ao buscar janelas bloqueadas SQLSERVER: " + e.getMessage());
            }
        }
        return janelasBloqueadas.exibirLista();
    }

    public List<Integer> buscarCadsAtivosNoSetorSql(Integer idSetor, Integer idEmpresa) {
        List<Integer> idCards = new ArrayList<>();

        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;

        try {
            conn = ConexaoSQLServer.getConection();

            ps = conn.prepareStatement("""
                    SELECT * FROM setor_tem_categoria WHERE ativo = 1 AND fk_empresa = ? AND fk_setor =?;
                    """);
            ps.setInt(1, idEmpresa);
            ps.setInt(2, idSetor);
            rs = ps.executeQuery();
            while (rs.next()) {

                idCards.add(rs.getInt("id_card"));
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar janelas bloqueadas SQLSERVER: " + e.getMessage());
        }
        return idCards;
    }
}
