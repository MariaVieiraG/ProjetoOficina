package com.mycompany.oficina.ordemservico;

import com.google.gson.reflect.TypeToken;
import com.mycompany.oficina.controlador.GerenciadorGenerico;
import com.mycompany.oficina.entidades.Carro;
import com.mycompany.oficina.entidades.Cliente;
import com.mycompany.oficina.entidades.Funcionario;
import com.mycompany.oficina.persistencia.PersistenciaJson;

import java.time.LocalDateTime;
import java.util.ArrayList;


/**
 * Classe responsável por gerenciar as Ordens de Serviço (OS).
 * Estende uma classe genérica de gerenciador que trata persistência e operações básicas.
 */
public class GerenciadorOrdemDeServico extends GerenciadorGenerico<OrdemDeServico> {

    /**
     * Construtor que recebe a persistência e configura o nome do arquivo JSON
     * e o tipo específico de lista que será manipulado.
     *
     * @param persistencia Instância para lidar com leitura e gravação JSON
     */
    public GerenciadorOrdemDeServico(PersistenciaJson persistencia) {
        // Passa para o construtor da superclasse o nome do arquivo e o tipo da lista
        super(persistencia, "ordens_servico", new TypeToken<ArrayList<OrdemDeServico>>() {});
    }

    /**
     * Cria uma nova Ordem de Serviço e a adiciona na lista gerenciada.
     *
     * @param cliente Cliente associado à OS
     * @param carro Veículo relacionado à OS
     * @param mecanico Funcionário (mecânico) responsável
     * @param defeito Descrição do defeito relatado
     * @return A nova OrdemDeServico criada ou null se dados inválidos
     */
    public OrdemDeServico abrirOS(Cliente cliente, Carro carro, Funcionario mecanico, String defeito) {
        // Valida se os dados obrigatórios foram informados
        if (cliente == null || carro == null || mecanico == null || defeito == null || defeito.trim().isEmpty()) {
            return null;
        }

        // Cria uma nova ordem com os dados fornecidos, usando horário atual como data de abertura
        OrdemDeServico novaOS = new OrdemDeServico(
            null,             // Número da OS pode ser gerado depois (ou dentro do construtor)
            cliente,
            carro,
            mecanico,
            defeito,
            LocalDateTime.now(),
            null,             // Data de finalização inicialmente nula
            new ArrayList<>(), // Lista vazia para peças usadas inicialmente
            new ArrayList<>()  // Lista vazia para serviços realizados inicialmente
        );

        // Adiciona a nova OS na lista gerenciada pela superclasse e salva no JSON
        super.adicionar(novaOS);

        return novaOS;
    }
    public void salvarAlteracoesOS(){
        super.salvarAlteracoes();
    }
}

