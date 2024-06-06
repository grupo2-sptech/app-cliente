package org.example.entities;

import com.github.britooo.looca.api.core.Looca;
import org.example.dao.*;
import org.example.dao.Implementation.*;
import org.example.entities.component.Registro;
import org.example.utilities.Slack;
import org.example.utilities.Utilitarios;
import org.example.utilities.console.FucionalidadeConsole;
import org.example.utilities.log.Log;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Maquina {

    Log logTeste = new Log();
    LocalDateTime currentDateTime = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");
    String formattedDateTime = currentDateTime.format(formatter);

    private Integer id;
    private String idPorcessador;
    private String mac;
    private String nome;
    private String modelo;
    private Double memorialTotal;
    private String sistemaOperacional;
    private Integer arquitetura;
    private Integer idSetor;
    private Integer idEmpresa;


    private List<Componente> componentes;

    final Looca locca = new Looca();
    final Utilitarios utilitarios = new Utilitarios();
    final Scanner sc = new Scanner(System.in);
    Registro registro = new Registro();
    Looca looca = new Looca();
    DaoAlerta daoAlerta = new DaoAlertaimple();
    DaoUsuario daoUsuario = new DaoUsuarioImple();
    Timer timer = new Timer();

    public Double getMemorialTotal() {
        return memorialTotal;
    }

    public void setMemorialTotal(Double memorialTotal) {
        this.memorialTotal = memorialTotal;
    }

    public Maquina(Integer id, String idPorcessador, String mac, String nome, String modelo, Double memorialTotal, String sistemaOperacional, Integer arquitetura) {
        this.id = id;
        this.idPorcessador = idPorcessador;
        this.mac = mac;
        this.nome = nome;
        this.modelo = modelo;
        this.memorialTotal = memorialTotal;
        this.sistemaOperacional = sistemaOperacional;
        this.arquitetura = arquitetura;
        componentes = new ArrayList<>();
    }

    public Maquina() {
        componentes = new ArrayList<>();
    }

    public void monitoramento(Maquina maquina, Usuario usuario) throws SQLException, InterruptedException {
        DaoComponente daoComponente = new DaoComponenteImple();
        DaoRegistro daoRegistro = new DaoRegistroImple();
        DaoJanelasBloqueadas daoJanelasBloqueadas = new DaoJanelasBloqueadasImple();
        FucionalidadeConsole fucionalidadeConsole = new FucionalidadeConsole();
        JanelasBloqueadas janelasBloqueadas = new JanelasBloqueadas();

        setComponentes(daoComponente.buscarComponenteSqlServer(maquina));

        Slack slack;
        slack = daoUsuario.getTokenSlack(usuario);
        List<String> listaBloqueio;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Double procentagerUsoRam = daoAlerta.buscarMediaUsoRam(maquina) / registro.converterGB(looca.getMemoria().getTotal()) * 100;
                if (procentagerUsoRam > 80) {
                    daoAlerta.inserirAlertaRam(procentagerUsoRam, maquina);
                    slack.mensagemSlack("Atenção!\nA " + maquina.getNome() + "teve um uso médio de RAM acima de 80% por 5 minutos.");
                    slack.mensagemSlack("Média de Uso: %.2f".formatted(procentagerUsoRam));
                }
            }
        }, 5 * 60 * 1000, 5 * 60 * 1000);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Double procentagerUsoCpu = daoAlerta.buscarMediaUsoCpu(maquina) * 2;
                if (procentagerUsoCpu > 70) {
                    daoAlerta.inserirAlertaCpu(procentagerUsoCpu, maquina);
                    slack.mensagemSlack("Atenção!\nA " + maquina.getNome() + "teve um uso médio de CPU acima de 70% por 2 minutos.");
                    slack.mensagemSlack("Média de Uso: %.2f".formatted(procentagerUsoCpu));
                }
            }
        }, 2 * 60 * 1000, 2 * 60 * 1000);

//        Thread registroThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    daoRegistro.inserirRegistroTempoReal(maquina);
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//
//                    }
//                }
//            }
//        });
//
        Thread monitoramentoThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    FucionalidadeConsole fucionalidadeConsole = new FucionalidadeConsole();
                    fucionalidadeConsole.limparConsole();
                    Utilitarios utilitarios = new Utilitarios();
                    utilitarios.mensagemInformativa();
                    try {
                        janelasBloqueadas.monitorarJanelas(daoJanelasBloqueadas.buscarJanelasBloqueadasSqlServer(daoJanelasBloqueadas.buscarCadsAtivosNoSetorSql(maquina.getIdSetor(), usuario.getIdEmpresa())), maquina);
                    } catch (InterruptedException e) {
                        System.out.println("Erro ao monitorar as janelas " + e);
                    }
                }
            }
        });
