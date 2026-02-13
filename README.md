# Finance Core 💰

> Sistema financeiro pessoal assistido por IA, com foco em redução de atrito cognitivo, arquitetura limpa e extensibilidade profissional.

[![Java](https://img.shields.io/badge/Java-17%2B-blue)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.0-brightgreen)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16%2B-blue)](https://www.postgresql.org/)
[![Clean Architecture](https://img.shields.io/badge/Architecture-Clean%20%2B%20DDD-lightgrey)]()

---

## Sobre o Projeto

**Finance Core** é um módulo de controle financeiro pessoal projetado para operar **offline-first**, com assistência por **inteligência artificial**, interface limpa e arquitetura preparada para evolução contínua.

### Diferenciais

- **Entrada por linguagem natural** (voz/texto) com IA como assistente, não como oráculo.
- **Clean Architecture + DDD leve** – domínio independente de frameworks.
- **Persistência local segura** com PostgreSQL + criptografia AES.
- **Modular e extensível** – pronto para novos módulos (auditoria, premium, sincronização em nuvem).
- **Gamificação psicológica** e visualização motivadora, sem infantilidade.

### Objetivos Técnicos

- Registrar entradas/saídas com precisão.
- Categorização automática via IA.
- Consolidação mensal e relatórios históricos.
- Detecção de inconsistências financeiras.
- Base preparada para futura sincronização e API REST.

---

### Arquitetura

O projeto segue **Clean Architecture** com as seguintes camadas:

┌────────────────────────────────────────────────────┐
│ Presentation │ (JavaFX / API futura)
├────────────────────────────────────────────────────┤
│ Application │ (Casos de Uso, DTOs)
├────────────────────────────────────────────────────┤
│ Domain │ (Entidades, Regras de Negócio)
├────────────────────────────────────────────────────┤
│ Infrastructure │ (Persistência, IA, Criptografia)
└────────────────────────────────────────────────────┘

**Princípios**:
- Domínio **não depende** de nenhuma camada externa.
- Repositórios definidos no domínio, implementados na infraestrutura.
- Casos de uso orquestram o fluxo, sem regras de negócio.

---

### Tecnologias Utilizadas

| Camada          | Tecnologia                         |
|-----------------|------------------------------------|
| Linguagem       | Java 17+                           |
| Framework       | Spring Boot 3.3.0, Spring Data JPA |
| Build           | Maven (multi‑module)               |
| Banco de Dados  | PostgreSQL 16                      |
| Migração        | Flyway                             |
| Testes          | JUnit 5, Mockito                   |
| Criptografia    | AES (camada de infraestrutura)     |
| IA (futuro)     | Strategy Pattern para providers    |

---

### Estrutura do Projeto (Módulos)

finance-core/
├── finance-domain/ # Núcleo do negócio (entidades, regras, interfaces)
├── finance-application/ # Casos de uso, comandos, DTOs
├── finance-infrastructure/ # Persistência, IA, migrações, criptografia
├── finance-interface/ # Controllers, adaptadores de UI
├── finance-bootstrap/ # Configuração e inicialização Spring Boot
└── documentation/ # Documentação completa (requisitos, arquitetura, etc.)

---

### Como Executar Localmente

### Pré-requisitos
- Java 17+
- Maven 3.9+
- PostgreSQL 16+

### Passos

1. **Clone o repositório**
   ```bash
   git clone hhttps://github.com/arianadeabreudesigndev/FinanceCore.git
   cd finance-core

2.Configure o banco de dados

    CREATE DATABASE finance_core;
    -- Usuário e senha devem ser configurados no application.properties

3. Execute as migrações (Flyway criará as tabelas automaticamente)

    mvn clean install
    mvn spring-boot:run -pl finance-bootstrap

4. Acesse a aplicação (interface JavaFX será iniciada)

--

### Testes
Testes unitários de domínio: 100% de cobertura nas regras de negócio.

Testes de integração com banco de dados em memória (H2) para repositórios.

Testes de aceitação baseados nos casos de uso.

    mvn test

--

### Documentação Completa
Toda a documentação do projeto (requisitos, casos de uso, modelo de domínio, regras de negócio, arquitetura, modelo de dados, decisões técnicas) está disponível na pasta /documentation.

Principais artefatos:

- Visão Geral do Sistema

- Requisitos Funcionais e Não Funcionais

- [![Casos de Uso Detalhados]()]()

- [![Modelo de Domínio]()]()

- [![Regras de Negócio]()]()

- [![Arquitetura]()]()

- [![Modelo de Dados Físico]()]()

- [![Política de Migração]()]()

- [![Estratégia de Testes]()]()

- [![Guia de Contribuição]()]()

--

### Licença

Copyright © 2026 Tailane Aparecida de Abreu Lopes. Todos os direitos reservados.

Este projeto é público para visualização como parte do portfólio profissional do autor.
Não é permitida a cópia, distribuição, modificação ou uso comercial sem autorização expressa.

--

### Contato
Autor: Tailane Aparecida de Abreu Lopes

LinkedIn: https://www.linkedin.com/in/arianadeabreudev/

Portfólio: https://portfolioariandeabreudesigndev.netlify.app

E-mail: arianadeabreudesigndev@gmail.com

--

⭐ Se este projeto foi útil para você, considere dar uma estrela! 