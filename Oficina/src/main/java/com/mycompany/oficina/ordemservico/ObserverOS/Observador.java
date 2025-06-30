/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.oficina.ordemservico.ObserverOS;

import com.mycompany.oficina.ordemservico.OrdemDeServico;

/**
 * Interface que define o contrato para objetos que desejam
 * ser notificados sobre mudanças no estado de uma Ordem de Serviço.
 */
public interface Observador {

    /**
     * Método chamado para atualizar o observador com as informações
     * da Ordem de Serviço que sofreu uma alteração.
     * 
     * @param os a Ordem de Serviço que foi atualizada
     */
    void atualizar(OrdemDeServico os);
}
