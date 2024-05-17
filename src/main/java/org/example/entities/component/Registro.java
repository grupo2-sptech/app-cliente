package org.example.entities.component;

import java.time.chrono.ChronoLocalDateTime;

public class Registro {
    private int idMaquina;
    private Double valorRegistro;


    public Registro(int fkMaquina, Double valorRegistro) {
        this.idMaquina = fkMaquina;
        this.valorRegistro = valorRegistro;
    }

    public Registro() {
    }

    public Double converterGB(Long numero) {
        Double numeroConvertido = Math.round(numero / (1024.0 * 1024.0 * 1024.0) * 100.0) / 100.0;
        return numeroConvertido;
    }

    public Double getValorRegistro() {
        return valorRegistro;
    }

    public void setValorRegistro(Double valorRegistro) {
        this.valorRegistro = valorRegistro;
    }

    public int getIdMaquina() {
        return idMaquina;
    }

    public void setIdMaquina(int idMaquina) {
        this.idMaquina = idMaquina;
    }

    @Override
    public String toString() {
        return """
                Registro{
                    idMaquina=%d,
                    valorRegistro=%.2f
                }
                """.formatted(idMaquina, valorRegistro);
    }



}
