package org.example;


import com.github.britooo.looca.api.core.Looca;
import org.example.db.DB;


import java.sql.*;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Scanner;

import static org.example.FucionalidadeConsole.limparConsole;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Utilitarios utils = new Utilitarios();
        Scanner sc = new Scanner(System.in);
        Looca looca = new Looca();
        Locale.setDefault(Locale.US);
        String pattern = "#.##";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);

//        System.out.println(looca.getMemoria().getTotal());
//        System.out.println(looca.getMemoria().getEmUso());
//        while (true){
//        System.out.println(looca.getProcessador().getUso());
//        Thread.sleep(1000);
//        }
//        System.out.println(looca.getGrupoDeDiscos().getTamanhoTotal());
//        System.out.println(looca.getGrupoDeDiscos().getVolumes().get(0).getDisponivel());


        Connection conn = null;
        Statement st = null;
        ResultSet rt = null;

        utils.centralizaTelaHorizontal(22);
        System.out.println("Email:");
        utils.centralizaTelaHorizontal(22);
        String email = sc.next();
        System.out.println();
        utils.centralizaTelaHorizontal(22);
        System.out.println("Senha:");
        utils.centralizaTelaHorizontal(22);
        String senha = sc.next();
        Integer id_usuario;
        Integer maquina_id;

        String query = """
                SELECT funcionario_id, nome_funcionario, maquina_id from
                funcionario join maquina on fk_funcionario = funcionario_id WHERE
                email_funcionario  = '%s' AND senha_acesso = '%s' OR
                login_acesso = '%s' AND senha_acesso = '%s';
                """.formatted(email, senha, email, senha);

        try {
            conn = DB.getConection();
            st = conn.createStatement();
            System.out.println(query);
            rt = st.executeQuery(query);

            if (rt.next()) {
                System.out.println("Usuario Valido");
                id_usuario = rt.getInt("funcionario_id");
                maquina_id = rt.getInt("maquina_id");
                System.out.println(id_usuario);
                System.out.println(maquina_id);
                System.out.println(query);


                while (true) {
                    PreparedStatement st1;
                    PreparedStatement st2;
                    st1 = conn.prepareStatement("update historico_hardware set ram_ocupada = ?, cpu_ocupada = ?, data_hora = now() where fk_maquina = ?;");
                    String formattedDouble = decimalFormat.format(looca.getProcessador().getUso());
                    st1.setLong(1, looca.getMemoria().getEmUso());
                    st1.setDouble(2, Double.parseDouble(formattedDouble));
                    st1.setInt(3, maquina_id);
                    System.out.println(st1);

                    st2 = conn.prepareStatement("update maquina set memoria_total_disco = ?, memoria_ocupada = ? where maquina_id = ?;");
                    st2.setLong(1, looca.getGrupoDeDiscos().getTamanhoTotal());
                    st2.setLong(2, looca.getGrupoDeDiscos().getVolumes().get(0).getDisponivel());
                    st2.setInt(3, maquina_id);
                    System.out.println(st2);
//
                    int linhasAfetadas1 = st1.executeUpdate();
                    int linhasAfetadas2 = st2.executeUpdate();

                    System.out.println("Linhas afetadas:" + linhasAfetadas1);
                    System.out.println("Linhas afetadas:" + linhasAfetadas2);
                    Thread.sleep(1500);
                }
            } else {
                System.out.println("Usiario invalido");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DB.closeStatementAndResultSet(st, rt);
            DB.cloneConection();
        }

               limparConsole();

//        do {
//            utils.exibirLogo();
//            utils.exibirMensagem();
//            utils.centralizaTelaHorizontal(22);
//            System.out.println("Email:");
//            utils.centralizaTelaHorizontal(22);
//            String email = sc.next();
//            System.out.println();
//            utils.centralizaTelaHorizontal(22);
//            System.out.println("Senha:");
//            utils.centralizaTelaHorizontal(22);
//            String senha = sc.next();
//            if (emailValido.equals(email) && senhaValida.equals(senha)) {
//                limparConsole();
//                utils.exibirLogo();
//                utils.centralizaTelaVertical(5);
//                utils.centralizaTelaHorizontal(30);
//                utils.exibirMenu();
//                Thread.sleep(3000);
//                limparConsole();
//                utils.centralizaTelaVertical(1);
//                utils.centralizaTelaHorizontal(60);
//                System.out.println("""
//
//                                                  Monitoramento ativo!
//
//                        Este computador é monitorado em tempo real, incluindo o hardware, para
//                        assegurar conformidade com as políticas da empresa.
//                        Todas as atividades serão verificadas e, se necessário, medidas serão
//                        tomadas automaticamente pelo sistema.""");
//                break;
//            } else {
//                limparConsole();
//                utils.centralizaTelaVertical(5);
//                utils.centralizaTelaHorizontal(25);
//                System.out.println("SENHA OU EMAIL INCORRETO!");
//                Thread.sleep(4000);
//                limparConsole();
//            }
//        } while (true);
//
//        FucionalidadeConsole.matarProcessos();

    }


}