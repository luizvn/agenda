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

    useEffect(() => {
        const handleCarregarAgendas = async () => {
            try {
                const response = await getListaAgendas();
                setAgendas(response.data);
                
                // Verificar se há uma agenda salva no localStorage
                const agendaSalva = localStorage.getItem('agendaSelecionada');
                if (agendaSalva) {
                    const agenda = JSON.parse(agendaSalva);
                    setAgendaSelecionada(agenda);
                } else if (response.data.length > 0) {
                    // Seleciona a primeira agenda por padrão, se existir
                    setAgendaSelecionada(response.data[0]);
                    localStorage.setItem('agendaSelecionada', JSON.stringify(response.data[0]));
                }
            } catch(e: any) {
                console.error(e);
            }
        }

        handleCarregarAgendas();
    }, []);

    const handleAgendaSelecionada = (agenda: Agenda) => {
        setAgendaSelecionada(agenda);
        setIsDialogSelecionarAgendaOpen(false);
        
        // Salvar no localStorage para o Home component acessar
        localStorage.setItem('agendaSelecionada', JSON.stringify(agenda));
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
            />
            <AdicionarAgenda 
                isOpen={isDialogAdicionarAgendaOpen} 
                onOpenChange={setIsDialogAdicionarAgendaOpen}
                onAgendaCriada={() => window.location.reload()}
            />
        </>
    );
}