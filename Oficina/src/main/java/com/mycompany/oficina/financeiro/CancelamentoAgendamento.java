package com.mycompany.oficina.financeiro;

import com.mycompany.oficina.entidades.Cliente;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Representa um registro de cancelamento de agendamento feito por um cliente.
 * Armazena informações sobre o cliente, valor cobrado, data da cobrança e o motivo do cancelamento.
 */
public class CancelamentoAgendamento {

    /**
     * Cliente que realizou o cancelamento.
     */
    private final Cliente cliente;

    /**
     * Valor cobrado pelo cancelamento.
     */
    private final double valor;

    /**
     * Data em que a cobrança foi registrada.
     */
    private final LocalDate dataCobranca;

    /**
     * Motivo informado para o cancelamento.
     */
    private final String motivo;

    /**
     * Construtor que inicializa o cancelamento com os dados fornecidos.
     * A data de cobrança é definida automaticamente como a data atual.
     *
     * @param cliente Cliente que cancelou o agendamento.
     * @param valor Valor da cobrança pelo cancelamento.
     * @param motivo Motivo do cancelamento.
     */
    public CancelamentoAgendamento(Cliente cliente, double valor, String motivo) {
        this.cliente = cliente;
        this.valor = valor;
        this.motivo = motivo;
        this.dataCobranca = LocalDate.now();
    }

    /**
     * Retorna o cliente responsável pelo cancelamento.
     *
     * @return Cliente.
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Retorna o valor cobrado pelo cancelamento.
     *
     * @return Valor da cobrança.
     */
    public double getValor() {
        return valor;
    }

    /**
     * Retorna a data em que a cobrança foi registrada.
     *
     * @return Data da cobrança.
     */
    public LocalDate getDataCobranca() {
        return dataCobranca;
    }

    /**
     * Retorna o motivo informado para o cancelamento.
     *
     * @return Motivo do cancelamento.
     */
    public String getMotivo() {
        return motivo;
    }

    /**
     * Retorna uma representação em texto do cancelamento, formatada com data, nome do cliente,
     * valor da cobrança e motivo.
     *
     * @return String formatada com os dados do cancelamento.
     */
    @Override
    public String toString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("Data: %s | Cliente: %s | Valor: R$ %.2f | Motivo: %s",
                dtf.format(dataCobranca),
                cliente.getNome(),
                valor,
                motivo
        );
    }
}