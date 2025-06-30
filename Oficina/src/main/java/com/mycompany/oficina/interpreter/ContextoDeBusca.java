/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.interpreter;
import com.mycompany.oficina.agendamento.AgendaOficina;

/**
 * Classe que representa o contexto de busca para a agenda da oficina.
 * Encapsula uma instância de AgendaOficina para fornecer acesso controlado.
 */
public class ContextoDeBusca {

    // Instância da agenda da oficina que será usada para operações de busca
    private final AgendaOficina agenda;

    /**
     * Construtor que recebe uma agenda da oficina para ser utilizada no contexto.
     * @param agenda a agenda da oficina a ser usada no contexto de busca
     */
    public ContextoDeBusca(AgendaOficina agenda) {
        this.agenda = agenda;
    }

    /**
     * Método que retorna a agenda da oficina associada a este contexto.
     * @return a agenda da oficina
     */
    public AgendaOficina getAgenda() {
        return agenda;
    }
}
