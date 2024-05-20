package org.example.dao;

import java.util.List;

public interface DaoJanelasBloqueadas {

    public List<String> buscarJanelasBloqueadasMysql(List<Integer> idCard);

    public List<String> buscarJanelasBloqueadasSqlServer(List<Integer> idCard);

    public List<Integer> buscarCadsAtivosNoSetorSql(Integer idSetor, Integer idEmpresa);
}
