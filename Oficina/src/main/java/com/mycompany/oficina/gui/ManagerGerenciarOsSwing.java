package com.mycompany.oficina.gui;

import com.mycompany.oficina.controller.MecanicoController;
import com.mycompany.oficina.gui.menus.ExtratoOsDialogo;
import com.mycompany.oficina.loja.Produto;
import com.mycompany.oficina.ordemservico.OrdemDeServico;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class ManagerGerenciarOsSwing extends JFrame {

    private final MecanicoController controller;      // Controlador para gerenciar OSs
    private final JTable tableView;                    // Tabela que mostra as ordens de serviço
    private final DefaultTableModel tableModel;        // Modelo da tabela para manipular os dados
    private final OrdemDeServico osInicial;             // Ordem de serviço selecionada inicialmente (opcional)

    // Construtor padrão que chama o outro passando null para ordem inicial
    public ManagerGerenciarOsSwing() {
        this(null);
    }

    // Construtor principal que recebe uma OS inicial para pré-seleção
    public ManagerGerenciarOsSwing(OrdemDeServico os) {
        this.controller = new MecanicoController();
        this.osInicial = os;

        setTitle("Gerenciar Ordens de Serviço Ativas");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fecha só essa janela
        setLocationRelativeTo(null); // Centraliza janela

        // Define colunas da tabela
        String[] columnNames = {"Nº OS", "Cliente", "Veículo", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; } // Não permite edição direta
        };
        tableView = new JTable(tableModel);

        // Cria painel com botões e ações da OS
        JPanel actionsPanel = createActionsPanel();

        // Layout principal com espaçamento
        setLayout(new BorderLayout(10, 10));
        add(new JScrollPane(tableView), BorderLayout.CENTER); // Tabela no centro
        add(actionsPanel, BorderLayout.EAST); // Painel de ações à direita

        carregarDados(); // Preenche a tabela com OSs ativas
    }

    // Preenche a tabela com as OS ativas do controlador
    private void carregarDados() {
        tableModel.setRowCount(0); // Limpa tabela
        for (OrdemDeServico os : controller.listarOSAtivas()) {
            tableModel.addRow(new Object[]{
                    os.getNumeroOS(),          // Número da OS
                    os.getCliente().getNome(), // Nome do cliente
                    os.getCarro().getModelo(), // Modelo do carro
                    os.getStatusAtual()        // Status atual da OS
            });
        }
        // Se uma OS inicial foi passada, seleciona ela na tabela
        if (osInicial != null) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).equals(osInicial.getNumeroOS())) {
                    tableView.setRowSelectionInterval(i, i);
                    break;
                }
            }
        }
    }

    // Cria painel com botões para ações sobre a OS
    private JPanel createActionsPanel() {
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS)); // Layout vertical
        actionsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Ações da OS"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Cria botões para as ações principais
        JButton btnIniciarInspecao = new JButton("Iniciar Inspeção");
        JButton btnIniciarServico = new JButton("Iniciar Serviço");
        JButton btnAdicionarPeca = new JButton("Adicionar Peça");
        JButton btnFinalizarServico = new JButton("Finalizar Serviço");
        JButton btnVerExtrato = new JButton("Ver Extrato");

        // Formata botões para ocupar toda largura e ficar centralizados
        formatButton(btnIniciarInspecao);
        formatButton(btnIniciarServico);
        formatButton(btnAdicionarPeca);
        formatButton(btnFinalizarServico);
        formatButton(btnVerExtrato);

        // Adiciona listeners para as ações dos botões
        btnIniciarInspecao.addActionListener(e -> {
            OrdemDeServico sel = getSelectedOS();
            if(sel != null) {
                controller.iniciarInspecaoOS(sel); // Muda status para inspeção
                carregarDados();                   // Atualiza tabela
            }
        });

        btnIniciarServico.addActionListener(e -> {
            OrdemDeServico sel = getSelectedOS();
            if(sel != null) {
                controller.iniciarServicoOS(sel);  // Muda status para serviço
                carregarDados();
            }
        });

        btnAdicionarPeca.addActionListener(e -> handleAdicionarPeca());

        btnFinalizarServico.addActionListener(e -> {
            OrdemDeServico sel = getSelectedOS();
            if(sel != null) {
                controller.finalizarServicoOS(sel); // Finaliza OS
                AlertsSwing.showExtrato("Serviço Finalizado", controller.gerarExtratoOS(sel)); // Mostra extrato
                carregarDados();
            }
        });

        btnVerExtrato.addActionListener(e -> {
            OrdemDeServico sel = getSelectedOS();
            if (sel != null) {
                new ExtratoOsDialogo(this, sel).setVisible(true);
            }
        });

        // Atualiza os botões conforme a seleção da OS na tabela e status dela
        tableView.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                OrdemDeServico sel = getSelectedOS();
                boolean isSelected = sel != null;
                String status = isSelected ? sel.getStatusAtual() : "";

                btnIniciarInspecao.setEnabled(isSelected && "Aguardando".equals(status));
                btnIniciarServico.setEnabled(isSelected && "Em Inspeção".equals(status));
                btnAdicionarPeca.setEnabled(isSelected && "Em Serviço".equals(status));
                btnFinalizarServico.setEnabled(isSelected && "Em Serviço".equals(status));
                btnVerExtrato.setEnabled(isSelected);
            }
        });

        // Adiciona botões ao painel com espaçamento
        actionsPanel.add(btnIniciarInspecao);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        actionsPanel.add(btnIniciarServico);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        actionsPanel.add(btnAdicionarPeca);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        actionsPanel.add(btnFinalizarServico);
        actionsPanel.add(Box.createVerticalStrut(20)); // Espaço maior antes do último botão
        actionsPanel.add(btnVerExtrato);

        return actionsPanel;
    }

    // Retorna a OS selecionada na tabela, ou null se nada selecionado
    private OrdemDeServico getSelectedOS() {
        int selectedRow = tableView.getSelectedRow();
        if (selectedRow < 0) return null;
        String osId = (String) tableModel.getValueAt(selectedRow, 0);
        return controller.buscarOS(osId);
    }

    // Lida com a ação de adicionar peça à OS selecionada
    private void handleAdicionarPeca() {
        OrdemDeServico osSel = getSelectedOS();
        if (osSel == null) return;

        // ComboBox para seleção de peças disponíveis no estoque
        JComboBox<Produto> pecaCombo = new JComboBox<>(new Vector<>(controller.listarProdutosEstoque()));
        JTextField quantidadeField = new JTextField("1", 5);

        // Renderizador customizado para mostrar nome e quantidade disponível da peça no ComboBox
        pecaCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Produto) {
                    setText(((Produto) value).getNome() + " (Qtd: " + ((Produto) value).getQuantidade() + ")");
                }
                return this;
            }
        });

        // Painel com layout de grade para inserir a peça e quantidade
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("Peça:"));
        panel.add(pecaCombo);
        panel.add(new JLabel("Quantidade:"));
        panel.add(quantidadeField);

        // Diálogo de confirmação
        int result = JOptionPane.showConfirmDialog(this, panel, "Adicionar Peça à OS", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            Produto produto = (Produto) pecaCombo.getSelectedItem();
            try {
                int quantidade = Integer.parseInt(quantidadeField.getText());
                if (produto != null && quantidade > 0) {
                    controller.adicionarPecaOS(osSel, produto, quantidade); // Adiciona peça na OS
                    AlertsSwing.showAlert("Sucesso", "Peça adicionada!", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    AlertsSwing.showAlert("Erro", "Peça ou quantidade inválida.", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                AlertsSwing.showAlert("Erro de Formato", "A quantidade deve ser um número.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Formata botão para largura máxima e alinhamento central
    private void formatButton(JButton button) {
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}
