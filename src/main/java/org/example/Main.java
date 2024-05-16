package org.example;

import com.github.britooo.looca.api.core.Looca;
import org.example.entities.Maquina;
import org.example.entities.Usuario;
import org.example.entities.component.Registro;
import org.example.utilities.Utilitarios;
import org.example.utilities.console.FucionalidadeConsole;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, InterruptedException {
        Registro registro = new Registro();
        Utilitarios utilitarios = new Utilitarios();
        FucionalidadeConsole fucionalidadeConsole = new FucionalidadeConsole();
        Usuario usuario = new Usuario();
        Looca looca = new Looca();
        Maquina maquina = new Maquina(
                null,
                looca.getProcessador().getId(),
                null,
                null,
                registro.converterGB(looca.getGrupoDeDiscos().getTamanhoTotal()),
                looca.getSistema().getSistemaOperacional(),
                looca.getSistema().getArquitetura()
        );

        do {
            fucionalidadeConsole.limparConsole();
            utilitarios.exibirLogo();
            if (!usuario.validarUsuario()) {
                utilitarios.senhaIncorreta();
                Thread.sleep(2000);
            } else {
                fucionalidadeConsole.limparConsole();
                utilitarios.exibirBemVindo();
                Thread.sleep(2000);
                break;
            }
        } while (true);
        maquina.monitoramento(maquina);
    }
}
