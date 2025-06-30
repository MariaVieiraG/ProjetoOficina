package com.mycompany.oficina.gui;

import com.mycompany.oficina.controller.GerenteController; // Controlador específico do gerente
import com.mycompany.oficina.entidades.Funcionario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Interface gráfica para gerenciamento de funcionários (usada por gerentes).
 * Permite adicionar, editar e remover funcionários com base em uma tabela.
 */
public class FuncionarioManagerSwing extends JFrame {

    private final GerenteController controller;     // Controlador que lida com lógica dos funcionários
    private final JTable tableView;                 // Componente de tabela que exibe os dados
    private final DefaultTableModel tableModel;     // Modelo de dados da tabela

    // Construtor da janela
    public FuncionarioManagerSwing() {
        this.controller = new GerenteController(); // Inicializa o controlador

        setTitle("Gerenciador de Funcionários");   // Título da janela
        setSize(700, 450);                         // Tamanho fixo
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fecha só essa janela
        setLocationRelativeTo(null);               // Centraliza na tela

        // Cabeçalhos da tabela
        String[] columnNames = {"ID", "Nome", "CPF", "Cargo"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Impede edição direta das células
            }
        };
        tableView = new JTable(tableModel); // Cria a tabela com o modelo

        // Painel com os botões de ação
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnEditar = new JButton("Editar");
        JButton btnRemover = new JButton("Remover");
        buttonPanel.add(btnAdicionar);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnRemover);

        // Ações dos botões
        btnAdicionar.addActionListener(e -> handleAdicionar());
        btnEditar.addActionListener(e -> handleEditar());
        btnRemover.addActionListener(e -> handleRemover());

        // Layout principal da janela
        setLayout(new BorderLayout());
        add(new JScrollPane(tableView), BorderLayout.CENTER); // Tabela central com scroll
        add(buttonPanel, BorderLayout.SOUTH);                 // Botões na parte de baixo

        carregarDados(); // Preenche a tabela com dados existentes
    }

    /**
     * Carrega os dados dos funcionários na tabela.
     */
    private void carregarDados() {
        tableModel.setRowCount(0); // Limpa a tabela
        for (Funcionario func : controller.listarFuncionarios()) {
            tableModel.addRow(new Object[]{
                    func.getIdUsuario(),
                    func.getNome(),
                    func.getCpf(),
                    func.getCargo()
            });
        }
    }

    /**
     * Trata a ação de adicionar novo funcionário.
     */
    private void handleAdicionar() {
        FuncionarioDialogo dialog = new FuncionarioDialogo(this, null); // Abre o diálogo vazio
        dialog.setVisible(true); // Exibe a janela

        if (dialog.isConfirmado()) { // Se o usuário clicou em "Salvar"
            Funcionario f = dialog.getFuncionario(); // Pega os dados informados
            if (controller.cadastrarFuncionario(f)) {
                AlertsSwing.showAlert("Sucesso", "Funcionário cadastrado!", JOptionPane.INFORMATION_MESSAGE);
                carregarDados(); // Atualiza a tabela
            } else {
                AlertsSwing.showAlert("Erro", "Não foi possível cadastrar. Verifique se o CPF já existe.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Trata a ação de edição de funcionário.
     */
    private void handleEditar() {
        int selectedRow = tableView.getSelectedRow(); // Pega a linha selecionada
        if (selectedRow < 0) {
            AlertsSwing.showAlert("Seleção Necessária", "Selecione um funcionário para editar.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cpfAntigo = (String) tableModel.getValueAt(selectedRow, 2); // Pega o CPF do funcionário
        Funcionario funcSelecionado = controller.buscarFuncionario(cpfAntigo); // Busca o funcionário pelo CPF

        FuncionarioDialogo dialog = new FuncionarioDialogo(this, funcSelecionado); // Preenche os dados no diálogo
        dialog.setVisible(true); // Exibe a janela

        if (dialog.isConfirmado()) { // Se usuário clicou em "Salvar"
            Funcionario editado = dialog.getFuncionario(); // Dados atualizados
            if (controller.editarFuncionario(cpfAntigo, editado.getCpf(), editado.getSenha(), editado.getCargo(), editado.getNome(), editado.getTelefone(), editado.getEndereco(), editado.getEmail())) {
                AlertsSwing.showAlert("Sucesso", "Funcionário atualizado!", JOptionPane.INFORMATION_MESSAGE);
                carregarDados(); // Atualiza a tabela
            } else {
                AlertsSwing.showAlert("Erro", "Não foi possível atualizar o funcionário.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Trata a ação de remoção de funcionário.
     */
    private void handleRemover() {
        int selectedRow = tableView.getSelectedRow(); // Pega a linha selecionada
        if (selectedRow < 0) {
            AlertsSwing.showAlert("Seleção Necessária", "Selecione um funcionário para remover.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nome = (String) tableModel.getValueAt(selectedRow, 1); // Nome para exibir na confirmação
        String cpf = (String) tableModel.getValueAt(selectedRow, 2);  // CPF usado para remoção

        int confirm = JOptionPane.showConfirmDialog(this, "Remover " + nome + "?", "Confirmar Remoção", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.removerFuncionario(cpf)) {
                AlertsSwing.showAlert("Sucesso", "Funcionário removido.", JOptionPane.INFORMATION_MESSAGE);
                carregarDados(); // Atualiza a tabela
            } else {
                AlertsSwing.showAlert("Erro", "Não foi possível remover o funcionário.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
