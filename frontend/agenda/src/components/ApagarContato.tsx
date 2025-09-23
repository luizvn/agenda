import { AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogFooter, AlertDialogHeader, AlertDialogTitle } from "./ui/alert-dialog";
import { Contato } from "@/core/contato";

interface ApagarContatoProps {
    isOpen: boolean;
    onOpenChange: (open: boolean) => void;
    onConfirmar: () => void;
    contato: Contato | null;
}

export default function ApagarContato({ isOpen, onOpenChange, onConfirmar, contato }: ApagarContatoProps) {
    
    const handleConfirmar = () => {
        onConfirmar();
    };

    const handleCancelar = () => {
        onOpenChange(false);
    };

    return(
        <AlertDialog open={isOpen} onOpenChange={onOpenChange}>
            <AlertDialogContent>
                <AlertDialogHeader>
                    <AlertDialogTitle>Deseja mesmo apagar o contato?</AlertDialogTitle>
                </AlertDialogHeader>
                {contato && (
                    <div className="py-2">
                        <p><strong>Nome:</strong> {contato.nome}</p>
                        <p><strong>Telefone:</strong> {contato.telefone}</p>
                    </div>
                )}
                <AlertDialogFooter>
                    <AlertDialogCancel onClick={handleCancelar}>Cancelar</AlertDialogCancel>
                    <AlertDialogAction 
                        className="bg-red-700 hover:bg-red-500"
                        onClick={handleConfirmar}
                    >
                        Apagar
                    </AlertDialogAction>
                </AlertDialogFooter>
            </AlertDialogContent>
        </AlertDialog>
    );
}