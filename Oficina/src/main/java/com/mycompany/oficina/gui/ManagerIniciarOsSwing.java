package com.mycompany.oficina.gui;

import com.mycompany.oficina.agendamento.Agendamento;
import com.mycompany.oficina.controller.MecanicoController;
import com.mycompany.oficina.ordemservico.OrdemDeServico;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class ManagerIniciarOsSwing extends JFrame {
    private final MecanicoController controller;     // Controlador para operações mecânicas
    private final JTable tableView;                   // Tabela para listar os agendamentos do dia
    private final DefaultTableModel tableModel;       // Modelo da tabela para manipular os dados
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"); // Formato para data/hora

    // Construtor da janela principal para iniciar ordens de serviço
    public ManagerIniciarOsSwing() {
        this.controller = new MecanicoController();

        setTitle("Agendamentos do Dia para Iniciar OS");   // Título da janela
        setSize(850, 500);                                  // Tamanho da janela
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fecha só esta janela
        setLocationRelativeTo(null);                        // Centraliza na tela

        // Define as colunas da tabela
        String[] columnNames = {"Data/Hora", "Cliente", "Veículo", "Serviço"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; } // Impede edição direta
        };
        tableView = new JTable(tableModel);

        // Painel para botões na parte inferior
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnIniciarOS = new JButton("Iniciar OS do Agendamento Selecionado");
        JButton btnAtualizar = new JButton("Atualizar Lista");
        buttonPanel.add(btnIniciarOS);
        buttonPanel.add(btnAtualizar);

        // Ação do botão para iniciar OS com base no agendamento selecionado
        btnIniciarOS.addActionListener(e -> handleIniciarOS());
        // Ação do botão para atualizar a lista de agendamentos
        btnAtualizar.addActionListener(e -> carregarDados());

        // Define layout principal e adiciona componentes
        setLayout(new BorderLayout());
        add(new JScrollPane(tableView), BorderLayout.CENTER);  // Tabela no centro da janela
        add(buttonPanel, BorderLayout.SOUTH);                   // Botões na parte inferior

        carregarDados(); // Carrega os dados na tabela ao abrir
    }

    // Método para carregar os agendamentos de hoje na tabela
    private void carregarDados() {
        tableModel.setRowCount(0); // Limpa dados anteriores
        for (Agendamento ag : controller.listarAgendamentosDeHoje()) {
            tableModel.addRow(new Object[]{
                    ag.getDataHora().format(formatter),                  // Formata a data/hora
                    ag.getCliente().getNome(),                            // Nome do cliente
                    ag.getCarro().getModelo() + " (" + ag.getCarro().getPlaca() + ")", // Veículo com placa
                    ag.getTipoServico().name()                            // Tipo de serviço
            });
        }
    }

    // Método acionado ao clicar em "Iniciar OS"
    private void handleIniciarOS() {
        int selectedRow = tableView.getSelectedRow();
        // Verifica se algum agendamento foi selecionado
        if (selectedRow < 0) {
            AlertsSwing.showAlert("Seleção Necessária", "Selecione um agendamento para iniciar a OS.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obtém o agendamento selecionado na tabela (na mesma ordem da lista)
        Agendamento agendamentoSel = controller.listarAgendamentosDeHoje().get(selectedRow);

        // Solicita ao usuário o defeito relatado via caixa de diálogo
        String defeito = JOptionPane.showInputDialog(this,
                "Iniciando OS para: " + agendamentoSel.getCliente().getNome() +
                "\n\nPor favor, digite o defeito relatado:",
                "Iniciar Ordem de Serviço",
                JOptionPane.QUESTION_MESSAGE);

        // Se o usuário digitou algo (não anulou ou deixou em branco)
        if (defeito != null && !defeito.isBlank()) {
            // Abre a OS no controlador com os dados do agendamento e defeito informado
            OrdemDeServico novaOS = controller.abrirOS(agendamentoSel, defeito);

            if (novaOS != null) {
                // Mostra mensagem de sucesso com o número da OS criada
                AlertsSwing.showAlert("Sucesso", "Ordem de Serviço " + novaOS.getNumeroOS() + " aberta com sucesso!", JOptionPane.INFORMATION_MESSAGE);
                carregarDados(); // Atualiza a lista, removendo o agendamento iniciado

                // Abre a tela para gerenciar a OS recém criada
                new com.mycompany.oficina.gui.ManagerGerenciarOsSwing(novaOS).setVisible(true);
            } else {
                // Caso falhe na abertura da OS, mostra erro
                AlertsSwing.showAlert("Erro", "Não foi possível abrir a Ordem de Serviço.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
