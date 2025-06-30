/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.controlador;

import com.google.gson.reflect.TypeToken;
import com.mycompany.oficina.entidades.Funcionario;
import com.mycompany.oficina.persistencia.PersistenciaJson;

import java.util.ArrayList;

/**
 * Gerencia os dados dos funcionários da oficina.
 * <p>
 * Permite criar, buscar, editar e armazenar informações dos funcionários.
 * Os dados são persistidos em arquivos JSON.
 */
public class GerenciadorFuncionario extends GerenciadorGenerico<Funcionario> {

    /**
     * Construtor do gerenciador de funcionários.
     *
     * @param persistencia Objeto responsável pela leitura e escrita dos dados dos funcionários.
     */
    public GerenciadorFuncionario(PersistenciaJson persistencia) {
        super(persistencia, "funcionarios", new TypeToken<ArrayList<Funcionario>>() {});
    }

    /**
     * Cria e adiciona um novo funcionário com os dados fornecidos.
     *
     * @param senha    Senha de acesso do funcionário.
     * @param cargo    Cargo ocupado pelo funcionário.
     * @param nome     Nome completo.
     * @param cpf      CPF do funcionário (identificador único).
     * @param telefone Telefone de contato.
     * @param endereco Endereço residencial.
     * @param email    E-mail para contato.
     * @return O objeto Funcionario criado e salvo.
     */
    public Funcionario criarFuncionario(Funcionario novoFuncionario) {

        // O método 'adicionar' da classe pai já salva os dados automaticamente
        super.adicionar(novoFuncionario);
        return novoFuncionario;
    }

    /**
     * Edita os dados de um funcionário existente, localizado pelo CPF.
     *
     * @param cpf          CPF atual do funcionário para busca.
     * @param novaSenha    Nova senha de acesso.
     * @param novoCargo    Novo cargo.
     * @param novoNome     Novo nome completo.
     * @param novoTelefone Novo telefone.
     * @param novoEndereco Novo endereço.
     * @param novoEmail    Novo e-mail.
     * @return true se o funcionário foi encontrado e editado; false caso contrário.
     */
    public boolean editarFuncionario(String cpf, String novaSenha, String novoCargo, String novoNome, String novoTelefone, String novoEndereco, String novoEmail) {
        Funcionario funcionarioParaEditar = this.buscarPorIdentificador(cpf);

        if (funcionarioParaEditar != null) {
            funcionarioParaEditar.setSenha(novaSenha);
            funcionarioParaEditar.setCargo(novoCargo);
            funcionarioParaEditar.setNome(novoNome);
            funcionarioParaEditar.setTelefone(novoTelefone);
            funcionarioParaEditar.setEndereco(novoEndereco);
            funcionarioParaEditar.setEmail(novoEmail);

            // Solicita à classe pai que salve as alterações
            super.salvarAlteracoes();

            return true;
        }

        return false;
    }
}
