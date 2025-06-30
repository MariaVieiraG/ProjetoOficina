package com.mycompany.oficina.controller;

import com.mycompany.oficina.application.OficinaAplicattion;
import com.mycompany.oficina.agendamento.AgendaOficina;
import com.mycompany.oficina.agendamento.Agendamento;
import com.mycompany.oficina.entidades.Funcionario;
import com.mycompany.oficina.financeiro.GerenciadorFinanceiro;
import com.mycompany.oficina.loja.Estoque;
import com.mycompany.oficina.loja.Produto;
import com.mycompany.oficina.ordemservico.GerenciadorOrdemDeServico;
import com.mycompany.oficina.ordemservico.OrdemDeServico;
import com.mycompany.oficina.seguranca.Sessao;
import com.mycompany.oficina.sistemaponto.GerenciadorPonto;
import com.mycompany.oficina.sistemaponto.RegistroPonto;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador responsável pelas operações relacionadas ao mecânico,
 * incluindo o gerenciamento das ordens de serviço, acesso ao estoque
 * e controle de ponto.
 */
public class MecanicoController {

    private final GerenciadorOrdemDeServico gerenciadorOS;
    private final AgendaOficina agenda;
    private final Estoque estoque;
    private final GerenciadorPonto gerenciadorPonto;
    private final GerenciadorFinanceiro gerenciadorFinanceiro;

    /**
     * Inicializa os gerenciadores necessários para o mecânico.
     */
    public MecanicoController() {
        OficinaAplicattion app = OficinaAplicattion.getInstance();
        this.gerenciadorOS = app.getGerenciadorOS();
        this.agenda = app.getAgenda();
        this.estoque = app.getEstoque();
        this.gerenciadorPonto = app.getGerenciadorPonto();
        this.gerenciadorFinanceiro = app.getGerenciadorFinanceiro();
    }

    /**
     * Retorna os agendamentos marcados para o dia atual.
     * @return Lista de agendamentos do dia.
     */
    public List<Agendamento> listarAgendamentosDeHoje() {
        return Arrays.stream(agenda.getHorariosDoDia(LocalDate.now()))
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Abre uma nova ordem de serviço para um agendamento e defeito especificados.
     * @param agendamento Agendamento relacionado à ordem.
     * @param defeito Descrição do defeito relatado.
     * @return Ordem de serviço criada, ou null se parâmetros inválidos.
     */
    public  OrdemDeServico abrirOS(Agendamento agendamento, String defeito) {
        if (agendamento == null || defeito == null || defeito.trim().isEmpty()) {
            return null;
        }
        OrdemDeServico novaOS = gerenciadorOS.abrirOS(
                agendamento.getCliente(),
                agendamento.getCarro(),
                Sessao.getInstance().getUsuarioLogado(),
                defeito
        );

        // --- LÓGICA CORRIGIDA ---
        // Se a OS foi criada com sucesso, usa o novo método para remover o agendamento
        // da agenda sem aplicar a taxa de cancelamento.
        if (novaOS != null) {
            agenda.removerAgendamento(agendamento);
        }

        return novaOS;
    }

    /**
     * Lista todas as ordens de serviço que ainda estão ativas
     * (não finalizadas ou canceladas).
     * @return Lista de ordens de serviço ativas.
     */
    public List<OrdemDeServico> listarOSAtivas() {
        return gerenciadorOS.listarTodos().stream()
                .filter(os -> !os.getStatusAtual().equals("Finalizada") && !os.getStatusAtual().equals("Cancelada"))
                .collect(Collectors.toList());
    }


    /**
     * Busca uma ordem de serviço pelo seu identificador.
     * @param idOS Identificador da ordem.
     * @return Ordem de serviço encontrada ou null.
     */
    public OrdemDeServico buscarOS(String idOS) {

        return gerenciadorOS.buscarPorIdentificador(idOS);
    }

    /**
     * Inicia a inspeção da ordem de serviço.
     * @param os Ordem de serviço.
     */
    public void iniciarInspecaoOS(OrdemDeServico os) {

        if (os != null) os.iniciarInspecao();
        gerenciadorOS.salvarAlteracoesOS();
    }

    /**
     * Inicia o serviço da ordem de serviço.
     * @param os Ordem de serviço.
     */
    public void iniciarServicoOS(OrdemDeServico os) {

        if (os != null) os.iniciarServico();
        gerenciadorOS.salvarAlteracoesOS();
    }

    /**
     * Finaliza o serviço da ordem de serviço.
     * @param os Ordem de serviço.
     */
    public void finalizarServicoOS(OrdemDeServico os) {

        if (os != null) os.finalizarServico();
        gerenciadorOS.salvarAlteracoesOS();
        gerenciadorFinanceiro.registrarFaturamentoOS(os);
    }

    /**
     * Adiciona uma peça à ordem de serviço.
     * @param os Ordem de serviço.
     * @param produto Produto a ser adicionado.
     * @param quantidade Quantidade da peça.
     */
    public void adicionarPecaOS(OrdemDeServico os, Produto produto, int quantidade) {
        if (os != null && produto != null && quantidade > 0) {
            os.adicionarPeca(produto, quantidade);
            gerenciadorOS.salvarAlteracoesOS();
        }
    }

    /**
     * Busca um produto no estoque pelo seu ID.
     * @param id Identificador do produto.
     * @return Produto encontrado ou null.
     */
    public Produto buscarProdutoPorId(String id) {
        return estoque.buscarProduto(id);
    }

    /**
     * Lista todos os produtos disponíveis no estoque.
     * @return Lista de produtos.
     */
    public List<Produto> listarProdutosEstoque() {
        return estoque.listarProdutos();
    }

    /**
     * Gera um extrato detalhado da ordem de serviço.
     * @param os Ordem de serviço.
     * @return String com o extrato ou mensagem de erro.
     */
    public String gerarExtratoOS(OrdemDeServico os) {
        if (os == null) {
            return "Ordem de Serviço inválida ou não encontrada.";
        }
        return os.gerarExtrato();
    }

 
    /**
     * Registra o ponto de entrada do funcionário logado.
     * @return Registro do ponto criado.
     */
    public RegistroPonto baterPontoEntrada() {
        return gerenciadorPonto.baterPontoEntrada(Sessao.getInstance().getUsuarioLogado());
    }

    /**
     * Registra o ponto de saída do funcionário logado.
     * @return Registro do ponto criado.
     */
    public RegistroPonto baterPontoSaida() {
        return gerenciadorPonto.baterPontoSaida(Sessao.getInstance().getUsuarioLogado());
    }

    /**
     * Retorna os registros de ponto do funcionário logado no dia atual.
     * @return Lista de registros do dia.
     */
    public List<RegistroPonto> verRegistrosDeHoje() {
        Funcionario usuarioLogado = Sessao.getInstance().getUsuarioLogado();
        if (usuarioLogado == null) return Collections.emptyList();

        List<RegistroPonto> meusRegistros = gerenciadorPonto.getRegistrosPorFuncionario(usuarioLogado);
        return meusRegistros.stream()
                .filter(r -> r.getDataHoraEntrada().toLocalDate().isEqual(LocalDate.now()))
                .collect(Collectors.toList());
    }
}