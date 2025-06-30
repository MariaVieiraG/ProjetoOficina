package com.mycompany.oficina.ordemservico;

import com.mycompany.oficina.entidades.Carro;
import com.mycompany.oficina.entidades.Cliente;
import com.mycompany.oficina.entidades.Entidades;
import com.mycompany.oficina.entidades.Funcionario;
import com.mycompany.oficina.loja.Produto;
import com.mycompany.oficina.ordemservico.ObserverOS.Assunto;
import com.mycompany.oficina.ordemservico.ObserverOS.Observador;
import com.mycompany.oficina.ordemservico.stateOS.EstadoAguardando;
import com.mycompany.oficina.ordemservico.stateOS.EstadoOS;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter; // Importar para formatar a data
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa uma Ordem de Serviço na oficina.
 * 
 * Essa classe mantém todas as informações necessárias para gerenciar
 * um atendimento, incluindo cliente, veículo, mecânico responsável,
 * defeito relatado, peças utilizadas, serviços realizados, datas de abertura
 * e fechamento, além do estado atual da ordem.
 * 
 * Implementa os padrões de projeto Observer (para notificar mudanças de estado)
 * e State (para delegar comportamentos conforme o estado atual da OS).
 * Também implementa as interfaces Assunto e Entidades para integração com o sistema.
 * 
 * A classe permite calcular o valor total do serviço, gerar extratos detalhados
 * e gerenciar o ciclo de vida da ordem, delegando ações ao estado corrente.
 */

public class OrdemDeServico implements Assunto, Entidades {

    private String numeroOS; // Número identificador único da ordem de serviço
    private static int contadorNumeroOS; // Contador estático para gerar números únicos das OS
    private final Cliente cliente; // Cliente relacionado à ordem de serviço
    private final Carro carro; // Carro que será atendido na ordem de serviço
    private final Funcionario mecanicoResponsavel; // Funcionário (mecânico) responsável pelo serviço
    private final String defeitoRelatado; // Descrição do defeito informado pelo cliente
    private final LocalDateTime dataAbertura; // Data e hora de abertura da OS
    private LocalDateTime dataFechamento; // Data e hora de fechamento da OS (pode ser nula se não finalizada)
    private final List<String> servicosRealizados; // Lista de serviços realizados na OS
    private final List<PecaUtilizada> pecasUtilizadas; // Lista de peças utilizadas na OS
    private transient EstadoOS estadoAtual; // Estado atual da ordem (padrão State)
    private final List<Observador> observadores = new ArrayList<>(); // Lista de observadores (padrão Observer)

    // 1. CONSTANTE PARA O VALOR FIXO DA MÃO DE OBRA
    private static final double VALOR_MAO_DE_OBRA = 150.0;
    private String nomeDaClasseDoEstado;

    /**
     * Construtor principal da Ordem de Serviço.
     * Inicializa os dados e já gera um número único para a OS.
     * Define o estado inicial como EstadoAguardando.
     */
    public OrdemDeServico(String numeroOS, Cliente cliente, Carro carro, Funcionario mecanicoResponsavel, String defeitoRelatado, LocalDateTime dataAbertura, LocalDateTime dataFechamento, List servicosRealizados, List pecasUtilizadas) {
        this.numeroOS = "Ordem-Serviço" + String.format("%03d", contadorNumeroOS++);
        this.cliente = cliente;
        this.carro = carro;
        this.mecanicoResponsavel = mecanicoResponsavel;
        this.defeitoRelatado = defeitoRelatado;
        this.dataAbertura = dataAbertura;
        this.dataFechamento = dataFechamento;
        this.servicosRealizados = servicosRealizados;
        this.pecasUtilizadas = pecasUtilizadas;
        setEstado(new EstadoAguardando(this)); // Define estado inicial da OS
    }


    private EstadoOS getEstado() {
        // Se o estado é nulo (porque o objeto foi carregado e o campo era 'transient')...
        if (estadoAtual == null) {
            // ...verificamos qual era o nome da classe do estado que salvamos no JSON.
            if (nomeDaClasseDoEstado != null && !nomeDaClasseDoEstado.isEmpty()) {
                try {
                    // Usa o nome da classe para criar uma nova instância do estado correto
                    Class<?> clazz = Class.forName(nomeDaClasseDoEstado);
                    // Assume que todos os estados têm um construtor que aceita OrdemDeServico
                    this.estadoAtual = (EstadoOS) clazz.getConstructor(OrdemDeServico.class).newInstance(this);
                } catch (Exception e) {
                    // Se algo der errado (ex: classe não encontrada), volta para o estado padrão.
                    this.estadoAtual = new EstadoAguardando(this);
                }
            } else {
                // Se até a "pista" (o nome da classe) for nula, define o estado padrão.
                this.estadoAtual = new EstadoAguardando(this);
            }
        }
        return estadoAtual;
    }
    /**
     * Atualiza o estado atual da OS e notifica os observadores da mudança.
     * @param novoEstado novo estado da OS
     */
    public final void setEstado(EstadoOS novoEstado) {
        this.estadoAtual = novoEstado;
        if (novoEstado != null) {
            // Salva o nome completo da classe como nossa "pista"
            this.nomeDaClasseDoEstado = novoEstado.getClass().getName();
        }
        this.notificarObservadores(); // Notifica sobre a mudança
    }

