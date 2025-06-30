package com.mycompany.oficina.gui; // Ou gui.swing

import com.mycompany.oficina.entidades.Carro;
import javax.swing.*;
import java.awt.*;

public class VeiculoDialogo extends JDialog {
    // Campos de texto para entrada dos dados do veículo
    private final JTextField fabricanteField = new JTextField(20);
    private final JTextField modeloField = new JTextField(20);
    private final JTextField placaField = new JTextField(20);
    private final JTextField chassiField = new JTextField(20);

    private Carro carro;       // Objeto Carro a ser criado ou editado
    private boolean confirmado; // Indica se o diálogo foi confirmado (salvo)

    // Construtor recebe janela pai e um objeto Carro (null para novo)
    public VeiculoDialogo(Frame owner, Carro carro) {
        // Define título conforme criação ou edição, diálogo modal
        super(owner, carro == null ? "Adicionar Veículo" : "Editar Veículo", true);
        this.carro = carro;

        setLayout(new BorderLayout());
        JPanel panel = new JPanel(new GridBagLayout()); // Painel com layout flexível
        GridBagConstraints gbc = createGbc();           // Configuração do GridBagConstraints

        // Adiciona labels e campos na posição correta da grade
        addField(panel, gbc, 0, "Fabricante:", fabricanteField);
        addField(panel, gbc, 1, "Modelo:", modeloField);
        addField(panel, gbc, 2, "Placa:", placaField);
        addField(panel, gbc, 3, "Chassi:", chassiField);

        // Se for edição, preenche os campos com dados do carro e bloqueia edição do chassi
        if (carro != null) {
            fabricanteField.setText(carro.getFabricante());
            modeloField.setText(carro.getModelo());
            placaField.setText(carro.getPlaca());
            chassiField.setText(carro.getChassi());
            chassiField.setEditable(false); // Chassi não pode ser alterado
        }

        // Botão salvar com ação para salvar os dados
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> onSave());

        // Botão cancelar que fecha o diálogo sem salvar
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        // Painel para os botões alinhados à direita
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnSalvar);

        // Adiciona o painel principal e o painel de botões à janela
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();                     // Ajusta tamanho da janela conforme componentes
        setLocationRelativeTo(owner); // Centraliza em relação à janela pai
    }

    // Método chamado ao clicar em Salvar para validar e criar objeto Carro
    private void onSave() {
        // Verifica se algum campo obrigatório está vazio
        if (fabricanteField.getText().isBlank() || modeloField.getText().isBlank() ||
                placaField.getText().isBlank() || chassiField.getText().isBlank()) {
            AlertsSwing.showAlert("Erro de Validação", "Todos os campos do veículo são obrigatórios.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Cria um novo objeto Carro com os dados dos campos
        // Os dois primeiros parâmetros "" e "" podem ser ID ou outro dado interno que não precisa aqui
        this.carro = new Carro("", "", fabricanteField.getText(), modeloField.getText(), placaField.getText(), chassiField.getText());
        this.confirmado = true; // Marca como confirmado para saber que salvou
        dispose();              // Fecha o diálogo
    }

    // Retorna o objeto Carro criado ou editado
    public Carro getCarro() {
        return carro;
    }

    // Retorna se o diálogo foi confirmado (salvo) ou não
    public boolean isConfirmado() {
        return confirmado;
    }

    // Método auxiliar para adicionar label e campo ao painel na linha 'y'
    private void addField(JPanel panel, GridBagConstraints gbc, int y, String label, JComponent component) {
        gbc.gridx = 0; gbc.gridy = y; panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.gridy = y; panel.add(component, gbc);
    }

    // Configuração básica do GridBagConstraints para layout
    private GridBagConstraints createGbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espaçamento entre componentes
        gbc.anchor = GridBagConstraints.WEST; // Alinhamento à esquerda
        return gbc;
    }
}
