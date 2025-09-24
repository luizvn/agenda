"use client"

import { Search } from "lucide-react";
import { AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogFooter, AlertDialogHeader, AlertDialogTitle } from "./ui/alert-dialog";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { useEffect, useState } from "react";
import { Contato } from "@/core/contato";
import { getContatosDaAgenda, removeAllContatosFromAgenda } from "@/service/agendaService";

interface ApagarContatoPorNomeProps {
    isOpen: boolean;
    onOpenChange: (open: boolean) => void;
    agendaId: number | null;
}

export default function ApagarPorNome({ isOpen, onOpenChange, agendaId }: ApagarContatoPorNomeProps) {

    const [busca, setBusca] = useState("");
    const [contatos, setContatos] = useState<Contato[]>([]);

    
    useEffect(() => {
        const carregarContatos = async () => {
            if (!agendaId) return;

            try {
                const response = await getContatosDaAgenda(agendaId, undefined, busca || undefined);
                setContatos(response.data);
            } catch (error) {
                console.error("Erro ao carregar contatos:", error);
            }
        };

        carregarContatos();
    }, [agendaId, busca]);

    const handleExcluirPorNome = async () => {
        try{
            if(!agendaId) return;
            await removeAllContatosFromAgenda(agendaId, busca);
        } catch (error) {
            console.error("Erro ao carregar contatos:", error);
        }
    }

    return(
        <AlertDialog open={isOpen} onOpenChange={onOpenChange}>
            <AlertDialogContent>
                <AlertDialogHeader>
                    <AlertDialogTitle className="text-cyan-700">Apagar por nome</AlertDialogTitle>
                </AlertDialogHeader>

                <div className="flex flex-col gap-5">
                    <div className="relative flex-grow w-full">
                        <Input 
                            type={"search"} 
                            placeholder="Buscar agenda" 
                            className="pl-4 pr-10 py-2 w-full rounded-lg h-10"
                            value={busca}
                            onChange={(e) => setBusca(e.target.value)}
                        />
                        <Button variant={"ghost"} size={"icon"} 
                            className="absolute right-1 top-1/2 transform -translate-y-1/2 h-8 w-8 cursor-pointer
                                        text-white hover:text-white bg-cyan-700 hover:bg-cyan-500">
                            <Search/>
                        </Button>
                    </div>

                    <div>
                        {contatos.map((contato) => (
                            <div key={contato.id} className="flex flex-col gap-2 justify-center h-12 overflow-y-auto">
                                <div className="bg-gray-700 h-0.5"></div>
                                <span>{contato.nome}</span>
                            </div>
                        ))}
                    </div>
                </div>

                <AlertDialogFooter>
                    <AlertDialogCancel>Cancelar</AlertDialogCancel>
                    <AlertDialogAction
                        onClick={(handleExcluirPorNome)}
                    >
                        Apagar
                    </AlertDialogAction>
                </AlertDialogFooter>

            </AlertDialogContent>
        </AlertDialog>
    )
}