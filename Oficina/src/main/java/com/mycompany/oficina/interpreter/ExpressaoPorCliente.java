/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.interpreter;

import com.mycompany.oficina.agendamento.Agendamento;

import com.mycompany.oficina.entidades.Cliente;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma expressão de busca filtrando agendamentos por um cliente específico.
 * <p>
 * Essa expressão retorna todos os agendamentos associados ao cliente informado.
 */
public class ExpressaoPorCliente implements Expressao {
    // Cliente que será usado como filtro na busca
    private final Cliente cliente;

    /**
     * Construtor que recebe o cliente para filtrar os agendamentos.
     *
     * @param cliente o cliente que deve ser buscado nos agendamentos
     */
    public ExpressaoPorCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * Interpreta a expressão filtrando os agendamentos que pertencem ao cliente especificado.
     *
     * @param contexto o contexto da busca contendo a agenda com os agendamentos
     * @return lista de agendamentos do cliente filtrado
     */
    @Override
    public List<Agendamento> interpreter(ContextoDeBusca contexto) {
        // Lista que armazenará os agendamentos encontrados para o cliente
        List<Agendamento> resultados = new ArrayList<>();

        // Percorre todas as datas que possuem agendamentos na agenda
        for (LocalDate data : contexto.getAgenda().getDatasAgendadas()) {
            // Para cada data, obtém a lista de agendamentos daquele dia
            for (Agendamento ag : contexto.getAgenda().getHorariosDoDia(data)) {
                // Verifica se o agendamento não é nulo e se o cliente bate com o cliente buscado
                if (ag != null && ag.getCliente().equals(this.cliente)) {
                    // Adiciona o agendamento na lista de resultados
                    resultados.add(ag);
                }
            }
        }
        // Retorna a lista de agendamentos encontrados
        return resultados;
    }
}
