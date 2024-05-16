package org.example.entities;

import com.github.britooo.looca.api.core.Looca;
import org.example.dao.DaoComponente;
import org.example.dao.DaoJanelasBloqueadas;
import org.example.dao.DaoMaquina;
import org.example.dao.DaoRegistro;
import org.example.dao.Implementation.DaoComponenteImple;
import org.example.dao.Implementation.DaoJanelasBloqueadasImple;
import org.example.dao.Implementation.DaoMaquinaImple;
import org.example.dao.Implementation.DaoRegistroImple;
import org.example.entities.component.Registro;
import org.example.utilities.Utilitarios;
import org.example.utilities.console.FucionalidadeConsole;

import java.sql.SQLException;
import java.util.*;

public class Maquina {
    private Integer id;
    private String idPorcessador;
    private String nome;
    private String modelo;
    private Double memorialTotal;
    private String sistemaOperacional;
    private Integer arquitetura;
    private Integer idSetor;


    private List<Componente> componentes;

    final Looca locca = new Looca();
    final Utilitarios utilitarios = new Utilitarios();
    final Scanner sc = new Scanner(System.in);
    Registro registro = new Registro();
    Looca looca = new Looca();

    public Double getMemorialTotal() {
        return memorialTotal;
    }

    public void setMemorialTotal(Double memorialTotal) {
        this.memorialTotal = memorialTotal;
    }

    public Maquina(Integer id, String idPorcessador, String nome, String modelo, Double memorialTotal, String sistemaOperacional, Integer arquitetura) {
        this.id = id;
        this.idPorcessador = idPorcessador;
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

    public void monitoramento(Maquina maquina) throws SQLException, InterruptedException {
        DaoMaquina daoMaquina = new DaoMaquinaImple();
        DaoComponente daoComponente = new DaoComponenteImple();
        DaoRegistro daoRegistro = new DaoRegistroImple();
        DaoJanelasBloqueadas daoJanelasBloqueadas = new DaoJanelasBloqueadasImple();
        FucionalidadeConsole fucionalidadeConsole = new FucionalidadeConsole();
        JanelasBloqueadas janelasBloqueadas = new JanelasBloqueadas();
        List<String> listaBloqueio = new ArrayList<>();


        maquina.setIdSetor(daoMaquina.validarMaquinaMysql(locca.getProcessador().getId()).getIdSetor());

        if (daoMaquina.validarMaquinaMysql(locca.getProcessador().getId()) != null) {

            while (true) {
                fucionalidadeConsole.limparConsole();
                Componente componenteRam = new Componente();
                Componente componenteCpu = new Componente();

                setComponentes(daoComponente.buscarComponenteMysql(maquina));

                List<Componente> componentesDisco = new ArrayList<>();
                for (int i = 0; i < listarComponentes().size(); i++) {
                    if (listarComponentes().get(i).getTipo().contains("Memória Ram")) {
                        componenteRam = listarComponentes().get(i);
                    } else if (listarComponentes().get(i).getTipo().contains("Processador")) {
                        componenteCpu = listarComponentes().get(i);
                    } else if (listarComponentes().get(i).getTipo().contains("Disco")) {
                        componentesDisco.add(listarComponentes().get(i));
                    }
                }

                daoRegistro.inserirRegistroTempoReal(componenteRam);
                daoRegistro.inserirRegistroTempoReal(componenteCpu);
                for (Componente componenteVez : componentesDisco) {
                    daoRegistro.inserirRegistroTempoReal(componenteVez);
                }

                utilitarios.mensagemInformativa();
                listaBloqueio = daoJanelasBloqueadas.buscarJanelasBloqueadasMysql(maquina.getIdSetor());
                janelasBloqueadas.monitorarJanelas(listaBloqueio);
                Thread.sleep(1000);
            }

        } else {
            utilitarios.centralizaTelaHorizontal(8);
            utilitarios.mensagemCadastroMaquina();
            utilitarios.centralizaTelaHorizontal(8);
            System.out.print("Insira o código aqui: ");
            Integer idCadastro = sc.nextInt();
            if (daoMaquina.buscarSetorMaquina(idCadastro) == null) {
                utilitarios.codigoIncorreto();
            } else {
                maquina.setId(idCadastro);
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
                        null,
                        looca.getProcessador().getFrequencia());

                maquina.addComponente(componenteCpu);

                Map<String, Componente> componentesDisco = new HashMap<>();

                for (int i = 0; i < looca.getGrupoDeDiscos().getDiscos().size(); i++) {
                    Componente componenteDisco = new Componente(
                            "Disco " + (i + 1),
                            registro.converterGB(looca.getGrupoDeDiscos().getVolumes().get(i).getTotal()),
                            registro.converterGB(looca.getGrupoDeDiscos().getVolumes().get(i).getDisponivel()),
                            looca.getGrupoDeDiscos().getDiscos().get(i).getModelo(),
                            null,
                            null
                    );


                    int idComponenteDisco = daoComponente.cadastrarComponenteMysql(componenteDisco, idCadastro);
                    componenteDisco.setIdComponente(idComponenteDisco);

                    // Adiciona o componente ao mapa com a chave única
                    componentesDisco.put("componenteDisco" + (i + 1), componenteDisco);

                    maquina.addComponente(componenteDisco);
                }

                daoMaquina.cadastrarMaquinaMysql(idCadastro, maquina);
                componenteRam.setIdComponente(daoComponente.cadastrarComponenteMysql(componenteRam, idCadastro));
                componenteCpu.setIdComponente(daoComponente.cadastrarComponenteMysql(componenteCpu, idCadastro));


                while (true) {
                    fucionalidadeConsole.limparConsole();
                    for (Componente componente : maquina.listarComponentes()) {
                        daoRegistro.inserirRegistroTempoReal(componente);
                    }
                    daoJanelasBloqueadas.buscarJanelasBloqueadasMysql(maquina.idSetor);
                    utilitarios.mensagemInformativa();
                    listaBloqueio = daoJanelasBloqueadas.buscarJanelasBloqueadasMysql(maquina.getIdSetor());
                    janelasBloqueadas.monitorarJanelas(listaBloqueio);
                    Thread.sleep(1000);
                }
            }
        }
    }

    public List<Componente> listarComponentes() {
        return componentes;
    }

    public void setComponentes(List<Componente> componentes) {
        this.componentes = componentes;
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