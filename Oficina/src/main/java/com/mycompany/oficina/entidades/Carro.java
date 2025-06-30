/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.entidades;

/**
 * Representa um carro com informações sobre fabricante, modelo, placa, chassi
 * e dados do dono (cliente). Cada carro possui um identificador único gerado automaticamente.
 */
public class Carro implements Entidades {

    /**
     * Identificador único do carro no formato "Carro-XXX".
     */
    private String idCarro;

    /**
     * Contador estático para geração sequencial do idCarro.
     */
    private static int contadorIdCarro = 1;

    /**
     * Fabricante do carro.
     */
    private String fabricante;

    /**
     * Modelo do carro.
     */
    private String modelo;

    /**
     * Placa do carro.
     */
    private String placa;

    /**
     * Número do chassi do carro.
     */
    private String chassi;

    /**
     * CPF do cliente dono do carro.
     */
    private String cpfDono;

    /**
     * Nome do cliente dono do carro.
     */
    private String nomeDono;

    /**
     * Construtor que inicializa o carro com as informações fornecidas.
     * O identificador do carro é gerado automaticamente.
     *
     * @param nomeDono Nome do dono do carro
     * @param cpfDono CPF do dono do carro
     * @param fabricante Fabricante do carro
     * @param modelo Modelo do carro
     * @param placa Placa do carro
     * @param chassi Número do chassi do carro
     */
    public Carro(String nomeDono, String cpfDono, String fabricante, String modelo, String placa, String chassi) {
        this.nomeDono = nomeDono;
        this.cpfDono = cpfDono;
        this.fabricante = fabricante;
        this.modelo = modelo;
        this.placa = placa;
        this.chassi = chassi;
        this.idCarro = "Carro-" + String.format("%03d", contadorIdCarro++);
    }

    /**
     * Construtor padrão.
     */
    public Carro() {
    }

    /**
     * Retorna o contador atual do ID dos carros.
     * Útil para saber qual será o próximo ID gerado.
     * 
     * @return contador do ID dos carros
     */
    public static int getContadorIdCarro() {
        return contadorIdCarro;
    }

    /**
     * Retorna o CPF do dono do carro.
     * 
     * @return CPF do proprietário
     */
    public String getCpfDono() {
        return cpfDono;
    }

    /**
     * Retorna o nome do dono do carro.
     * 
     * @return nome do proprietário
     */
    public String getNomeDono() {
        return nomeDono;
    }

    /**
     * Retorna o identificador único do carro.
     * 
     * @return ID do carro no formato "Carro-XXX"
     */
    public String getIdCarro() {
        return idCarro;
    }

    /**
     * Define o identificador único do carro.
     * Normalmente gerado automaticamente e não deve ser alterado manualmente.
     * 
     * @param idCarro novo identificador do carro
     */
    public void setIdCarro(String idCarro) {
        this.idCarro = idCarro;
    }

    /**
     * Retorna o fabricante do carro.
     * 
     * @return fabricante do carro
     */
    public String getFabricante() {
        return fabricante;
    }

    /**
     * Define o fabricante do carro.
     * 
     * @param fabricante fabricante do carro
     */
    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    /**
     * Retorna o modelo do carro.
     * 
     * @return modelo do carro
     */
    public String getModelo() {
        return modelo;
    }

    /**
     * Define o modelo do carro.
     * 
     * @param modelo modelo do carro
     */
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    /**
     * Retorna a placa do carro.
     * 
     * @return placa do carro
     */
    public String getPlaca() {
        return placa;
    }

    /**
     * Define a placa do carro.
     * 
     * @param placa placa do carro
     */
    public void setPlaca(String placa) {
        this.placa = placa;
    }

    /**
     * Retorna o número do chassi do carro.
     * 
     * @return chassi do carro
     */
    public String getChassi() {
        return chassi;
    }

    /**
     * Define o número do chassi do carro.
     * 
     * @param chassi número do chassi
     */
    public void setChassi(String chassi) {
        this.chassi = chassi;
    }

    /**
     * Retorna o identificador para a interface Entidades.
     * Aqui, o chassi é usado como identificador único do carro.
     * 
     * @return chassi do carro
     */
    @Override
    public String getIdentificador() {
        return this.getChassi();
    }

    @Override
    public String toString() {
        return "Carro{" +
                "cpfDono='" + cpfDono + '\'' +
                ", idCarro='" + idCarro + '\'' +
                ", fabricante='" + fabricante + '\'' +
                ", modelo='" + modelo + '\'' +
                ", placa='" + placa + '\'' +
                ", chassi='" + chassi + '\'' +
                '}';
    }
}

