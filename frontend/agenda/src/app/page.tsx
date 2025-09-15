import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Search } from "lucide-react";
import Image from "next/image";

export default function Home() {
  return (
    <div className="">
      {/* |=======| BUSCA DO CONTATO |=======| */}
      <div className="relative flex-grow w-full">
          <Input type={"search"} placeholder="Buscar contato" 
              className="pl-4 pr-10 py-2 w-full rounded-lg h-10"/>
          <Button variant={"ghost"} size={"icon"} 
              className="absolute right-1 top-1/2 transform -translate-y-1/2 h-8 w-8 cursor-pointer
                        text-white hover:text-white bg-cyan-700 hover:bg-cyan-500">
              <Search/>
          </Button>
      </div>  

      {/* |=======| TABELA DE CONTATOS |=======| */}   
    </div>
  );
}
