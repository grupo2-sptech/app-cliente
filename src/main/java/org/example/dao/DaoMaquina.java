package org.example.dao;

import org.example.entities.Maquina;

import java.sql.SQLException;

public interface DaoMaquina {

    void cadastrarMaquinaMysql(Integer id_cadastro, Maquina maquina) throws SQLException;

    Integer buscarSetorMaquinaMysql(Integer idMaquina);

    Maquina validarMaquinaMysql(String idProcessador);

    void cadastrarMaquinaSqlServer(Integer id_cadastro, Maquina maquina) throws SQLException;

    Integer buscarSetorMaquinaSqlServer(Integer idMaquina) throws SQLException;

    Maquina validarMaquinaSqlServer(String idProcessador) throws SQLException;

}