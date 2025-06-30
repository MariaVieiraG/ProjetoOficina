/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.interpreter;

import com.mycompany.oficina.agendamento.Agendamento;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa a expressão lógica "E" (AND) que combina duas expressões.
 * <p>
 * Essa expressão retorna a interseção dos resultados das duas expressões,
 * ou seja, os agendamentos que satisfazem ambas as condições.
 */
public class ExpressaoE implements Expressao {

    // Expressão do lado esquerdo da operação AND
    private final Expressao esquerda;
    // Expressão do lado direito da operação AND
    private final Expressao direita;

    /**
     * Construtor que recebe as duas expressões a serem combinadas.
     *
     * @param esquerda expressão do lado esquerdo
     * @param direita expressão do lado direito
     */
    public ExpressaoE(Expressao esquerda, Expressao direita) {
        this.esquerda = esquerda;
        this.direita = direita;
    }

    /**
     * Interpreta a expressão lógica AND.
     * <p>
     * Avalia as duas expressões dentro do mesmo contexto e retorna somente
     * os agendamentos presentes em ambas as listas (interseção).
     *
     * @param contexto o contexto da busca contendo os dados necessários para avaliação
     * @return lista com agendamentos que satisfazem as duas expressões
     */
    @Override
    public List<Agendamento> interpreter(ContextoDeBusca contexto) {
        // Executa a interpretação da expressão do lado esquerdo
        List<Agendamento> resultadoEsquerda = esquerda.interpreter(contexto);
        // Executa a interpretação da expressão do lado direito
        List<Agendamento> resultadoDireita = direita.interpreter(contexto);

        List<Agendamento> resultadoFinal = new ArrayList<>();

        // Mantém apenas os agendamentos presentes em ambas as listas (interseção)
        for (Agendamento ag : resultadoEsquerda) {
            if (resultadoDireita.contains(ag)) {
                resultadoFinal.add(ag);
            }
        }

        return resultadoFinal;
    }
}

