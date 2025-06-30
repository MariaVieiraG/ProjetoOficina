package com.mycompany.oficina.financeiro;


import com.mycompany.oficina.entidades.Funcionario;
import com.mycompany.oficina.ordemservico.OrdemDeServico;
import com.mycompany.oficina.persistencia.PersistenciaJson;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.reflect.TypeToken;

/**
 * Classe responsável pelo controle financeiro da oficina.
 * É um Singleton que armazena e gerencia todas as transações financeiras
 * (receitas, despesas, comissões, salários, etc.) e gera relatórios.
 */
public class GerenciadorFinanceiro {

    /**
     * Instância única do GerenciadorFinanceiro (Singleton).
     */
    private static GerenciadorFinanceiro instance;

    /**
     * Lista com todos os registros financeiros salvos.
     */
    private final List<RegistroFinanceiro> registros;

    /**
     * Objeto responsável por persistência em JSON.
     */
    private final PersistenciaJson persistencia;

    /**
     * Formato de data padrão para exibição nos relatórios.
     */
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Construtor privado que inicializa o gerenciador com persistência e carrega os dados existentes.
     *
     * @param persistencia Instância de persistência JSON.
     */
    private GerenciadorFinanceiro(PersistenciaJson persistencia) {
        this.persistencia = persistencia;
        this.registros = this.persistencia.carregarLista("financeiro", new TypeToken<ArrayList<RegistroFinanceiro>>() {});
    }
    
    
    public List<RegistroFinanceiro> getRegistros() {
        return registros;
    }
    
    

    /**
     * Retorna a instância única do GerenciadorFinanceiro.
     * Se não existir, cria uma nova com a persistência informada.
     *
     * @param persistencia Objeto de persistência para carregar/salvar dados.
     * @return Instância única do gerenciador.
     */
    public static GerenciadorFinanceiro getInstance(PersistenciaJson persistencia) {
        if (instance == null) {
            instance = new GerenciadorFinanceiro(persistencia);
        }
        return instance;
    }

    /**
     * Salva a lista atual de registros no arquivo JSON.
     */
    private void salvar() {
        persistencia.salvarLista("financeiro", this.registros);
    }

    // ============================ REGISTROS DE TRANSAÇÕES ============================

    /**
     * Registra uma receita por taxa de cancelamento de agendamento.
     *
     * @param clienteNome Nome do cliente que cancelou.
     * @param valor Valor cobrado.
     * @param motivo Justificativa do cancelamento.
     */
    public void registrarReceitaCancelamento(String clienteNome, double valor, String motivo) {
        String descricao = "Taxa de cancelamento para cliente " + clienteNome + ". Motivo: " + motivo;
        registros.add(new RegistroFinanceiro(descricao, valor, TipoRegistro.RECEITA_CANCELAMENTO, LocalDateTime.now()));
        salvar();
    }

    /**
     * Registra o faturamento de uma Ordem de Serviço e a despesa com a comissão do mecânico.
     *
     * @param os Ordem de Serviço finalizada.
     */
    public void registrarFaturamentoOS(OrdemDeServico os) {
        String descReceita = "Receita da OS #" + os.getNumeroOS() + " para cliente " + os.getCliente().getNome();
        registros.add(new RegistroFinanceiro(descReceita, os.calcularValorTotal(), TipoRegistro.RECEITA_SERVICO, os.getDataAbertura()));

        double comissao = os.calcularValorTotal() * 0.05;
        String descComissao = "Comissão (5%) da OS #" + os.getNumeroOS() + " para mecânico " + os.getMecanicoResponsavel().getNome();
        registros.add(new RegistroFinanceiro(descComissao, comissao, TipoRegistro.DESPESA_COMISSAO, os.getDataAbertura()));

        salvar();
    }

    /**
     * Registra a despesa com a compra de peças para o estoque.
     *
     * @param notaFiscal Descrição ou número da nota fiscal.
     * @param valorTotal Valor total da compra.
     */
    public void registrarDespesaCompraPecas(String notaFiscal, double valorTotal) {
        registros.add(new RegistroFinanceiro(notaFiscal, valorTotal, TipoRegistro.DESPESA_PECAS, LocalDateTime.now()));
        salvar();
    }

    /**
     * Registra o pagamento de salários para funcionários da oficina.
     * Atendentes recebem R$ 1000 e mecânicos R$ 1500.
     *
     * @param todosFuncionarios Lista de funcionários ativos.
     */
    public void pagarSalarios(List<Funcionario> todosFuncionarios) {
        for (Funcionario f : todosFuncionarios) {
            double salario = 0;
            if ("Atendente".equals(f.getCargo())) salario = 1000;
            if ("Mecanico".equals(f.getCargo())) salario = 1500;

            if (salario > 0) {
                registros.add(new RegistroFinanceiro("Salário de " + f.getNome(), salario, TipoRegistro.DESPESA_SALARIO, LocalDateTime.now()));
            }
        }
        System.out.println("Folha de pagamento registrada.");
        salvar();
    }