    /**
     * Calcula o valor total da ordem de serviço, somando o custo das peças utilizadas mais o valor fixo da mão de obra.
     * @return valor total da OS
     */
    public double calcularValorTotal() {
        double totalPecas = 0.0;
        for (PecaUtilizada peca : pecasUtilizadas) {
            totalPecas += peca.getSubtotal();
        }
        return totalPecas + VALOR_MAO_DE_OBRA;
    }

    /**
     * Gera um extrato detalhado da ordem de serviço, com informações formatadas sobre peças, serviços, valores e status.
     * @return String formatada contendo o extrato completo da OS
     */
    public String gerarExtrato() {
        StringBuilder extrato = new StringBuilder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        extrato.append("\n================[ EXTRATO DA ORDEM DE SERVIÇO ]================\n");
        extrato.append("OS Nº: ").append(getNumeroOS()).append("\n");
        extrato.append("Data de Abertura: ").append(dtf.format(dataAbertura)).append("\n");
        extrato.append("Cliente: ").append(getCliente().getNome()).append("\n");
        extrato.append("Veículo: ").append(getCarro().getModelo()).append(" | Placa: ").append(getCarro().getPlaca()).append("\n");
        extrato.append("Status: ").append(getStatusAtual()).append("\n");
        extrato.append("-----------------------------------------------------------------\n");
        extrato.append("ITENS E SERVIÇOS:\n\n");

        if (pecasUtilizadas.isEmpty()) {
            extrato.append("  - Nenhuma peça utilizada.\n");
        } else {
            extrato.append(String.format("  %-15s %-5s %-10s %-10s\n", "Peça", "Qtd.", "Preço Un.", "Subtotal"));
            extrato.append("  -------------------------------------------------------------\n");
            for (PecaUtilizada peca : pecasUtilizadas) {
                extrato.append(String.format("  %-15s %-5d R$ %-8.2f R$ %-8.2f\n",
                        peca.getProdutoOriginal().getNome(),
                        peca.getQuantidadeUtilizada(),
                        peca.getPrecoNoMomentoDoUso(),
                        peca.getSubtotal()));
            }
        }

        extrato.append("\n");
        extrato.append(String.format("  + Mão de Obra (Valor Fixo): .................... R$ %.2f\n", VALOR_MAO_DE_OBRA));
        extrato.append("-----------------------------------------------------------------\n");
        extrato.append(String.format("  VALOR TOTAL A PAGAR: .......................... R$ %.2f\n", calcularValorTotal()));
        extrato.append("=================================================================\n");

        return extrato.toString();
    }


    // --- Métodos que delegam ações para o estado atual da OS (padrão State) ---
    public void iniciarInspecao() { this.estadoAtual.iniciarInspecao(); }
    public void iniciarServico() { this.estadoAtual.iniciarServico(); }
    public void adicionarPeca(Produto produtoDoEstoque, int quantidade) { this.estadoAtual.adicionarPeca(produtoDoEstoque, quantidade); }
    public void finalizarServico() { this.estadoAtual.finalizarServico(); }
    public void cancelar(String motivo) { this.estadoAtual.cancelar(motivo); }





    // Getters de atributos importantes para acesso externo
    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public Funcionario getMecanicoResponsavel() {
        return mecanicoResponsavel;
    }

    // --- Implementação do padrão Observer ---
    @Override
    public void adicionarObservador(Observador observador) { this.observadores.add(observador); }
    @Override
    public void removerObservador(Observador observador) { this.observadores.remove(observador); }
    @Override
    public void notificarObservadores() { observadores.forEach(obs -> obs.atualizar(this)); }

    // --- Outros getters ---
    public List<PecaUtilizada> getListaDePecasUtilizadas() { return pecasUtilizadas; }
    public String getNumeroOS() { return numeroOS; }
    public Cliente getCliente() { return cliente; }
    public Carro getCarro() { return carro; }
    public String getStatusAtual() { return getEstado().getStatus(); }

    /**
     * Retorna o identificador da OS, usado por entidades no sistema.
     */
    @Override
    public String getIdentificador() {
        return this.getNumeroOS();
    }
}
