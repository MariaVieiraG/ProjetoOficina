package com.mycompany.oficina.controlador;

import com.google.gson.reflect.TypeToken;
import com.mycompany.oficina.entidades.Entidades;
import com.mycompany.oficina.persistencia.PersistenciaJson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe genérica para gerenciar entidades que implementam a interface
 * 'Entidades'.
 * <p>
 * Suporta persistência automática dos dados usando arquivos JSON.
 *
 * @param <T> O tipo da entidade gerenciada (por exemplo, Cliente, Funcionario).
 */
public abstract class GerenciadorGenerico<T extends Entidades> {

    private List<T> lista; // Lista interna de entidades carregadas da persistência
    private final PersistenciaJson persistencia; // Objeto responsável por carregar e salvar os dados em JSON
    private final String entidadeChave; // Chave que identifica o tipo da entidade, usada para nomear o arquivo
    private final TypeToken<ArrayList<T>> tipoToken; // Informação de tipo para o Gson saber desserializar corretamente

    /**
     * Construtor do gerenciador genérico.
     *
     * @param persistencia Instância do gerenciador de persistência JSON.
     * @param entidadeChave Chave usada para identificar o arquivo (ex:
     * "clientes").
     * @param tipoToken Token que descreve o tipo da lista para o Gson.
     */
    public GerenciadorGenerico(PersistenciaJson persistencia, String entidadeChave, TypeToken<ArrayList<T>> tipoToken) {
        this.persistencia = persistencia;
        this.entidadeChave = entidadeChave;
        this.tipoToken = tipoToken;
        // Carrega os dados do arquivo JSON na criação do gerenciador
        this.lista = carregarDados();
    }

    // Método privado que carrega a lista do arquivo JSON
    private List<T> carregarDados() {
        return persistencia.carregarLista(this.entidadeChave, this.tipoToken);
    }

    // Método privado que salva a lista atual no arquivo JSON
    private void salvarDados() {
        persistencia.salvarLista(this.entidadeChave, this.lista);
    }

    /**
     * Adiciona um novo item à lista e salva as alterações.
     *
     * @param item Item a ser adicionado.
     */
    public void adicionar(T item) {
        lista.add(item);
        salvarDados();
    }

    /**
     * Busca um item na lista pelo seu identificador único.
     *
     * @param identificador Identificador do item a ser buscado.
     * @return O item encontrado ou null caso não exista.
     */
    public T buscarPorIdentificador(String identificador) {
        for (T item : lista) {
            if (item.getIdentificador().equals(identificador)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Remove um item da lista pelo seu identificador. Salva a lista se a
     * remoção for bem-sucedida.
     *
     * @param identificador Identificador do item a ser removido.
     * @return true se o item foi removido, false caso contrário.
     */
    public boolean removerItemPorIdentificador(String identificador) {
        T itemParaRemover = buscarPorIdentificador(identificador);
        if (itemParaRemover != null) {
            boolean removido = lista.remove(itemParaRemover);
            if (removido) {
                salvarDados();
            }
            return removido;
        }
        return false;
    }

    /**
     * Retorna uma lista não modificável contendo todos os itens gerenciados.
     *
     * @return Lista imutável com todos os itens.
     */
    public List<T> listarTodos() {
        return Collections.unmodifiableList(lista);
    }

    /**
     * Método protegido para que classes filhas possam salvar alterações
     * específicas.
     */
    public void salvarAlteracoes() {
        salvarDados();
    }
}

