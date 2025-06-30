package com.mycompany.oficina.persistencia;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Adapter para serializar e desserializar objetos LocalDateTime no formato JSON,
 * usando a biblioteca Gson.
 * 
 * Esse adapter converte LocalDateTime para String no padrão ISO_LOCAL_DATE_TIME
 * durante a escrita (serialização), e converte de String para LocalDateTime
 * durante a leitura (desserialização).
 */
public class LocalDataTimeAdapter extends TypeAdapter<LocalDateTime> {
    
    // Formato padrão ISO para LocalDateTime (ex: 2023-06-29T14:30:00)
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Serializa o LocalDateTime em uma String formatada no JSON.
     * Se o valor for nulo, escreve um valor nulo no JSON.
     * 
     * @param out JsonWriter para escrever a saída JSON
     * @param value LocalDateTime a ser convertido para String JSON
     * @throws IOException Caso ocorra erro na escrita
     */
    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
        if (value == null) {
            out.nullValue(); // Escreve null no JSON
        } else {
            out.value(formatter.format(value)); // Escreve a data formatada como String
        }
    }

    /**
     * Lê uma String do JSON e a converte para um objeto LocalDateTime.
     * Caso o valor JSON seja null, retorna null.
     * 
     * @param in JsonReader para ler a entrada JSON
     * @return LocalDateTime representado pela String JSON
     * @throws IOException Caso ocorra erro na leitura
     */
    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
            in.nextNull(); // Consome o null do JSON
            return null; // Retorna null para o objeto LocalDateTime
        }
        String value = in.nextString(); // Lê a String com a data
        return LocalDateTime.parse(value, formatter); // Converte para LocalDateTime
    }
}
