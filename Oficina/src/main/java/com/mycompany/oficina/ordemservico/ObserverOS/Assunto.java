/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.oficina.ordemservico.ObserverOS;

/**
 * Interface que define o comportamento de um "Assunto" no padrão Observer.
 * 
 * Qualquer classe que implemente essa interface será capaz de
 * gerenciar observadores que desejam ser notificados sobre mudanças no estado do objeto.
 */
public interface Assunto {
    
    /**
     * Adiciona um observador à lista de observadores deste assunto.
     * 
     * @param observador o observador que deseja receber notificações
     */
    void adicionarObservador(Observador observador);
    
    /**
     * Remove um observador da lista de observadores.
     * 
     * @param observador o observador que não deseja mais receber notificações
     */
    void removerObservador(Observador observador);
    
    /**
     * Notifica todos os observadores registrados sobre uma mudança no estado.
     * 
     * Este método deve ser chamado sempre que ocorrer uma alteração relevante
     * no estado do objeto que os observadores acompanham.
     */
    void notificarObservadores();
}
