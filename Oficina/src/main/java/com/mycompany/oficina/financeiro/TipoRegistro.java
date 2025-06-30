package com.mycompany.oficina.financeiro;

/**
 * Enumeração que define os tipos possíveis de registros financeiros
 * utilizados no sistema da oficina.
 *
 * Cada valor representa uma categoria de transação que pode ser registrada.
 */
public enum TipoRegistro {

    /**
     * Receita proveniente da realização de um serviço (ex: conclusão de uma OS).
     */
    RECEITA_SERVICO,

    /**
     * Receita gerada por taxas de cancelamento de agendamentos.
     */
    RECEITA_CANCELAMENTO,

    /**
     * Despesa referente ao pagamento de salários de funcionários.
     */
    DESPESA_SALARIO,

    /**
     * Despesa referente ao pagamento de comissões para mecânicos.
     */
    DESPESA_COMISSAO,

    /**
     * Despesa referente à reposição de peças no estoque.
     */
    DESPESA_PECAS;
}
