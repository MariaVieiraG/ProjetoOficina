package com.mycompany.oficina.sistemaponto;

import com.mycompany.oficina.entidades.Funcionario;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa um registro de ponto de um funcionário, armazenando os horários
 * de entrada e saída, e possibilitando o cálculo das horas trabalhadas.
 */
public class RegistroPonto {
    // Referência ao funcionário relacionado a este registro de ponto
    private final Funcionario funcionario;

    // Data e hora em que o funcionário registrou a entrada
    private final LocalDateTime dataHoraEntrada;

    // Data e hora em que o funcionário registrou a saída; pode ser nula se ainda não tiver registrado
    private LocalDateTime dataHoraSaida;

    /**
     * Construtor que inicializa um novo registro de ponto para o funcionário.
     * Marca a entrada no momento da criação do objeto.
     *
     * @param funcionario O funcionário que está registrando o ponto.
     */
    public RegistroPonto(Funcionario funcionario) {
        this.funcionario = funcionario;
        this.dataHoraEntrada = LocalDateTime.now(); // Marca o horário atual como entrada
        this.dataHoraSaida = null; // A saída ainda não foi registrada
    }

    // Getter para a data/hora de saída
    public LocalDateTime getDataHoraSaida() {
        return dataHoraSaida;
    }

    // Getter para a data/hora de entrada
    public LocalDateTime getDataHoraEntrada() {
        return dataHoraEntrada;
    }

    // Getter para o funcionário
    public Funcionario getFuncionario() {
        return funcionario;
    }

    /**
     * Marca a hora atual como o horário de saída do ponto.
     */
    public void setDataHoraSaida() {
        this.dataHoraSaida = LocalDateTime.now();
    }

    /**
     * Calcula as horas trabalhadas entre a entrada e a saída.
     * Se a saída ainda não foi registrada, retorna 0.
     *
     * @return Horas trabalhadas em formato decimal (exemplo: 8.5 para 8h30min).
     */
    public double getHorasTrabalhadas() {
        if (dataHoraSaida == null) {
            return 0;
        }
        // Calcula a duração entre entrada e saída
        Duration duracao = Duration.between(dataHoraEntrada, dataHoraSaida);

        // Converte a duração para minutos e depois para horas com ponto flutuante
        return duracao.toMinutes() / 60.0;
    }

    /**
     * Indica se o ponto está "em aberto", ou seja,
     * se ainda não teve a saída registrada.
     *
     * @return true se o ponto está aberto, false caso contrário.
     */
    public boolean isPontoAberto() {
        return this.dataHoraSaida == null;
    }

    /**
     * Representação em string do registro, com formatação legível.
     * Mostra o nome do funcionário, entrada, saída e horas trabalhadas.
     * Caso o ponto esteja em aberto, indica isso no campo de saída.
     */
    @Override
    public String toString() {
        // Define formato de data e hora para exibição
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        // Formata a data/hora de entrada
        String entradaFormatada = formatter.format(dataHoraEntrada);

        // Formata a data/hora de saída, ou indica "PONTO EM ABERTO" se null
        String saidaFormatada = (dataHoraSaida != null) ? formatter.format(dataHoraSaida) : "PONTO EM ABERTO";

        // Retorna uma string formatada com todas as informações do registro
        return String.format("Funcionário: %s | Entrada: %s | Saída: %s | Horas: %.2f",
                funcionario.getNome(),
                entradaFormatada,
                saidaFormatada,
                getHorasTrabalhadas()
        );
    }
}

