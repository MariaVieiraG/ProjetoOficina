package com.mycompany.oficina.utilidades;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

public class FormatadorCpf {

    /**
     * Anonimiza um número de CPF, substituindo os primeiros dígitos por asteriscos,
     * mantendo visíveis apenas os últimos 5 caracteres.
     * 
     * Exemplo: 123.456.789-00 -> ***.***.789-00
     * 
     * @param cpf Número de CPF no formato de string (com ou sem pontuação).
     * @return CPF anonimizado com os primeiros dígitos ocultos e últimos 5 visíveis.
     */
    public static String anonimizar(String cpf) {
        // Retorna uma string com os primeiros 6 dígitos substituídos por "***.***."
        // e mantém os últimos 5 caracteres originais do CPF.
        return "***.***." + cpf.substring(cpf.length() - 5);
    }
}
