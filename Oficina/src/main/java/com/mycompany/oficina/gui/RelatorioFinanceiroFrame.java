package com.mycompany.oficina.gui;
import com.mycompany.oficina.financeiro.RegistroFinanceiro;
import com.mycompany.oficina.financeiro.TipoRegistro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RelatorioFinanceiroFrame extends JFrame {
    private final DefaultTableModel tableModel;
    private final JTable tableView;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DecimalFormat df = new DecimalFormat("R$ #,##0.00");

    public RelatorioFinanceiroFrame(Frame owner, String titulo, List<RegistroFinanceiro> registros) {
        super(titulo);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        setResizable(true);

        String[] columnNames = {"Data", "Tipo", "Valor", "Descrição"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableView = new JTable(tableModel);

        tableView.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        tableView.setFillsViewportHeight(true);

        // MODIFICAÇÃO PRINCIPAL: Configurar as larguras manualmente
        // Em vez de usar o método `ajustarLarguraColunas`.
        TableColumnModel columnModel = tableView.getColumnModel();

        // Coluna "Data" (Índice 0)
        columnModel.getColumn(0).setMinWidth(90);
        columnModel.getColumn(0).setPreferredWidth(90);

        // Coluna "Tipo" (Índice 1)
        columnModel.getColumn(1).setMinWidth(150); // Aumentar para caber "DESPESA_COMISSAO", etc.
        columnModel.getColumn(1).setPreferredWidth(150);

        // Coluna "Valor" (Índice 2)
        columnModel.getColumn(2).setMinWidth(120);
        columnModel.getColumn(2).setPreferredWidth(120);

        // A coluna "Descrição" (Índice 3) não precisa ser configurada,
        // pois ela se esticará automaticamente para preencher o resto.

        add(new JScrollPane(tableView), BorderLayout.CENTER);

        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        JLabel lblTotal = new JLabel();
        summaryPanel.add(lblTotal);
        add(summaryPanel, BorderLayout.SOUTH);

        popularTabela(registros, lblTotal);

        // REMOÇÃO: A chamada para `ajustarLarguraColunas(tableView);` foi removida.
    }

    private void popularTabela(List<RegistroFinanceiro> registros, JLabel lblTotal) {
        tableModel.setRowCount(0);
        double totalReceitas = 0;
        double totalDespesas = 0;

        for (RegistroFinanceiro r : registros) {
            boolean isReceita = r.getTipo() == TipoRegistro.RECEITA_SERVICO || r.getTipo() == TipoRegistro.RECEITA_CANCELAMENTO;

            if (isReceita) {
                totalReceitas += r.getValor();
            } else {
                totalDespesas += r.getValor();
            }

            tableModel.addRow(new Object[]{
                    r.getData().format(dtf),
                    r.getTipo().name(),
                    df.format(r.getValor()),
                    r.getDescricao()
            });
        }

        double balanco = totalReceitas - totalDespesas;
        lblTotal.setText(String.format("Receitas: %s | Despesas: %s | Balanço: %s",
                df.format(totalReceitas), df.format(totalDespesas), df.format(balanco)));
    }

    // NOVO MÉTODO: Para calcular e definir a largura ideal de cada coluna
   /* private void ajustarLarguraColunas(JTable table) {
        // Itera sobre cada coluna da tabela
        for (int column = 0; column < table.getColumnCount(); column++) {
            TableColumn tableColumn = table.getColumnModel().getColumn(column);
            int preferredWidth = tableColumn.getMinWidth(); // Começa com a largura mínima
            int maxWidth = tableColumn.getMaxWidth();

            // Calcula a largura do cabeçalho da coluna
            TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
            Component headerComp = headerRenderer.getTableCellRendererComponent(table, tableColumn.getHeaderValue(), false, false, 0, column);
            int headerWidth = headerComp.getPreferredSize().width;
            preferredWidth = Math.max(preferredWidth, headerWidth);

            // Itera sobre cada linha para encontrar a largura máxima da célula
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
                Component c = table.prepareRenderer(cellRenderer, row, column);
                int width = c.getPreferredSize().width + table.getIntercellSpacing().width;
                preferredWidth = Math.max(preferredWidth, width);
            }

            // Adiciona uma pequena margem (padding) para não ficar colado
            // e define a largura da coluna.
            tableColumn.setPreferredWidth(preferredWidth + 15);
        }
    }*/

}
