package org.example;

import com.github.britooo.looca.api.core.Looca;
import org.example.dao.DaoMaquina;
import org.example.dao.Implementation.DaoMaquinaImple;
import org.example.entities.Maquina;
import org.example.entities.Usuario;
import org.example.entities.component.Registro;
import org.example.utilities.Utilitarios;
import org.example.utilities.console.FucionalidadeConsole;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, InterruptedException, IOException {



        Registro registro = new Registro();
        Utilitarios utilitarios = new Utilitarios();
        FucionalidadeConsole fucionalidadeConsole = new FucionalidadeConsole();
        Usuario usuario = new Usuario();
        DaoMaquina daoMaquina = new DaoMaquinaImple();
        Looca looca = new Looca();

        String mac = looca.getRede().getGrupoDeInterfaces().getInterfaces().get(looca.getRede().getGrupoDeInterfaces().getInterfaces().size() - 1).getEnderecoMac();

        for (int i = 0; i < looca.getRede().getGrupoDeInterfaces().getInterfaces().size(); i++) {
            if (looca.getRede().getGrupoDeInterfaces().getInterfaces().get(i).getNome().equals("enX0")) {
                mac = looca.getRede().getGrupoDeInterfaces().getInterfaces().get(i).getEnderecoMac();
            }

            Maquina maquina = new Maquina(
                    null,
                    looca.getProcessador().getId(),
                    mac,
                    null,
                    null,
                    registro.converterGB(looca.getGrupoDeDiscos().getTamanhoTotal()),
                    looca.getSistema().getSistemaOperacional(),
                    looca.getSistema().getArquitetura()
            );

            fucionalidadeConsole.limparConsole();
            utilitarios.exibirLogo();
            usuario = usuario.validarUsuario();
            while (true) {
                if (usuario == null) {
                    utilitarios.senhaIncorreta();
                    Thread.sleep(2000);
                    fucionalidadeConsole.limparConsole();
                    utilitarios.exibirLogo();
                    usuario = new Usuario();
                    usuario = usuario.validarUsuario();
                } else {
                    fucionalidadeConsole.limparConsole();
                    utilitarios.exibirBemVindo();
                    Thread.sleep(2000);
                    break;
                }
            }
            if (daoMaquina.validarMaquinaSqlServer(maquina, usuario) == null) {
                maquina.cadastrarMaquina(maquina, usuario);
            }

            maquina = daoMaquina.validarMaquinaSqlServer(maquina, usuario);

            registro.entradaUser(usuario, maquina);

            maquina.monitoramento(maquina, usuario);
        }
    }
}