/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.ordemservico.stateOS;

import com.mycompany.oficina.loja.Produto;
import com.mycompany.oficina.ordemservico.OrdemDeServico;
import com.mycompany.oficina.ordemservico.PecaUtilizada;

/**
 * Representa o estado "Em Serviço" da Ordem de Serviço.
 * 
 * Nesse estado, o serviço está em execução e peças podem ser adicionadas à OS,
 * além da possibilidade de finalizar o serviço.
 */
public class EstadoEmServico extends EstadoBaseOS {

    /**
     * Construtor que recebe a Ordem de Serviço e passa para a superclasse.
     * 
     * @param os OrdemDeServico que estará no estado "Em Serviço"
     */
    public EstadoEmServico(OrdemDeServico os) {
        super(os);
    }

    /**
     * Adiciona uma peça à Ordem de Serviço, caso o estoque tenha quantidade suficiente.
     * Atualiza o estoque e registra a peça utilizada na OS.
     * 
     * @param produtoDoEstoque Produto que será utilizado na OS
     * @param quantidade Quantidade da peça a ser usada
     */
    @Override
    public void adicionarPeca(Produto produtoDoEstoque, int quantidade) {
        System.out.println("...Tentando adicionar " + quantidade + "x " + produtoDoEstoque.getNome() + " à OS #" + getOs().getNumeroOS());

        int estoqueAtual = produtoDoEstoque.getQuantidade();

        // Verifica se há estoque suficiente para a quantidade solicitada
        if (estoqueAtual >= quantidade) {
            // Atualiza o estoque subtraindo a quantidade utilizada
            produtoDoEstoque.setQuantidade(estoqueAtual - quantidade);

            // Cria o objeto PecaUtilizada para registrar o uso da peça na OS
            PecaUtilizada pecaParaOS = new PecaUtilizada(produtoDoEstoque, quantidade);

            // Adiciona a peça usada na lista de peças da OS
            getOs().getListaDePecasUtilizadas().add(pecaParaOS);

            System.out.println("...Peça registrada e estoque atualizado com sucesso!");
        } else {
            // Caso o estoque seja insuficiente, exibe mensagem de erro
            System.err.println("!!! FALHA: Estoque insuficiente para a peça '" + produtoDoEstoque.getNome() + "'.");
        }
    }

    /**
     * Finaliza o serviço, mudando o estado da OS para "Finalizada".
     */
    @Override
    public void finalizarServico() {
        getOs().setEstado(new EstadoFinalizada(getOs())); // Transita direto para estado Finalizada
    }

    /**
     * Retorna o status atual da OS como "Em Serviço".
     * 
     * @return String representando o estado atual
     */
    @Override
    public String getStatus() {
        return "Em Serviço";
    }
}