//
//        registroThread.start();
        monitoramentoThread.start();

        while (true) {
            daoRegistro.inserirRegistroTempoReal(maquina);
//            fucionalidadeConsole.limparConsole();
//            Utilitarios utilitarios = new Utilitarios();
//            utilitarios.mensagemInformativa();
//            janelasBloqueadas.monitorarJanelas(daoJanelasBloqueadas.buscarJanelasBloqueadasSqlServer(daoJanelasBloqueadas.buscarCadsAtivosNoSetorSql(maquina.getIdSetor(), usuario.getIdEmpresa())), maquina);
//            Thread.sleep(1000);
        }
    }

    public void cadastrarMaquina(Maquina maquina, Usuario usuario) throws SQLException, IOException {

        logTeste.geradorLog("[" + logTeste.fomatarHora() + "] Funcionario responsavel pelo cadastro: " + usuario.getNome() + " - Cargo: " + usuario.getCargo() + " - Login: " + usuario.getLogin(), "criação");
        DaoMaquina daoMaquina = new DaoMaquinaImple();
        DaoComponente daoComponente = new DaoComponenteImple();


        Integer idCadastro;

        while (true) {
            try {
                utilitarios.centralizaTelaHorizontal(8);
                utilitarios.mensagemCadastroMaquina();
                utilitarios.centralizaTelaHorizontal(8);
                System.out.print("Insira o código aqui: ");
                idCadastro = sc.nextInt();
                if (daoMaquina.buscarSetorMaquinaSqlServer(idCadastro) == null) {
                    utilitarios.codigoIncorreto();
                    logTeste.geradorLog("[" + logTeste.fomatarHora() + "] Código de cadastro invalido ID:" + idCadastro, "criação");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                utilitarios.centralizaTelaHorizontal(8);
                System.out.println("Entrada inválida. Por favor, insira um codigo válido.");
                sc.nextLine(); // Limpa a entrada inválida
            } catch (Exception e) {
                System.out.println("Erro inesperado: " + e.getMessage());



            }
        }

        maquina.setId(idCadastro);

        logTeste.geradorLog("[" + logTeste.fomatarHora() + "] Iniciando cadastro da máquina com ID: " + idCadastro, "criação");

        // Adicionando componente de memória RAM
        Componente componenteRam = new Componente(
                "Memória Ram",
                registro.converterGB(looca.getMemoria().getTotal()),
                null,
                null,
                null,
                null);
        maquina.addComponente(componenteRam);


        Componente componenteCpu = new Componente(
                "Processador",
                null,
                null,
                looca.getProcessador().getFabricante(),
                looca.getProcessador().getNome(),
                looca.getProcessador().getFrequencia());
        maquina.addComponente(componenteCpu);


        Map<String, Componente> componentesDisco = new HashMap<>();
        for (int i = 0; i < looca.getGrupoDeDiscos().getDiscos().size(); i++) {
            Componente componenteDisco = new Componente(
                    "Disco " + (i + 1),
                    registro.converterGB(looca.getGrupoDeDiscos().getVolumes().get(i).getTotal()),
                    registro.converterGB(looca.getGrupoDeDiscos().getVolumes().get(i).getDisponivel()),
                    null,
                    looca.getGrupoDeDiscos().getDiscos().get(i).getModelo(),
                    null);

            Integer idComponenteDisco = daoComponente.cadastrarComponenteSqlServer(componenteDisco, idCadastro);

            logTeste.geradorLog("[" + logTeste.fomatarHora() + "] Componente disco cadastrado no SQL Server com ID: " + idComponenteDisco, "criação");

            daoComponente.cadastrarComponenteMysql(componenteDisco, idCadastro);
            componenteDisco.setIdComponente(idComponenteDisco);
            componentesDisco.put("componenteDisco" + (i + 1), componenteDisco);
            maquina.addComponente(componenteDisco);

        }

        daoMaquina.cadastrarMaquinaSqlServer(idCadastro, maquina);
        daoMaquina.cadastrarMaquinaMysql(idCadastro, maquina);

        maquina = daoMaquina.validarMaquinaSqlServer(maquina, usuario);
        maquina.setIdSetor(maquina.getIdSetor());
        maquina.setId(maquina.getId());

        componenteRam.setIdComponente(daoComponente.cadastrarComponenteSqlServer(componenteRam, idCadastro));
        componenteCpu.setIdComponente(daoComponente.cadastrarComponenteSqlServer(componenteCpu, idCadastro));
        daoComponente.cadastrarComponenteMysql(componenteRam, idCadastro);
        daoComponente.cadastrarComponenteMysql(componenteCpu, idCadastro);

        logTeste.geradorLog("[" + logTeste.fomatarHora() + "] Máquina cadastrada com sucesso no SQL Server e MySQL: " + maquina.getNome(), "criação");
        logTeste.geradorLog("[" + logTeste.fomatarHora() + "] Componente RAM cadastrado com ID: " + componenteRam.getIdComponente(), "criação");
        logTeste.geradorLog("[" + logTeste.fomatarHora() + "] Componente CPU cadastrado com ID: " + componenteCpu.getIdComponente(), "criação");
        logTeste.geradorLog("[" + logTeste.fomatarHora() + "] Máquina cadastrada com sucesso", "criação");

    }


    public List<Componente> listarComponentes() {
        return componentes;
    }

    public void setComponentes(List<Componente> componentes) {
        this.componentes = componentes;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public void addComponente(Componente componente) {
        componentes.add(componente);
    }

    public void removeComponente(Componente componente) {
        componentes.remove(componente);
    }

    public void listarCaracteristicas() {

    }

    public Integer getIdSetor() {
        return idSetor;
    }

    public void setIdSetor(Integer idSetor) {
        this.idSetor = idSetor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdPorcessador() {
        return idPorcessador;
    }

    public void setIdPorcessador(String idPorcessador) {
        this.idPorcessador = idPorcessador;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getSistemaOperacional() {
        return sistemaOperacional;
    }

    public void setSistemaOperacional(String sistemaOperacional) {
        this.sistemaOperacional = sistemaOperacional;
    }

    public Integer getArquitetura() {
        return arquitetura;
    }

    public void setArquitetura(Integer arquitetura) {
        this.arquitetura = arquitetura;
    }

    @Override
    public String toString() {
        return
                """
                        Maquina : {
                        id : %d
                        idPorcessador : %s
                        nome : %s
                        modelo : %s
                        sistemaOperacional : %s
                        memoryTotal : %.2f
                        arquitetura : %d
                        componentes : %s
                        }
                                                """.formatted(id, idPorcessador, nome, modelo, sistemaOperacional, memorialTotal, arquitetura, componentes);
    }
}
// Path: src/main/java/org/example/entidade/Componente.java