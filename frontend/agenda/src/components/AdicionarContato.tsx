import { AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogDescription, AlertDialogFooter, AlertDialogHeader, AlertDialogTitle } from "./ui/alert-dialog";
import { Input } from "./ui/input";

interface AdicionarContatoProps {
    isOpen: boolean;
    onOpenChange: (open: boolean) => void;
}

export default function AdicionarContato ({ isOpen, onOpenChange}:AdicionarContatoProps) {
    return(
        <AlertDialog open={isOpen} onOpenChange={onOpenChange}>
            <AlertDialogContent>
                <AlertDialogHeader>
                    <AlertDialogTitle>Adicionar Contato</AlertDialogTitle>
                </AlertDialogHeader>

                <div className="grid grid-cols-1 gap-3">
                    <div className="flex flex-col gap-0.5">
                        <span>Nome</span>
                        <Input placeholder="Insira o nome"/>
                    </div>
                    <div className="flex flex-col gap-0.5">
                        <span>Telefone</span>
                        <Input placeholder="Insira o telefone"/>
                    </div>
                </div>

                <AlertDialogFooter>
                    <AlertDialogCancel>Cancel</AlertDialogCancel>
                    <AlertDialogAction className="bg-cyan-700 hover:bg-cyan-500">Continue</AlertDialogAction>
                </AlertDialogFooter>
            </AlertDialogContent>
        </AlertDialog>
    )
}