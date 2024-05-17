package org.example.dao;

import java.util.List;

public interface DaoJanelasBloqueadas {

    List<String> buscarJanelasBloqueadasMysql(Integer setor);

    List<String> buscarJanelasBloqueadasSqlServer(Integer setor);
}
