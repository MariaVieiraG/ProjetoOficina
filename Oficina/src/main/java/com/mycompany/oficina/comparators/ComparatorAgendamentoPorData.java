package com.mycompany.oficina.comparators;

import com.mycompany.oficina.agendamento.Agendamento;

import java.util.Comparator;

/**
 * Comparador que ordena agendamentos com base na data e hora.
 * <p>
 * Permite comparar dois objetos do tipo Agendamento para ordenação
 * cronológica (do mais antigo para o mais recente).
 */
public class ComparatorAgendamentoPorData implements Comparator<Agendamento> {

    /**
     * Compara dois agendamentos de acordo com a data e hora.
     *
     * @param o1 o primeiro agendamento
     * @param o2 o segundo agendamento
     * @return -1 se o1 for antes de o2, 1 se o1 for depois de o2, ou 0 se forem iguais
     */
    @Override
    public int compare(Agendamento o1, Agendamento o2) {
        if (o1.getDataHora().isBefore(o2.getDataHora())) {
            return -1;
        }
        if (o1.getDataHora().isAfter(o2.getDataHora())) {
            return 1;
        }
        return 0;
    }
}

