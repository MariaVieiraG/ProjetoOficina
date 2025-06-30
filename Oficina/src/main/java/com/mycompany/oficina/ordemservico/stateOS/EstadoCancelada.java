/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.ordemservico.stateOS;

import com.mycompany.oficina.ordemservico.OrdemDeServico;

/**
 * Representa o estado "Cancelada" da Ordem de Serviço.
 * 
 * Esse estado indica que a ordem de serviço foi cancelada,
 * portanto, nenhuma outra operação além de consultar o status
 * é permitida.
 */
public class EstadoCancelada extends EstadoBaseOS {

    /**
     * Construtor que recebe a Ordem de Serviço e o motivo do cancelamento.
     * 
     * @param os OrdemDeServico que será marcada como cancelada
     * @param motivo motivo do cancelamento (não armazenado neste exemplo)
     */
    public EstadoCancelada(OrdemDeServico os, String motivo) {
        super(os);
    }

    /**
     * Retorna o status atual da OS, que é "Cancelada".
     * 
     * @return string indicando o estado "Cancelada"
     */
    @Override
    public String getStatus() {
        return "Cancelada";
    }

    /**
     * Caso a operação cancelar seja chamada novamente,
     * lança exceção porque a OS já está cancelada e não pode ser cancelada duas vezes.
     * 
     * @param motivo motivo do cancelamento (não utilizado)
     * @throws UnsupportedOperationException se tentar cancelar novamente
     */
    @Override
    public void cancelar(String motivo) {
        throw new UnsupportedOperationException("A OS já está cancelada.");
    }
}
