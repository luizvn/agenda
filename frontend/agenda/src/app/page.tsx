// No page.tsx (Home)
"use client"

import AdicionarContato from "@/components/AdicionarContato";
import ApagarContato from "@/components/ApagarContato";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { ArrowLeftRight, Phone, Search, Trash2, UserPlus } from "lucide-react";
import { useState, useEffect } from "react";
import { getContatosDaAgenda, removerContato, converterTipoAgenda } from "@/service/agendaService"; // Importe a função
import { Contato } from "@/core/contato";

export default function Home() {
    const [isDialogAdicionarContatoOpen, setIsDialogAdicionarContatoOpen] = useState(false);
    const [isDialogApagarContatoOpen, setIsDialogApagarContatoOpen] = useState(false);
    const [contatos, setContatos] = useState<Contato[]>([]);
    const [busca, setBusca] = useState("");
    const [agendaId, setAgendaId] = useState<number | null>(null);
    const [contatoParaExcluir, setContatoParaExcluir] = useState<Contato | null>(null);
    const [convertendo, setConvertendo] = useState(false); // Estado para loading

    useEffect(() => {
        const carregarContatos = async () => {
            if (!agendaId) return;

            try {
                const response = await getContatosDaAgenda(agendaId, busca || undefined);
                setContatos(response.data);
            } catch (error) {
                console.error("Erro ao carregar contatos:", error);
            }
        };

        carregarContatos();
    }, [agendaId, busca]);

    useEffect(() => {
        const agendaSalva = localStorage.getItem('agendaSelecionada');
        if (agendaSalva) {
            const agenda = JSON.parse(agendaSalva);
            setAgendaId(agenda.id);
        }
    }, []);

    const handleConverterAgenda = async () => {
        if (!agendaId) {
            alert("Selecione uma agenda primeiro");
            return;
        }

        try {
            setConvertendo(true);
            
            const response = await converterTipoAgenda(agendaId);
            const agendaConvertida = response.data;
            
            localStorage.setItem('agendaSelecionada', JSON.stringify(agendaConvertida));
            
            const contatosResponse = await getContatosDaAgenda(agendaId, busca || undefined);
            setContatos(contatosResponse.data);
            
            alert(`Agenda convertida com sucesso! ${agendaConvertida.nome}`);
            
        } catch (error: any) {
            console.error("Erro ao converter agenda:", error);
            
            if (error.response?.status === 400) {
                alert("Erro: " + (error.response.data?.message || "Não foi possível converter a agenda"));
            } else {
                alert("Erro ao converter agenda. Tente novamente.");
            }
        } finally {
            setConvertendo(false);
        }
    };

    const handleBuscarContato = (e: React.FormEvent) => {
        e.preventDefault();
    };

    const handleAbrirDialogExcluir = (contato: Contato) => {
        setContatoParaExcluir(contato);
        setIsDialogApagarContatoOpen(true);
    };

    const handleExcluirContato = async () => {
        if (!contatoParaExcluir || !agendaId) return;

        try {
            await removerContato(agendaId, contatoParaExcluir.id);
            
            setContatos(contatos.filter(c => c.id !== contatoParaExcluir.id));
            setContatoParaExcluir(null);
            setIsDialogApagarContatoOpen(false);
            
        } catch (error) {
            console.error("Erro ao excluir contato:", error);
            alert("Erro ao excluir contato");
        }
    };

    const handleContatoAdicionado = () => {
        if (agendaId) {
            getContatosDaAgenda(agendaId, busca || undefined)
                .then(response => setContatos(response.data))
                .catch(error => console.error("Erro ao recarregar contatos:", error));
        }
    };

    return (
        <div className="flex flex-col gap-8">
            {/* |=======| BUSCA DO CONTATO |=======| */}
            <form onSubmit={handleBuscarContato} className="relative flex-grow w-full">
                <Input 
                    type={"search"} 
                    placeholder="Buscar contato por telefone" 
                    className="pl-4 pr-10 py-2 w-full rounded-lg h-10"
                    value={busca}
                    onChange={(e) => setBusca(e.target.value)}
                />
                <Button 
                    type="submit"
                    variant={"ghost"} 
                    size={"icon"} 
                    className="absolute right-1 top-1/2 transform -translate-y-1/2 h-8 w-8 cursor-pointer
                                text-white hover:text-white bg-cyan-700 hover:bg-cyan-500"
                >
                    <Search/>
                </Button>
            </form>  

            {/* |=======| TABELA DE CONTATOS |=======| */} 
            <div className="flex flex-col gap-5">
                <div className="flex flex-row gap-5 items-center text-2xl font-semibold">
                    <Phone />
                    <h2>Contatos</h2>
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
                        {contatos.length === 0 ? (
                            <TableRow>
                                <TableCell colSpan={3} className="text-center text-gray-500">
                                    {agendaId ? "Nenhum contato encontrado" : "Selecione uma agenda primeiro"}
                                </TableCell>
                            </TableRow>
                        ) : (
                            contatos.map((contato) => (
                                <TableRow key={contato.id}>
                                    <TableCell>{contato.nome}</TableCell>
                                    <TableCell>{contato.telefone}</TableCell>
                                    <TableCell>
                                        <Button 
                                            size={"icon"} 
                                            variant={"ghost"}
                                            onClick={(e) => {
                                                e.preventDefault();
                                                handleAbrirDialogExcluir(contato);
                                            }}
                                        >
                                            <Trash2 className="text-red-500"/>
                                        </Button>
                                    </TableCell>
                                </TableRow>
                            ))
                        )}
                    </TableBody>
                </Table>
            </div>  

            {/* |=======| BOTÕES ABAIXO |=======| */}
            <div className="flex flex-row justify-between">
                <Button 
                    className="bg-cyan-700 cursor-pointer hover:bg-cyan-500"
                    onClick={handleConverterAgenda}
                    disabled={!agendaId || convertendo} // Desabilita durante a conversão
                >
                    <ArrowLeftRight />
                    {convertendo ? "Convertendo..." : "Converter Agenda"}
                </Button>
                <Button 
                    className="bg-cyan-700 cursor-pointer hover:bg-cyan-500"
                    onClick={(e) => {
                        e.preventDefault();
                        setIsDialogAdicionarContatoOpen(true);
                    }}
                    disabled={!agendaId}
                >
                    <UserPlus />
                    Adicionar contato
                </Button>
            </div>
            
            <AdicionarContato 
                isOpen={isDialogAdicionarContatoOpen} 
                onOpenChange={setIsDialogAdicionarContatoOpen} 
                agendaId={agendaId}
                onContatoAdicionado={handleContatoAdicionado}
            />
            <ApagarContato 
                isOpen={isDialogApagarContatoOpen} 
                onOpenChange={setIsDialogApagarContatoOpen}
                onConfirmar={handleExcluirContato}
                contato={contatoParaExcluir}
            />
        </div>
    );
}