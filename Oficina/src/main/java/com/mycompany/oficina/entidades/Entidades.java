/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.oficina.entidades;

/**
 * Interface que define o contrato para entidades que possuem um identificador único.
 * 
 * Classes que implementam esta interface devem fornecer uma forma de obter
 * um identificador único que as diferencie dentro do sistema.
 */
public interface Entidades {

    /**
     * Retorna o identificador único da entidade.
     * 
     * @return uma String representando o identificador único da entidade
     */
    String getIdentificador();
}
