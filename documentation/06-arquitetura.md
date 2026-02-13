# Documento de Arquitetura do Sistema – Finance Core

---

## 1. Objetivo da Arquitetura

Este documento define a **Arquitetura do Sistema Finance Core**, estabelecendo a organização estrutural do software, seus módulos, responsabilidades, padrões adotados e princípios de decisão.

O objetivo é garantir que o sistema seja:

- Consistente com o domínio financeiro definido.
- Escalável e extensível de forma controlada.
- Tecnicamente sustentável no longo prazo.
- Preparado para evolução futura (novos módulos, IA, premium).

Este documento **não define implementação detalhada**, mas sim **decisões arquiteturais estruturantes**.

---

## 2. Princípios Arquiteturais Fundamentais

A arquitetura do Finance Core é guiada pelos seguintes princípios:

### 2.1 Separação Estrita de Responsabilidades

- O domínio **não depende** de infraestrutura.
- Regras de negócio **não dependem** de interface.
- Persistência é um detalhe técnico isolado.

### 2.2 Domínio como Centro do Sistema

- O **Modelo de Domínio** (`04-modelo-dominio.md`) é a fonte primária de verdade.
- Toda decisão técnica deve respeitar entidades, agregados, invariantes e regras de negócio.

### 2.3 Evolução Controlada

- Novos módulos devem **acoplar-se ao núcleo**, nunca deformá‑lo.
- Funcionalidades premium não alteram regras essenciais.

### 2.4 Arquitetura Modular

- Cada módulo possui fronteiras claras.
- Dependências são direcionais e explícitas.

---

## 3. Estilo Arquitetural Adotado

### 3.1 Arquitetura em Camadas com Ênfase em Domínio

O sistema adota uma **arquitetura em camadas**, organizada da seguinte forma:

┌─────────────────────────────────────────────────────┐
│ Camada de Apresentação │ (UI / Interface)
├─────────────────────────────────────────────────────┤
│ Camada de Aplicação │ (Casos de Uso, DTOs)
├─────────────────────────────────────────────────────┤
│ Camada de Domínio │ (Entidades, Regras, Interfaces)
├─────────────────────────────────────────────────────┤
│ Camada de Infraestrutura│ (Persistência, IA, IO)
└─────────────────────────────────────────────────────┘


**Regra fundamental:** A Camada de Domínio **não depende** de nenhuma outra camada.

---

## 4. Camadas do Sistema – Detalhamento

| Camada              | Responsabilidade                                                                 | Contém                                                                 | Não contém                          |
|---------------------|----------------------------------------------------------------------------------|------------------------------------------------------------------------|-------------------------------------|
| **Apresentação**    | Interação com o usuário (UI/UX), coleta de dados, exibição de resultados.        | Controllers, adaptadores de UI, telas.                                | Regras de negócio, lógica de cálculo. |
| **Aplicação**       | Coordenar casos de uso, orquestrar fluxos entre domínio e infraestrutura.        | Serviços de aplicação, DTOs, casos de uso, mapeadores.                | Lógica financeira complexa.         |
| **Domínio**         | Representar o negócio, entidades, agregados, invariantes, regras de negócio.     | Entidades, Value Objects, agregados, serviços de domínio, interfaces de repositório. | Dependências de frameworks, persistência. |
| **Infraestrutura**  | Persistência, integração externa, serviços técnicos.                            | Implementações de repositórios, ORM, criptografia, provedores de IA, exportadores. | Regras de negócio, lógica de domínio. |

---

## 5. Organização Modular do Sistema

O sistema é implementado como um **Modular Monolith** multi‑módulo, com separação física e lógica.

### 5.1 Módulos Principais

| Módulo                | Artefato                  | Responsabilidade                                                                 |
|-----------------------|---------------------------|----------------------------------------------------------------------------------|
| **finance-domain**    | `finance-domain`          | Núcleo do negócio: entidades, agregados, regras, interfaces de repositório.     |
| **finance-application**| `finance-application`    | Casos de uso, comandos, DTOs, serviços de aplicação, mapeadores.                |
| **finance-infrastructure**| `finance-infrastructure`| Persistência (JPA, PostgreSQL), provedores de IA, criptografia, Flyway.         |
| **finance-interface** | `finance-interface`       | Controllers, adaptadores de UI (JavaFX/Compose), DTOs de entrada/saída.         |
| **finance-bootstrap** | `finance-bootstrap`       | Configuração e inicialização Spring Boot, composição da aplicação.              |

**Observação:** O módulo `finance-domain` é **obrigatório e imutável** – nenhuma regra de negócio pode ser movida para fora dele.

---

## 6. Módulos Futuros e Extensibilidade

