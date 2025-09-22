import { X } from "lucide-react";
import { AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogFooter, AlertDialogHeader, AlertDialogTitle } from "./ui/alert-dialog";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { RadioGroup, RadioGroupItem } from "./ui/radio-group";

interface AdicionarAgendaProps {
    isOpen: boolean;
    onOpenChange: (open: boolean) => void;
}

export default function AdicionarAgenda ({ isOpen, onOpenChange}:AdicionarAgendaProps) {
    return(
        <AlertDialog open={isOpen} onOpenChange={onOpenChange}>
            <AlertDialogContent>
                <AlertDialogHeader>
                    <AlertDialogTitle>Adicionar Agenda</AlertDialogTitle>
                    <Button className="absolute right-4 top-4" size={"icon"} variant={"outline"} onClick={() => {
                        onOpenChange(false);
                    }}><X /></Button>
                </AlertDialogHeader>

                <div className="grid grid-cols-1 gap-2.5">
                    <div className="flex flex-col gap-0.5">
                        <span>Nome</span>
                        <Input placeholder="insira o nome"/>
                    </div>
                    <div>
                        <RadioGroup defaultValue="lista">
                            <div className="flex justify-between px-10">
                                <span>Tipo:</span>
                                <div className="flex flex-row gap-2 items-center">
                                    <RadioGroupItem value="lista"></RadioGroupItem>
                                    <span>Lista</span>
                                </div>
                                <div className="flex flex-row gap-2 items-center">
                                    <RadioGroupItem value="dicionario"></RadioGroupItem>
                                    <span>Dicionário</span>
                                </div>
                            </div>
                        </RadioGroup>
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