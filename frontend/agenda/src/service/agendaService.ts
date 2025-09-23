import { Contato } from "@/core/contato";
import api from "./api";
import { Agenda } from "@/core/agenda";


// |=======| TIPAGENS ESPECÍFICAS |=======|
export interface CreateAgendaRequest {
  nome: string;
  tipo: string;
}

export interface ContatoRequest {
  nome: string;
  telefone: string;
}

export interface AgendaTypedResponse extends Agenda {
  contatos: Contato[] | { [key: string]: Contato };
}

// |=======| POST PARA CRIAR AGENDA |=======|
export async function criarAgenda(agenda: CreateAgendaRequest) {
  return api.post<AgendaTypedResponse>('/agendas', agenda);
}

// |=======| GET DA LISTA DE AGENDAS |=======|
export async function getListaAgendas() {
  return api.get<Agenda[]>('/agendas');
}

// |=======| GET DE UMA AGENDA POR ID |=======|
export async function getAgenda(id: number) {
  return api.get<Agenda>(`/agendas/${id}`);
}

// |=======| DELETE PARA EXCLUIR AGENDA |=======|
export async function excluirAgenda(id: number) {
  return api.delete(`/agendas/${id}`);
}

// |=======| POST PARA ADICIONAR CONTATO À AGENDA |=======|
export async function adicionarContato(agendaId: number, contato: ContatoRequest) {
  return api.post<Contato>(`/agendas/${agendaId}/contatos`, contato);
}

// |=======| DELETE PARA REMOVER CONTATO DA AGENDA |=======|
export async function removerContato(agendaId: number, contatoId: number) {
  return api.delete(`/agendas/${agendaId}/contatos/${contatoId}`);
}

// |=======| GET DA LISTA DE CONTATOS DE UMA AGENDA |=======|
export async function getContatosDaAgenda(agendaId: number, telefone?: string) {
  const params = telefone ? { telefone } : {};
  return api.get<Contato[]>(`/agendas/${agendaId}/contatos`, { params });
}

// |=======| POST PARA CONVERTER TIPO DA AGENDA |=======|
export async function converterTipoAgenda(id: number) {
  return api.post<AgendaTypedResponse>(`/agendas/${id}/convert`);
}

// |=======| GET DE AGENDA COM CONTATOS (TYPED) |=======|
export async function getAgendaComContatos(id: number) {
  return api.get<AgendaTypedResponse>(`/agendas/${id}`);
}

// |=======| GET PARA BUSCAR AS AGENDAS |=======|
export async function getAgendas(nome?: string){
  const params = nome ? { nome } : {};
  return api.get<Agenda[]>('/agendas', { params })
}