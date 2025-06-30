package com.mycompany.oficina.strategy;

/**
 * Valida um número de telefone verificando se contém exatamente 11 dígitos numéricos,
 * considerando DDD + número.
 */
public class TelefoneValidate implements Validate {

    /**
     * Valida o telefone recebido, removendo todos os caracteres não numéricos
     * e verificando se o resultado possui 11 dígitos.
     *
     * @param dado String contendo o telefone a ser validado.
     * @return true se o telefone contiver exatamente 11 dígitos numéricos, false caso contrário.
     */
    @Override
    public boolean validar(String dado) {
        if (dado == null) return false;
        return dado.replaceAll("\\D", "").length() == 11;
    }

    /**
     * Mensagem de erro padrão para indicar formato inválido de telefone.
     *
     * @return Mensagem informando que o telefone deve conter 11 dígitos (DDD + número).
     */
    @Override
    public String getMensagemErro() {
        return "Telefone inválido. Por favor, insira 11 dígitos (DDD + número).";
    }
}
