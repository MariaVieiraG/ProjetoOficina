package com.mycompany.oficina.gui;

import com.mycompany.oficina.entidades.Funcionario;
import com.mycompany.oficina.strategy.*;
import javax.swing.*;
import java.awt.*;

/**
 * Diálogo Swing para cadastro ou edição de funcionários.
 */
public class FuncionarioDialogo extends JDialog {
    // Campos de entrada de dados do funcionário
    private final JTextField nomeField = new JTextField(20);
    private final JTextField cpfField = new JTextField(20);
    private final JTextField telefoneField = new JTextField(20);
    private final JTextField enderecoField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JPasswordField senhaField = new JPasswordField(20);
    private final JComboBox<String> cargoComboBox = new JComboBox<>(new String[]{"Admin", "Atendente", "Mecanico"});

    // Funcionário que está sendo criado ou editado
    private Funcionario funcionario;
    private boolean confirmado; // Flag que indica se o usuário confirmou a operação

    // Construtor
    public FuncionarioDialogo(Frame owner, Funcionario func) {
        super(owner, func == null ? "Adicionar Funcionário" : "Editar Funcionário", true);
        this.funcionario = func;

        setLayout(new BorderLayout());
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = createGbc();

        // Adiciona os campos ao painel
        addField(panel, gbc, 0, "Nome:", nomeField);
        addField(panel, gbc, 1, "CPF:", cpfField);
        addField(panel, gbc, 2, "Telefone:", telefoneField);
        addField(panel, gbc, 3, "Endereço:", enderecoField);
        addField(panel, gbc, 4, "Email:", emailField);
        addField(panel, gbc, 5, "Cargo:", cargoComboBox);
        addField(panel, gbc, 6, "Senha:", senhaField);

        // Se estiver editando um funcionário existente, preenche os campos
        if (func != null) {
            nomeField.setText(func.getNome());
            cpfField.setText(func.getCpf());
            telefoneField.setText(func.getTelefone());
            enderecoField.setText(func.getEndereco());
            emailField.setText(func.getEmail());
            cargoComboBox.setSelectedItem(func.getCargo());
            senhaField.setToolTipText("Deixe em branco para não alterar");
        }

        // Botões de ação
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> onSave());

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnSalvar);

        // Adiciona os painéis à janela
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();                     // Ajusta o tamanho automático da janela
        setLocationRelativeTo(owner); // Centraliza em relação à janela principal
    }

    // Ação executada ao clicar em "Salvar"
    private void onSave() {
        if (!validateInput()) return; // Valida os campos

        String senha = new String(senhaField.getPassword());
        if(funcionario == null) {
            // Criação de novo funcionário
            this.funcionario = new Funcionario(
                senha,
                (String) cargoComboBox.getSelectedItem(),
                nomeField.getText(),
                cpfField.getText(),
                telefoneField.getText(),
                enderecoField.getText(),
                emailField.getText()
            );
        } else {
            // Atualização de dados
            if(!senha.isBlank()) {
                this.funcionario.setSenha(senha); // Altera a senha somente se foi informada
            }
            this.funcionario.setCargo((String) cargoComboBox.getSelectedItem());
            this.funcionario.setNome(nomeField.getText());
            this.funcionario.setCpf(cpfField.getText());
            this.funcionario.setTelefone(telefoneField.getText());
            this.funcionario.setEndereco(enderecoField.getText());
            this.funcionario.setEmail(emailField.getText());
        }
        confirmado = true;
        dispose(); // Fecha o diálogo
    }

    // Validação dos campos de entrada
    private boolean validateInput() {
        boolean isEditing = (this.funcionario != null);
        // Verifica se os campos obrigatórios estão preenchidos
        if (nomeField.getText().isBlank() || cpfField.getText().isBlank() ||
            telefoneField.getText().isBlank() || enderecoField.getText().isBlank() ||
            emailField.getText().isBlank() || cargoComboBox.getSelectedItem() == null ||
            (!isEditing && new String(senhaField.getPassword()).isBlank())) {
            AlertsSwing.showAlert("Erro de Validação", "Todos os campos devem ser preenchidos.", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Valida CPF, telefone e e-mail usando Strategy
        if (!new CpfValidate().validar(cpfField.getText()) ||
            !new TelefoneValidate().validar(telefoneField.getText()) ||
            !new EmailValidate().validar(emailField.getText())) {
            AlertsSwing.showAlert("Erro de Validação", "CPF, Telefone ou E-mail inválido.", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    // Retorna o funcionário criado ou editado
    public Funcionario getFuncionario() {
        return funcionario;
    }

    // Informa se o botão "Salvar" foi pressionado
    public boolean isConfirmado() {
        return confirmado;
    }

    // Adiciona um rótulo e campo ao painel
    private void addField(JPanel panel, GridBagConstraints gbc, int y, String label, JComponent component) {
        gbc.gridx = 0; gbc.gridy = y; panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.gridy = y; panel.add(component, gbc);
    }

    // Cria um GridBagConstraints com padding e alinhamento padrão
    private GridBagConstraints createGbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        return gbc;
    }
}
