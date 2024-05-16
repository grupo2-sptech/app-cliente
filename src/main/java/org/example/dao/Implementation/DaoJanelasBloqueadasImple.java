package org.example.dao.Implementation;

import org.example.database.ConexaoMysql;
import org.example.entities.JanelasBloqueadas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class DaoJanelasBloqueadasImple implements org.example.dao.DaoJanelasBloqueadas{

    public List<String> buscarJanelasBloqueadasMysql(Integer setor) {
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;

        JanelasBloqueadas janelasBloqueadas = new JanelasBloqueadas();

        try {
            conn = ConexaoMysql.getConection();

            ps = conn.prepareStatement("""
                    SELECT  pj.titulo_processo from processos_bloqueados_no_setor as pb join setor on pb.fk_setor = setor.setor_id
                    JOIN processos_janelas as pj ON pj.processo_id = pb.fk_processo
                    WHERE setor.setor_id = ?;
                    """);
            ps.setInt(1, setor);
            rs = ps.executeQuery();
            while (rs.next()) {

               janelasBloqueadas.addBloqueioNaLista(rs.getString("titulo_processo"));
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar janelas bloqueadas MYSQL: " + e.getMessage());
        }
        return janelasBloqueadas.exibirLista();
    }
}
