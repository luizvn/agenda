import { X } from "lucide-react";
import { AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogFooter, AlertDialogHeader, AlertDialogTitle } from "./ui/alert-dialog";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { RadioGroup, RadioGroupItem } from "./ui/radio-group";
import { useState } from "react";
import { criarAgenda } from "@/service/agendaService";

interface AdicionarAgendaProps {
    isOpen: boolean;
    onOpenChange: (open: boolean) => void;
    onAgendaCriada?: () => void;
}

export default function AdicionarAgenda({ isOpen, onOpenChange, onAgendaCriada }: AdicionarAgendaProps) {
    const [nome, setNome] = useState("");
    const [tipo, setTipo] = useState<"LIST" | "MAP">("LIST"); // ← Corrigido para LIST e MAP

    const handleSubmit = async () => {
        if (!nome.trim()) {
            alert("Por favor, insira um nome para a agenda");
            return;
        }

        try {
            await criarAgenda({ 
                nome: nome.trim(), 
                tipo: tipo // ← Agora envia LIST ou MAP
            });
            
            // Limpar formulário e fechar modal
            setNome("");
            setTipo("LIST");
            onOpenChange(false);
            
            if (onAgendaCriada) {
                onAgendaCriada();
            } else {
                window.location.reload();
    }
            
        } catch (error: any) {
            console.error("Erro ao criar agenda:", error);
            alert(`Erro ao criar agenda: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleClose = () => {
        setNome("");
        setTipo("LIST");
        onOpenChange(false);
    };

    return (
        <AlertDialog open={isOpen} onOpenChange={onOpenChange}>
            <AlertDialogContent>
                <AlertDialogHeader>
                    <AlertDialogTitle>Adicionar Agenda</AlertDialogTitle>
                    <Button 
                        className="absolute right-4 top-4" 
                        size={"icon"} 
                        variant={"outline"} 
                        onClick={handleClose}
                    >
                        <X />
                    </Button>
                </AlertDialogHeader>

                <div className="grid grid-cols-1 gap-2.5">
                    <div className="flex flex-col gap-0.5">
                        <span>Nome</span>
                        <Input 
                            placeholder="Insira o nome"
                            value={nome}
                            onChange={(e) => setNome(e.target.value)}
                        />
                    </div>
                    <div>
                        <RadioGroup 
                            value={tipo} // ← Já em inglês (LIST/MAP)
                            onValueChange={(value) => setTipo(value as "LIST" | "MAP")}
                        >
                            <div className="flex justify-between px-10">
                                <span>Tipo:</span>
                                <div className="flex flex-row gap-2 items-center">
                                    <RadioGroupItem value="LIST" />
                                    <span>Lista</span>
                                </div>
                                <div className="flex flex-row gap-2 items-center">
                                    <RadioGroupItem value="MAP" />
                                    <span>Dicionário</span>
                                </div>
                            </div>
                        </RadioGroup>
                    </div>
                </div>

                <AlertDialogFooter>
                    <AlertDialogCancel>Cancelar</AlertDialogCancel>
                    <AlertDialogAction 
                        className="bg-cyan-700 hover:bg-cyan-500"
                        onClick={handleSubmit}
                    >
                        Criar Agenda
                    </AlertDialogAction>
                </AlertDialogFooter>

            </AlertDialogContent>
        </AlertDialog>
    );
}