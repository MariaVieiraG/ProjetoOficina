package com.mycompany.oficina.seguranca;

import com.mycompany.oficina.entidades.Funcionario;

/**
 * Classe Singleton que gerencia a sessão atual do usuário no sistema.
 * Mantém o funcionário logado e fornece métodos para login, logout e acesso a informações do usuário.
 */
public class Sessao {
    // Instância única da sessão (Singleton)
    private static Sessao instance;

    // Usuário (funcionário) atualmente logado
    private Funcionario usuarioLogado;

    // Construtor privado para evitar instância externa
    private Sessao() {
    }

    /**
     * Retorna a instância única da Sessao.
     * Se não existir, cria uma nova instância.
     * 
     * @return Instância única da Sessao
     */
    public static Sessao getInstance() {
        if (instance == null) {
            instance = new Sessao();
        }
        return instance;
    }

    /**
     * Realiza o login definindo o usuário logado.
     * 
     * @param funcionario Funcionário que efetuou o login
     */
    public void login(Funcionario funcionario) {
        usuarioLogado = funcionario;
    }

    /**
     * Realiza logout, removendo o usuário logado.
     */
    public void logout() {
        usuarioLogado = null;
    }

    /**
     * Verifica se há um usuário logado atualmente.
     * 
     * @return true se houver usuário logado, false caso contrário
     */
    public boolean IsLogado() {
        return usuarioLogado != null;
    }

    /**
     * Retorna o cargo do usuário logado.
     * 
     * @return Cargo do usuário logado, ou null se não houver usuário logado
     */
    public String getCargoUsuarioLogado() {
        if (IsLogado()) {
            return usuarioLogado.getCargo();
        }
        return null;
    }

    /**
     * Retorna o objeto do funcionário logado.
     * 
     * @return Funcionário logado, ou null se ninguém estiver logado
     */
    public Funcionario getUsuarioLogado() {
        return usuarioLogado;
    }
}

