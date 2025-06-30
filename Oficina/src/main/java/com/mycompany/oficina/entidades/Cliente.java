/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.entidades;

import com.mycompany.oficina.entidades.Pessoa;

/**
 * Representa um cliente da oficina, que é uma especialização da classe Pessoa.
 * Cada cliente possui um identificador único gerado automaticamente.
 * 
 */
public class Cliente extends Pessoa implements Entidades {

    /**
     * Identificador único do cliente, no formato "CL-XXX".
     */
    private String idCliente;

    /**
     * Contador estático para gerar IDs sequenciais dos clientes.
     */
    private static int contadorIdCliente = 1;

    /**
     * Construtor para criar um novo cliente com os dados fornecidos.
     * O ID do cliente é gerado automaticamente no formato "CL-XXX", onde XXX é um número sequencial.
     * 
     * @param nome Nome completo do cliente.
     * @param cpf CPF do cliente.
     * @param telefone Telefone do cliente.
     * @param endereco Endereço do cliente.
     * @param email E-mail do cliente.
     */
    public Cliente(String nome, String cpf, String telefone, String endereco, String email) {
        super(nome, cpf, telefone, endereco, email);
        this.idCliente = "CL-" + String.format("%03d", contadorIdCliente++);
    }
    public Cliente(){
        contadorIdCliente++;
    }


    /**
     * Obtém o identificador único do cliente.
     * Normalmente usado para identificar o cliente no sistema.
     * 
     * @return ID do cliente, no formato "CL-XXX".
     */
    public String getIdCliente() {
        return idCliente;
    }

    /**
     * Define um novo identificador para o cliente.
     * Geralmente não utilizado, pois o ID é gerado automaticamente e deve ser único.
     * 
     * @param idCliente Novo ID do cliente.
     */
    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    @Override
    public String toString() {
        return "Cliente{" + "idCliente=" + idCliente + '}';
    }

    /**
     * Retorna o identificador usado pela interface Entidades.
     * Neste caso, o CPF do cliente é utilizado como identificador único.
     * 
     * @return CPF do cliente.
     */
    @Override
    public String getIdentificador() {
        return this.getCpf();
    }
}
