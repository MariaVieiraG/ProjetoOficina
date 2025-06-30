package com.mycompany.oficina.financeiro;


import java.time.LocalDateTime;

/**
 * Representa um registro financeiro do sistema da oficina.
 * Pode ser uma receita (ex: serviço realizado, taxa de cancelamento) ou uma despesa (ex: salário, peças, comissão).
 */
public class RegistroFinanceiro {

    /**
     * Descrição textual da transação financeira.
     */
    private final String descricao;

    /**
     * Valor da transação.
     */
    private final double valor;

    /**
     * Tipo da transação (receita ou despesa).
     */
    private final TipoRegistro tipo;

    /**
     * Data e hora em que a transação foi registrada.
     */
    private final LocalDateTime data;

    /**
     * Construtor da classe RegistroFinanceiro.
     *
     * @param descricao Descrição da transação.
     * @param valor Valor monetário da transação.
     * @param tipo Tipo da transação (definido pelo enum {@link TipoRegistro}).
     * @param data Data e hora do registro.
     */
    public RegistroFinanceiro(String descricao, double valor, TipoRegistro tipo, LocalDateTime data) {
        this.descricao = descricao;
        this.valor = valor;
        this.tipo = tipo;
        this.data = data;
    }

    /**
     * Retorna a descrição da transação.
     *
     * @return Descrição textual.
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Retorna o valor monetário da transação.
     *
     * @return Valor da transação.
     */
    public double getValor() {
        return valor;
    }

    /**
     * Retorna o tipo da transação (receita ou despesa).
     *
     * @return Tipo da transação.
     */
    public TipoRegistro getTipo() {
        return tipo;
    }

    /**
     * Retorna a data e hora em que a transação foi registrada.
     *
     * @return Data e hora do registro.
     */
    public LocalDateTime getData() {
        return data;
    }
}
