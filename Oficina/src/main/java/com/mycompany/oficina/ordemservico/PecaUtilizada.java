/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.ordemservico;

import com.mycompany.oficina.loja.Produto;

/**
 * Representa uma peça que foi utilizada em uma ordem de serviço.
 * 
 * Mantém uma referência ao produto original do estoque, a quantidade usada dessa peça,
 * e o preço do produto no momento em que foi utilizado.
 * 
 * Isso garante que o valor cobrado na ordem não será alterado por mudanças futuras
 * no preço do produto no estoque.
 */
public class PecaUtilizada {
    // Referência ao produto original no estoque (ex: uma peça cadastrada)
    private final Produto produtoOriginal;

    // Quantidade dessa peça que foi efetivamente utilizada na ordem de serviço
    private final int quantidadeUtilizada;

    // Preço do produto "congelado" no momento do uso para evitar variações futuras
    private final double precoNoMomentoDoUso;

    /**
     * Construtor que cria uma instância de peça utilizada.
     * Valida se o produto original não é nulo e captura o preço atual do produto.
     * 
     * @param produtoOriginal produto que foi usado (não pode ser nulo)
     * @param quantidadeUtilizada quantidade usada na ordem
     * @throws IllegalArgumentException se produtoOriginal for nulo
     */
    public PecaUtilizada(Produto produtoOriginal, int quantidadeUtilizada) {
        if (produtoOriginal == null) {
            throw new IllegalArgumentException("O produto original não pode ser nulo.");
        }
        this.produtoOriginal = produtoOriginal;
        this.quantidadeUtilizada = quantidadeUtilizada;
        this.precoNoMomentoDoUso = produtoOriginal.getPreco(); // Congela o preço no momento do uso
    }

    // --- Getters ---

    /**
     * Retorna o produto original do estoque.
     * 
     * @return o produto utilizado
     */
    public Produto getProdutoOriginal() {
        return produtoOriginal;
    }

    /**
     * Retorna a quantidade da peça utilizada na ordem.
     * 
     * @return quantidade utilizada
     */
    public int getQuantidadeUtilizada() {
        return quantidadeUtilizada;
    }

    /**
     * Retorna o preço do produto no momento que foi utilizado.
     * 
     * @return preço "congelado" no momento do uso
     */
    public double getPrecoNoMomentoDoUso() {
        return precoNoMomentoDoUso;
    }
    
    /**
     * Calcula o subtotal desta peça na ordem, multiplicando quantidade pelo preço.
     * 
     * @return subtotal da peça utilizada
     */
    public double getSubtotal() {
        return this.quantidadeUtilizada * this.precoNoMomentoDoUso;
    }

    /**
     * Retorna uma representação textual da peça utilizada,
     * indicando quantidade, nome do produto e preço unitário formatado.
     * 
     * @return string descritiva da peça utilizada
     */
    @Override
    public String toString() {
        return quantidadeUtilizada + "x " + produtoOriginal.getNome() + " (R$ " + String.format("%.2f", precoNoMomentoDoUso) + " cada)";
    }
}
