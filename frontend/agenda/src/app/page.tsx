import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { ArrowLeftRight, Phone, Search, Trash2, UserPlus } from "lucide-react";

export default function Home() {

  const contatos = [
    {id: "1", nome: "Luiza de Sá Florentino", telefone: "(71) 9 1234-4321"},
    {id: "2", nome: "Iuri Brandão", telefone: "(71) 9 1234-4321"},
    {id: "3", nome: "Caio Melo Seixas", telefone: "(71) 9 1234-4321"},
    {id: "4", nome: "Luiz Vinícius", telefone: "(71) 9 1234-4321"},
    {id: "5", nome: "Eduardo Américo", telefone: "(71) 9 1234-4321"},
  ]


  return (
    <div className="flex flex-col gap-8">
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
      <div className="flex flex-col gap-5">
        <div className="flex flex-row gap-5 items-center text-2xl font-semibold">
          <Phone />
          <h2 >Contatos</h2>
        </div>  

        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Nome</TableHead>
              <TableHead>Telefone</TableHead>
              <TableHead>Excluir</TableHead>
            </TableRow>
          </TableHeader>
          
          <TableBody>
            {contatos.map((contato) => (
              <TableRow key={contato.id}>
                <TableCell>{contato.nome}</TableCell>
                <TableCell>{contato.telefone}</TableCell>
                <TableCell>
                  <Button size={"icon"} variant={"ghost"}>
                    <Trash2 className="text-red-500"/>
                  </Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>

      </div>  

      {/* |=======| BOTÕES ABAIXO |=======| */}
      <div className="flex flex-row justify-between">
          <Button className="bg-cyan-700 cursor-pointer hover:bg-cyan-500">
              <ArrowLeftRight />
              Converter Agenda
          </Button>
          <Button className="bg-cyan-700 cursor-pointer hover:bg-cyan-500">
              <UserPlus />
              Adicionar contato
          </Button>
      </div>

    </div>
  );
}