### 6.1 Módulos de Suporte (Futuros)

- Auditoria Financeira
- Limite Psicológico
- Simulação de Corte
- Detecção de Gasto Compulsivo
- Linha do Tempo Financeira

**Características:**
- Dependem do módulo central (`finance-domain`).
- Não alteram regras centrais.
- Podem ser ativados ou desativados conforme licença.

### 6.2 Módulos Premium (Futuros)

- Perfis Financeiros
- Consultoria Financeira
- Análises Avançadas

**Características:**
- Totalmente desacoplados do núcleo.
- Implementados como módulos independentes que consomem a API pública do sistema.

---

## 7. Padrões Arquiteturais e de Design

| Padrão                 | Aplicação                                                                                       |
|------------------------|-------------------------------------------------------------------------------------------------|
| **Domain‑Driven Design (DDD)** | Abordagem conceitual: linguagem ubíqua, agregados, invariantes, serviços de domínio.     |
| **Ports and Adapters (Hexagonal)** | Isolamento do domínio através de interfaces (ports) implementadas na infraestrutura (adapters). |
| **Repository Pattern** | Interfaces de repositório definidas no domínio; implementações concretas na infraestrutura.    |
| **Strategy Pattern**   | Provedores de IA intercambiáveis (OpenAI, DeepSeek, modelos locais).                          |
| **Command Pattern**    | Representação de ações financeiras (gasto, entrada, parcelamento) como objetos.                |
| **Observer / Event Bus** | Notificações e atualizações da camada de analytics.                                          |
| **Factory Pattern**    | Criação de provedores de IA e outros componentes intercambiáveis.                             |
| **Specification Pattern** | Regras financeiras complexas (ex.: identificação de gasto essencial).                       |

---

## 8. Decisões Arquiteturais Registradas (ADRs)

| ID      | Decisão                                      | Status    | Descrição Resumida                                                                 |
|---------|----------------------------------------------|-----------|------------------------------------------------------------------------------------|
| ADR-01  | Domínio Independente de Tecnologia           | Aprovado  | O domínio não deve conter anotações, heranças ou dependências de frameworks.       |
| ADR-02  | Persistência como Detalhe                   | Aprovado  | O modelo de domínio não conhece a estratégia de persistência; repositórios são interfaces. |
| ADR-03  | Modularização Progressiva                   | Aprovado  | O sistema iniciará como monólito modular e poderá ser decomposto futuramente.      |
| ADR-04  | Sem C4 Model Neste Momento                  | Aprovado  | O nível atual de documentação não justifica a adoção do modelo C4.                 |

---

## 9. Considerações de Evolução

A arquitetura foi deliberadamente projetada para suportar:

- **Crescimento funcional** sem refatoração drástica – novos casos de uso adicionam apenas novas classes, sem alterar o núcleo.
- **Integração futura com IA** – através de uma interface única (Strategy) e múltiplos provedores.
- **Exportações e consolidações** – separação entre write model (domínio) e read model (queries otimizadas).
- **Monetização via módulos premium** – módulos desacoplados que consomem a mesma base de domínio.

---

## 10. Rastreabilidade com os Demais Artefatos

- O **Modelo de Domínio** (`04-modelo-dominio.md`) define as entidades e agregados que esta arquitetura deve preservar.
- As **Regras de Negócio** (`05-regras-negocio.md`) são aplicadas na camada de domínio e orquestradas pela camada de aplicação.
- O **Modelo de Dados** (`07-modelo-dados.md`) deriva das entidades de domínio e é implementado na infraestrutura.
- A **Política de Migrações** (`08-migracoes.md`) garante a evolução controlada do esquema de banco de dados.
- A **Estratégia de Testes** (`09-testes.md`) valida cada camada de forma isolada e integrada.

---

## 11. Encerramento

Este documento define **como o sistema deve ser estruturado**, não como será codificado linha a linha.  
Ele é **obrigatório** para orientar:

- Design de APIs e contratos entre camadas.
- Modelo de dados e mapeamento objeto‑relacional.
- Implementação de casos de uso e testes.
- Decisões futuras de infraestrutura e deploy.

---

**Documentos relacionados:**  
- [01-visao-geral.md](01-visao-geral.md)  
- [02-requisitos.md](02-requisitos.md)  
- [03-casos-de-uso.md](03-casos-de-uso.md)  
- [04-modelo-dominio.md](04-modelo-dominio.md)  
- [05-regras-negocio.md](05-regras-negocio.md)  
- [07-modelo-dados.md](07-modelo-dados.md)  
- [08-migracoes.md](08-migracoes.md)  
- [09-testes.md](09-testes.md)  
- [10-contribuicao.md](10-contribuicao.md)