package com.mycompany.oficina.strategy;

/**
 * Implementa a validação de CPF, garantindo que o dado informado contenha
 * exatamente 11 dígitos numéricos, ignorando quaisquer caracteres não numéricos.
 */
public class CpfValidate implements Validate {

    /**
     * Valida se a string de entrada representa um CPF válido no formato básico,
     * ou seja, se possui 11 dígitos numéricos após remover caracteres não numéricos.
     *
     * @param dado A string contendo o CPF a ser validado.
     * @return true se o CPF possui 11 dígitos numéricos, false caso contrário.
     */
    @Override
    public boolean validar(String dado) {
        if (dado == null) return false; // Não aceita valor nulo
        // Remove tudo que não for dígito e verifica se tem 11 caracteres
        return dado.replaceAll("\\D", "").length() == 11;
    }

    /**
     * Mensagem de erro padrão que deve ser exibida caso a validação falhe.
     *
     * @return Mensagem informando que o CPF é inválido.
     */
    @Override
    public String getMensagemErro() {
        return "CPF inválido. Por favor, insira 11 dígitos numéricos.";
    }
}
