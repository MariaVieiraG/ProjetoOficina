package com.mycompany.oficina.gui;

import com.mycompany.oficina.controller.GerenteController;
import com.mycompany.oficina.gui.ProdutoDialogo;
import com.mycompany.oficina.loja.Produto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Janela Swing para gerenciamento do estoque de produtos/peças.
 * Permite cadastrar novas peças e repor o estoque de peças existentes.
 */
public class ManagerEstoqueSwing extends JFrame {

    private final GerenteController controller; // Controlador responsável pela lógica de estoque
    private final JTable tableView;             // Tabela para exibir os produtos
    private final DefaultTableModel tableModel; // Modelo de dados da tabela

    // Construtor da janela
    public ManagerEstoqueSwing() {
        this.controller = new GerenteController(); // Instancia o controlador

        setTitle("Módulo de Estoque"); // Define o título da janela
        setSize(700, 450);             // Define tamanho fixo
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fecha somente essa janela
        setLocationRelativeTo(null);   // Centraliza na tela

        // Define os nomes das colunas da tabela
        String[] columnNames = {"ID", "Nome", "Preço Venda", "Qtd.", "Fornecedor"};

        // Inicializa o modelo da tabela, impedindo edição direta das células
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableView = new JTable(tableModel); // Cria a tabela com o modelo

        // Painel para botões com espaçamento
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnCadastrarPeca = new JButton("Cadastrar Nova Peça");
        JButton btnReporEstoque = new JButton("Repor Estoque");
        buttonPanel.add(btnCadastrarPeca);
        buttonPanel.add(btnReporEstoque);

        // Adiciona ação ao botão cadastrar nova peça
        btnCadastrarPeca.addActionListener(e -> handleCadastrarPeca());
        // Adiciona ação ao botão repor estoque
        btnReporEstoque.addActionListener(e -> handleReporEstoque());

        // Layout principal da janela: tabela central e botões embaixo
        setLayout(new BorderLayout());
        add(new JScrollPane(tableView), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        carregarDados(); // Preenche a tabela com dados do estoque
    }

    /**
     * Carrega os dados atuais do estoque na tabela.
     */
    private void carregarDados() {
        tableModel.setRowCount(0); // Limpa as linhas existentes
        for (Produto produto : controller.listarProdutos()) {
            tableModel.addRow(new Object[]{
                    produto.getIdProduto(),                  // ID do produto
                    produto.getNome(),                       // Nome do produto
                    String.format("%.2f", produto.getPreco()), // Preço formatado com 2 casas decimais
                    produto.getQuantidade(),                 // Quantidade disponível
                    produto.getFornecedor()                  // Nome do fornecedor
            });
        }
    }

    /**
     * Trata a ação de cadastrar uma nova peça no estoque.
     */
    private void handleCadastrarPeca() {
        ProdutoDialogo dialog = new ProdutoDialogo(this, null); // Abre diálogo vazio para cadastro
        dialog.setVisible(true);

        if (dialog.isConfirmado()) { // Se usuário confirmou o cadastro
            Produto p = dialog.getProduto(); // Obtém os dados da nova peça
            if (controller.cadastrarNovaPeca(p.getNome(), p.getPreco(), p.getQuantidade(), p.getFornecedor())) {
                AlertsSwing.showAlert("Sucesso", "Nova peça cadastrada e despesa registrada!", JOptionPane.INFORMATION_MESSAGE);
                carregarDados(); // Atualiza a tabela após cadastro
            } else {
                AlertsSwing.showAlert("Erro", "Não foi possível cadastrar a peça.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Trata a ação de repor a quantidade em estoque de uma peça selecionada.
     */
    private void handleReporEstoque() {
        int selectedRow = tableView.getSelectedRow(); // Linha selecionada na tabela
        if (selectedRow < 0) {
            AlertsSwing.showAlert("Seleção Necessária", "Selecione um produto para repor o estoque.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obtém o ID do produto da linha selecionada
        String idProduto = (String) tableModel.getValueAt(selectedRow, 0);
        Produto sel = controller.buscarProduto(idProduto); // Busca o produto pelo ID

        // Pergunta ao usuário a quantidade para adicionar ao estoque
        String qtdStr = JOptionPane.showInputDialog(this, "Quantidade a adicionar:", "Repor Estoque para: " + sel.getNome(), JOptionPane.QUESTION_MESSAGE);

        if (qtdStr != null) { // Se o usuário não cancelou
            try {
                int quantidade = Integer.parseInt(qtdStr); // Tenta converter para número inteiro
                if (controller.reporEstoque(sel, quantidade)) {
                    AlertsSwing.showAlert("Sucesso", "Estoque atualizado!", JOptionPane.INFORMATION_MESSAGE);
                    carregarDados(); // Atualiza tabela
                } else {
                    AlertsSwing.showAlert("Erro", "Não foi possível repor o estoque.", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                AlertsSwing.showAlert("Erro de Formato", "Por favor, insira um número válido.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
