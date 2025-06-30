package com.mycompany.oficina.gui;

import com.mycompany.oficina.controller.GerenteController;
import com.mycompany.oficina.financeiro.RegistroFinanceiro;
import com.mycompany.oficina.gui.RelatorioFinanceiroFrame;

import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.YearMonth;

/**
 * Interface gráfica para o módulo financeiro do sistema.
 * Permite emitir relatórios financeiros e executar ações como pagamento de salários.
 */
public class ManagerFinanceiroSwing extends JFrame {

    private final GerenteController controller; // Controlador que gerencia a lógica financeira

    /**
     * Construtor da janela financeira, recebendo o controlador para executar operações.
     */
    public ManagerFinanceiroSwing(GerenteController controller) {
        this.controller = controller;

        setTitle("Módulo Financeiro");                 // Título da janela
        setSize(400, 350);                              // Dimensão fixa da janela
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fecha apenas esta janela
        setLocationRelativeTo(null);                    // Centraliza a janela na tela

        // Painel principal com layout vertical
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Margem interna

        // Label de título com fonte e alinhamento
        JLabel titleLabel = new JLabel("Relatórios e Ações Financeiras");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Botões para as funcionalidades financeiras
        JButton btnBalancoDiario = new JButton("Emitir Balanço do Dia");
        JButton btnBalancoMensal = new JButton("Emitir Balanço do Mês Atual");
        JButton btnRelatorioDespesas = new JButton("Relatório de Despesas do Mês");
        JButton btnPagarSalarios = new JButton("Pagar Salários");

        // Formata os botões para largura máxima e alinhamento centralizado
        formatButton(btnBalancoDiario);
        formatButton(btnBalancoMensal);
        formatButton(btnRelatorioDespesas);
        formatButton(btnPagarSalarios);

        // Ação do botão para emitir balanço diário
        btnBalancoDiario.addActionListener(e -> {
            LocalDate hoje = LocalDate.now(); // Data atual
            // Obtém registros financeiros do dia atual (início e fim iguais)
            List<RegistroFinanceiro> registros = controller.getRegistrosFinanceiros(hoje, hoje);
            // Abre diálogo para mostrar os registros
            new RelatorioFinanceiroFrame(this, "Balanço do Dia", registros).setVisible(true);
        });

        // Ação do botão para emitir balanço mensal
        btnBalancoMensal.addActionListener(e -> {
            YearMonth mes = YearMonth.now(); // Mês atual
            // Obtém registros financeiros do primeiro ao último dia do mês
            List<RegistroFinanceiro> registros = controller.getRegistrosFinanceiros(mes.atDay(1), mes.atEndOfMonth());
            // Abre diálogo para mostrar os registros
            new RelatorioFinanceiroFrame(this, "Balanço Mensal", registros).setVisible(true);
        });

        // Ação do botão para relatório de despesas do mês
        btnRelatorioDespesas.addActionListener(e -> {
            YearMonth mes = YearMonth.now();
            // Obtém registros financeiros do mês atual (a filtragem de despesas deve ser feita no controller)
            List<RegistroFinanceiro> despesas = controller.getRegistrosFinanceiros(mes.atDay(1), mes.atEndOfMonth());
            // Reutiliza diálogo para mostrar esses registros (espera-se que sejam só despesas)
            new RelatorioFinanceiroFrame(this, "Relatório de Despesas do Mês", despesas).setVisible(true);
        });

        // Ação do botão para pagar salários
        btnPagarSalarios.addActionListener(e -> {
            controller.pagarSalarios(); // Executa pagamento via controller
            // Mostra mensagem de sucesso após pagamento registrado
            AlertsSwing.showAlert("Sucesso", "Folha de pagamento registrada com sucesso no sistema financeiro.", JOptionPane.INFORMATION_MESSAGE);
        });

        // Adiciona componentes ao painel com espaçamentos
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(btnBalancoDiario);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnBalancoMensal);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnRelatorioDespesas);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnPagarSalarios);

        // Adiciona painel principal à janela
        add(panel);
    }

    /**
     * Ajusta a aparência do botão para ocupar toda a largura disponível e ficar centralizado.
     * @param button Botão a ser formatado
     */
    private void formatButton(JButton button) {
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    /**
     * Método auxiliar para capturar saída do console durante a execução de uma ação.
     * Útil para testes ou logs gerados em System.out.
     * @param action Runnable que executa uma ação que gera saída no console
     * @return String com todo o conteúdo impresso no console durante a execução
     */
    private String captureConsoleOutput(Runnable action) {
        PrintStream originalOut = System.out;              // Salva saída original
        ByteArrayOutputStream baos = new ByteArrayOutputStream(); // Buffer para capturar saída
        try (PrintStream newOut = new PrintStream(baos)) {
            System.setOut(newOut);                          // Redireciona saída para buffer
            action.run();                                   // Executa ação que imprime no console
        } finally {
            System.out.flush();                             // Garante saída limpa
            System.setOut(originalOut);                     // Restaura saída original
        }
        return baos.toString();                             // Retorna texto capturado
    }
}
