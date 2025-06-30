/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.ordemservico.ObserverOS;

import com.mycompany.oficina.ordemservico.OrdemDeServico;

/**
 * Implementação da interface Observador para notificar o cliente sobre
 * as mudanças no status da Ordem de Serviço (OS).
 */
public class NotificadorOs implements Observador {

    /**
     * Método chamado sempre que a Ordem de Serviço sofre uma atualização.
     * Envia uma mensagem personalizada ao cliente dependendo do estado atual da OS.
     * 
     * @param os a Ordem de Serviço que foi atualizada
     */
    @Override
    public void atualizar(OrdemDeServico os) {
        String status = os.getStatusAtual(); // Obtém o status atual da OS
        String mensagem = null;               // Inicializa a variável para a mensagem

        // Verifica o status para montar a mensagem apropriada para o cliente
        switch (status) {
            case "Aguardando":
                mensagem = String.format(
                    "Olá, %s! Recebemos seu veículo %s. A OS (#%s) foi aberta e em breve iniciaremos a inspeção.",
                    os.getCliente().getNome(), os.getCarro().getModelo(), os.getNumeroOS());
                break;

            case "Em Inspeção":
                mensagem = String.format(
                    "Olá, %s! A inspeção do seu veículo %s (OS #%s) foi iniciada por nossa equipe.",
                    os.getCliente().getNome(), os.getCarro().getModelo(), os.getNumeroOS());
                break;

            case "Em Serviço":
                mensagem = String.format(
                    "Boas notícias, %s! O serviço no seu veículo (OS #%s) foi iniciado.",
                    os.getCliente().getNome(), os.getNumeroOS());
                break;

            case "Finalizada":
                mensagem = String.format(
                    "Serviço Concluído! Olá, %s, o serviço no seu veículo %s (placa %s) foi finalizado e ele está pronto para retirada.",
                    os.getCliente().getNome(), os.getCarro().getModelo(), os.getCarro().getPlaca());
                break;

            case "Cancelada":
                mensagem = String.format(
                    "Atenção, %s. A OS (#%s) do seu veículo foi cancelada.",
                    os.getCliente().getNome(), os.getNumeroOS());
                break;
        }

        // Se uma mensagem válida foi criada, envia a notificação ao cliente
        if (mensagem != null) {
            enviarNotificacao(os.getCliente().getTelefone(), mensagem);
        }
    }

    /**
     * Simula o envio de uma notificação para o cliente.
     * No momento, apenas imprime a mensagem no console.
     * 
     * @param telefone número de telefone do cliente
     * @param mensagem mensagem a ser enviada
     */
    private void enviarNotificacao(String telefone, String mensagem) {
        System.out.println("\n=================[ SIMULADOR DE NOTIFICAÇÃO ]=================");
        System.out.println("DESTINATÁRIO: " + telefone + " | MENSAGEM: " + mensagem);
        System.out.println("==============================================================\n");
    }
}
