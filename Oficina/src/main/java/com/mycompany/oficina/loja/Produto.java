/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.loja;

/**
 * Representa um produto do estoque com atributos básicos como
 * ID, nome, preço, quantidade disponível e fornecedor.
 * O ID é gerado automaticamente no formato "PR-001", "PR-002", etc.
 */
public class Produto {
    // Contador estático para gerar IDs únicos sequenciais para cada produto criado
    private static int contadorId = 1;

    // Identificador único do produto no formato "PR-XXX"
    private String idProduto;

    // Nome do produto
    private String nome;

    // Preço unitário do produto
    private double preco;

    // Quantidade disponível em estoque
    private int quantidade;

    // Nome do fornecedor do produto
    private String fornecedor;

    /**
     * Construtor para criar um novo produto com dados fornecidos.
     * O ID é gerado automaticamente no formato "PR-XXX" usando o contador estático.
     *
     * @param nome Nome do produto
     * @param preco Preço unitário do produto
     * @param quantidade Quantidade disponível em estoque
     * @param fornecedor Nome do fornecedor do produto
     */
    public Produto(String nome, double preco, int quantidade, String fornecedor) {
        this.idProduto = "PR-" + String.format("%03d", contadorId++);
        this.nome = nome;
        this.preco = preco;
        this.fornecedor = fornecedor;
        this.quantidade = quantidade;
    }

    /**
     * Retorna o valor atual do contador de produtos.
     * Pode ser útil para saber quantos produtos foram criados.
     *
     * @return Contador atual de produtos criados
     */
    public static int getContador() {
        return contadorId;
    }

    /**
     * Retorna o ID único do produto.
     *
     * @return ID do produto
     */
    public String getIdProduto() {
        return idProduto;
    }

    /**
     * Retorna o nome do produto.
     *
     * @return Nome do produto
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define um novo nome para o produto.
     *
     * @param nome Novo nome do produto
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Retorna o preço atual do produto.
     *
     * @return Preço do produto
     */
    public double getPreco() {
        return preco;
    }

    /**
     * Define um novo preço para o produto.
     *
     * @param preco Novo preço do produto
     */
    public void setPreco(double preco) {
        this.preco = preco;
    }

    /**
     * Retorna a quantidade disponível do produto no estoque.
     *
     * @return Quantidade disponível
     */
    public int getQuantidade() {
        return quantidade;
    }

    /**
     * Atualiza a quantidade disponível do produto no estoque.
     *
     * @param quantidade Nova quantidade disponível
     */
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    /**
     * Retorna o nome do fornecedor do produto.
     *
     * @return Nome do fornecedor
     */
    public String getFornecedor() {
        return fornecedor;
    }

    /**
     * Define o nome do fornecedor do produto.
     *
     * @param fornecedor Novo nome do fornecedor
     */
    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    /**
     * Retorna uma representação textual do produto com seus detalhes.
     *
     * @return String com informações do produto
     */
    @Override
    public String toString() {
        return "Produto{" +
               "id=" + idProduto +
               ", nome=" + nome +
               ", preco=" + preco +
               ", quantidade=" + quantidade +
               ", fornecedor=" + fornecedor +
               '}';
    }
}

