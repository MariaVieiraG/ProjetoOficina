/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.gui;

import javax.swing.JFrame;
import com.mycompany.oficina.controller.GerenteController;
import com.mycompany.oficina.gui.menus.ExtratoOsDialogo;
import com.mycompany.oficina.ordemservico.OrdemDeServico;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 *
 * @author DSG_2
 */
public class ManagerExtratoOsSwing extends JFrame{
    private final GerenteController controller;
    private final JTable tableView;
    private final DefaultTableModel tableModel;
     public ManagerExtratoOsSwing() {
        this.controller = new GerenteController();
        setTitle("Extratos de Ordens de Serviço");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnNames = {"Nº OS", "Cliente", "Veículo", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableView = new JTable(tableModel);

        JButton btnVerExtrato = new JButton("Ver Extrato");
        btnVerExtrato.addActionListener(e -> {
            OrdemDeServico sel = getSelectedOS();
            if (sel != null) {
                new ExtratoOsDialogo(this, sel).setVisible(true);
            } else {
                AlertsSwing.showAlert("Seleção Necessária", "Selecione uma Ordem de Serviço para ver o extrato.", JOptionPane.WARNING_MESSAGE);
            }
        });
          JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnVerExtrato);

        setLayout(new BorderLayout(10, 10));
        add(new JScrollPane(tableView), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        carregarDados();
    }
      private void carregarDados() {
        tableModel.setRowCount(0);
        List<OrdemDeServico> oss = controller.listarTodasOS();
        for (OrdemDeServico os : oss) {
            tableModel.addRow(new Object[]{
                    os.getNumeroOS(),
                    os.getCliente().getNome(),
                    os.getCarro().getModelo(),
                    os.getStatusAtual()
            });
        }
    }

    private OrdemDeServico getSelectedOS() {
        int selectedRow = tableView.getSelectedRow();
        if (selectedRow < 0) return null;
        String osId = (String) tableModel.getValueAt(selectedRow, 0);
        return controller.buscarOS(osId);
    }
}
