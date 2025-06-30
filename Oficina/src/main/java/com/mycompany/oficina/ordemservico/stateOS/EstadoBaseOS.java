/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.ordemservico.stateOS;

import com.mycompany.oficina.loja.Produto;
import com.mycompany.oficina.ordemservico.OrdemDeServico;

/**
 * Classe abstrata base para os diferentes estados de uma Ordem de Serviço.
 * 
 * Fornece uma implementação padrão dos métodos da interface EstadoOS,
 * lançando exceção para operações não permitidas no estado atual,
 * exceto para o método de cancelar, que é implementado.
 * 
 * Também mantém uma referência à OrdemDeServico associada para que os estados possam manipulá-la.
 */
public abstract class EstadoBaseOS implements EstadoOS {
    // Referência para a Ordem de Serviço à qual esse estado pertence
    private  final OrdemDeServico os;

    /**
     * Construtor que recebe a OrdemDeServico associada.
     * 
     * @param os OrdemDeServico a ser gerenciada por este estado
     */
    public EstadoBaseOS(OrdemDeServico os) {
        this.os = os;
    }
    
    /**
     * Getter para acesso controlado à OrdemDeServico.
     * Permite que classes filhas acessem a OS sem expor diretamente o atributo.
     * 
     * @return OrdemDeServico associada a este estado
     */
    public OrdemDeServico getOs() {
        return this.os;
    }
    
    /**
     * Método auxiliar que lança exceção para operações inválidas
     * no estado atual da Ordem de Serviço.
     */
    private void lancaErro() { 
        throw new UnsupportedOperationException("Operação não permitida no estado atual: " + getStatus()); 
    }
    
    /**
     * Métodos padrão da interface EstadoOS que lançam exceção,
     * indicando que essas operações não são permitidas neste estado.
     */
    @Override public void iniciarInspecao() { lancaErro(); }
    @Override public void iniciarServico() { lancaErro(); }
    @Override public void adicionarPeca(Produto p, int q) { lancaErro(); }
    @Override public void finalizarServico() { lancaErro(); }
    
    /**
     * Implementação padrão para cancelar a Ordem de Serviço.
     * Ao cancelar, a OS muda para o estado EstadoCancelada, informando o motivo.
     * 
     * @param motivo motivo do cancelamento
     */
    @Override 
    public void cancelar(String motivo) { 
        getOs().setEstado(new EstadoCancelada(getOs(), motivo)); 
    }
}
