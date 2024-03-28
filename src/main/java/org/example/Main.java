package org.example;



import com.github.britooo.looca.api.core.Looca;


import java.util.Scanner;

import static org.example.FucionalidadeConsole.limparConsole;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Utilitarios utils = new Utilitarios();
        Scanner sc = new Scanner(System.in);
        Looca looca = new Looca();
        System.out.println(looca.getTemperatura());

//        System.out.println(looca.getMemoria().getTotal());
//        System.out.println(looca.getMemoria().getEmUso());
//        System.out.println(looca.getGrupoDeDiscos().getTamanhoTotal());
//        System.out.println(looca.getGrupoDeDiscos().getVolumes().get(0).getDisponivel());
//        System.out.println(looca.getProcessador().getUso());

        String emailValido = "fernanda.caramico@gmail.com";
        String senhaValida = "teste123";
        limparConsole();

        do {
            utils.exibirLogo();
            utils.exibirMensagem();
            utils.centralizaTelaHorizontal(22);
            System.out.println("Email:");
            utils.centralizaTelaHorizontal(22);
            String email = sc.next();
            System.out.println();
            utils.centralizaTelaHorizontal(22);
            System.out.println("Senha:");
            utils.centralizaTelaHorizontal(22);
            String senha = sc.next();
            if (emailValido.equals(email) && senhaValida.equals(senha)) {
                limparConsole();
                utils.exibirLogo();
                utils.centralizaTelaVertical(5);
                utils.centralizaTelaHorizontal(30);
                utils.exibirMenu();
                Thread.sleep(3000);
                limparConsole();
                utils.centralizaTelaVertical(1);
                utils.centralizaTelaHorizontal(60);
                System.out.println("""
                                                  
                                                  Monitoramento ativo!
                                           
                        Este computador é monitorado em tempo real, incluindo o hardware, para
                        assegurar conformidade com as políticas da empresa.
                        Todas as atividades serão verificadas e, se necessário, medidas serão
                        tomadas automaticamente pelo sistema.""");
                break;
            } else {
                limparConsole();
                utils.centralizaTelaVertical(5);
                utils.centralizaTelaHorizontal(25);
                System.out.println("SENHA OU EMAIL INCORRETO!");
                Thread.sleep(4000);
                limparConsole();
            }
        } while (true);

        FucionalidadeConsole.matarProcessos();

    }


}