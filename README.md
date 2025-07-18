Sistema de Gerenciamento de Oficina Mecânica

![Java](https://img.shields.io/badge/Java-17-blue)
![Swing](https://img.shields.io/badge/UI-Swing-orange)
![Maven](https://img.shields.io/badge/Build-Maven-red)
![Database](https://img.shields.io/badge/Database-JSON-yellow)

Sobre o Projeto

Este projeto é um sistema de desktop para o gerenciamento completo de uma oficina mecânica. Desenvolvido em **Java** com a interface gráfica construída em **Swing**, o sistema oferece uma solução robusta para o controle de clientes, veículos, agendamentos, ordens de serviço, estoque de peças e finanças.
A aplicação é estruturada em três perfis de usuário (Gerente, Atendente e Mecânico), cada um com permissões e funcionalidades específicas para otimizar o fluxo de trabalho da oficina.

## ✨ Funcionalidades Principais

O sistema organiza as funcionalidades de acordo com o perfil de usuário logado.

### 👨‍💼 Gerente
Acesso total ao sistema, com permissões para todas as operações, incluindo:
- **Gestão de Pessoal:** Cadastrar, editar e remover funcionários (Atendentes, Mecânicos).
- **Módulo Financeiro:**
    - Emitir balanços diários e mensais (receitas vs. despesas).
    - Gerar relatórios detalhados de despesas.
    - Registrar o pagamento de salários dos funcionários.
- **Controle de Estoque:**
    - Cadastrar novas peças e registrar a despesa da compra.
    - Realizar a reposição de itens no estoque.
- **Visualização de Ordens de Serviço:** Acesso a todos os extratos de OS.

### 👩‍💻 Atendente
Responsável pelo atendimento ao cliente e gerenciamento inicial dos serviços:
- **Gestão de Clientes:** CRUD completo de clientes.
- **Gestão de Veículos:** CRUD completo de veículos, sempre associados a um cliente.
- **Agendamento de Serviços:** Criar e cancelar agendamentos para os clientes.
- **Registro de Ponto:** Realizar o registro de entrada e saída.

### 🔧 Mecânico
Focado na execução e gerenciamento técnico dos serviços:
- **Gestão de Ordens de Serviço (OS):**
    - Iniciar uma OS a partir de um agendamento do dia.
    - Acompanhar e alterar o status de uma OS (`Em Inspeção`, `Em Serviço`, `Finalizada`).
    - Adicionar peças à OS, com baixa automática no estoque.
    - Gerar o extrato detalhado ao finalizar o serviço.
- **Registro de Ponto:** Realizar o registro de entrada e saída.

## 🏛️ Arquitetura e Padrões de Projeto

O projeto foi desenvolvido com foco na organização e manutenibilidade, aplicando diversos padrões de projeto sobre a arquitetura **MVC (Model-View-Controller)**.

- **Singleton:** Garante uma instância única para classes centrais como `OficinaAplicattion` (orquestrador geral) e `Sessao` (controle de login).
- **State:** Gerencia o ciclo de vida da `OrdemDeServico` (`Aguardando`, `Em Inspeção`, `Em Serviço`, `Finalizada`, `Cancelada`). Cada estado é uma classe que define as operações permitidas, tornando o código mais limpo e extensível.
- **Observer:** Utilizado para notificar o cliente sobre mudanças no status de sua `OrdemDeServico`. A classe `NotificadorOs` observa a OS e simula o envio de uma notificação a cada mudança de estado.
- **Strategy:** Aplicado para a validação de dados de entrada (CPF, E-mail, Telefone). Cada regra de validação é uma estratégia encapsulada em sua própria classe, o que facilita a adição de novas validações.
- **Interpreter:** Utilizado para criar um sistema de busca flexível para agendamentos, permitindo consultas por data ou por nome de cliente.

## 🛠️ Tecnologias

- **Java 17**
- **Java Swing**
- **Gson 2.10.1** (para manipulação de JSON)
- **Maven** (para gerenciamento de dependências)

## 🚀 Como Executar o Projeto

#### Pré-requisitos
- JDK 17 ou superior
- Apache Maven

#### Passos
1.  Clone o repositório:
    ```bash
    git clone [https://github.com/mariavieirag/projetooficina.git](https://github.com/mariavieirag/projetooficina.git)
    ```
2.  Navegue até a pasta do projeto:
    ```bash
    cd projetooficina/Oficina
    ```
3.  Compile e execute o projeto utilizando o Maven:
    ```bash
    mvn clean compile exec:java
    ```
    Alternativamente, você pode importar o projeto em sua IDE (IntelliJ, Eclipse, etc.) como um projeto Maven e executar a classe `com.mycompany.oficina.Oficina`.

#### Primeiro Acesso
Ao iniciar o sistema pela primeira vez, um usuário **Admin** padrão é criado automaticamente. Utilize as seguintes credenciais:
- **CPF:** `00000000000`
- **Senha:** `admin`

## 🗃️ Persistência de Dados

O sistema utiliza arquivos **JSON** como meio de persistência de dados. Todas as informações (clientes, funcionários, ordens de serviço, etc.) são salvas em arquivos correspondentes dentro do diretório `data/`, que é criado na raiz do projeto durante a primeira execução.
