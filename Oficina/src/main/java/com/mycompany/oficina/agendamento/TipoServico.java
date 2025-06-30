package com.mycompany.oficina.agendamento;

/**
 * Enumeração que representa os tipos de serviços oferecidos na oficina.
 * O uso de um enum garante a consistência e a segurança de tipos para os serviços em todo o sistema.
 */
public enum TipoServico {

    /** Serviço de alinhamento das rodas do veículo para garantir que todas estejam paralelas, melhorando a dirigibilidade e evitando desgaste irregular dos pneus. */
    ALINHAMENTO,

    /** Serviço de balanceamento das rodas que corrige o peso desigual nos pneus e rodas, evitando vibrações e melhorando a estabilidade do veículo. */
    BALANCEAMENTO,

    /** Troca do óleo do motor, serviço essencial para manter a lubrificação adequada, reduzindo o desgaste das peças e melhorando o desempenho do motor. */
    TROCADEOLEO,

    /** Inspeção geral do veículo, onde são verificados diversos sistemas para garantir a segurança e funcionamento correto do automóvel. */
    INSPECAO,

    /** Reparo de algum problema identificado, abrangendo consertos diversos que possam ser necessários para restaurar o funcionamento do veículo. */
    REPARO;
}

