package com.mycompany.oficina.gui;

import com.mycompany.oficina.loja.Produto;
import javax.swing.*;
import java.awt.*;

public class ProdutoDialogo extends JDialog {
    // Campos de texto para entrada de dados do produto
    private final JTextField nomeField = new JTextField(20);
    private final JTextField precoField = new JTextField(10);
    private final JTextField quantidadeField = new JTextField(10);
    private final JTextField fornecedorField = new JTextField(20);

    private Produto produto;     // Produto a ser criado ou editado
    private boolean confirmado;  // Indica se o diálogo foi confirmado (salvo)

    // Construtor: recebe janela pai e produto (null para novo)
    public ProdutoDialogo(Frame owner, Produto produto) {
        // Define título conforme edição ou criação, e modal para bloquear janela pai
        super(owner, produto == null ? "Adicionar Peça" : "Editar Peça", true);
        this.produto = produto;

        setLayout(new BorderLayout());
        JPanel panel = new JPanel(new GridBagLayout()); // Painel com layout para organizar campos
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);               // Espaçamento entre componentes
        gbc.anchor = GridBagConstraints.WEST;           // Alinhamento à esquerda

        // Labels e campos na posição correta da grid
        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(nomeField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Preço de Venda:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(precoField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Quantidade:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(quantidadeField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Fornecedor:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; panel.add(fornecedorField, gbc);

        // Se estiver editando, preenche os campos com dados do produto
        if (produto != null) {
            nomeField.setText(produto.getNome());
            precoField.setText(String.valueOf(produto.getPreco()));
            quantidadeField.setText(String.valueOf(produto.getQuantidade()));
            fornecedorField.setText(produto.getFornecedor());

            // Se o produto já tem ID, bloqueia edição da quantidade
            if (produto.getIdProduto() != null) {
                quantidadeField.setEditable(false);
            }
        }

        // Botão para salvar, chama onSave ao clicar
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> onSave());

        // Botão para cancelar e fechar diálogo sem salvar
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        // Painel para botões alinhados à direita
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnSalvar);

        add(panel, BorderLayout.CENTER);       // Painel principal no centro
        add(buttonPanel, BorderLayout.SOUTH);  // Botões na parte inferior

        pack();                     // Ajusta tamanho da janela conforme conteúdo
        setLocationRelativeTo(owner); // Centraliza a janela em relação ao pai
    }

    // Método chamado ao clicar em Salvar para validar e criar/atualizar produto
    private void onSave() {
        // Validação: nenhum campo pode estar vazio
        if (nomeField.getText().isBlank() || precoField.getText().isBlank() || quantidadeField.getText().isBlank() || fornecedorField.getText().isBlank()) {
            AlertsSwing.showAlert("Erro de Validação", "Todos os campos do produto são obrigatórios.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String nome = nomeField.getText();
            double preco = Double.parseDouble(precoField.getText());
            int quantidade = Integer.parseInt(quantidadeField.getText());

            // Validação de valores positivos para preço e quantidade
            if (preco <= 0 || quantidade < 0) {
                AlertsSwing.showAlert("Erro de Validação", "Preço e quantidade devem ser valores positivos.", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Cria um novo produto com os dados fornecidos
            this.produto = new Produto(nome, preco, quantidade, fornecedorField.getText());
            this.confirmado = true;  // Marca como confirmado para o chamador saber que salvou
            dispose();               // Fecha o diálogo
        } catch (NumberFormatException e) {
            // Caso preço ou quantidade não sejam números válidos
            AlertsSwing.showAlert("Erro de Formato", "Preço e quantidade devem ser números válidos.", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Retorna o produto criado ou editado após confirmação
    public Produto getProduto() {
        return produto;
    }

    // Indica se o usuário confirmou (salvou) ou não o diálogo
    public boolean isConfirmado() {
        return confirmado;
    }
}
