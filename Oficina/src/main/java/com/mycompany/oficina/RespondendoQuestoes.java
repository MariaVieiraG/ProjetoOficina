package com.mycompany.oficina;

import com.mycompany.oficina.agendamento.AgendaOficina;
import com.mycompany.oficina.agendamento.Agendamento;
import com.mycompany.oficina.agendamento.TipoServico;
import com.mycompany.oficina.application.OficinaAplicattion;
import com.mycompany.oficina.comparators.ComparatorAgendamentoPorData;
import com.mycompany.oficina.comparators.ComparatorGenerico;
import com.mycompany.oficina.controlador.GerenciadorCarros;
import com.mycompany.oficina.controlador.GerenciadorCliente;
import com.mycompany.oficina.controlador.GerenciadorFuncionario;
import com.mycompany.oficina.controller.MecanicoController;
import com.mycompany.oficina.entidades.Carro;
import com.mycompany.oficina.entidades.Cliente;
import com.mycompany.oficina.entidades.Funcionario;
import com.mycompany.oficina.loja.Estoque;
import com.mycompany.oficina.loja.Produto;
import com.mycompany.oficina.ordemservico.GerenciadorOrdemDeServico;
import com.mycompany.oficina.ordemservico.OrdemDeServico;
import com.mycompany.oficina.persistencia.PersistenciaJson;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class RespondendoQuestoes {
    //Questao 11
    private static int contadorClientePrivado = 0;
    protected static int contadorClientesProtected = 0;
    //Questao 12
    private static int contadorCarro = 0;

    // Questao 17
    public static Cliente findCliente(List<Cliente> lista, Cliente chave, Comparator<Cliente> comparador) {
        Iterator<Cliente> iterator = lista.iterator();
        while (iterator.hasNext()) {
            Cliente clienteDaLista = iterator.next();
            if (comparador.compare(clienteDaLista, chave) == 0) {
                return clienteDaLista;
            }
        }
        return null;
    }

    public static void main(String[] args) {

        System.out.println("### INICIANDO EXECUÇÃO DAS QUESTÕES ###\n");

        OficinaAplicattion app = OficinaAplicattion.getInstance();
        PersistenciaJson persistencia = new PersistenciaJson();
        GerenciadorCliente gerenciadorCliente = app.getGerenciadorCliente();
        GerenciadorCarros gerenciadorCarros = app.getGerenciadorCarros();
        GerenciadorFuncionario gerenciadorFuncionario = app.getGerenciadorFuncionario();
        GerenciadorOrdemDeServico gerenciadorOS = app.getGerenciadorOS();
        Estoque estoque = app.getEstoque();
        MecanicoController mecanicoController = new MecanicoController();
        AgendaOficina agenda = app.getAgenda();

        //Questao 5 - O sistema deverá armazenar de forma estática (Vetor com tamanho fixo) as informações dos 3 elevadores da oficina.
        System.out.println("--- Questão 5 ---");
        System.out.println("Lógica implementada na classe Elevador.java.\n");


        //Questao - 6 Deve ser possível cadastrar os colaboradores no sistema, alterar ou editar seus atributos.
        System.out.println("--- Questão 6 ---");
        Funcionario funcMarcos =  new Funcionario("1234","atendente", "Marcos", "12345678910", "38994556856", "N/A", "blablabla@gmail.com");
        gerenciadorFuncionario.criarFuncionario(funcMarcos);
        System.out.println("Colaborador criado: " + funcMarcos.getNome());
        boolean remocao6 = gerenciadorFuncionario.removerItemPorIdentificador("12345678910");
        System.out.println("Remoção do colaborador 'Marcos' foi: " + remocao6 + "\n");


        //Questao 7 - Cadastrar, alterar ou excluir clientes;
        System.out.println("--- Questão 7 ---");
        Cliente novoCLiente = new Cliente("Maria", "77777777777", "3865456", "N/A", "blabala@gmail.com");
        gerenciadorCliente.addCliente(novoCLiente);
        System.out.println("Cliente 'Maria' criado com CPF: " + novoCLiente.getCpf());
        boolean edicaoCliente = gerenciadorCliente.editarCliente("Maria Silva", "77777777777", "99999999", "Rua Nova", "maria.silva@gmail.com");
        System.out.println("Edição da cliente 'Maria' foi: " + edicaoCliente);
        boolean remocaoCliente = gerenciadorCliente.removerItemPorIdentificador("77777777777");
        System.out.println("Remoção da cliente 'Maria' foi: " + remocaoCliente + "\n");


        //Questão 8 - Verificar e imprimir dados das ordens de serviço de cada cliente;
        System.out.println("--- Questão 8 ---");
        Cliente clienteOs = new Cliente("Cleiton", "88888888889", "N/A", "N/A", "N/A");
        gerenciadorCliente.addCliente(clienteOs);
        Carro carroOs = gerenciadorCarros.cadastrarCarro(clienteOs, "VW", "Nivus", "NVS-1234", "CHASSISOS1");
        gerenciadorOS.abrirOS(clienteOs, carroOs, funcMarcos, "Revisão geral");

        for (Cliente cliente : gerenciadorCliente.listarTodos()) {
            System.out.println("Verificando OS para o cliente: " + cliente.getNome());
            boolean encontrouOS = false;
            for (OrdemDeServico servico : gerenciadorOS.listarTodos()) {
                if (servico.getCliente().getIdentificador().equals(cliente.getIdentificador())) {
                    System.out.println("  -> Encontrou OS: " + servico.getNumeroOS());
                    encontrouOS = true;
                }
            }
            if (!encontrouOS) {
                System.out.println("  -> Nenhuma OS encontrada.");
            }
        }
        System.out.println();


        //Questsao 9- As ordens de serviço, ações do estoque e os clientes devem ser salvos de forma dinâmica no sistema.
        System.out.println("--- Questão 9 ---");
        System.out.println("Listas dinâmicas carregadas:");
        System.out.println("  -> Ordens de Serviço: " + gerenciadorOS.listarTodos().size() + " itens.");
        System.out.println("  -> Clientes: " + gerenciadorCliente.listarTodos().size() + " itens.");
        System.out.println("  -> Estoque: " + estoque.listarProdutos().size() + " itens.\n");


        //Questao 10 - Cada serviço e venda efetuados vão gerar um extrato que deverá ser impresso e salvo junto
        System.out.println("--- Questão 10 ---");
        String extrato = gerenciadorOS.listarTodos().get(0).gerarExtrato();
        System.out.println("Extrato da primeira Ordem de Serviço gerado e salvo:");
        System.out.println(extrato);


        //Questao 11 - Criar duas variáveis de classe (static) que irão armazenar quantas
        // instâncias foram criadas dos tipos Veículo dentro da classe Sistema ou
        // Cliente usando duas soluções diferentes: Private e Protected

        System.out.println("--- Questão 11 ---");
        System.out.println("Valores Iniciais: Privado=" + contadorClientePrivado + ", Protegido=" + contadorClientesProtected);
        Cliente clienteContador = new Cliente("Mario", "51122233345", "N/A", "N/A", "N/A");
        gerenciadorCliente.addCliente(clienteContador);
        contadorClientePrivado++;
        contadorClientesProtected++;
        System.out.println("Valores Finais: Privado=" + contadorClientePrivado + ", Protegido=" + contadorClientesProtected + "\n");
        /*
                Nessa situação não fica muito clara, mas o private é um uma estrategia de controle mais restritiva,
                pois qualquer classe precisaria de um getter para acessar seus atributos e metodos privados. Já a
                protected oferece as classes que a herdam, um acesso livre.

         */

        //Questao12 - Criar um metodo de classe para classe Sistema ou Cliente que deverá retornar quantas instâncias
        // foram criadas do tipo Veículo;
        System.out.println("--- Questão 12 ---");
        System.out.println("Contagem inicial de carros: " + contadorCarro);
        gerenciadorCarros.cadastrarCarro(clienteOs, "Fiat", "Uno", "UNO-5678", "CHASSISUNO1");
        contadorCarro++;
        System.out.println("Contagem final de carros: " + contadorCarro + "\n");


        //Questao13 e 16 - Implementar a interface Comparator para as classes Agendamento e Cliente
        // e fazer comparações por diferentes atributos
        // - Apresentar no main testes do comparator implementado.
        System.out.println("--- Questão 13 e 16 ---");
        List<Cliente> listaParaOrdenar = new ArrayList<>(gerenciadorCliente.listarTodos());
        System.out.println("\nClientes antes da ordenação por nome:");
        listaParaOrdenar.forEach(c -> System.out.println(" -> " + c.getNome()));

        ComparatorGenerico<Cliente> porNome = new ComparatorGenerico<>(cliente -> cliente.getNome());
        listaParaOrdenar.sort(porNome);
        System.out.println("\nClientes DEPOIS da ordenação por nome:");
        listaParaOrdenar.forEach(c -> System.out.println(" -> " + c.getNome()));

        ComparatorGenerico<Cliente> porCpf = new ComparatorGenerico<>(cliente -> cliente.getCpf());
        listaParaOrdenar.sort(porCpf);
        System.out.println("\nClientes DEPOIS da ordenação por CPF:");
        listaParaOrdenar.forEach(c -> System.out.println(" -> " + c.getCpf() + " | " + c.getNome()));

        System.out.println();

        //Agendamento
        Cliente clienteAna = new Cliente("Ana Agendamento", "101010", "N/A", "N/A", "N/A");
        Carro carroAna = new Carro(clienteAna.getNome(), clienteAna.getCpf(), "Renault", "Kwid", "KWD-1010", "CHASSISKWID");
        agenda.agendar(new Agendamento(clienteAna, carroAna, funcMarcos, TipoServico.ALINHAMENTO, null, LocalDateTime.now().plusHours(2)));

        List<Agendamento> agendamentos = agenda.listarTodosAgendamentos();
        System.out.println("Agendamentos antes da ordenação por nome de cliente:");
        agendamentos.forEach(ag -> System.out.println(" -> " + ag.getCliente().getNome()));

        ComparatorGenerico<Agendamento> comparadorPorNomeCliente = new ComparatorGenerico<>(ag -> ag.getCliente().getNome());
        agendamentos.sort(comparadorPorNomeCliente);
        System.out.println("\nAgendamentos DEPOIS da ordenação por nome de cliente:");
        agendamentos.forEach(ag -> System.out.println(" -> " + ag.getCliente().getNome()));
        System.out.println();


        //Questao15
        System.out.println("--- Questão 15 ---");
        System.out.println("Percorrendo a lista de clientes com Iterator:");
        Iterator<Cliente> iteratorClientes = listaParaOrdenar.iterator();
        while (iteratorClientes.hasNext()) {
            Cliente clienteAgora = iteratorClientes.next();
            System.out.println("  -> Iterator encontrou: " + clienteAgora.getNome());
        }
        System.out.println();


        //Questao 17- Criar um metodo find para clientes utilizando o interator e comparator.
        // Apresentar testes do metodo implementado.
        //Fazer chamadas ao binarySearch() da classe collections e comparar com o find IMPLEMENTADO
        System.out.println("--- Questão 17 ---");
        Cliente clienteBusca = listaParaOrdenar.get(0); // Pega o primeiro cliente da lista ordenada por CPF
        System.out.println("Buscando pelo cliente: " + clienteBusca.getNome());

        // Teste com findCliente
        Cliente resultadoFind = findCliente(listaParaOrdenar, clienteBusca, porNome);
        System.out.println("Resultado do nosso find(): " + (resultadoFind != null ? resultadoFind.getNome() : "Não encontrado"));

        // Teste com binarySearch
        // A lista já está ordenada por CPF, vamos reordenar por nome para o binarySearch funcionar
        Collections.sort(listaParaOrdenar, porNome);
        int indice = Collections.binarySearch(listaParaOrdenar, clienteBusca, porNome);
        System.out.println("Resultado do binarySearch(): " + (indice >= 0 ? listaParaOrdenar.get(indice).getNome() + " encontrado no índice " + indice : "Não encontrado"));
        System.out.println();
        /*
            -find() é uma busca linear, compara nome por chave até encontrar o que precisa
            -binarySearch() é uma busca que necessita que a lista esteja ordenada para que aconteça.
            ela vai ao meio da lista ordenada e a partir disso consegue descartar metade da lista
            na pesquisa.
         */

        //Questao - 18 Apresentar o funcionamento básico para o atendimento de 10 clientes da oficina,
        // desde o cadastro do cliente até a criação das ordens de serviço para cada atendimento,
        // com as baixas no estoque e denotação correta dos serciços realizados, até finalizar com a
        // emissão de nota fiscal após a coclusão das vendas.
        //Mostrar na Interface.
    }
}