package com.mycompany.oficina.controller;

import com.mycompany.oficina.application.OficinaAplicattion;
import com.mycompany.oficina.controlador.GerenciadorFuncionario;
import com.mycompany.oficina.entidades.Funcionario;
import com.mycompany.oficina.financeiro.GerenciadorFinanceiro;
import com.mycompany.oficina.financeiro.RegistroFinanceiro;
import com.mycompany.oficina.loja.Estoque;
import com.mycompany.oficina.loja.Produto;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
/**
 * Controlador responsável pelas ações disponíveis ao gerente no sistema da oficina.
 * Estende o {@link AtendenteController}, herdando as funcionalidades de cliente, agendamento e ponto.
 * Adiciona operações de gerenciamento de funcionários, finanças e estoque.
 * 
 * Este controlador centraliza as ações administrativas da oficina, como pagamento de salários,
 * emissão de relatórios financeiros, cadastro e controle de peças, e controle de usuários do sistema.
 * 
 */
public class GerenteController extends AtendenteController {

    private final GerenciadorFuncionario gerenciadorFuncionario;
    private final GerenciadorFinanceiro gerenciadorFinanceiro;
    private final Estoque estoque;

    /**
     * Construtor que inicializa os gerenciadores usados pelo gerente.
     */
    public GerenteController() {
        super();
        OficinaAplicattion app = OficinaAplicattion.getInstance();
        this.gerenciadorFuncionario = app.getGerenciadorFuncionario();
        this.gerenciadorFinanceiro = app.getGerenciadorFinanceiro();
        this.estoque = app.getEstoque();
    }

    // --- LÓGICA DE FUNCIONÁRIOS ---

    /**
     * Lista todos os funcionários cadastrados.
     *
     * @return lista de funcionários
     */
    public List<Funcionario> listarFuncionarios() {
        return gerenciadorFuncionario.listarTodos();
    }

    /**
     * Busca um funcionário pelo CPF.
     *
     * @param cpf CPF do funcionário
     * @return funcionário correspondente ou null
     */
    public Funcionario buscarFuncionario(String cpf) {
        return gerenciadorFuncionario.buscarPorIdentificador(cpf);
    }

    /**
     * Cadastra um novo funcionário.
     *
     * @param senha senha de acesso
     * @param cargo cargo do funcionário
     * @param nome nome completo
     * @param cpf CPF do funcionário
     * @param telefone telefone de contato
     * @param endereco endereço residencial
     * @param email email de contato
     * @return true se cadastrado com sucesso, false caso contrário
     */
    public boolean cadastrarFuncionario(Funcionario f) {
        if (gerenciadorFuncionario.buscarPorIdentificador(f.getCpf()) != null) return false;
        gerenciadorFuncionario.adicionar(f);
        return f != null;
    }

    /**
     * Edita os dados de um funcionário.
     *
     * @param cpf CPF atual
     * @param novoCpf novo CPF (opcional)
     * @param novaSenha nova senha (opcional)
     * @param novoCargo novo cargo (opcional)
     * @param novoNome novo nome (opcional)
     * @param novoTelefone novo telefone (opcional)
     * @param novoEndereco novo endereço (opcional)
     * @param novoEmail novo email (opcional)
     * @return true se editado com sucesso, false caso contrário
     */
    public boolean editarFuncionario(String cpf, String novoCpf, String novaSenha, String novoCargo, String novoNome, String novoTelefone, String novoEndereco, String novoEmail) {
        Funcionario f = buscarFuncionario(cpf);
        if (f == null) return false;

        String senhaFinal = (novaSenha == null || novaSenha.isEmpty()) ? f.getSenha() : novaSenha;
        String cargoFinal = (novoCargo == null || novoCargo.isEmpty()) ? f.getCargo() : novoCargo;
        String nomeFinal = (novoNome == null || novoNome.isEmpty()) ? f.getNome() : novoNome;
        String cpfFinal = (novoCpf == null || novoCpf.isEmpty()) ? f.getCpf() : novoCpf;
        String telFinal = (novoTelefone == null || novoTelefone.isEmpty()) ? f.getTelefone() : novoTelefone;
        String endFinal = (novoEndereco == null || novoEndereco.isEmpty()) ? f.getEndereco() : novoEndereco;
        String emailFinal = (novoEmail == null || novoEmail.isEmpty()) ? f.getEmail() : novoEmail;

        return gerenciadorFuncionario.editarFuncionario(cpf, senhaFinal, cargoFinal, nomeFinal, telFinal, endFinal, emailFinal);
    }

