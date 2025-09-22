"use client"

import { BookPlus, Contact } from "lucide-react";
import { Button } from "./ui/button";
import { Agenda } from "@/core/agenda";
import { useState } from "react";
import SelecionarAgenda from "./SelecionarAgenda";
import AdicionarAgenda from "./AdicionarAgenda";

export default function Header () {

    const [isDialogSelecionarAgendaOpen, setIsDialogSelecionarAgendaOpen] = useState(false);
    const [isDialogAdicionarAgendaOpen, setIsDialogAdicionarAgendaOpen] = useState(false);

    const agendas: Agenda[] = [
        {id: 1, nome: "Agenda 01"},
        {id: 2, nome: "Agenda 02"},
        {id: 3, nome: "Agenda 03"},
        {id: 4, nome: "Agenda 04"},
        {id: 5, nome: "Agenda 05"},
    ]

    return(
        <>
            <header className="flex flex-row justify-between items-center px-5 py-3 shadow-md shadow-blue-300">
                <div className="text-2xl">
                    Agenda 01
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
            <SelecionarAgenda isOpen={isDialogSelecionarAgendaOpen} onOpenChange={setIsDialogSelecionarAgendaOpen} agendas={agendas}/>
            <AdicionarAgenda isOpen={isDialogAdicionarAgendaOpen} onOpenChange={setIsDialogAdicionarAgendaOpen}/>
        </>
    );
}