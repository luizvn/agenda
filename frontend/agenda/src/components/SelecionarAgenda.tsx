import { Search, Trash2, X } from "lucide-react";
import { AlertDialog, AlertDialogContent, AlertDialogHeader, AlertDialogTitle } from "./ui/alert-dialog";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { Agenda } from "@/core/agenda";

interface AdicionarContatoProps {
    isOpen: boolean;
    onOpenChange: (open: boolean) => void;
    agendas : Agenda[];
}

export default function SelecionarAgenda ({ isOpen, onOpenChange, agendas }:AdicionarContatoProps) {
    return(
        <AlertDialog open={isOpen} onOpenChange={onOpenChange}>
            <AlertDialogContent>
                <AlertDialogHeader className="">
                    <AlertDialogTitle className="text-cyan-700">Selecionar Agenda</AlertDialogTitle>
                    <Button className="absolute right-4 top-4" size={"icon"} variant={"outline"} onClick={() => {
                        onOpenChange(false);
                    }}><X /></Button>
                </AlertDialogHeader>
                <div className="grid grid-cols-1">
                    <div className="relative flex-grow w-full">
                        <Input type={"search"} placeholder="Buscar agenda" 
                            className="pl-4 pr-10 py-2 w-full rounded-lg h-10"/>
                        <Button variant={"ghost"} size={"icon"} 
                            className="absolute right-1 top-1/2 transform -translate-y-1/2 h-8 w-8 cursor-pointer
                                        text-white hover:text-white bg-cyan-700 hover:bg-cyan-500">
                            <Search/>
                        </Button>
                    </div>  

                    <div className="flex flex-col gap-2 mt-5 overflow-y-auto max-h-40">
                        <div className="bg-gray-400 h-0.5 w-full "></div>
                        {agendas.map((agenda) => (
                            <div key={agenda.id} >
                                <div className="flex flex-row justify-between items-center mb-1">
                                    <p>{agenda.nome}</p>
                                    <Button className="bg-cyan-700 hover:bg-cyan-500">Abrir</Button>
                                    <Button size={"icon"} variant={"ghost"}>
                                        <Trash2 className="text-red-500"/>
                                    </Button>
                                </div>
                                <div className="bg-gray-400 h-0.5 w-full"></div>
                            </div>
                        ))}
                    </div>

                </div>
            </AlertDialogContent>
        </AlertDialog>
    )
}