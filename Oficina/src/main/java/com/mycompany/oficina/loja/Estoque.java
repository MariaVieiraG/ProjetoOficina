/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.loja;

import com.google.gson.reflect.TypeToken;
import com.mycompany.oficina.persistencia.PersistenciaJson; 

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa o estoque de produtos da oficina.
 * Responsável por gerenciar a lista de produtos, incluindo
 * operações de adicionar, editar, remover e buscar produtos,
 * além de salvar e carregar os dados utilizando persistência em JSON.
 */
public class Estoque {

    // Lista interna que armazena os produtos no estoque
    private List<Produto> produtos;

    // Objeto responsável pela persistência dos dados em arquivo JSON
    private PersistenciaJson persistencia;

    /**
     * Construtor que recebe a instância de PersistenciaJson para carregar
     * os dados do estoque a partir do arquivo JSON.
     *
     * @param persistencia objeto responsável por carregar e salvar os dados
     */
    public Estoque(PersistenciaJson persistencia) {
        this.persistencia = persistencia;
        // Carrega a lista de produtos salva no arquivo "estoque"
        this.produtos = this.persistencia.carregarLista("estoque", new TypeToken<ArrayList<Produto>>() {});
    }

    /**
     * Salva o estado atual da lista de produtos no arquivo JSON.
     * Deve ser chamado sempre que o estoque for modificado.
     */
    public void salvarEstoque() {
        this.persistencia.salvarLista("estoque", this.produtos);
    }

    /**
     * Retorna a lista de produtos atualmente no estoque.
     * @return lista dos produtos
     */
    public List<Produto> getProdutos() {
        return produtos;
    }

    /**
     * Adiciona um novo produto à lista e salva imediatamente a alteração.
     * @param produto novo produto a ser adicionado
     */
    public void adicionarProduto(Produto produto) {
        produtos.add(produto);
        salvarEstoque(); // Salva imediatamente após adicionar
    }

    /**
     * Cadastra um novo produto somente se não existir outro com o mesmo ID.
     * @param produto produto a ser cadastrado
     * @return true se cadastro for bem sucedido, false caso o ID já exista
     */
    public boolean cadastrarProduto(Produto produto) {
        if (buscarProduto(produto.getIdProduto()) != null) {
            // Produto com esse ID já existe
            return false;
        }
        // Adiciona o produto e salva a lista
        adicionarProduto(produto);
        return true;
    }

    /**
     * Edita os dados de um produto existente identificado pelo ID.
     * Se o produto for encontrado, atualiza seus atributos e salva o estoque.
     * @param idProduto ID do produto a ser editado
     * @param novoNome novo nome do produto
     * @param novoPreco novo preço do produto
     * @param novaQuantidade nova quantidade em estoque
     * @param novoFornecedor novo fornecedor do produto
     * @return true se o produto foi editado com sucesso, false caso não exista
     */
    public boolean editarProduto(String idProduto, String novoNome, double novoPreco, int novaQuantidade, String novoFornecedor) {
        Produto produto = buscarProduto(idProduto);
        if (produto != null) {
            produto.setNome(novoNome);
            produto.setPreco(novoPreco);
            produto.setQuantidade(novaQuantidade);
            produto.setFornecedor(novoFornecedor);
            salvarEstoque(); // Salva após edição
            return true;
        }
        return false;
    }

    /**
     * Remove um produto da lista baseado no seu ID.
     * Se removido, salva imediatamente o estoque atualizado.
     * @param idProduto ID do produto a ser removido
     * @return true se o produto foi removido, false se não foi encontrado
     */
    public boolean removerProduto(String idProduto) {
        Produto produto = buscarProduto(idProduto);
        if (produto != null) {
            produtos.remove(produto);
            salvarEstoque(); // Salva após remoção
            return true;
        }
        return false;
    }

    /**
     * Busca um produto na lista pelo seu ID.
     * @param idProduto ID do produto buscado
     * @return o produto encontrado ou null se não existir
     */
    public Produto buscarProduto(String idProduto) {
        for (Produto p : produtos) {
            if (p.getIdProduto().equals(idProduto)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Retorna uma nova lista contendo todos os produtos do estoque.
     * Essa cópia evita modificações externas diretas na lista interna.
     * @return lista dos produtos
     */
    public List<Produto> listarProdutos() {
        return new ArrayList<>(produtos);
    }
}
