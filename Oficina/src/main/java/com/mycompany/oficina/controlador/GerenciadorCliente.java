/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.controlador;

import com.google.gson.reflect.TypeToken;
import com.mycompany.oficina.entidades.Cliente;
import com.mycompany.oficina.persistencia.PersistenciaJson;

import java.util.ArrayList;

/**
 * Gerencia os dados dos clientes da oficina.
 * <p>
 * Permite adicionar, buscar e editar informações dos clientes.
 * Os dados são armazenados e carregados via arquivos JSON.
 */
public class GerenciadorCliente extends GerenciadorGenerico<Cliente> {

    /**
     * Construtor do gerenciador de clientes.
     *
     * @param persistencia Objeto responsável pela leitura e gravação dos dados de clientes.
     */
    public GerenciadorCliente(PersistenciaJson persistencia) {
        super(persistencia, "clientes", new TypeToken<ArrayList<Cliente>>() {});
    }

    /**
     * Adiciona um novo cliente ao sistema com os dados fornecidos.
     *
     * @param nome     Nome do cliente.
     * @param cpf      CPF do cliente (usado como identificador único).
     * @param telefone Telefone de contato.
     * @param endereco Endereço completo.
     * @param email    E-mail do cliente.
     * @return O objeto Cliente criado e adicionado à lista.
     */
    public Cliente addCliente(Cliente cliente) {
    
        super.adicionar(cliente);
        return cliente;
    }

    /**
     * Edita os dados de um cliente já existente, localizado pelo CPF original.
     *
     * @param novoNome     Novo nome do cliente.
     * @param cpf          CPF atual do cliente (identificador para busca).
     * @param novoTelefone Novo número de telefone.
     * @param novoEndereco Novo endereço.
     * @param novoEmail    Novo e-mail.
     * @return true se o cliente foi encontrado e atualizado com sucesso, false caso contrário.
     */
    public boolean editarCliente(String novoNome, String cpf, String novoTelefone, String novoEndereco, String novoEmail) {
        Cliente clienteEditar = this.buscarPorIdentificador(cpf);
        if (clienteEditar != null) {
            clienteEditar.setNome(novoNome);
            clienteEditar.setTelefone(novoTelefone);
            clienteEditar.setEndereco(novoEndereco);
            clienteEditar.setEmail(novoEmail);
            super.salvarAlteracoes();
            return true;
        }
        return false;
    }
}