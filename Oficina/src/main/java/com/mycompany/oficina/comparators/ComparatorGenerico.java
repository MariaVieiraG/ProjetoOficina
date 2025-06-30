package com.mycompany.oficina.comparators;

import java.util.Comparator;
import java.util.function.Function;

/**
 * Comparador genérico que permite comparar objetos de qualquer tipo com base em um atributo String extraído.
 * <p>
 * A lógica de comparação é feita caractere por caractere, similar à comparação lexicográfica.
 *
 * @param <T> O tipo de objeto que será comparado.
 */
public class ComparatorGenerico<T> implements Comparator<T> {

    // Função que extrai o atributo String a partir de um objeto do tipo T
    private final Function<T, String> extratorAtributo;

    /**
     * Construtor do comparador genérico.
     *
     * @param extratorAtributo Função que extrai o atributo String dos objetos a serem comparados.
     */
    public ComparatorGenerico(Function<T, String> extratorAtributo) {
        this.extratorAtributo = extratorAtributo;
    }

    /**
     * Compara dois objetos com base no atributo String extraído por meio da função fornecida.
     *
     * @param o1 o primeiro objeto a ser comparado
     * @param o2 o segundo objeto a ser comparado
     * @return valor negativo se o1 &lt; o2, valor positivo se o1 &gt; o2, ou 0 se forem iguais
     */
    @Override
    public int compare(T o1, T o2) {
        // Extrai as strings dos objetos usando a função fornecida
        String str1 = extratorAtributo.apply(o1);
        String str2 = extratorAtributo.apply(o2);

        // Lógica de comparação de strings (caractere por caractere)
        int len1 = str1.length();
        int len2 = str2.length();
        int lim = Math.min(len1, len2);

        for (int k = 0; k < lim; k++) {
            char ch1 = str1.charAt(k);
            char ch2 = str2.charAt(k);
            if (ch1 != ch2) {
                return ch1 - ch2;
            }
        }
        return len1 - len2;
    }
}