    /**
     * Remove um funcionário pelo CPF.
     *
     * @param cpf CPF do funcionário
     * @return true se removido com sucesso
     */
    public boolean removerFuncionario(String cpf) {
        return gerenciadorFuncionario.removerItemPorIdentificador(cpf);
    }

    // --- LÓGICA FINANCEIRA ---

    /**
     * Retorna os registros financeiros dentro de um intervalo de datas.
     *
     * @param inicio data inicial (inclusive)
     * @param fim data final (inclusive)
     * @return lista de registros no intervalo
     */
    public List<RegistroFinanceiro> getRegistrosFinanceiros(LocalDate inicio, LocalDate fim) {
        return gerenciadorFinanceiro.getRegistros().stream()
                .filter(r -> !r.getData().toLocalDate().isBefore(inicio) && !r.getData().toLocalDate().isAfter(fim))
                .collect(Collectors.toList());
    }

    /**
     * Imprime no console o balanço financeiro entre duas datas.
     *
     * @param inicio data inicial
     * @param fim data final
     */
    public void emitirBalanco(LocalDate inicio, LocalDate fim) {
        System.out.println("\n--- Balanço Financeiro de " + inicio + " a " + fim + " ---");
        gerenciadorFinanceiro.emitirBalanco(inicio, fim);
    }

    /**
     * Imprime no console o relatório detalhado de despesas.
     *
     * @param inicio data inicial
     * @param fim data final
     */
    public void emitirRelatorioDespesas(LocalDate inicio, LocalDate fim) {
        System.out.println("\n--- Relatório de Despesas de " + inicio + " a " + fim + " ---");
        gerenciadorFinanceiro.emitirRelatorioDespesasDetalhado(inicio, fim);
    }

    /**
     * Realiza o pagamento dos salários de todos os funcionários.
     */
    public void pagarSalarios() {
        gerenciadorFinanceiro.pagarSalarios(gerenciadorFuncionario.listarTodos());
    }

    // --- LÓGICA DE ESTOQUE ---

    /**
     * Lista todos os produtos cadastrados no estoque.
     *
     * @return lista de produtos
     */
    public List<Produto> listarProdutos() {
        return estoque.listarProdutos();
    }

    /**
     * Busca um produto pelo seu ID.
     *
     * @param id identificador do produto
     * @return produto encontrado ou null
     */
    public Produto buscarProduto(String id) {
        return estoque.buscarProduto(id);
    }

    /**
     * Cadastra uma nova peça no estoque e registra a despesa.
     *
     * @param nome nome da peça
     * @param precoVenda preço de venda
     * @param quantidade quantidade inicial
     * @param fornecedor fornecedor
     * @return true se cadastrada com sucesso
     */
    public boolean cadastrarNovaPeca(String nome, double precoVenda, int quantidade, String fornecedor) {
        Produto novoProduto = new Produto(nome, precoVenda, quantidade, fornecedor);
        double precoCompraUnidade = Math.max(0, precoVenda - 15.00);
        double custoTotal = precoCompraUnidade * quantidade;
        gerenciadorFinanceiro.registrarDespesaCompraPecas("Compra inicial de " + quantidade + "x " + nome, custoTotal);

        return estoque.cadastrarProduto(novoProduto);
    }

    /**
     * Edita os dados de uma peça no estoque.
     *
     * @param id identificador da peça
     * @param novoNome novo nome
     * @param novoPreco novo preço de venda
     * @param novaQtd nova quantidade
     * @param novoForn novo fornecedor
     * @return true se atualizado
     */
    public boolean editarPeca(String id, String novoNome, double novoPreco, int novaQtd, String novoForn) {
        return estoque.editarProduto(id, novoNome, novoPreco, novaQtd, novoForn);
    }

    /**
     * Realiza a reposição de uma peça no estoque e registra a despesa.
     *
     * @param peca peça a repor
     * @param quantidade quantidade a adicionar
     * @return true se reposta com sucesso
     */
    public boolean reporEstoque(Produto peca, int quantidade) {
        if (peca == null || quantidade <= 0) return false;

        peca.setQuantidade(peca.getQuantidade() + quantidade);
        double precoCompraUnidade = Math.max(0, peca.getPreco() - 15.00);
        double custoTotal = precoCompraUnidade * quantidade;
        gerenciadorFinanceiro.registrarDespesaCompraPecas("Reposição de " + quantidade + "x " + peca.getNome(), custoTotal);

        estoque.salvarEstoque();
        return true;
    }
}
