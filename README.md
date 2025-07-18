# Sistema de Gestão de Oficina – Milho Verde

**Projeto acadêmico de Programação Orientada a Objetos (POO)** | Status: ✔️ Completo

## 📖 Descrição

[cite_start]Este sistema foi desenvolvido para gerenciar de forma eficiente todos os processos operacionais de uma oficina mecânica, como proposto no trabalho prático da disciplina[cite: 1, 66]. O software, construído com uma **interface gráfica em Java Swing**, permite automatizar e organizar tarefas rotineiras como controle de clientes, veículos, estoque, agendamentos, ordens de serviço, funcionários e finanças, integrando funcionalidades essenciais em uma solução robusta.

## ✨ Principais Funcionalidades

-   **Gerenciamento de Serviços**: Controle completo das ordens de serviço com status (`Aguardando`, `Em Inspeção`, `Em Serviço`, `Finalizada`, `Cancelada`) gerenciados pelo **Padrão State**.
-   **Agendamento Inteligente**: Cadastro, edição e controle de agendamentos em um calendário funcional, com verificação de disponibilidade e aplicação de taxas para cancelamentos no mesmo dia.
-   **Cadastro de Clientes e Veículos**: Inclusão, edição e busca de clientes e seus respectivos veículos, com associação clara entre eles.
-   **Gestão de Funcionários**: Cadastro, edição, autenticação, controle de ponto e permissões por cargo (`Admin`, `Atendente`, `Mecanico`).
-   **Controle de Estoque**: Gerenciamento de produtos/peças, com registro de compras, baixa automática para serviços e atualização do estoque.
-   **Extratos e Relatórios**: Emissão de extratos detalhados por Ordem de Serviço, relatórios diários/mensais de vendas e balanço financeiro.
-   **Controle de Acesso**: Autenticação obrigatória de funcionário para iniciar o sistema, com acesso a funcionalidades restritas de acordo com o cargo.
-   **Gestão de Elevadores**: O sistema gerencia 3 elevadores de forma estática, que podem ser associados aos agendamentos.

## ✔️ Requisitos e Critérios Atendidos

O projeto foi desenvolvido para atender a todos os requisitos obrigatórios e boas práticas da disciplina de POO:

-   **Diagramas UML**: A modelagem do sistema (Casos de Uso, Classes, Sequência, Estados) foi realizada para guiar a implementação.
-   **Pilares da POO**: O código aplica de forma rigorosa os conceitos de Herança, Polimorfismo, Encapsulamento e Abstração.
-   [cite_start]**Estruturas de Dados**: Utiliza tanto vetores estáticos (`Elevador`) quanto listas dinâmicas (`ArrayList` para clientes, OS, etc.)[cite: 36, 41].
-   **Persistência de Dados**: Todas as informações são salvas e recuperadas de arquivos `.json` utilizando a biblioteca **Gson**, com tratamento seguro de recursos.
-   [cite_start]**Interface Gráfica (Extra)**: A interface gráfica foi desenvolvida com **Java Swing**, cumprindo o requisito extra da disciplina[cite: 53].
-   **Testes e Conceitos Avançados**: A classe `RespondendoQuestoes.java` demonstra a aplicação de `Iterator`, `Comparator`, `Collections.sort()` e `binarySearch`, conforme solicitado.

## 📂 Estrutura do Projeto

O código está organizado de forma modular, separando responsabilidades para facilitar a manutenção e o entendimento.

```
com/mycompany/oficina/
│
├── application/    # Classe principal e Singleton que une o sistema
├── controller/     # Lógica de negócio e regras da aplicação
├── entidades/      # Classes de modelo (Cliente, Veículo, Funcionario, etc.)
├── persistencia/   # Classes de acesso e controle dos arquivos JSON
├── gui/            # Interface gráfica (telas e diálogos com Swing)
├── agendamento/    # Lógica e classes relacionadas a agendamentos
├── financeiro/     # Classes para controle de receitas e despesas
├── ordemservico/   # Lógica e padrões (State, Observer) para Ordens de Serviço
├── strategy/       # Padrão Strategy para validações de dados
├── interpreter/    # Padrão Interpreter para o sistema de busca
└── ...
```

## 🚀 Como Usar

1.  **Pré-requisitos**: JDK 17+ e Maven.
2.  **Clone o repositório**: `git clone https://github.com/mariavieirag/projetooficina.git`
3.  **Execute o projeto** via sua IDE ou pelo terminal com o Maven:
    ```bash
    cd projetooficina/Oficina
    mvn clean compile exec:java
    ```
4.  **Faça o login** com o usuário administrador padrão (criado na primeira execução):
    * **CPF:** `00000000000`
    * **Senha:** `admin`
5.  Navegue pelos menus para gerenciar as funcionalidades da oficina.

## 📦 Dependências

-   **Gson**: Biblioteca da Google para serialização e leitura de arquivos JSON.
-   **Java SE 17** ou superior.

## 📝 Observações

-   O projeto foi desenvolvido como uma avaliação acadêmica, focando na aplicação correta dos conceitos de POO e padrões de projeto.
-   A persistência em JSON foi um requisito obrigatório da disciplina.
-   O código é amplamente documentado para facilitar a compreensão e a manutenção.
