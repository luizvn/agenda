package com.agenda.backend.entity;

public class AgendaFactory {
    
    private static final AgendaFactory instance = new AgendaFactory();

    public static final String AGENDALIST = "LIST";
    public static final String AGENDAMAP = "MAP";

    private AgendaFactory() {}

    public static AgendaFactory getInstance() {
        return instance;
    }

    public Agenda createAgenda(String tipo){
        switch (tipo) {
            case AGENDALIST:
                return new AgendaList();
            case AGENDAMAP:
                return new AgendaMap();
            default:
                throw new IllegalArgumentException("Tipo de agenda desconhecido.");
        }
    }
}
