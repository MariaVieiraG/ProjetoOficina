package com.mycompany.oficina.gui;

import com.mycompany.oficina.controller.AtendenteController;
import com.mycompany.oficina.entidades.Carro;
import com.mycompany.oficina.entidades.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VeiculoManagerSwing extends JFrame {

    private final AtendenteController controller; // Controlador para lógica de negócios
    private final JTable tableView;               // Tabela para exibir os veículos
    private final DefaultTableModel tableModel;  // Modelo da tabela (dados e colunas)

    public VeiculoManagerSwing() {
        this.controller = new AtendenteController();
        setTitle("Gerenciador de Veículos");         // Título da janela
        setSize(800, 500);                            // Tamanho da janela
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fecha só esta janela, não a app inteira
        setLocationRelativeTo(null);                  // Centraliza a janela na tela

        // Define as colunas da tabela
        String[] columnNames = {"Chassi", "Fabricante", "Modelo", "Placa", "Proprietário"};
        // Cria o modelo da tabela, com células não editáveis
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableView = new JTable(tableModel);

        // Painel com botões para ações (Adicionar, Editar, Remover)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnEditar = new JButton("Editar");
        JButton btnRemover = new JButton("Remover");
        buttonPanel.add(btnAdicionar);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnRemover);

        // Define as ações dos botões para abrir diálogos ou executar remoção
        btnAdicionar.addActionListener(e -> handleAdicionar());
        btnEditar.addActionListener(e -> handleEditar());
        btnRemover.addActionListener(e -> handleRemover());

        // Define o layout da janela e adiciona a tabela e os botões
        setLayout(new BorderLayout());
        add(new JScrollPane(tableView), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Carrega os dados dos veículos na tabela ao iniciar
        carregarDados();
    }

    // Método para carregar os dados da lista de veículos na tabela
    private void carregarDados() {
        tableModel.setRowCount(0); // Limpa linhas anteriores
        for (Carro carro : controller.listarVeiculos()) {
            tableModel.addRow(new Object[]{
                    carro.getChassi(),
                    carro.getFabricante(),
                    carro.getModelo(),
                    carro.getPlaca(),
                    carro.getNomeDono() // Mostra nome do proprietário
            });
        }
    }

    // Ação ao clicar em "Adicionar"
    private void handleAdicionar() {
        // Solicita o CPF do proprietário para associar o veículo
        String cpf = JOptionPane.showInputDialog(this, "Para adicionar um veículo, informe o CPF do proprietário:", "Buscar Cliente", JOptionPane.QUESTION_MESSAGE);

        if (cpf == null || cpf.isBlank()) return; // Se cancelar ou vazio, sai

        Cliente proprietario = controller.buscarCliente(cpf); // Busca cliente pelo CPF
        if (proprietario == null) {
            // Se cliente não existe, mostra erro
            AlertsSwing.showAlert("Erro", "Cliente não encontrado. Cadastre o cliente primeiro.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Abre diálogo para cadastrar veículo, sem veículo pré-existente
        VeiculoDialogo dialog = new VeiculoDialogo(this, null);
        dialog.setVisible(true);

        // Se o diálogo foi confirmado, tenta cadastrar o veículo pelo controller
        if (dialog.isConfirmado()) {
            Carro carro = dialog.getCarro();
            if (controller.cadastrarVeiculo(proprietario.getCpf(), carro.getFabricante(), carro.getModelo(), carro.getPlaca(), carro.getChassi())) {
                AlertsSwing.showAlert("Sucesso", "Veículo cadastrado!", JOptionPane.INFORMATION_MESSAGE);
                carregarDados(); // Atualiza tabela
            } else {
                // Pode falhar se chassi já existe ou dados inválidos
                AlertsSwing.showAlert("Erro", "Chassi já existe ou dados inválidos.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Ação ao clicar em "Editar"
    private void handleEditar() {
        int selectedRow = tableView.getSelectedRow();
        if (selectedRow < 0) {
            // Se nada selecionado, avisa
            AlertsSwing.showAlert("Seleção Necessária", "Selecione um veículo para editar.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obtém o chassi do veículo selecionado para buscar objeto completo
        String chassi = (String) tableModel.getValueAt(selectedRow, 0);
        Carro carroSelecionado = controller.buscarVeiculo(chassi);

        // Abre diálogo com dados do veículo para editar
        VeiculoDialogo dialog = new VeiculoDialogo(this, carroSelecionado);
        dialog.setVisible(true);

        if (dialog.isConfirmado()) {
            Carro editado = dialog.getCarro();
            // Tenta atualizar o veículo pelo controller
            if (controller.editarVeiculo(chassi, editado.getFabricante(), editado.getModelo(), editado.getPlaca())) {
                AlertsSwing.showAlert("Sucesso", "Veículo atualizado!", JOptionPane.INFORMATION_MESSAGE);
                carregarDados(); // Atualiza tabela
            } else {
                AlertsSwing.showAlert("Erro", "Não foi possível atualizar o veículo.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Ação ao clicar em "Remover"
    private void handleRemover() {
        // 1. Obtém linha selecionada na tabela
        int selectedRow = tableView.getSelectedRow();
        if (selectedRow < 0) {
            AlertsSwing.showAlert("Seleção Necessária", "Selecione um veículo para remover.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Obtém informações para mensagem de confirmação
        String chassi = (String) tableModel.getValueAt(selectedRow, 0);
        String modelo = (String) tableModel.getValueAt(selectedRow, 2);

        // 3. Confirma remoção com usuário
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja remover o veículo " + modelo + " (Chassi: " + chassi + ")?",
                "Confirmar Remoção",
                JOptionPane.YES_NO_OPTION
        );

        // 4. Se confirmado, tenta remover via controller
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.removerVeiculo(chassi)) {
                AlertsSwing.showAlert("Sucesso", "Veículo removido com sucesso!", JOptionPane.INFORMATION_MESSAGE);
                carregarDados(); // Atualiza tabela após remoção
            } else {
                // Pode falhar se veículo estiver vinculado a ordens de serviço, por exemplo
                AlertsSwing.showAlert("Erro", "Não foi possível remover o veículo.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
