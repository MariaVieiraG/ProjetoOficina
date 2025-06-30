/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.oficina.interpreter;

import com.mycompany.oficina.agendamento.Agendamento;
import java.util.List;

/**
 * Define a interface comum para todos os elementos de uma expressão de busca.
 * <p>
 * Cada implementação dessa interface deve definir como interpretar a expressão
 * dentro de um dado contexto de busca, retornando uma lista de agendamentos que
 * satisfazem a condição da expressão.
 */
public interface Expressao {
    
    /**
     * Método que interpreta a expressão dentro do contexto fornecido.
     * 
     * @param contexto contexto que contém os dados e estado necessários para avaliar a expressão.
     * @return uma lista de agendamentos que correspondem à condição definida pela expressão.
     */
    List<Agendamento> interpreter(ContextoDeBusca contexto);
}
