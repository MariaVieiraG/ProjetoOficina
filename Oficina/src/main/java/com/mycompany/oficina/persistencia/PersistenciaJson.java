package com.mycompany.oficina.persistencia;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.mycompany.oficina.ordemservico.stateOS.EstadoOS;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe responsável por gerenciar a persistência dos dados em arquivos JSON.
 * Utiliza a biblioteca Gson para serializar e desserializar objetos.
 * 
 * Cada tipo de entidade é salvo em um arquivo JSON separado dentro do diretório "data".
 */
public class PersistenciaJson {

    // Diretório onde os arquivos JSON serão armazenados
    private static final String DATA_DIRECTORY = "data";

    // Instância do Gson configurada com adaptador para LocalDateTime e impressão formatada
    private final Gson gson;

    // Mapeamento entre uma chave (nome da entidade) e o nome do arquivo JSON correspondente
    private final Map<String, String> arquivosDeEntidade = new HashMap<>();

    /**
     * Construtor que inicializa o Gson e prepara o diretório de dados e os arquivos de entidades.
     */
    public PersistenciaJson() {
        GsonBuilder builder = new GsonBuilder();

        // Registra adaptador customizado para serializar e desserializar LocalDateTime
        builder.registerTypeAdapter(LocalDateTime.class, new LocalDataTimeAdapter());
        builder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());

        // Define que o JSON deve ser formatado com quebras de linha e indentação
        builder.setPrettyPrinting();

        // Cria a instância final do Gson
        this.gson = builder.create();

        // Cria o diretório "data" caso ele não exista
        File dir = new File(DATA_DIRECTORY);

