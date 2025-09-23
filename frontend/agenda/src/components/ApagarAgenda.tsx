import { Agenda } from "@/core/agenda";
import { AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogFooter, AlertDialogHeader, AlertDialogTitle } from "./ui/alert-dialog";

interface ApagarAgendaProps {
    isOpen: boolean;
    onOpenChange: (open: boolean) => void;
    onConfirmar: () => void;
    agenda: Agenda | null;
}

export default function ApagarAgenda({ isOpen, onOpenChange, onConfirmar, agenda }: ApagarAgendaProps) {
    
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
                    <AlertDialogTitle>Deseja mesmo apagar a Agenda?</AlertDialogTitle>
                </AlertDialogHeader>
                {agenda && (
                    <div className="py-2">
                        <p><strong>Nome:</strong> {agenda.nome}</p>
                        <p><strong>ID:</strong> {agenda.id}</p>
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