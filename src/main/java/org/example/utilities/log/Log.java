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


        Path path = Paths.get("C:/Users/luana/Documents/Projeto 2 semestre/app-cliente/src/main/java/org/example/utilities/log/logTeste" + tipoLog);

        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }

        String textFileLog = "log_" + timestamp + ".txt";
        File logFile = new File(path.toString(), textFileLog);

        if (!logFile.exists()) {
            logFile.createNewFile();
            cabecalho();
        }

        cabecalho();

        FileWriter fw = new FileWriter(logFile, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(cabecalho());
        bw.write(mensagem);
        bw.newLine();
        bw.close();
        fw.close();
    }

    private String cabecalho() throws IOException {
        String sistemaOperacional = looca.getSistema().getSistemaOperacional();
        String textCabecalho = "=== Hardware Security ===\n" +
                "Data de Criação: " + timestamp + "\n" +
                "Sistema Operacional: " + sistemaOperacional + "\n" +
                "====================\n";

        return textCabecalho;
    }

}
