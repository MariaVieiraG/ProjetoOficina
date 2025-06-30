package com.mycompany.oficina.sistemaponto;

import com.google.gson.reflect.TypeToken;
import com.mycompany.oficina.entidades.Funcionario;
import com.mycompany.oficina.persistencia.PersistenciaJson;

import java.util.ArrayList;
import java.util.List;

/**
 * Gerencia o registro de ponto dos funcionários, com persistência de dados integrada.
 */
public class GerenciadorPonto {

    // Lista que armazena todos os registros de ponto carregados da persistência
    private final List<RegistroPonto> todosOsRegistros;

    // Objeto responsável pela persistência dos dados em JSON
    private final PersistenciaJson persistencia;

    /**
     * Construtor do GerenciadorPonto.
     * Inicializa a lista de registros carregando do arquivo JSON usando a persistência.
     *
     * @param persistencia A instância do gerenciador de persistência.
     */
    public GerenciadorPonto(PersistenciaJson persistencia) {
        this.persistencia = persistencia;
        // Carrega os registros de ponto do arquivo JSON na inicialização
        this.todosOsRegistros = this.persistencia.carregarLista("pontos", new TypeToken<ArrayList<RegistroPonto>>() {});
    }

    /**
     * Registra a entrada de um funcionário (batida de ponto de entrada).
     * Verifica se já existe um ponto em aberto para esse funcionário antes de registrar.
     * Salva imediatamente a lista atualizada na persistência.
     *
     * @param funcionario O funcionário que está a bater o ponto.
     * @return O novo RegistroPonto criado, ou null se já existir um ponto em aberto.
     */
    public RegistroPonto baterPontoEntrada(Funcionario funcionario) {
        // Verifica se já existe um ponto aberto para o funcionário
        boolean pontoEmAberto = false;
        for (RegistroPonto registro : todosOsRegistros) {
            if (registro.getFuncionario().equals(funcionario) && registro.isPontoAberto()) {
                pontoEmAberto = true;
                break;
            }
        }

        if (pontoEmAberto) {
            // Caso já exista, exibe erro e não registra novo ponto
            System.out.println("ERRO: " + funcionario.getNome() + " já possui um registro de ponto em aberto.");
            return null;
        }

        // Cria um novo registro de ponto com horário de entrada atual
        RegistroPonto novoRegistro = new RegistroPonto(funcionario);
        this.todosOsRegistros.add(novoRegistro);
        System.out.println("SUCESSO: Ponto de entrada registado para " + funcionario.getNome() + ".");

        // Salva a lista atualizada no arquivo JSON imediatamente
        persistencia.salvarLista("pontos", this.todosOsRegistros);

        return novoRegistro;
    }

    /**
     * Registra a saída de um funcionário (batida de ponto de saída).
     * Procura o último registro aberto para esse funcionário e fecha ele.
     * Salva a lista atualizada na persistência.
     *
     * @param funcionario O funcionário que está a bater o ponto.
     * @return O RegistroPonto atualizado, ou null se não houver ponto em aberto.
     */
    public RegistroPonto baterPontoSaida(Funcionario funcionario) {
        // Busca o registro de ponto aberto para o funcionário
        RegistroPonto registroAberto = null;
        for (RegistroPonto registro : todosOsRegistros) {
            if (registro.getFuncionario().equals(funcionario) && registro.isPontoAberto()) {
                registroAberto = registro;
                break;
            }
        }

        if (registroAberto != null) {
            // Fecha o ponto definindo a data/hora de saída
            registroAberto.setDataHoraSaida();

            // Salva a lista atualizada no arquivo JSON imediatamente
            persistencia.salvarLista("pontos", this.todosOsRegistros);

            return registroAberto;
        } else {
            // Nenhum ponto aberto encontrado para fechar
            return null;
        }
    }

    /**
     * Retorna todos os registros de ponto de um funcionário específico.
     * 
     * @param funcionario O funcionário cujos registros devem ser listados.
     * @return Uma lista contendo todos os registros de ponto desse funcionário.
     */
    public List<RegistroPonto> getRegistrosPorFuncionario(Funcionario funcionario) {
        List<RegistroPonto> registrosEncontrados = new ArrayList<>();
        // Percorre todos os registros filtrando pelo funcionário informado
        for (RegistroPonto registro : this.todosOsRegistros) {
            if (registro.getFuncionario().equals(funcionario)) {
                registrosEncontrados.add(registro);
            }
        }
        return registrosEncontrados;
    }

    /**
     * Retorna uma cópia de segurança com todos os registros de ponto.
     * 
     * @return Uma nova lista contendo todos os registros armazenados.
     */
    public List<RegistroPonto> getTodosOsRegistros() {
        // Retorna uma cópia para evitar alterações diretas na lista original
        return new ArrayList<>(this.todosOsRegistros);
    }
}
