package com.mycompany.oficina.controller;

import com.mycompany.oficina.application.OficinaAplicattion;
import com.mycompany.oficina.agendamento.AgendaOficina;
import com.mycompany.oficina.agendamento.Agendamento;
import com.mycompany.oficina.agendamento.TipoServico;
import com.mycompany.oficina.controlador.GerenciadorCarros;
import com.mycompany.oficina.controlador.GerenciadorCliente;
import com.mycompany.oficina.controlador.GerenciadorFuncionario;
import com.mycompany.oficina.entidades.Carro;
import com.mycompany.oficina.entidades.Cliente;
import com.mycompany.oficina.entidades.Elevador;
import com.mycompany.oficina.entidades.Funcionario;
import com.mycompany.oficina.financeiro.GerenciadorFinanceiro;
import com.mycompany.oficina.interpreter.ContextoDeBusca;
import com.mycompany.oficina.interpreter.Expressao;
import com.mycompany.oficina.interpreter.ExpressaoPorCliente;
import com.mycompany.oficina.interpreter.ExpressaoPorData;
import com.mycompany.oficina.ordemservico.GerenciadorOrdemDeServico;
import com.mycompany.oficina.seguranca.Sessao;
import com.mycompany.oficina.sistemaponto.GerenciadorPonto;
import com.mycompany.oficina.sistemaponto.RegistroPonto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador principal das operações realizadas pelo atendente no sistema da oficina.
 * Responsável pelas funcionalidades de cadastro e edição de clientes e veículos,
 * agendamento de serviços, e registro de ponto.
 *
 * Esta classe encapsula as regras de negócio básicas do sistema, sendo utilizada na
 * interface do atendente para gerenciar as entidades principais.
 * 
 */
public class AtendenteController {

    private final GerenciadorCliente gerenciadorCliente;
    private final GerenciadorCarros gerenciadorCarros;
    private final GerenciadorFuncionario gerenciadorFuncionario;
    private final GerenciadorPonto gerenciadorPonto;
    private final GerenciadorFinanceiro gerenciadorFinanceiro;
    private final AgendaOficina agenda;
    private final GerenciadorOrdemDeServico gerenciadorOS;

    /**
     * Construtor que inicializa os gerenciadores a partir da aplicação principal.
     */
    public AtendenteController() {
        OficinaAplicattion app = OficinaAplicattion.getInstance();
        this.gerenciadorCliente = app.getGerenciadorCliente();
        this.gerenciadorCarros = app.getGerenciadorCarros();
        this.gerenciadorFuncionario = app.getGerenciadorFuncionario();
        this.gerenciadorPonto = app.getGerenciadorPonto();
        this.gerenciadorFinanceiro = app.getGerenciadorFinanceiro();
        this.gerenciadorOS = app.getGerenciadorOS();
        this.agenda = app.getAgenda();
    }

    // --- LÓGICA DE CLIENTES ---

    /**
     * Lista todos os clientes cadastrados.
     *
     * @return lista de clientes
     */
    public List<Cliente> listarClientes() {
        return gerenciadorCliente.listarTodos();
    }

