package com.mycompany.oficina.gui;

import com.mycompany.oficina.controller.AtendenteController;
import com.mycompany.oficina.entidades.Cliente;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Classe responsável pela interface gráfica de gerenciamento de clientes.
 * Permite adicionar, editar e remover clientes por meio de uma tabela.
 */
public class ClienteManagerSwing extends JFrame {

    private final AtendenteController controller; // Controlador que gerencia a lógica
    private final JTable tableView;               // Tabela que exibe os clientes
    private final DefaultTableModel tableModel;   // Modelo da tabela (estrutura de dados)

    public ClienteManagerSwing() {
        this.controller = new AtendenteController(); // Inicializa o controlador
        setTitle("Gerenciador de Clientes");         // Define o título da janela
        setSize(800, 500);                           // Define o tamanho da janela
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fecha a janela sem encerrar a aplicação
        setLocationRelativeTo(null);                // Centraliza a janela

        // Define o cabeçalho da tabela
        String[] columnNames = {"ID", "Nome", "CPF", "Telefone", "Email"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Torna as células da tabela não editáveis
            }
        };
        tableView = new JTable(tableModel); // Cria a tabela com o modelo

        // Cria painel de botões (adicionar, editar, remover)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnEditar = new JButton("Editar");
        JButton btnRemover = new JButton("Remover");
        buttonPanel.add(btnAdicionar);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnRemover);

        // Define as ações dos botões
        btnAdicionar.addActionListener(e -> handleAdicionar());
        btnEditar.addActionListener(e -> handleEditar());
        btnRemover.addActionListener(e -> handleRemover());

        // Define o layout principal da janela
        setLayout(new BorderLayout());
        add(new JScrollPane(tableView), BorderLayout.CENTER); // Adiciona a tabela com scroll
        add(buttonPanel, BorderLayout.SOUTH);                 // Adiciona os botões na parte inferior

        carregarDados(); // Carrega os dados dos clientes na tabela
    }

    /**
     * Carrega os dados dos clientes na tabela.
     */
    private void carregarDados() {
        tableModel.setRowCount(0); // Limpa a tabela
        for (Cliente cliente : controller.listarClientes()) {
            tableModel.addRow(new Object[]{
                    cliente.getIdCliente(),
                    cliente.getNome(),
                    cliente.getCpf(),
                    cliente.getTelefone(),
                    cliente.getEmail()
            });
        }
    }

    /**
     * Manipula a ação de adicionar novo cliente.
     */
    private void handleAdicionar() {
        ClienteDialogo dialog = new ClienteDialogo(this, null); // Abre o diálogo vazio
        dialog.setVisible(true); // Exibe o diálogo

        if (dialog.isConfirmado()) {
            Cliente novoCliente = dialog.getCliente(); // Obtém o cliente preenchido
            // Tenta cadastrar o cliente
            if (controller.cadastrarCliente(novoCliente)) {
                AlertsSwing.showAlert("Sucesso", "Cliente cadastrado!", JOptionPane.INFORMATION_MESSAGE);
                carregarDados(); // Atualiza a tabela
            } else {
                AlertsSwing.showAlert("Erro", "CPF já existe ou dados inválidos.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Manipula a ação de edição de cliente.
     */
    private void handleEditar() {
        int selectedRow = tableView.getSelectedRow(); // Obtém a linha selecionada
        if (selectedRow < 0) {
            AlertsSwing.showAlert("Seleção Necessária", "Selecione um cliente para editar.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cpfAntigo = (String) tableModel.getValueAt(selectedRow, 2); // Pega o CPF do cliente selecionado
        Cliente clienteSelecionado = controller.buscarCliente(cpfAntigo); // Busca o cliente no sistema

        if (clienteSelecionado == null) {
            AlertsSwing.showAlert("Erro", "Não foi possível encontrar o cliente selecionado.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ClienteDialogo dialog = new ClienteDialogo(this, clienteSelecionado); // Abre o diálogo preenchido
        dialog.setVisible(true); // Exibe o diálogo

        if (dialog.isConfirmado()) {
            Cliente clienteEditado = dialog.getCliente(); // Pega os dados atualizados
            // Tenta editar o cliente
            if (controller.editarCliente(cpfAntigo, clienteEditado.getNome(), clienteEditado.getCpf(), clienteEditado.getTelefone(), clienteEditado.getEndereco(), clienteEditado.getEmail())) {
                AlertsSwing.showAlert("Sucesso", "Cliente atualizado!", JOptionPane.INFORMATION_MESSAGE);
                carregarDados(); // Atualiza a tabela
            } else {
                AlertsSwing.showAlert("Erro", "Não foi possível atualizar o cliente.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Manipula a ação de remoção de cliente.
     */
    private void handleRemover() {
        int selectedRow = tableView.getSelectedRow(); // Obtém a linha selecionada
        if (selectedRow < 0) {
            AlertsSwing.showAlert("Seleção Necessária", "Selecione um cliente para remover.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nome = (String) tableModel.getValueAt(selectedRow, 1); // Nome do cliente
        String cpf = (String) tableModel.getValueAt(selectedRow, 2);  // CPF do cliente

        // Confirma a remoção com o usuário
        int confirm = JOptionPane.showConfirmDialog(this, "Remover " + nome + "?", "Confirmar Remoção", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Tenta remover o cliente
            if (controller.removerCliente(cpf)) {
                AlertsSwing.showAlert("Sucesso", "Cliente removido.", JOptionPane.INFORMATION_MESSAGE);
                carregarDados(); // Atualiza a tabela
            } else {
                AlertsSwing.showAlert("Erro", "Não foi possível remover. Verifique se o cliente possui veículos.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
