import { Contact } from "lucide-react";
import { Button } from "./ui/button";

export default function Header () {
    return(
        <header className="flex justify-between">
            <div>
                Agenda 01
            </div>
            <div>
                <Button>
                    <Contact />
                    Adicionar
                </Button>
            </div>
        </header>
    );
}