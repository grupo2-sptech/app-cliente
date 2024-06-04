package org.example.utilities.log;

import com.github.britooo.looca.api.core.Looca;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {

    Looca looca = new Looca();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    String timestamp = LocalDateTime.now().format(formatter);

    public Log() {
    }

    public void geradorLog(String mensagem, String tipoLog) throws IOException {
        // Obter o caminho do diretório onde o .jar ou .class atual está localizado
        String jarDir = getExecutionPath();

        // Caminho do diretório de logs
        Path path = Paths.get(jarDir, "logs", tipoLog);

        // Criar o diretório de logs se não existir
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        String textFileLog = "log_" + timestamp + ".txt";
        File logFile = new File(path.toString(), textFileLog);

        // Criar o arquivo de log se não existir
        if (!logFile.exists()) {
            logFile.createNewFile();
        }

        // Escrever mensagem no log
        try (FileWriter fw = new FileWriter(logFile, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(mensagem);
            bw.newLine();
        }
    }

    public String cabecalho() {
        String sistemaOperacional = looca.getSistema().getSistemaOperacional();
        String textCabecalho = "=== Hardware Security ===\n" +
                "Data de Criação: " + fomatarHora() + "\n" +
                "Sistema Operacional: " + sistemaOperacional + "\n" +
                "-----------------------------------------------------------------------";
        return textCabecalho;
    }

    public String fomatarHora() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        return timestamp;
    }


    private String getExecutionPath() {
        try {
            // Obter o caminho do diretório onde o .jar ou .class atual está localizado
            String path = Log.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            File file = new File(path);
            return file.getParent();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
