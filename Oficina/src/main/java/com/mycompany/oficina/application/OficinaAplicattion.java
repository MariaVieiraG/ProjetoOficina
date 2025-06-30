package com.mycompany.oficina.application;

import com.mycompany.oficina.entidades.Funcionario;
import com.mycompany.oficina.financeiro.GerenciadorFinanceiro;
import com.mycompany.oficina.agendamento.AgendaOficina;
import com.mycompany.oficina.controlador.GerenciadorCarros;
import com.mycompany.oficina.controlador.GerenciadorCliente;
import com.mycompany.oficina.controlador.GerenciadorFuncionario;
import com.mycompany.oficina.loja.Estoque;
import com.mycompany.oficina.ordemservico.GerenciadorOrdemDeServico;
import com.mycompany.oficina.persistencia.PersistenciaJson;
import com.mycompany.oficina.seguranca.ServicoAutenticacao;
import com.mycompany.oficina.sistemaponto.GerenciadorPonto;

/**
 * Classe principal que centraliza o acesso aos gerenciadores e serviços da
 * aplicação da oficina.
 * <p>
 * Implementa o padrão Singleton para garantir que exista apenas uma instância
 * de {@code OficinaAplicattion} durante a execução do sistema.
 */
public class OficinaAplicattion {

    private static OficinaAplicattion instance; // Instância única da aplicação (Singleton)
    private final GerenciadorFuncionario gerenciadorFuncionario; // Gerencia os funcionários da oficina
    private final GerenciadorCliente gerenciadorCliente;  // Gerencia os clientes da oficina
    private final GerenciadorCarros gerenciadorCarros; // Gerencia os carros cadastrados
    private final GerenciadorOrdemDeServico gerenciadorOS; // Gerencia as ordens de serviço
    private final GerenciadorPonto gerenciadorPonto; // Gerencia os registros de ponto dos funcionários
    private final AgendaOficina agenda; // Controla os agendamentos de serviços
    private final Estoque estoque;  // Gerencia o estoque de produtos da oficina
    private final GerenciadorFinanceiro gerenciadorFinanceiro; // Responsável pelo controle financeiro (receitas e despesas)
    private final ServicoAutenticacao servicoAutenticacao;  // Responsável pela autenticação de usuários (login)
    private final PersistenciaJson persistencia;  // Responsável pela leitura e gravação de dados em arquivos JSON

    /**
     * Construtor privado da aplicação. Inicializa todos os componentes do
     * sistema com base na persistência.
     */
    private OficinaAplicattion() {
        this.persistencia = new PersistenciaJson();
        this.gerenciadorFuncionario = new GerenciadorFuncionario(persistencia);
        this.gerenciadorCliente = new GerenciadorCliente(persistencia);
        this.gerenciadorCarros = new GerenciadorCarros(persistencia);
        this.gerenciadorOS = new GerenciadorOrdemDeServico(persistencia);
        this.gerenciadorPonto = new GerenciadorPonto(persistencia);
        this.gerenciadorFinanceiro = GerenciadorFinanceiro.getInstance(persistencia);
        this.agenda = new AgendaOficina(persistencia);
        this.estoque = new Estoque(persistencia);
        this.servicoAutenticacao = new ServicoAutenticacao(gerenciadorFuncionario);
    }

    /**
     * Retorna a instância única da aplicação. Cria a instância se ela ainda não
     * existir.
     *
     * @return A única instância de OficinaAplicattion.
     */
    public static synchronized OficinaAplicattion getInstance() {
        if (instance == null) {
            instance = new OficinaAplicattion();
        }
        return instance;
    }

    // Métodos de acesso aos componentes do sistema:
    /**
     * Retorna o serviço de autenticação.
     */
    public ServicoAutenticacao getServicoAutenticacao() {
        return servicoAutenticacao;
    }

    /**
     * Retorna o gerenciador de clientes.
     */
    public GerenciadorCliente getGerenciadorCliente() {
        return gerenciadorCliente;
    }

    /**
     * Retorna o gerenciador de carros.
     */
    public GerenciadorCarros getGerenciadorCarros() {
        return gerenciadorCarros;
    }

    /**
     * Retorna o gerenciador de funcionários.
     */
    public GerenciadorFuncionario getGerenciadorFuncionario() {
        return gerenciadorFuncionario;
    }

    /**
     * Retorna a agenda da oficina.
     */
    public AgendaOficina getAgenda() {
        return agenda;
    }

    /**
     * Retorna o gerenciador de ordens de serviço.
     */
    public GerenciadorOrdemDeServico getGerenciadorOS() {
        return gerenciadorOS;
    }

    /**
     * Retorna o gerenciador de ponto dos funcionários.
     */
    public GerenciadorPonto getGerenciadorPonto() {
        return gerenciadorPonto;
    }

    /**
     * Retorna o gerenciador financeiro.
     */
    public GerenciadorFinanceiro getGerenciadorFinanceiro() {
        return gerenciadorFinanceiro;
    }

    /**
     * Retorna o controle de estoque da oficina.
     */
    public Estoque getEstoque() {
        return estoque;
    }

    /**
     * Verifica se já existe um funcionário no sistema. Caso contrário, cria um
     * usuário administrador padrão para o primeiro acesso.
     */
    public void verificarECriarAdminPadrao() {
        if (gerenciadorFuncionario.listarTodos().isEmpty()) {
            System.out.println("[Sistema] Nenhum funcionário encontrado. Criando usuário 'Admin' padrão...");
            Funcionario funPadrao = new Funcionario("admin", "Admin", "Administrador do Sistema", "00000000000", "00000000000", "N/A", "admin@oficina.com");
            gerenciadorFuncionario.adicionar(funPadrao);
            System.out.println("[Sistema] Usuário 'Admin' criado. Use CPF '00000000000' e senha 'admin' para o primeiro login.");
        }
    }
}
