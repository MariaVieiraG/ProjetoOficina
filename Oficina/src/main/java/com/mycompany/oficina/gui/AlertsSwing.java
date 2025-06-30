package com.mycompany.oficina.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Classe utilitária para exibição de mensagens e extratos em caixas de diálogo usando Swing.
 * 
 * Fornece métodos estáticos para mostrar alertas simples e extratos formatados em uma
 * janela de rolagem.
 * 
 * Esta classe é usada principalmente na camada de interface gráfica da aplicação.
 * 
 * @author Maria
 */
public class AlertsSwing {

    /**
     * Exibe uma mensagem de alerta utilizando {@code JOptionPane}.
     * 
     * @param title título da janela
     * @param content mensagem de conteúdo a ser exibida
     * @param messageType tipo da mensagem (ex: {@code JOptionPane.WARNING_MESSAGE}, {@code JOptionPane.ERROR_MESSAGE})
     */
    public static void showAlert(String title, String content, int messageType) {
        JOptionPane.showMessageDialog(null, content, title, messageType);
    }

    /**
     * Exibe um extrato de texto em uma área rolável.
     * 
     * Útil para mostrar informações extensas como relatórios, extratos financeiros, etc.
     * 
     * @param title título da janela
     * @param extratoContent conteúdo do extrato a ser exibido
     */
    public static void showExtrato(String title, String extratoContent) {
        JTextArea textArea = new JTextArea(extratoContent);
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(null, scrollPane, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
