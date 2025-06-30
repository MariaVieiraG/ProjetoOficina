/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.ordemservico.stateOS;

import com.mycompany.oficina.ordemservico.OrdemDeServico;

/**
 * Representa o estado "Finalizada" de uma Ordem de Serviço.
 * 
 * Nesse estado, o serviço foi concluído e não permite mais alterações,
 * como cancelamento da OS.
 */
public class EstadoFinalizada extends EstadoBaseOS {

    /**
     * Construtor que recebe a Ordem de Serviço e passa para a superclasse.
     * 
     * @param os OrdemDeServico que estará no estado "Finalizada"
     */
    public EstadoFinalizada(OrdemDeServico os) {
        super(os);
    }

    /**
     * Retorna o status atual da OS como "Finalizada".
     * 
     * @return String representando o estado atual
     */
    @Override
    public String getStatus() {
        return "Finalizada";
    }

    /**
     * Método para cancelar a OS. 
     * Neste estado, o cancelamento não é permitido e lança exceção.
     * 
     * @param motivo Motivo do cancelamento (não utilizado aqui)
     * @throws UnsupportedOperationException se chamado
     */
    @Override
    public void cancelar(String motivo) {
        throw new UnsupportedOperationException("Não é possível cancelar uma OS já finalizada.");
    }
}