    // ============================ RELATÓRIOS ============================

    /**
     * Gera um relatório com todas as receitas de serviços dentro de um período.
     *
     * @param inicio Data de início do período.
     * @param fim Data de fim do período.
     */
    public void emitirRelatorioServicos(LocalDate inicio, LocalDate fim) {
        System.out.println("\n--- Relatório de Serviços de " + inicio.format(dtf) + " a " + fim.format(dtf) + " ---");

        List<RegistroFinanceiro> servicos = registros.stream()
                .filter(r -> r.getTipo() == TipoRegistro.RECEITA_SERVICO)
                .filter(r -> !r.getData().toLocalDate().isBefore(inicio) && !r.getData().toLocalDate().isAfter(fim))
                .toList();

        if (servicos.isEmpty()) {
            System.out.println("Nenhum serviço encontrado no período.");
            return;
        }

        servicos.forEach(s -> System.out.printf("Data: %s | Descrição: %s | Valor: R$ %.2f\n",
                s.getData().format(dtf), s.getDescricao(), s.getValor()));
    }

    /**
     * Emite um balanço geral entre receitas e despesas dentro de um período.
     *
     * @param inicio Data inicial.
     * @param fim Data final.
     */
    public void emitirBalanco(LocalDate inicio, LocalDate fim) {
        List<RegistroFinanceiro> registrosPeriodo = registros.stream()
                .filter(r -> !r.getData().toLocalDate().isBefore(inicio) && !r.getData().toLocalDate().isAfter(fim))
                .toList();

        double receitas = registrosPeriodo.stream()
                .filter(r -> r.getTipo() == TipoRegistro.RECEITA_SERVICO || r.getTipo() == TipoRegistro.RECEITA_CANCELAMENTO)
                .mapToDouble(RegistroFinanceiro::getValor)
                .sum();

        double despesas = registrosPeriodo.stream()
                .filter(r -> r.getTipo() != TipoRegistro.RECEITA_SERVICO && r.getTipo() != TipoRegistro.RECEITA_CANCELAMENTO)
                .mapToDouble(RegistroFinanceiro::getValor)
                .sum();

        System.out.println("\n--- Balanço Financeiro de " + inicio.format(dtf) + " a " + fim.format(dtf) + " ---");
        System.out.printf("Total de Receitas: R$ %.2f\n", receitas);
        System.out.printf("Total de Despesas: R$ %.2f\n", despesas);
        System.out.println("----------------------------------------");
        System.out.printf("Lucro (Salário do Gerente): R$ %.2f\n", (receitas - despesas));
        System.out.println("----------------------------------------");
    }

    /**
     * Emite um relatório detalhado com todas as despesas no período especificado.
     *
     * @param inicio Data inicial.
     * @param fim Data final.
     */
    public void emitirRelatorioDespesasDetalhado(LocalDate inicio, LocalDate fim) {
        System.out.println("\n--- Relatório Detalhado de Despesas de " + inicio.format(dtf) + " a " + fim.format(dtf) + " ---");

        List<RegistroFinanceiro> despesasPeriodo = registros.stream()
                .filter(r -> r.getTipo() != TipoRegistro.RECEITA_SERVICO && r.getTipo() != TipoRegistro.RECEITA_CANCELAMENTO)
                .filter(r -> !r.getData().toLocalDate().isBefore(inicio) && !r.getData().toLocalDate().isAfter(fim))
                .toList();

        if (despesasPeriodo.isEmpty()) {
            System.out.println("Nenhuma despesa encontrada no período.");
            return;
        }

        for (RegistroFinanceiro despesa : despesasPeriodo) {
            System.out.printf("Data: %s | Tipo: %-20s | Valor: R$ %-10.2f | Descrição: %s\n",
                    despesa.getData().format(dtf),
                    despesa.getTipo().name(),
                    despesa.getValor(),
                    despesa.getDescricao()
            );
        }

        double totalDespesas = despesasPeriodo.stream().mapToDouble(RegistroFinanceiro::getValor).sum();
        System.out.println("----------------------------------------------------------------------------------");
        System.out.printf("Total de Despesas no Período: R$ %.2f\n", totalDespesas);
        System.out.println("----------------------------------------------------------------------------------");
    }
}
