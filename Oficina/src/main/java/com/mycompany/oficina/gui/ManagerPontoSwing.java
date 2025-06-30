package com.mycompany.oficina.gui;

import com.mycompany.oficina.controller.AtendenteController;
import com.mycompany.oficina.controller.MecanicoController;
import com.mycompany.oficina.sistemaponto.RegistroPonto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class ManagerPontoSwing extends JFrame {
    private final Object controller;               // Pode ser AtendenteController ou MecanicoController
    private final JTable tableView;                 // Tabela para mostrar registros de ponto
    private final DefaultTableModel tableModel;     // Modelo da tabela para manipulação dos dados
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"); // Formato da data/hora

    public ManagerPontoSwing(Object controller) {
        this.controller = controller;

        setTitle("Registro de Ponto");              // Título da janela
        setSize(700, 400);                           // Tamanho da janela
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fecha apenas essa janela
        setLocationRelativeTo(null);                 // Centraliza na tela

        // Define as colunas da tabela
        String[] columnNames = {"Funcionário", "Entrada", "Saída"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Impede edição direta nas células
            }
        };
        tableView = new JTable(tableModel);

        // Painel com botões para registrar ponto e atualizar tabela
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnEntrada = new JButton("Bater Ponto de Entrada");
        JButton btnSaida = new JButton("Bater Ponto de Saída");
        JButton btnAtualizar = new JButton("Atualizar");
        buttonPanel.add(btnEntrada);
        buttonPanel.add(btnSaida);
        buttonPanel.add(btnAtualizar);

        // Ações dos botões vinculadas a métodos específicos
        btnEntrada.addActionListener(e -> handleEntrada());
        btnSaida.addActionListener(e -> handleSaida());
        btnAtualizar.addActionListener(e -> carregarDados());

        // Layout da janela com tabela central e botões abaixo
        setLayout(new BorderLayout());
        add(new JScrollPane(tableView), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        carregarDados(); // Inicializa a tabela com os dados atuais
    }

    // Método para carregar os registros de ponto e mostrar na tabela
    private void carregarDados() {
        tableModel.setRowCount(0); // Limpa tabela antes de preencher

        List<RegistroPonto> registros = Collections.emptyList();

        // Verifica o tipo do controlador para obter os registros corretos
        if (controller instanceof AtendenteController) {
            registros = ((AtendenteController) controller).verRegistrosDeHoje();
        } else if (controller instanceof MecanicoController) {
            registros = ((MecanicoController) controller).verRegistrosDeHoje();
        }

        // Preenche a tabela com cada registro
        for (RegistroPonto r : registros) {
            String saidaFormatada = (r.getDataHoraSaida() != null) 
                ? r.getDataHoraSaida().format(formatter) 
                : "PONTO EM ABERTO";  // Se saída não registrada, mostra aviso

            tableModel.addRow(new Object[]{
                r.getFuncionario().getNome(),
                r.getDataHoraEntrada().format(formatter),
                saidaFormatada
            });
        }
    }

    // Método para registrar ponto de entrada
    private void handleEntrada() {
        RegistroPonto registro = null;

        // Chama método apropriado do controlador dependendo do tipo
        if (controller instanceof AtendenteController) {
            registro = ((AtendenteController) controller).baterPontoEntrada();
        } else if (controller instanceof MecanicoController) {
            registro = ((MecanicoController) controller).baterPontoEntrada();
        }

        // Exibe mensagem conforme sucesso ou falha
        if (registro != null) {
            AlertsSwing.showAlert("Sucesso", "Ponto de entrada registrado!", JOptionPane.INFORMATION_MESSAGE);
        } else {
            AlertsSwing.showAlert("Erro", "Não foi possível registrar a entrada. Você já pode ter um ponto em aberto.", JOptionPane.ERROR_MESSAGE);
        }
        carregarDados(); // Atualiza a tabela
    }

    // Método para registrar ponto de saída
    private void handleSaida() {
        RegistroPonto registro = null;

        // Chama método apropriado do controlador dependendo do tipo
        if (controller instanceof AtendenteController) {
            registro = ((AtendenteController) controller).baterPontoSaida();
        } else if (controller instanceof MecanicoController) {
            registro = ((MecanicoController) controller).baterPontoSaida();
        }

        // Exibe mensagem conforme sucesso ou falha
        if (registro != null) {
            AlertsSwing.showAlert("Sucesso", "Ponto de saída registrado!", JOptionPane.INFORMATION_MESSAGE);
        } else {
            AlertsSwing.showAlert("Erro", "Não foi possível registrar a saída. Não há ponto de entrada aberto.", JOptionPane.ERROR_MESSAGE);
        }
        carregarDados(); // Atualiza a tabela
    }
}
