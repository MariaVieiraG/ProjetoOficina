package com.mycompany.oficina.entidades;

/**
 * Representa um elevador da oficina, utilizado para serviços como alinhamento e balanceamento.
 * 
 * A classe mantém um vetor estático com exatamente três elevadores pré-configurados, garantindo
 * que o sistema controle apenas esses elevadores fixos.
 * 
 * Cada elevador possui um identificador único, uma descrição e um estado de disponibilidade
 * que indica se ele está livre para uso ou ocupado.
 * 
 * O construtor é privado para impedir a criação externa, assegurando o controle centralizado
 * sobre as instâncias dos elevadores.
 *  
 */
public class Elevador {

    private int id;
    private String descricao;
    private boolean disponivel;

    // Questao 5 - O sistema deverá armazenar de forma estática (Vetor com tamanho fixo)
    // as informações dos 3 elevadores da oficina.
    private static final Elevador[] elevadores = new Elevador[3];

    // Inicializa os elevadores automaticamente na carga da classe
    static {
        elevadores[0] = new Elevador(1, "Elevador Alinhamento e Balanceamento", true);
        elevadores[1] = new Elevador(2, "Elevador 2", true);
        elevadores[2] = new Elevador(3, "Elevador 3", true);
    }

    /**
     * Construtor privado. Não permite criação externa de elevadores,
     * garantindo controle centralizado pela classe.
     *
     * @param id identificador único do elevador
     * @param descricao descrição do elevador
     * @param disponivel se o elevador está disponível
     */
    private Elevador(int id, String descricao, boolean disponivel) {
        this.id = id;
        this.descricao = descricao;
        this.disponivel = disponivel;
    }

    /** 
     * Retorna o identificador único do elevador.
     * 
     * @return id do elevador
     */
    public int getId() {
        return id;
    }

    /** 
     * Retorna a descrição do elevador.
     * 
     * @return descrição do elevador
     */
    public String getDescricao() {
        return descricao;
    }

    /** 
     * Verifica se o elevador está disponível para uso.
     * 
     * @return true se disponível, false caso contrário
     */
    public boolean isDisponivel() {
        return disponivel;
    }

    /**
     * Define o estado de disponibilidade do elevador.
     *
     * @param disponivel novo estado de disponibilidade (true para disponível, false para ocupado)
     */
    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    /**
     * Retorna o array contendo os três elevadores da oficina.
     *
     * @return vetor estático com os elevadores cadastrados
     */
    public static Elevador[] getElevadores() {
        return elevadores;
    }

    /**
     * Busca um elevador específico pelo seu identificador.
     *
     * @param id identificador do elevador buscado
     * @return o elevador com o id correspondente, ou null se não encontrado
     */
    public static Elevador getElevadorPorId(int id) {
        for (Elevador e : elevadores) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }

    /**
     * Retorna uma representação textual dos dados do elevador, incluindo id, descrição e disponibilidade.
     *
     * @return string formatada com informações do elevador
     */
    @Override
    public String toString() {
        return
                + id + descricao + '\''
               ;
    }
}
