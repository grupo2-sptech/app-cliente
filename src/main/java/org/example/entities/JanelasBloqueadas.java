package org.example.entities;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.janelas.Janela;
import org.example.utilities.Utilitarios;
import org.example.utilities.console.FucionalidadeConsole;

import java.util.ArrayList;
import java.util.List;

public class JanelasBloqueadas {

    private List<String> listaJanelasBloqueadas;

    Looca janelaGroup = new Looca();
    FucionalidadeConsole func = new FucionalidadeConsole();
    Utilitarios utilitarios = new Utilitarios();

    public JanelasBloqueadas() {
        listaJanelasBloqueadas = new ArrayList<>();
    }

    public void monitorarJanelas(List<String> listaJanelasBloqueadas) throws InterruptedException {
        for (Janela janela : janelaGroup.getGrupoDeJanelas().getJanelas()) {
            for (int i = 0; i < listaJanelasBloqueadas.size(); i++) {
                if (janela.getTitulo().contains(listaJanelasBloqueadas.get(i))) {
                    func.encerraProcesso(Math.toIntExact(janela.getPid()));
                    utilitarios.centralizaTelaVertical(1);
                    utilitarios.centralizaTelaHorizontal(8);
                    System.out.println("Processo " + janela.getTitulo() + " foi encerrado por violar as políticas de segurança da empresa!");
                    Thread.sleep(3000);
                }
            }
        }
    }

    public void addBloqueioNaLista(String nome) {
        listaJanelasBloqueadas.add(nome);
    }

    public List<String> exibirLista() {
        return listaJanelasBloqueadas;
    }

    public void setNome(List<String> nome) {
        this.listaJanelasBloqueadas = nome;
    }

    @Override
    public String toString() {
        return "JanelaBloqueada{" + "nome=" + listaJanelasBloqueadas + '}';
    }

}
