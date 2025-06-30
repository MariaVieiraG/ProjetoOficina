/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.interpreter;

import com.mycompany.oficina.agendamento.Agendamento;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Representa a expressão lógica "OU" (OR) que combina duas expressões.
 * <p>
 * Essa expressão retorna a união dos resultados das duas expressões,
 * ou seja, todos os agendamentos que satisfazem pelo menos uma das condições.
 */
public class ExpressaoOu implements Expressao {

    // Expressão do lado esquerdo da operação OR
    private final Expressao esquerda;
    // Expressão do lado direito da operação OR
    private final Expressao direita;

    /**
     * Construtor que recebe as duas expressões a serem combinadas.
     *
     * @param esquerda expressão do lado esquerdo
     * @param direita expressão do lado direito
     */
    public ExpressaoOu(Expressao esquerda, Expressao direita) {
        this.esquerda = esquerda;
        this.direita = direita;
    }

    /**
     * Interpreta a expressão lógica OR.
     * <p>
     * Avalia as duas expressões dentro do mesmo contexto e retorna uma lista
     * contendo todos os agendamentos que aparecem em pelo menos uma das listas.
     *
     * @param contexto o contexto da busca contendo os dados necessários para avaliação
     * @return lista com agendamentos que satisfazem pelo menos uma das expressões
     */
    @Override
    public List<Agendamento> interpreter(ContextoDeBusca contexto) {
        // Executa a interpretação da expressão do lado esquerdo
        List<Agendamento> resultadoEsquerda = esquerda.interpreter(contexto);
        // Executa a interpretação da expressão do lado direito
        List<Agendamento> resultadoDireita = direita.interpreter(contexto);

        // Cria um Set para armazenar a união sem duplicatas
        Set<Agendamento> uniao = new HashSet<>(resultadoEsquerda);
        // Adiciona todos os elementos do resultado direito ao conjunto
        uniao.addAll(resultadoDireita);

        // Retorna a união como uma lista
        return new ArrayList<>(uniao);
    }
}
