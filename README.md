# Agenda de Contatos

Este é um projeto full-stack de uma agenda de contatos, com um backend em Java com Spring Boot e um frontend em Next.js.

## Visão Geral

A aplicação permite que os usuários criem e gerenciem suas agendas de contatos. Atualmente, o foco está na criação de agendas, com a funcionalidade de adicionar, remover e buscar contatos.

O backend oferece suporte a dois tipos de estruturas de armazenamento de contatos: `LIST` (usando `ArrayList`) e `MAP` (usando `HashMap`). A escolha da estrutura é feita no momento da criação da agenda.

## Tecnologias Utilizadas

### Backend

* **Java 17**
* **Spring Boot:** Framework para criação da aplicação.
* **Spring Data JPA:** Para persistência de dados.
* **PostgreSQL:** Banco de dados relacional.
* **Docker:** Para containerização do banco de dados.
* **Lombok:** Para reduzir código boilerplate em entidades e DTOs.
* **Maven:** Gerenciador de dependências.

### Frontend

* **Next.js:** Framework React para o frontend.
* **React**
* **TypeScript**
* **Tailwind CSS:** Para estilização.
* **Lucide React:** Ícones.

## Estrutura do Projeto

O projeto é dividido em duas partes principais:

* `agenda/backend`: Contém a aplicação Spring Boot.
* `agenda/app`: Contém a aplicação Next.js.

### Backend

A estrutura do backend segue as convenções do Spring Boot, com pacotes para controllers, services, repositories, entities e DTOs.

### Frontend

O frontend utiliza a estrutura de diretórios do Next.js App Router.

## Como Executar

### Pré-requisitos

* Java 17 ou superior
* Maven
* Node.js e npm (ou Yarn)

### Backend

1.  Navegue até o diretório `agenda/backend`.
2.  Execute `mvn spring-boot:run`.
3.  O servidor estará disponível em `http://localhost:8080`.

### Frontend

1.  Navegue até o diretório `agenda`.
2.  Execute `npm install` (ou `yarn`).
3.  Execute `npm run dev` (ou `yarn dev`).
4.  A aplicação estará disponível em `http://localhost:3000`.

## Autores

* Caio Melo
* Gabriel Rodrigues
* Iuri Brandão
* Luiz Vinícius Souza
* Luiza Limoeiro