    /**
     * Busca um cliente pelo CPF.
     *
     * @param cpf CPF do cliente
     * @return cliente encontrado ou null
     */
    public Cliente buscarCliente(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) return null;
        return gerenciadorCliente.buscarPorIdentificador(cpf.replaceAll("\\D", ""));
    }

    /**
     * Cadastra um novo cliente.
     *
     * @param nome nome do cliente
     * @param cpf CPF do cliente
     * @param telefone telefone
     * @param endereco endereço
     * @param email email
     * @return true se cadastrado com sucesso
     */
    public boolean cadastrarCliente(Cliente novoCliente) {
        if (buscarCliente(novoCliente.getCpf()) != null) {
            return false;
        }
        gerenciadorCliente.addCliente(novoCliente);
        return novoCliente != null;
    }

    /**
     * Edita os dados de um cliente.
     *
     * @param cpfAntigo CPF atual
     * @param novoNome novo nome
     * @param novoCpf novo CPF
     * @param novoTelefone novo telefone
     * @param novoEndereco novo endereço
     * @param novoEmail novo email
     * @return true se editado com sucesso
     */
    public boolean editarCliente(String cpfAntigo, String novoNome, String novoCpf, String novoTelefone, String novoEndereco, String novoEmail) {
        Cliente cliente = buscarCliente(cpfAntigo);
        if (cliente == null) return false;

        String nomeFinal = (novoNome == null || novoNome.isEmpty()) ? cliente.getNome() : novoNome;
        String cpfFinal = (novoCpf == null || novoCpf.isEmpty()) ? cliente.getCpf() : novoCpf;
        String telefoneFinal = (novoTelefone == null || novoTelefone.isEmpty()) ? cliente.getTelefone() : novoTelefone;
        String enderecoFinal = (novoEndereco == null || novoEndereco.isEmpty()) ? cliente.getEndereco() : novoEndereco;
        String emailFinal = (novoEmail == null || novoEmail.isEmpty()) ? cliente.getEmail() : novoEmail;

        return gerenciadorCliente.editarCliente(nomeFinal, cpfAntigo, telefoneFinal, enderecoFinal, emailFinal);
    }

    /**
     * Remove um cliente, desde que ele não possua veículos cadastrados.
     *
     * @param cpf CPF do cliente
     * @return true se removido com sucesso
     */
    public boolean removerCliente(String cpf) {
        Cliente cliente = buscarCliente(cpf);
        if (cliente == null) return false;

        boolean temCarros = gerenciadorCarros.listarTodos().stream()
                .anyMatch(c -> c.getCpfDono().equals(cliente.getCpf()));
        if (temCarros) {
            return false;
        }
        return gerenciadorCliente.removerItemPorIdentificador(cpf);
    }

    // --- LÓGICA DE VEÍCULOS ---

    /**
     * Lista todos os veículos cadastrados.
     *
     * @return lista de veículos
     */
    public List<Carro> listarVeiculos() {
        return gerenciadorCarros.listarTodos();
    }

    /**
     * Busca um veículo pelo chassi.
     *
     * @param chassi identificador do veículo
     * @return veículo encontrado ou null
     */
    public Carro buscarVeiculo(String chassi) {
        return gerenciadorCarros.buscarPorIdentificador(chassi);
    }

    /**
     * Lista os veículos de um cliente.
     *
     * @param cpf CPF do cliente
     * @return lista de veículos associados ao CPF
     */
    public List<Carro> listarVeiculosDoCliente(String cpf) {
        return gerenciadorCarros.listarTodos().stream()
                .filter(c -> c.getCpfDono().equals(cpf))
                .collect(Collectors.toList());
    }

    /**
     * Cadastra um novo veículo para um cliente.
     *
     * @param cpfDono CPF do dono
     * @param fabricante fabricante do veículo
     * @param modelo modelo
     * @param placa placa
     * @param chassi chassi (identificador)
     * @return true se cadastrado
     */
    public boolean cadastrarVeiculo(String cpfDono, String fabricante, String modelo, String placa, String chassi) {
        Cliente dono = buscarCliente(cpfDono);
        if (dono == null || buscarVeiculo(chassi) != null) {
            return false;
        }
        Carro novoCarro = gerenciadorCarros.cadastrarCarro(dono, fabricante, modelo, placa, chassi);
        return novoCarro != null;
    }

    /**
     * Edita os dados de um veículo.
     *
     * @param chassi identificador do veículo
     * @param novoFabricante novo fabricante
     * @param novoModelo novo modelo
     * @param novaPlaca nova placa
     * @return true se editado
     */
    public boolean editarVeiculo(String chassi, String novoFabricante, String novoModelo, String novaPlaca) {
        Carro carro = buscarVeiculo(chassi);
        if (carro == null) return false;

        String fabFinal = (novoFabricante == null || novoFabricante.isEmpty()) ? carro.getFabricante() : novoFabricante;
        String modFinal = (novoModelo == null || novoModelo.isEmpty()) ? carro.getModelo() : novoModelo;
        String placaFinal = (novaPlaca == null || novaPlaca.isEmpty()) ? carro.getPlaca() : novaPlaca;

        return gerenciadorCarros.editarCarro(chassi, fabFinal, modFinal, placaFinal);
    }

    /**
     * Remove um veículo pelo chassi.
     *
     * @param chassi identificador
     * @return true se removido
     */
    public boolean removerVeiculo(String chassi) {
        if (chassi == null || chassi.isBlank()) {
            return false;
        }
        return gerenciadorCarros.removerItemPorIdentificador(chassi);
    }

    // --- LÓGICA DE AGENDAMENTOS ---

    /**
     * Busca agendamentos por data (formato dd/MM/yyyy) ou nome de cliente.
     *
     * @param termoBusca data ou nome do cliente
     * @return lista de agendamentos encontrados
     */
    public List<Agendamento> buscarAgendamentos(String termoBusca) {
        Expressao expressaoDeBusca;
        try {
            LocalDate data = LocalDate.parse(termoBusca, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            expressaoDeBusca = new ExpressaoPorData(data);
        } catch (java.time.format.DateTimeParseException e) {
            Cliente cliente = buscarCliente(termoBusca);
            if (cliente == null) return Collections.emptyList();
            expressaoDeBusca = new ExpressaoPorCliente(cliente);
        }
        return expressaoDeBusca.interpreter(new ContextoDeBusca(agenda));
    }

    /**
     * Lista todos os mecânicos disponíveis.
     *
     * @return lista de funcionários com cargo "Mecanico"
     */
    public List<Funcionario> listarMecanicosDisponiveis() {
        return gerenciadorFuncionario.listarTodos().stream()
                .filter(f -> "Mecanico".equals(f.getCargo()))
                .collect(Collectors.toList());
    }

    /**
     * Cria um novo agendamento.
     *
     * @param cliente cliente
     * @param carro carro
     * @param mecanico mecânico responsável
     * @param tipo tipo de serviço
     * @param dataHora data e hora do serviço
     * @return true se agendado
     */
    public boolean criarAgendamento(Cliente cliente, Carro carro, Funcionario mecanico, TipoServico tipo, LocalDateTime dataHora) {
        if (cliente == null || carro == null || mecanico == null || tipo == null || dataHora == null) return false;
        Agendamento novoAgendamento = new Agendamento(cliente, carro, mecanico, tipo, null, dataHora);
        return agenda.agendar(novoAgendamento);
    }

    /**
     * Cancela um agendamento. Se for no mesmo dia, aplica taxa.
     *
     * @param agendamento agendamento a cancelar
     * @return true se cancelado
     */
    public boolean cancelarAgendamento(Agendamento agendamento) {
        if (agendamento.getDataHora().toLocalDate().isEqual(LocalDate.now())) {
            double taxa = 150.0 * 0.20;
            String motivo = "Cancelamento no dia do serviço (" + LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ")";
            this.gerenciadorFinanceiro.registrarReceitaCancelamento(agendamento.getCliente().getNome(), taxa, motivo);
        }
        return agenda.cancelarAgendamento(agendamento);
    }

    /**
     * Lista todos os agendamentos da oficina.
     *
     * @return lista de agendamentos
     */
    public List<Agendamento> listarTodosAgendamentos() {
        return agenda.listarTodosAgendamentos();
    }

    public List<Elevador> listarElevadores() {
        // O método getElevadores() já existe e é estático na classe Elevador
        return Arrays.asList(Elevador.getElevadores());
    }
    // --- LÓGICA DE PONTO ---

    /**
     * Registra o ponto de entrada do funcionário logado.
     *
     * @return registro de ponto
     */
    public RegistroPonto baterPontoEntrada() {
        return gerenciadorPonto.baterPontoEntrada(Sessao.getInstance().getUsuarioLogado());
    }

    /**
     * Registra o ponto de saída do funcionário logado.
     *
     * @return registro de ponto
     */
    public RegistroPonto baterPontoSaida() {
        return gerenciadorPonto.baterPontoSaida(Sessao.getInstance().getUsuarioLogado());
    }

    /**
     * Lista os registros de ponto de hoje do funcionário logado.
     *
     * @return lista de registros do dia
     */
    public List<RegistroPonto> verRegistrosDeHoje() {
        Funcionario usuarioLogado = Sessao.getInstance().getUsuarioLogado();
        if (usuarioLogado == null) return Collections.emptyList();

        List<RegistroPonto> meusRegistros = gerenciadorPonto.getRegistrosPorFuncionario(usuarioLogado);
        return meusRegistros.stream()
                .filter(r -> r.getDataHoraEntrada().toLocalDate().isEqual(LocalDate.now()))
                .collect(Collectors.toList());
    }
        public GerenciadorOrdemDeServico getGerenciadorOS() {
        return gerenciadorOS;
    }
}
