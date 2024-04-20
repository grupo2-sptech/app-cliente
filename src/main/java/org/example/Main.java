package org.example;


import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.janelas.Janela;
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

        limparConsole();
        utils.exibirLogo();
        utils.exibirMenu();
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
        Integer maquinaId = 0;

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

        try {


            conn = DB.getConection();
            st = conn.createStatement();
            rt = st.executeQuery(query);

            if (rt.next()) {
                utils.centralizaTelaVertical(1);
                utils.centralizaTelaHorizontal(22);
                System.out.println("Usuario Valido");
                Thread.sleep(3000);
                limparConsole();
                utils.mensagemInformativa();

                processosBloqueados.add(rt.getString("titulo_processo"));

                while (rt.next()) {
                    processosBloqueados.add(rt.getString("titulo_processo"));
                }

                rt = null;
                st = null;

                String queryMaquina = """
                        SELECT maquina_id from
                        maquina where processador_id = '%s';
                        """.formatted(looca.getProcessador().getId());

                st = conn.createStatement();
                rt = st.executeQuery(queryMaquina);

                if (rt.next()) {
                    maquinaId = rt.getInt("maquina_id");
                }


                PreparedStatement st3;
                st3 = conn.prepareStatement("""
                        update componente set tamanho_total_gb = ?,
                        tamanho_disponivel_gb = ?
                        where fk_maquina = ? and tipo_componente = disco
                        ;
                        """);
                st3.setLong(1, looca.getGrupoDeDiscos().getTamanhoTotal());
                st3.setLong(2, looca.getGrupoDeDiscos().getVolumes().get(0).getDisponivel());
                st3.setInt(3, maquinaId);

                //Encerrar processo por PID

                Looca janelaGroup = new Looca();
                FucionalidadeConsole func = new FucionalidadeConsole();

                while (true) {
                    for (Janela janela : janelaGroup.getGrupoDeJanelas().getJanelas()) {
                        for (int i = 0; i < processosBloqueados.size(); i++) {
                            if (janela.getTitulo().contains(processosBloqueados.get(i))) {
                                func.encerraProcesso(Math.toIntExact(janela.getPid()));
                                utils.centralizaTelaVertical(1);
                                utils.centralizaTelaHorizontal(8);
                                System.out.println("Processo " + janela.getTitulo() + " foi encerrado por violar as políticas de segurança da empresa!");

                                Thread.sleep(5000);
                                limparConsole();

                            }
                        }

                    }

                    PreparedStatement st1;
                    st1 = conn.prepareStatement("""
                            insert into historico_hardware (cpu_ocupada, ram_ocupada, fk_maquina, data_hora)
                            values(?, ?, ?, now());
                            """);
                    st1.setDouble(1, Math.round(looca.getProcessador().getUso() * 100.0) / 100.0);
                    st1.setDouble(2, Math.round((double) looca.getMemoria().getEmUso() / Math.pow(1024, 3) * 100.0) / 100.0);
                    st1.setInt(3, maquinaId);

                    st1.executeUpdate();
                    Thread.sleep(1000);
                    String processos = "";

                    limparConsole();
                    utils.mensagemInformativa();

                    for (int i = 0; i < processosBloqueados.size(); i++) {
                        if (i == processosBloqueados.size() - 1) {
                            processos += processosBloqueados.get(i);
                        }else {
                            processos += processosBloqueados.get(i) + ", ";
                        }

                    }
                    utils.centralizaTelaHorizontal(8);
                    System.out.println("Processos bloqueados: " + processos);

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
    
}


