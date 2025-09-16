import { BookPlus, Contact } from "lucide-react";
import { Button } from "./ui/button";

export default function Header () {
    return(
        <header className="flex flex-row justify-between items-center px-5 py-3 shadow-md shadow-blue-300">
            <div className="text-2xl">
                Agenda 01
            </div>
            <div className="flex flex-row gap-4">
                <Button className="bg-cyan-700 cursor-pointer hover:bg-cyan-500">
                    <Contact />
                    Selecionar
                </Button>
                <Button className="bg-cyan-700 cursor-pointer hover:bg-cyan-500">
                    <BookPlus />
                    Adicionar
                </Button>
            </div>
        </header>
    );
}