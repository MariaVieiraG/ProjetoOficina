package com.mycompany.oficina.gui;

import java.util.List;

import com.mycompany.oficina.agendamento.Agendamento;
import com.mycompany.oficina.agendamento.TipoServico;
import com.mycompany.oficina.controller.AtendenteController;
import com.mycompany.oficina.entidades.*;
import com.mycompany.oficina.gui.AlertsSwing;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Vector;

/**
 * Diálogo de interface gráfica responsável por criar um novo agendamento
 * de serviço na oficina.
 * <p>
 * Esta classe exibe uma janela modal com campos para selecionar cliente,
 * veículo, mecânico, tipo de serviço e data/hora. O agendamento é validado
 * e criado somente após a confirmação do usuário.
 * <p>
 * A interação é realizada com base no controlador {@code AtendenteController}.
 *
 * @author Maria
 */
public class AgendamentoDialogo extends JDialog {

    private final AtendenteController controller;
    private final JComboBox<Cliente> clienteCombo;
    private final JComboBox<Carro> carroCombo;
    private final JComboBox<Funcionario> mecanicoCombo;
    private final JComboBox<TipoServico> servicoCombo;
    private final JComboBox<Elevador> elevadorCombo;
    private final JTextField dataHoraField = new JTextField("dd/MM/yyyy HH:mm", 15);

    private Agendamento agendamento;
    private boolean confirmado;

    /**
     * Constrói o diálogo de agendamento, configurando campos e ouvintes.
     *
     * @param owner      janela pai
     * @param controller controlador usado para acessar dados
     */
    public AgendamentoDialogo(Frame owner, AtendenteController controller) {
        super(owner, "Novo Agendamento", true);
        this.controller = controller;

        clienteCombo = new JComboBox<>(new Vector<>(controller.listarClientes()));
        carroCombo = new JComboBox<>();
        mecanicoCombo = new JComboBox<>(new Vector<>(controller.listarMecanicosDisponiveis()));
        servicoCombo = new JComboBox<>(new Vector<>(List.of(TipoServico.values())));
        elevadorCombo = new JComboBox<>(new Vector<>(controller.listarElevadores()));
        setupLayout();
        setupComboBoxRenderers();
        setupListeners();

        pack();
        setLocationRelativeTo(owner);
    }

    /**
     * Define o layout do formulário de agendamento.
     */
    private void setupLayout() {
        setLayout(new BorderLayout());
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = createGbc();

        addField(panel, gbc, 0, "Cliente:", clienteCombo);
        addField(panel, gbc, 1, "Veículo:", carroCombo);
        addField(panel, gbc, 2, "Mecânico:", mecanicoCombo);
        addField(panel, gbc, 3, "Elevador:", elevadorCombo);
        addField(panel, gbc, 4, "Serviço:", servicoCombo);
        addField(panel, gbc, 5, "Data e Hora:", dataHoraField);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> onSave());

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnSalvar);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Define como os itens dos JComboBoxes serão renderizados na interface.
     */
    private void setupComboBoxRenderers() {
        clienteCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Cliente) {
                    setText(((Cliente) value).getNome());
                }
                return this;
            }
        });

        carroCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Carro) {
                    Carro carro = (Carro) value;
                    setText(carro.getModelo() + " (" + carro.getPlaca() + ")");
                }
                return this;
            }
        });

        elevadorCombo.setRenderer(new DefaultListCellRenderer() {
            @Override

            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Elevador) {
                    // O método toString() da classe Elevador já é bom para exibição
                    setText(value.toString());
                }
                return this;
            }
        });
        elevadorCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Elevador) {
                    // Deve usar getDescricao()
                    setText(((Elevador) value).getDescricao());
                }
                return this;
            }
        });

    }

    /**
     * Define os ouvintes de eventos, como a atualização dos veículos ao trocar o cliente.
     */
    private void setupListeners() {
        clienteCombo.addActionListener(e -> {
            Cliente selecionado = (Cliente) clienteCombo.getSelectedItem();
            carroCombo.removeAllItems();
            if (selecionado != null) {
                controller.listarVeiculosDoCliente(selecionado.getCpf()).forEach(carroCombo::addItem);
                carroCombo.setEnabled(true);
            } else {
                carroCombo.setEnabled(false);
            }
        });
    }

    /**
     * Executado quando o botão "Salvar" é pressionado. Valida os campos e cria o agendamento.
     */
    private void onSave() {
        if (clienteCombo.getSelectedItem() == null || carroCombo.getSelectedItem() == null
                || mecanicoCombo.getSelectedItem() == null || servicoCombo.getSelectedItem() == null
                || elevadorCombo.getSelectedItem() == null
                || dataHoraField.getText().isBlank()) {
            AlertsSwing.showAlert("Campos Vazios", "Todos os campos são obrigatórios.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime dataHora = LocalDateTime.parse(dataHoraField.getText(), formatter);

            this.agendamento = new Agendamento(
                    (Cliente) clienteCombo.getSelectedItem(),
                    (Carro) carroCombo.getSelectedItem(),
                    (Funcionario) mecanicoCombo.getSelectedItem(),
                    (TipoServico) servicoCombo.getSelectedItem(),
                    (Elevador) elevadorCombo.getSelectedItem(),
                    dataHora
            );
            this.confirmado = true;
            dispose();

        } catch (DateTimeParseException e) {
            AlertsSwing.showAlert("Erro de Formato", "O formato da data/hora está inválido. Use dd/MM/yyyy HH:mm.", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Retorna o agendamento criado, se confirmado.
     *
     * @return objeto {@code Agendamento}
     */
    public Agendamento getAgendamento() {
        return agendamento;
    }

    /**
     * Indica se o usuário confirmou o agendamento.
     *
     * @return true se confirmado, false caso contrário
     */
    public boolean isConfirmado() {
        return confirmado;
    }

    /**
     * Adiciona um campo com rótulo ao painel, utilizando GridBagConstraints.
     *
     * @param panel     painel alvo
     * @param gbc       constraints
     * @param y         linha do grid
     * @param label     texto do rótulo
     * @param component componente a adicionar
     */
    private void addField(JPanel panel, GridBagConstraints gbc, int y, String label, JComponent component) {
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        gbc.gridy = y;
        panel.add(component, gbc);
    }

    /**
     * Cria o objeto {@code GridBagConstraints} padrão utilizado nos componentes.
     *
     * @return objeto {@code GridBagConstraints} configurado
     */
    private GridBagConstraints createGbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        return gbc;
    }
}
