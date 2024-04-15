package org.example;


import com.github.britooo.looca.api.core.Looca;
import org.example.db.DB;


import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;

import static org.example.FucionalidadeConsole.limparConsole;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Utilitarios utils = new Utilitarios();
        Scanner sc = new Scanner(System.in);
        Looca looca = new Looca();
        Locale.setDefault(Locale.US);
        String pattern = "#.##";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);

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
        Integer id_setor;
        Integer idProcessador = 0;
        Integer idRam = 0;
        Integer idDisco = 0;

        List<String> processosBloqueados = new ArrayList();

        String query = """
                SELECT funcionario_id, nome_funcionario, setor.setor_id, pj.titulo_processo from
                funcionario
                    JOIN setor ON setor.setor_id = funcionario.fk_setor
                    JOIN processos_bloqueados_no_setor as pb ON pb.fk_setor = setor.setor_id
                    JOIN processos_janelas as pj ON pj.processo_id = pb.fk_processo
                    WHERE email_funcionario  = '%s' AND senha_acesso = '%s' OR
                    login_acesso = '%s' AND senha_acesso = '%s';
                """.formatted(email, senha, email, senha);

        System.out.println(query);

        try {


            conn = DB.getConection();
            st = conn.createStatement();
            rt = st.executeQuery(query);

            if (rt.next()) {
                System.out.println("Usuario Valido");
                id_setor = rt.getInt("setor_id");

                processosBloqueados.add(Arrays.toString(rt.getString("titulo_processo").split(",")));

                while (rt.next()) {
                    processosBloqueados.add(Arrays.toString(rt.getString("titulo_processo").split(",")));
                }

                rt = null;
                st = null;

                String queryMaquina = """
                        SELECT fk_ram, fk_processador, fk_disco from
                        maquina where processador_id = '%s';
                        """.formatted(looca.getProcessador().getId());

                System.out.println(queryMaquina);
                st = conn.createStatement();
                rt = st.executeQuery(queryMaquina);

                if (rt.next()) {
                    idProcessador = rt.getInt("fk_processador");
                    idRam = rt.getInt("fk_ram");
                    idDisco = rt.getInt("fk_disco");
                }


                PreparedStatement st3;
                st3 = conn.prepareStatement("update disco set tamanho_total = ?, tamanho_disponivel = ? where id_processador = ?;");
                st3.setLong(1, looca.getGrupoDeDiscos().getTamanhoTotal());
                st3.setLong(2, looca.getGrupoDeDiscos().getVolumes().get(0).getDisponivel());
                st3.setInt(3, idDisco);
                System.out.println(st3);


                while (true) {
                    PreparedStatement st1;
                    PreparedStatement st2;

                    st1 = conn.prepareStatement("update processador set uso_processador = ?, hora_data_processador = now() where id_processador = ?;");
                    st1.setDouble(1, Math.round(looca.getProcessador().getUso() * 100.0) / 100.0);
                    st1.setInt(2, idProcessador);
                    System.out.println(st1);

                    st2 = conn.prepareStatement("update memoria_ram set uso_ram_gb = ?, id_memoria_ram = ?;");
                    st2.setDouble(1, Math.round((double) looca.getMemoria().getEmUso() / Math.pow(1024, 3) * 100.0) / 100.0);
                    st2.setInt(2, idRam);
                    System.out.println(st2);

                    int linhasAfetadas1 = st1.executeUpdate();
                    int linhasAfetadas2 = st2.executeUpdate();

                    System.out.println("Linhas afetadas:" + linhasAfetadas1);
                    System.out.println("Linhas afetadas:" + linhasAfetadas2);
                    Thread.sleep(1000);
                }

            } else {
                System.out.println("Usuario invalido");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DB.closeStatementAndResultSet(st, rt);
            DB.cloneConection();
        }
    }


//               limparConsole();
//
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
//
//    }
}


