package com.mycompany.oficina.agendamento;

import com.google.gson.reflect.TypeToken;
import com.mycompany.oficina.persistencia.PersistenciaJson;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Gerencia a coleção completa de agendamentos da oficina, agora com
 * persistência de dados integrada.
 */
public final class AgendaOficina {

    private final int horaInicioManha = 8;
    private final int horaFimManha = 12;
    private final int horaInicioTarde = 14;
    private final int horaFimTarde = 18;
    private final int totalDeHorarios;

    // A estrutura de dados principal
    private Map<LocalDate, Agendamento[]> agenda;

    // --- ATRIBUTOS PARA PERSISTÊNCIA ---
    private final PersistenciaJson persistencia;
    private final String CHAVE_ARQUIVO = "agenda";

    /**
     * Construtor que agora recebe a instância de persistência e carrega os dados
     * do arquivo agenda.json na inicialização.
     */
    public AgendaOficina(PersistenciaJson persistencia) {
        this.persistencia = persistencia;

        int slotsManha = horaFimManha - horaInicioManha;
        int slotsTarde = horaFimTarde - horaInicioTarde;
        this.totalDeHorarios = slotsManha + slotsTarde;

        // Carrega o mapa do arquivo JSON. Se não existir ou estiver vazio, cria um novo.
        this.agenda = this.persistencia.carregarMapa(CHAVE_ARQUIVO, new TypeToken<HashMap<LocalDate, Agendamento[]>>() {});
        if (this.agenda == null) {
            this.agenda = new HashMap<>();
        }
    }

    /**
     * Método privado que centraliza a lógica de salvamento.
     */
    private void salvarAgenda() {
        this.persistencia.salvarMapa(CHAVE_ARQUIVO, this.agenda);
    }

    /**
     * Agenda um novo serviço e salva o estado atual no JSON.
     */
    public boolean agendar(Agendamento agendamento) {
        LocalDateTime dataHora = agendamento.getDataHora();
        int indice = converterHoraParaIndice(dataHora);

        if (indice == -1) {
            return false;
        }

        LocalDate data = dataHora.toLocalDate();
        // getOrDefault garante que um novo array seja criado se for o primeiro agendamento do dia
        Agendamento[] horariosDoDia = agenda.getOrDefault(data, new Agendamento[this.totalDeHorarios]);

        if (horariosDoDia[indice] != null) {
            return false;
        }

        horariosDoDia[indice] = agendamento;
        agenda.put(data, horariosDoDia); // Garante que o mapa seja atualizado

        // --- SALVAMENTO ADICIONADO ---
        salvarAgenda();
        System.out.println("SUCESSO: Agendamento realizado e salvo para " + data);
        return true;
    }

    /**
     * Cancela um agendamento e salva a alteração no JSON.
     */
    public boolean cancelarAgendamento(Agendamento agendamento) {
        if (agendamento == null) {
            return false;
        }

        LocalDateTime dataHora = agendamento.getDataHora();
        LocalDate data = dataHora.toLocalDate();
        Agendamento[] horariosDoDia = agenda.get(data);

        if (horariosDoDia == null) {
            return false;
        }

        int indice = converterHoraParaIndice(dataHora);
        if (indice == -1 || horariosDoDia[indice] == null) {
            return false;
        }

        if (horariosDoDia[indice].equals(agendamento)) {
            horariosDoDia[indice] = null;
            // --- SALVAMENTO ADICIONADO ---
            salvarAgenda();
            return true;
        }

        return false;
    }
    public boolean removerAgendamento(Agendamento agendamento) {
        if (agendamento == null) {
            return false;
        }

        LocalDateTime dataHora = agendamento.getDataHora();
        LocalDate data = dataHora.toLocalDate();
        Agendamento[] horariosDoDia = agenda.get(data);

        if (horariosDoDia == null) {
            return false; // Não há agendamentos para este dia.
        }

        int indice = converterHoraParaIndice(dataHora);
        if (indice == -1 || horariosDoDia[indice] == null) {
            return false; // Horário inválido ou já está vago.
        }

        // Verifica se o agendamento na posição é exatamente o que queremos remover
        if (horariosDoDia[indice].equals(agendamento)) {
            horariosDoDia[indice] = null; // Remove o agendamento
            salvarAgenda(); // Salva a alteração no arquivo JSON
            return true;
        }

        return false;
    }

    // ... O restante da sua classe (getHorariosDoDia, listarTodos, etc.) permanece o mesmo ...
    public Agendamento[] getHorariosDoDia(LocalDate data) {
        Agendamento[] horariosOriginais = agenda.get(data);
        if (horariosOriginais == null) {
            return new Agendamento[this.totalDeHorarios];
        }
        return Arrays.copyOf(horariosOriginais, horariosOriginais.length);
    }

    public Set<LocalDate> getDatasAgendadas() {
        return Collections.unmodifiableSet(agenda.keySet());
    }

    private int converterHoraParaIndice(LocalDateTime dataHora) {
        int hora = dataHora.getHour();
        int slotsManha = horaFimManha - horaInicioManha;

        if (hora >= horaInicioManha && hora < horaFimManha) {
            return hora - horaInicioManha;
        }

        if (hora >= horaInicioTarde && hora < horaFimTarde) {
            return (hora - horaInicioTarde) + slotsManha;
        }

        return -1;
    }

    public List<Agendamento> listarTodosAgendamentos() {
        List<Agendamento> todos = new ArrayList<>();
        for (Agendamento[] horariosDoDia : agenda.values()) {
            for (Agendamento agendamento : horariosDoDia) {
                if (agendamento != null) {
                    todos.add(agendamento);
                }
            }
        }
        return todos;
    }
}