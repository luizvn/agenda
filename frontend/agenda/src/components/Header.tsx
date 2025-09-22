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
    const [agendas, setAgendas] = useState<Agenda[]>([])


    useEffect(() => {
        const handleCarregarAgendas = async () => {
            try{
                const response = await getListaAgendas();
                setAgendas(response.data);
            } catch(e:any){
                console.error(e)
            }
        }

        handleCarregarAgendas();

    }, []);


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