        // Linha de diagnóstico: mostra o caminho do diretório de dados que o sistema usará
        System.out.println("[DIAGNÓSTICO] O sistema está procurando o diretório de dados aqui: " + dir.getAbsolutePath());

        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Registra as entidades com suas respectivas chaves e nomes de arquivos JSON
        registrarEntidade("clientes", "clientes.json");
        registrarEntidade("funcionarios", "funcionarios.json");
        registrarEntidade("carros", "carros.json");
        registrarEntidade("pontos", "registros_ponto.json");
        registrarEntidade("ordens_servico", "ordens_servico.json");
        registrarEntidade("agenda", "agenda.json");
        registrarEntidade("estoque", "estoque.json");
        registrarEntidade("financeiro", "financeiro.json");
    }

    /**
     * Registra uma entidade associando uma chave a um arquivo JSON dentro do diretório "data".
     * 
     * @param chave Identificador da entidade (ex: "clientes")
     * @param nomeArquivo Nome do arquivo JSON para armazenar essa entidade (ex: "clientes.json")
     */
    private void registrarEntidade(String chave, String nomeArquivo) {
        arquivosDeEntidade.put(chave, DATA_DIRECTORY + "/" + nomeArquivo);
    }

    // --- MÉTODOS PARA CARREGAR LISTAS DE OBJETOS ---

    /**
     * Carrega uma lista de objetos de um arquivo JSON baseado na chave da entidade.
     * Caso o arquivo não exista ou não tenha dados, retorna uma lista vazia.
     * 
     * @param <T> Tipo dos objetos da lista
     * @param chave Chave da entidade (ex: "clientes")
     * @param tipoToken Tipo genérico para deserialização da lista
     * @return Lista carregada ou vazia caso arquivo ausente ou erro
     */
    public <T> List<T> carregarLista(String chave, TypeToken<ArrayList<T>> tipoToken) {
        String nomeArquivo = getNomeArquivo(chave);
        if (nomeArquivo == null) return new ArrayList<>();

        // Linha de diagnóstico: indica tentativa de carregar o arquivo
        System.out.println("[DIAGNÓSTICO] Tentando carregar a lista da chave '" + chave + "' do arquivo: " + nomeArquivo);

        List<T> lista = carregar(nomeArquivo, tipoToken.getType());

        // Linha de diagnóstico: verifica se a lista foi carregada com sucesso
        if (lista == null) {
            // Caso arquivo não exista ou tenha erro de sintaxe, retorna lista vazia
            return new ArrayList<>();
        } else {
            System.out.println("[DIAGNÓSTICO] SUCESSO! Para a chave '" + chave + "', foram carregados " + lista.size() + " item(ns).");
            return lista;
        }
    }

    // --- MÉTODOS PRIVADOS PARA CARREGAR DADOS ---

    /**
     * Método genérico que lê um arquivo JSON e converte para o tipo especificado.
     * Se o arquivo não existir ou houver erro na leitura/sintaxe, retorna null.
     * 
     * @param <T> Tipo do objeto a ser retornado
     * @param nomeArquivo Caminho do arquivo JSON
     * @param tipo Tipo genérico para deserialização
     * @return Objeto desserializado ou null em caso de erro
     */
    private <T> T carregar(String nomeArquivo, Type tipo) {
        File file = new File(nomeArquivo);
        if (!file.exists()) {
            // Linha de diagnóstico: alerta sobre arquivo inexistente
            System.out.println("[DIAGNÓSTICO] ALERTA: O arquivo '" + nomeArquivo + "' não foi encontrado.");
            return null;
        }

        try (FileReader reader = new FileReader(nomeArquivo)) {
            // Deserializa JSON para o tipo solicitado
            return gson.fromJson(reader, tipo);
        } catch (IOException e) {
            System.err.println("[DIAGNÓSTICO] ERRO DE LEITURA no arquivo " + nomeArquivo + ": " + e.getMessage());
            return null;
        } catch (JsonSyntaxException e) {
            // Linha de diagnóstico: alerta sobre erro de sintaxe no JSON
            System.err.println("[DIAGNÓSTICO] ERRO DE SINTAXE no arquivo " + nomeArquivo + ". Verifique se há vírgulas a mais ou chaves/colchetes incorretos. Causa: " + e.getMessage());
            return null;
        }
    }

    // --- MÉTODOS AUXILIARES ---

    /**
     * Retorna o nome do arquivo JSON associado a uma chave.
     * Se a chave não estiver registrada, exibe erro e retorna null.
     * 
     * @param chave Chave da entidade
     * @return Nome do arquivo JSON ou null se não encontrada
     */
    private String getNomeArquivo(String chave) {
        String nomeArquivo = arquivosDeEntidade.get(chave);
        if (nomeArquivo == null) {
            System.err.println("ERRO: Nenhuma entidade registrada para a chave: " + chave);
            return null;
        }
        return nomeArquivo;
    }

    // --- MÉTODOS PARA SALVAR LISTAS E MAPAS ---

    /**
     * Salva uma lista de objetos em arquivo JSON baseado na chave da entidade.
     * 
     * @param <T> Tipo dos objetos da lista
     * @param chave Chave da entidade
     * @param lista Lista de objetos a salvar
     */
    public <T> void salvarLista(String chave, List<T> lista) {
        String nomeArquivo = getNomeArquivo(chave);
        if (nomeArquivo == null) return;
        salvar(lista, nomeArquivo);
    }

    /**
     * Método genérico que serializa qualquer objeto e salva em arquivo JSON.
     * 
     * @param <T> Tipo do objeto a salvar
     * @param dados Objeto a ser salvo
     * @param nomeArquivo Caminho do arquivo JSON
     */
    private <T> void salvar(T dados, String nomeArquivo) {
        try (FileWriter writer = new FileWriter(nomeArquivo)) {
            gson.toJson(dados, writer);
        } catch (IOException e) {
            System.err.println("ERRO CRÍTICO ao salvar o arquivo " + nomeArquivo + ": " + e.getMessage());
        }
    }

    /**
     * Salva um mapa (HashMap) em arquivo JSON baseado na chave da entidade.
     * 
     * @param <K> Tipo da chave do mapa
     * @param <V> Tipo do valor do mapa
     * @param chave Chave da entidade
     * @param mapa Mapa a salvar
     */
    public <K, V> void salvarMapa(String chave, Map<K, V> mapa) {
        String nomeArquivo = getNomeArquivo(chave);
        if (nomeArquivo == null) return;
        salvar(mapa, nomeArquivo);
    }

    /**
     * Carrega um mapa (HashMap) de um arquivo JSON baseado na chave da entidade.
     * Se não existir ou erro, retorna mapa vazio.
     * 
     * @param <K> Tipo da chave do mapa
     * @param <V> Tipo do valor do mapa
     * @param chave Chave da entidade
     * @param tipoToken Tipo genérico para deserialização do mapa
     * @return Mapa carregado ou vazio em caso de erro
     */
    public <K, V> Map<K, V> carregarMapa(String chave, TypeToken<HashMap<K, V>> tipoToken) {
        String nomeArquivo = getNomeArquivo(chave);
        if (nomeArquivo == null) return new HashMap<>();

        Map<K, V> mapa = carregar(nomeArquivo, tipoToken.getType());
        return mapa != null ? mapa : new HashMap<>();
    }
}
