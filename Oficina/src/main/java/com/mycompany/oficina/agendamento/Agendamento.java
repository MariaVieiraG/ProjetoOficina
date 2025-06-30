package com.mycompany.oficina.agendamento;

import com.mycompany.oficina.entidades.Carro;
import com.mycompany.oficina.entidades.Cliente;
import com.mycompany.oficina.entidades.Elevador;
import com.mycompany.oficina.entidades.Funcionario;

import java.time.LocalDateTime;

/**
 * Representa um agendamento de serviço imutável na oficina.
 * <p>
 * Contém todas as informações pertinentes a um serviço, como cliente, veículo,
 * mecânico, tipo de serviço e a data e hora exatas.
 *
 * Autor: Miguel
 */
public final class Agendamento {

    // Cliente que solicitou o serviço
    private final Cliente cliente;

    // Carro que será atendido no serviço
    private final Carro carro;

    // Funcionário (mecânico) responsável pelo atendimento
    private final Funcionario mecanico;

    // Tipo de serviço que será realizado
    private final TipoServico tipoServico;

    // Elevador da oficina reservado para o serviço
    private final Elevador elevador;

    // Data e hora marcadas para a realização do serviço
    private final LocalDateTime dataHora;

    /**
     * Retorna uma representação em texto do agendamento,
     * incluindo os dados principais como cliente, carro, mecânico,
     * tipo de serviço, elevador e data/hora.
     */
    @Override
    public String toString() {
        return "Agendamento{" +
                "cliente=" + cliente +
                ", carro=" + carro +
                ", mecanico=" + mecanico +
                ", tipoServico=" + tipoServico +
                ", elevador=" + elevador +
                ", dataHora=" + dataHora +
                '}';
    }

    /**
     * Cria um novo agendamento com os dados fornecidos.
     * 
     * @param cliente Cliente que agendou o serviço
     * @param carro Carro que será atendido
     * @param mecanico Mecânico responsável
     * @param tipoServico Tipo de serviço a ser feito
     * @param elevador Elevador a ser utilizado
     * @param dataHora Data e hora marcadas para o serviço
     */
    public Agendamento(Cliente cliente, Carro carro, Funcionario mecanico, TipoServico tipoServico, Elevador elevador, LocalDateTime dataHora) {
        this.cliente = cliente;
        this.carro = carro;
        this.mecanico = mecanico;
        this.tipoServico = tipoServico;
        this.elevador = elevador;
        this.dataHora = dataHora;
    }

    // Retorna o cliente do agendamento
    public Cliente getCliente() {
        return cliente;
    }

    // Retorna o carro que será atendido
    public Carro getCarro() {
        return carro;
    }

    // Retorna o mecânico responsável pelo serviço
    public Funcionario getMecanico() {
        return mecanico;
    }

    // Retorna o tipo de serviço a ser realizado
    public TipoServico getTipoServico() {
        return tipoServico;
    }

    // Retorna o elevador reservado para o serviço
    public Elevador getElevador() {
        return elevador;
    }

    // Retorna a data e hora do agendamento
    public LocalDateTime getDataHora() {
        return dataHora;
    }
}
