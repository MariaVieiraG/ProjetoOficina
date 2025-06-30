package com.mycompany.oficina.gui.menus;

import com.mycompany.oficina.ordemservico.OrdemDeServico;
import com.mycompany.oficina.ordemservico.PecaUtilizada;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

public class ExtratoOsDialogo  extends JDialog {
    public ExtratoOsDialogo(Frame owner, OrdemDeServico os) {
        super(owner, "Extrato da OS: " + os.getNumeroOS(), true);
        setSize(550, 450);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        // PAINEL SUPERIOR: DADOS DO CLIENTE E VEÍCULO
        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Informações Gerais"));
        infoPanel.add(new JLabel("Cliente:"));
        infoPanel.add(new JLabel(os.getCliente().getNome()));
        infoPanel.add(new JLabel("Veículo:"));
        infoPanel.add(new JLabel(os.getCarro().getModelo() + " | " + os.getCarro().getPlaca()));
        infoPanel.add(new JLabel("Status:"));
        infoPanel.add(new JLabel(os.getStatusAtual()));
        add(infoPanel, BorderLayout.NORTH);

        // PAINEL CENTRAL: TABELA DE PEÇAS E SERVIÇOS
        String[] columnNames = {"Item", "Qtd.", "Valor Unit.", "Subtotal"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tableView = new JTable(tableModel);
        add(new JScrollPane(tableView), BorderLayout.CENTER);

        // PAINEL INFERIOR: TOTAIS
        JPanel totalPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        totalPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        JLabel lblTotalItens = new JLabel();
        JLabel lblTotalGeral = new JLabel();
        lblTotalGeral.setFont(new Font("Arial", Font.BOLD, 14));
        totalPanel.add(lblTotalItens);
        totalPanel.add(lblTotalGeral);
        add(totalPanel, BorderLayout.SOUTH);

        // POPULAR TABELA E TOTAIS
        popularTabela(os, tableModel, lblTotalItens, lblTotalGeral);
    }

    private void popularTabela(OrdemDeServico os, DefaultTableModel model, JLabel lblItens, JLabel lblGeral) {
        DecimalFormat df = new DecimalFormat("R$ #,##0.00");
        double totalPecas = 0;

        for (PecaUtilizada peca : os.getListaDePecasUtilizadas()) {
            model.addRow(new Object[]{
                    peca.getProdutoOriginal().getNome(),
                    peca.getQuantidadeUtilizada(),
                    df.format(peca.getPrecoNoMomentoDoUso()),
                    df.format(peca.getSubtotal())
            });
            totalPecas += peca.getSubtotal();
        }

        // Adiciona a mão de obra como um item fixo na tabela
        model.addRow(new Object[]{"Mão de Obra", 1, df.format(150.00), df.format(150.00)});

        lblItens.setText("Total Peças + Serviços: " + df.format(totalPecas + 150.00));
        lblGeral.setText("VALOR TOTAL A PAGAR: " + df.format(os.calcularValorTotal()));
    }
}
