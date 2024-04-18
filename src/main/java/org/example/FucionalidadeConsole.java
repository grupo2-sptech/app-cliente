package org.example;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.ptr.IntByReference;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FucionalidadeConsole {

    static void matarProcessos() {
        Utilitarios utilitarios = new Utilitarios();
        try {
            while (true) {
                String os = System.getProperty("os.name");
                if (os.contains("Windows")) {
                    // Se for Windows, mantenha o código existente
                    limparConsole();
                    System.out.println("""
                                  
                                               Monitoramento ativo!
                                               
                            Este computador é monitorado em tempo real, incluindo o hardware, para
                            assegurar conformidade com as políticas da empresa.
                            Todas as atividades serão verificadas e, se necessário, medidas serão
                            tomadas automaticamente pelo sistema.
                                                            
                            """);
                    if (isProcessRunning("whatsApp.exe")) {
                        Thread.sleep(3000);
                        limparConsole();
                        utilitarios.centralizaTelaVertical(5);
                        utilitarios.centralizaTelaHorizontal(15);
                        System.out.println("Programa indevido localizado");
                        utilitarios.centralizaTelaHorizontal(15);
                        System.out.println("Encerrando programa indevido!");
                        Thread.sleep(3000);
                        limparConsole();
                        utilitarios.barraLoad(1);
                        new ProcessBuilder("cmd", "/c", "taskkill", "/f", "/im", "whatsApp.exe").inheritIO().start().waitFor();
                        Thread.sleep(3000);
                        limparConsole();
                        utilitarios.centralizaTelaVertical(2);
                        utilitarios.centralizaTelaHorizontal(7);
                        System.out.println("Programa indevido foi encerrado com sucesso!");
                    }
                } else {
                    // Se for Linux, execute o comando para matar o processo
                    try {
                        Process process = Runtime.getRuntime().exec("ps -e");
                        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (line.contains("whatsApp")) {
                                String[] parts = line.trim().split("\\s+");
                                String pid = parts[0];
                                Process killProcess = Runtime.getRuntime().exec("kill -9 " + pid);
                                killProcess.waitFor();
                                // Adicione aqui a lógica para notificar que o processo foi encerrado
                            }
                        }
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Thread.sleep(5000);
            }
        } catch (final Exception exception) {
            System.out.println("Erro ao Limpar o console!");
        }
    }

    static void limparConsole() {
        try {
            String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (final Exception exception) {
            System.out.println("Erro ao Limpar o console!");
        }
    }

    public static boolean isProcessRunning(String processName) throws IOException {
        Process process = new ProcessBuilder("tasklist", "/fi", "imagename eq " + processName).start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(processName)) {
                    return true;
                }
            }
        }
        return false;
    }

    //Encerrar processo por PID

//    private static void encerrarJanela(Integer pid) {
//        try {
//            Sistema sistema = new Sistema();
//            sistema.encerraProcesso(pid);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void encerraProcesso(Integer pid) {
        try {
            Runtime.getRuntime().exec("taskkill /F /PID " + pid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void bloquearSite(String site) {
        String hostsFilePath = System.getenv("SystemRoot") + "\\System32\\drivers\\etc\\hosts";
        String linhaBloqueio = "127.0.0.1 " + site;
        try (FileWriter fw = new FileWriter(hostsFilePath, true);
             PrintWriter out = new PrintWriter(fw)) {
            out.println(linhaBloqueio);
            System.out.println("Site bloqueado com sucesso: " + site);
        } catch (IOException e) {
            System.err.println("Erro ao bloquear o site: " + e.getMessage());
        }
    }


}

