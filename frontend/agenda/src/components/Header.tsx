"use client"

import { BookPlus, Contact } from "lucide-react";
import { Button } from "./ui/button";
import { Agenda } from "@/core/agenda";
import { useEffect, useState } from "react";
import SelecionarAgenda from "./SelecionarAgenda";
import AdicionarAgenda from "./AdicionarAgenda";
import { getListaAgendas } from "@/service/agendaService";

export default function Header () {
    const [isDialogSelecionarAgendaOpen, setIsDialogSelecionarAgendaOpen] = useState(false);
    const [isDialogAdicionarAgendaOpen, setIsDialogAdicionarAgendaOpen] = useState(false);
    const [agendas, setAgendas] = useState<Agenda[]>([]);
    const [agendaSelecionada, setAgendaSelecionada] = useState<Agenda | null>(null);

    const carregarAgendas = async () => {
        try {
            const response = await getListaAgendas();
            setAgendas(response.data);
            
            const agendaSalva = localStorage.getItem('agendaSelecionada');
            if (agendaSalva) {
                const agenda = JSON.parse(agendaSalva);
                setAgendaSelecionada(agenda);
            } else if (response.data.length > 0) {
                setAgendaSelecionada(response.data[0]);
                localStorage.setItem('agendaSelecionada', JSON.stringify(response.data[0]));
            }
        } catch(e: any) {
            console.error(e);
        }
    };

    useEffect(() => {
        carregarAgendas();
    }, []);

    const handleAgendaSelecionada = (agenda: Agenda) => {
        setAgendaSelecionada(agenda);
        setIsDialogSelecionarAgendaOpen(false);
        localStorage.setItem('agendaSelecionada', JSON.stringify(agenda));
    };

    const handleAgendaExcluida = (agendaExcluida: Agenda) => {
        carregarAgendas();
        
        if (agendaSelecionada && agendaExcluida.id === agendaSelecionada.id) {
            setAgendaSelecionada(null);
            localStorage.removeItem('agendaSelecionada');
        }
    };

    return(
        <>
            <header className="flex flex-row justify-between items-center px-5 py-3 shadow-md shadow-blue-300">
                <div className="text-2xl">
                    {agendaSelecionada ? agendaSelecionada.nome : "Nenhuma agenda selecionada"}
                </div>
                <div className="flex flex-row gap-4">
                    <Button className="bg-cyan-700 cursor-pointer hover:bg-cyan-500"
                        onClick={(e) => {
                            e.preventDefault();
                            setIsDialogSelecionarAgendaOpen(true);
                        }}>
                        <Contact />
                        Selecionar
                    </Button>
                    <Button className="bg-cyan-700 cursor-pointer hover:bg-cyan-500"
                        onClick={(e) => {
                            e.preventDefault();
                            setIsDialogAdicionarAgendaOpen(true);
                        }}>
                        <BookPlus />
                        Adicionar
                    </Button>
                </div>
            </header>
            <SelecionarAgenda 
                isOpen={isDialogSelecionarAgendaOpen} 
                onOpenChange={setIsDialogSelecionarAgendaOpen} 
                agendas={agendas}
                onAgendaSelecionada={handleAgendaSelecionada}
                onAgendaExcluida={handleAgendaExcluida}
            />
            <AdicionarAgenda 
                isOpen={isDialogAdicionarAgendaOpen} 
                onOpenChange={setIsDialogAdicionarAgendaOpen}
                onAgendaCriada={carregarAgendas}
            />
        </>
    );
}