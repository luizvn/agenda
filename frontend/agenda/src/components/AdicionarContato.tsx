import { X } from "lucide-react";
import { AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogFooter, AlertDialogHeader, AlertDialogTitle } from "./ui/alert-dialog";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { useState } from "react";
import { adicionarContato } from "@/service/agendaService";

interface AdicionarContatoProps {
    isOpen: boolean;
    onOpenChange: (open: boolean) => void;
    agendaId: number | null;
    onContatoAdicionado?: () => void;
}

export default function AdicionarContato({ isOpen, onOpenChange, agendaId, onContatoAdicionado }: AdicionarContatoProps) {
    const [nome, setNome] = useState("");
    const [telefone, setTelefone] = useState("");

    const handleSubmit = async () => {
        if (!nome.trim() || !telefone.trim()) {
            alert("Por favor, preencha todos os campos");
            return;
        }

        if (!agendaId) {
            alert("Nenhuma agenda selecionada");
            return;
        }

        try {
            await adicionarContato(agendaId, { 
                nome: nome.trim(), 
                telefone: telefone.trim() 
            });
            
            // Limpar formulário
            setNome("");
            setTelefone("");
            
            // Fechar modal
            onOpenChange(false);
            
            // Chamar callback ou recarregar
            if (onContatoAdicionado) {
                onContatoAdicionado();
            } else {
                window.location.reload();
            }
            
        } catch (error: any) {
            console.error("Erro ao adicionar contato:", error);
            alert(`Erro ao adicionar contato: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleClose = () => {
        setNome("");
        setTelefone("");
        onOpenChange(false);
    };

    return (
        <AlertDialog open={isOpen} onOpenChange={onOpenChange}>
            <AlertDialogContent>
                <AlertDialogHeader>
                    <AlertDialogTitle>Adicionar Contato</AlertDialogTitle>
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
                    <div className="flex flex-col gap-0.5">
                        <span>Telefone</span>
                        <Input 
                            placeholder="Insira o telefone"
                            value={telefone}
                            onChange={(e) => setTelefone(e.target.value)}
                        />
                    </div>
                </div>

                <AlertDialogFooter>
                    <AlertDialogCancel>Cancelar</AlertDialogCancel>
                    <AlertDialogAction 
                        className="bg-cyan-700 hover:bg-cyan-500"
                        onClick={handleSubmit}
                        disabled={!nome.trim() || !telefone.trim() || !agendaId}
                    >
                        Adicionar Contato
                    </AlertDialogAction>
                </AlertDialogFooter>

            </AlertDialogContent>
        </AlertDialog>
    );
}