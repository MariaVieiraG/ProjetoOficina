/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.ordemservico.stateOS;

import com.mycompany.oficina.ordemservico.OrdemDeServico;

/**
 * Representa o estado "Em Inspeção" da Ordem de Serviço.
 * 
 * Quando a OS está neste estado, significa que a inspeção do veículo está em andamento.
 */
public class EstadoEmInspecao extends EstadoBaseOS {

    /**
     * Construtor que recebe a Ordem de Serviço e passa para a superclasse.
     * 
     * @param os OrdemDeServico que estará no estado "Em Inspeção"
     */
    public EstadoEmInspecao(OrdemDeServico os) {
        super(os);
    }

    /**
     * Método que inicia o serviço, alterando o estado da OS para "Em Serviço".
     * 
     * Aqui, utilizamos o método protegido getOs() da superclasse para acessar a OS
     * e alterar seu estado para uma nova instância de EstadoEmServico.
     */
    @Override
    public void iniciarServico() {
        getOs().setEstado(new EstadoEmServico(getOs()));
    }

    /**
     * Retorna o status atual da OS como "Em Inspeção".
     * 
     * @return String representando o estado atual
     */
    @Override
    public String getStatus() {
        return "Em Inspeção";
    }
}
