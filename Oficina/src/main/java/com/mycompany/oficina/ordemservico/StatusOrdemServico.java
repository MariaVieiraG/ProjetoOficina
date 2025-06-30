/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.mycompany.oficina.ordemservico;

// Enumeração que representa os diferentes status que uma Ordem de Serviço pode ter
public enum StatusOrdemServico {
    // Status que indica que a Ordem de Serviço está aberta, ainda não iniciada
    ABERTA,

    // Status que indica que a Ordem de Serviço está em andamento, ou seja, o serviço está sendo realizado
    EM_ANDAMENTO,

    // Status que indica que a Ordem de Serviço está aguardando peças para continuar o serviço
    AGUARDANDO_PECAS,

    // Status que indica que a Ordem de Serviço foi concluída com sucesso
    CONCLUIDA,

    // Status que indica que a Ordem de Serviço foi cancelada antes da conclusão
    CANCELADA;

    // Método estático para retornar o status ABERTA
    public static StatusOrdemServico getABERTA() {
        return ABERTA;
    }

    // Método estático para retornar o status EM_ANDAMENTO
    public static StatusOrdemServico getEM_ANDAMENTO() {
        return EM_ANDAMENTO;
    }

    // Método estático para retornar o status AGUARDANDO_PECAS
    public static StatusOrdemServico getAGUARDANDO_PECAS() {
        return AGUARDANDO_PECAS;
    }

    // Método estático para retornar o status CONCLUIDA
    public static StatusOrdemServico getCONCLUIDA() {
        return CONCLUIDA;
    }

    // Método estático para retornar o status CANCELADA
    public static StatusOrdemServico getCANCELADA() {
        return CANCELADA;
    }
}
