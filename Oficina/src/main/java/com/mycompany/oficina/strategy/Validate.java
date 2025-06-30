package com.mycompany.oficina.strategy;

/**
 * Interface para classes que implementam validações de dados.
 * Define os métodos necessários para validar uma string e obter uma mensagem de erro.
 */
public interface Validate {

    /**
     * Valida o dado fornecido.
     * 
     * @param dado String a ser validada.
     * @return true se o dado for válido, false caso contrário.
     */
    boolean validar(String dado);

    /**
     * Retorna a mensagem de erro que deve ser exibida caso a validação falhe.
     * 
     * @return Mensagem descritiva do erro de validação.
     */
    String getMensagemErro();
}
