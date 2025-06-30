/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.ordemservico.stateOS;

import com.mycompany.oficina.ordemservico.OrdemDeServico;

/**
 * Estado que representa a Ordem de Serviço quando está no status "Aguardando".
 * 
 * Nesta fase, a OS ainda não iniciou a inspeção e está aguardando esse processo.
 */
public class EstadoAguardando extends EstadoBaseOS {

    /**
     * Construtor que recebe a Ordem de Serviço associada a esse estado.
     * 
     * @param os a Ordem de Serviço que está neste estado
     */
    public EstadoAguardando(OrdemDeServico os) { 
        super(os); 
    }

    /**
     * Método para iniciar a inspeção da Ordem de Serviço.
     * Ao ser chamado, muda o estado para EstadoEmInspecao.
     */
    @Override
    public void iniciarInspecao() {
        getOs().setEstado(new EstadoEmInspecao(getOs()));
    }

    /**
     * Retorna o nome do status atual representado por esse estado.
     * 
     * @return String "Aguardando"
     */
    @Override
    public String getStatus() {
        return "Aguardando";
    }
}
