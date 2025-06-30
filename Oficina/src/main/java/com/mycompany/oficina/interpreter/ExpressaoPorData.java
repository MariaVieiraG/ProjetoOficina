/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.interpreter;
import com.mycompany.oficina.agendamento.AgendaOficina;
import com.mycompany.oficina.agendamento.Agendamento;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Representa uma expressão de busca que filtra agendamentos por uma data específica.
 * <p>
 * Retorna todos os agendamentos que ocorrem na data informada.
 */
public class ExpressaoPorData implements Expressao {
    // Data que será usada como critério de filtro
    private final LocalDate data;

    /**
     * Construtor que recebe a data para filtrar os agendamentos.
     *
     * @param data a data para a qual se deseja buscar os agendamentos
     */
    public ExpressaoPorData(LocalDate data) {
        this.data = data;
    }

    /**
     * Interpreta a expressão retornando todos os agendamentos que ocorrem na data especificada.
     *
     * @param contexto o contexto da busca que contém a agenda com os agendamentos
     * @return lista de agendamentos agendados para a data especificada
     */
    @Override
    public List<Agendamento> interpreter(ContextoDeBusca contexto) {
        // Obtém a agenda do contexto
        AgendaOficina agenda = contexto.getAgenda();

        // Obtém os agendamentos (horários) do dia específico
        Agendamento[] horariosDoDia = agenda.getHorariosDoDia(data);

        // Transforma o array em stream, filtra os elementos não nulos e coleta em uma lista
        return Arrays.stream(horariosDoDia)
                .filter(Objects::nonNull)  // remove possíveis posições vazias no array
                .collect(Collectors.toList());
    }
}
