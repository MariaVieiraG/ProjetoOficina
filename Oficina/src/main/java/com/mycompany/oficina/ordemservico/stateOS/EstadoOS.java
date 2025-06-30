/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.oficina.ordemservico.stateOS;

import com.mycompany.oficina.loja.Produto;

/**
 * Interface que define as operações possíveis para os estados de uma Ordem de Serviço (OS).
 * Cada estado da OS implementa essa interface para controlar o comportamento da OS em diferentes fases.
 */
public interface EstadoOS {
    
    /**
     * Inicia a inspeção da Ordem de Serviço.
     * Geralmente usado para transitar do estado "Aguardando" para "Em Inspeção".
     */
    void iniciarInspecao();

    /**
     * Inicia o serviço da Ordem de Serviço.
     * Geralmente usado para transitar do estado "Em Inspeção" para "Em Serviço".
     */
    void iniciarServico();

    /**
     * Adiciona uma peça ao serviço, considerando o estoque e quantidade.
     * Usado principalmente no estado "Em Serviço".
     * 
     * @param produtoDoEstoque Produto que será utilizado na OS
     * @param quantidade Quantidade da peça a ser usada
     */
    void adicionarPeca(Produto produtoDoEstoque, int quantidade);

    /**
     * Finaliza o serviço da Ordem de Serviço.
     * Geralmente transita para o estado "Finalizada".
     */
    void finalizarServico();

    /**
     * Cancela a Ordem de Serviço com um motivo especificado.
     * Dependendo do estado atual, pode ou não ser permitido cancelar.
     * 
     * @param motivo Justificativa para o cancelamento
     */
    void cancelar(String motivo);

    /**
     * Retorna o status atual da Ordem de Serviço como uma String.
     * 
     * @return O nome do estado atual
     */
    String getStatus();
}
