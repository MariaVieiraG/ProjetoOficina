package com.mycompany.oficina.strategy;



/**
 * Implementa a validação básica de e-mail, verificando se a string contém 
 * os caracteres '@' e '.' essenciais para um formato válido.
 */
public class EmailValidate implements Validate {

    /**
     * Verifica se o dado informado não é nulo e contém os caracteres '@' e '.'.
     *
     * @param dado A string contendo o e-mail a ser validado.
     * @return true se o e-mail contém '@' e '.', false caso contrário.
     */
    @Override
    public boolean validar(String dado) {
        return dado != null && dado.contains("@") && dado.contains(".");
    }

    /**
     * Mensagem de erro padrão que deve ser exibida caso a validação falhe.
     *
     * @return Mensagem indicando formato de e-mail inválido.
     */
    @Override
    public String getMensagemErro() {
        return "E-mail inválido. O formato parece incorreto.";
    }
}
