package com.mycompany.oficina.seguranca;

import com.mycompany.oficina.controlador.GerenciadorFuncionario;
import com.mycompany.oficina.entidades.Funcionario;

/**
 * Serviço responsável pela autenticação de funcionários.
 * Utiliza o GerenciadorFuncionario para buscar o funcionário pelo CPF
 * e valida a senha fornecida para permitir o acesso.
 */
public class ServicoAutenticacao {
    // Gerenciador responsável por manipular os funcionários cadastrados
    private final GerenciadorFuncionario gerenciadorFuncionario;

    /**
     * Construtor que recebe a instância do gerenciador de funcionários.
     * 
     * @param gerenciadorFuncionario Gerenciador usado para buscar os funcionários
     */
    public ServicoAutenticacao(GerenciadorFuncionario gerenciadorFuncionario) {
        this.gerenciadorFuncionario = gerenciadorFuncionario;
    }

    /**
     * Autentica um funcionário baseado no CPF e senha fornecidos.
     * Se o CPF existir e a senha corresponder, retorna o funcionário.
     * Caso contrário, retorna null.
     * 
     * @param cpf CPF do funcionário
     * @param senha Senha para autenticação
     * @return Funcionário autenticado ou null se falhar
     */
    public Funcionario autenticar(String cpf, String senha) {
        // Busca funcionário pelo CPF
        Funcionario funcionario = gerenciadorFuncionario.buscarPorIdentificador(cpf);

        // Verifica se funcionário existe e senha confere
        if (funcionario != null && funcionario.getSenha().equals(senha)) {
            return funcionario;  // Autenticação bem-sucedida
        }

        return null;  // Autenticação falhou
    }
}

