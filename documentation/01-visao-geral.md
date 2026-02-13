# Visão Geral do Sistema – Finance Core

---

## 1. Propósito e Diferenciais

### 1.1 Propósito do Módulo

O **Finance Core** é um módulo de controle financeiro pessoal projetado para operar **offline-first**, com **assistência por inteligência artificial**. Seu propósito central é **reduzir o atrito cognitivo e operacional** no gerenciamento financeiro diário, eliminando a digitação manual excessiva e oferecendo um controle realista, visualmente limpo e motivador.

Além disso, o módulo foi concebido como uma **base integrável** a outros sistemas do ecossistema do usuário, como ferramentas de organização pessoal e estudos.

### 1.2 Diferenciais Estratégicos

- **Entrada de dados por linguagem natural** (voz ou texto) – a IA atua como **assistente**, não como “oráculo”: ela confirma, pergunta e valida, nunca executando regras financeiras sem supervisão.
- **Separação explícita entre configuração estrutural e uso diário** – o usuário configura uma única vez (rendas fixas, categorias, metas, métodos de pagamento) e revisita raramente; o uso cotidiano concentra-se apenas no registro de movimentações.
- **Gamificação psicológica e leve** – recompensas simbólicas, feedback emocional e visualizações motivadoras, sem elementos infantis.
- **Arquitetura profissional, extensível e desacoplada** – baseada em Clean Architecture, DDD leve e Modular Monolith, preparada para migração futura para nuvem e adição de módulos premium.

---

## 2. Domínio Funcional – Bounded Contexts

A modelagem conceitual do domínio é organizada em quatro núcleos (bounded contexts), cada um com responsabilidades bem definidas.

| Contexto             | Responsabilidade                                                                                                                           | Frequência de Uso                |       
|----------------------|--------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------|
| **FinanceConfig**    | Dados estruturais: fontes de renda, despesas fixas, categorias, métodos de pagamento, metas, parâmetros de notificação, templates visuais. | Raramente (configuração única)   |
| **FinanceFlow**      | Operações diárias: registro de gastos, entradas pontuais, pagamento de parcelas, marcação de essencial/não essencial, confirmação via IA.  | 90% do uso real                  |
| **FinanceAnalytics** | Consolidação e análise: totais mensais, percentuais comprometidos, saldo livre, progresso de metas, séries temporais, base para recomendações futuras. | Consultas periódicas |
| **FinanceAssistant** | Interação inteligente: interpretação de linguagem natural, diálogo de confirmação, classificação automática, sugestões e alertas, interface única para múltiplos provedores de IA. | Sob demanda |

---

## 3. Arquitetura do Sistema

### 3.1 Estilo Arquitetural

Adota-se **Clean Architecture** combinada com **DDD leve** e **Modular Monolith**, pelos seguintes motivos:

- Baixo acoplamento entre camadas.
- Execução local (desktop) com possibilidade de evolução para nuvem sem refatoração dolorosa.
- Alta testabilidade e clareza de responsabilidades.
- Independência de frameworks – o domínio não conhece infraestrutura.

### 3.2 Camadas


┌─────────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                 UI (Desktop) │ ← JavaFX / Compose
└─────────────────────────────────────────────────▲───────────────────────────────────────────────────┘
│
┌─────────────────────────────────────────────────┴───────────────────────────────────────────────────┐
│                        Application Layer │ ← Casos de Uso, Serviços, DTOs
└─────────────────────────────────────────────────▲───────────────────────────────────────────────────┘
│
┌─────────────────────────────────────────────────┴───────────────────────────────────────────────────┐
│      Domain Layer │ ← Entidades, Value Objects, Regras de Negócio, Interfaces de Repositório
└─────────────────────────────────────────────────▲───────────────────────────────────────────────────┘
│
┌─────────────────────────────────────────────────┴───────────────────────────────────────────────────┐
│       Infrastructure │ ← Persistência, IA, Criptografia, Sistema de Arquivos, Notificações
└─────────────────────────────────────────────────────────────────────────────────────────────────────┘

**Princípios fundamentais:**

- A camada de domínio **não depende** de nenhuma outra camada.
- Repositórios são definidos como **interfaces no domínio**; suas implementações residem na infraestrutura.
- Casos de uso orquestram o fluxo, mas **não contêm regras de negócio** – elas estão nas entidades.
- A interface (UI) é um **adaptador** que se comunica exclusivamente com a camada de aplicação.

### 3.3 Principais Padrões de Projeto

| Padrão                   | Aplicação                                                                  |
|--------------------------|----------------------------------------------------------------------------|
| **Strategy**             | Provedores de IA intercambiáveis (OpenAI, DeepSeek, modelos locais).       |
| **Adapter**              | Integração com sistemas externos (voz, notificações, bancos futuros).      |
| **Factory**              | Criação de provedores de IA e outros componentes intercambiáveis.          |
| **Observer / Event Bus** | Notificações e atualizações da camada de analytics.                        |
| **Command**              | Representação de ações financeiras (gasto, entrada, parcelamento).         |
| **Repository**           | Persistência desacoplada do domínio.                                       |
| **Specification**        | Regras financeiras complexas (ex.: identificação de gasto essencial).      |

---

## 4. Persistência de Dados

### 4.1 Estratégia Atual (Offline‑first)

- **Banco de dados:** PostgreSQL 16 (instância local por usuário).
- **Acesso:** JPA / Hibernate com Spring Data JPA.
- **Criptografia:** AES na camada de infraestrutura, por usuário.
- **Versionamento de schema:** Flyway, com scripts versionados e aplicados automaticamente na inicialização.
- **Modelo físico:** conforme definido no documento `07-modelo-dados.md`.

### 4.2 Estratégia Futura

- **Sync Engine** para sincronização entre dispositivos.
- **API REST** para exposição de funcionalidades.
- **Cloud storage** opcional, mantendo compatibilidade com o domínio existente.
- **Exportação manual** de dados (CSV, PDF).

> Nenhuma alteração no domínio será necessária – apenas a substituição de implementações na infraestrutura.

---

## 5. Objetivos Técnicos

### 5.1 Primários

- Registrar entradas e saídas financeiras com precisão.
- Automatizar categorização e organização via IA.
- Consolidar dados financeiros mensais.
- Gerar relatórios históricos simplificados (PDF/HTML).
- Detectar inconsistências entre saldo esperado e saldo real informado.

### 5.2 Secundários

- Suportar gamificação leve.
- Preparar base para auditoria financeira pessoal.
- Permitir evolução para funcionalidades premium.
- Garantir fácil integração futura (API, sincronização em nuvem, versão mobile).

---

## 6. Escopo do Sistema

### 6.1 Incluído (versão atual)

- Controle mensal de receitas e despesas.
- Gastos fixos, variáveis e parcelados.
- Classificação de gastos em **essenciais** e **não essenciais**.
- Metas financeiras (caixinhas).
- Histórico financeiro consolidado.
- Relatórios automáticos (PDF/HTML).
- Assistente de IA para entrada e validação de dados.

### 6.2 Fora do Escopo (versão atual)

- Integração direta com bancos (Open Finance).
- Consultoria financeira humana.
- Investimentos automatizados.
- Multiusuário remoto.
- Sincronização em nuvem.

> Estes itens poderão ser incorporados em versões futuras, mantendo a arquitetura preparada para tal.

---

## 7. IA e Extensibilidade

### 7.1 Abstração de Provedores

O sistema define uma **interface única** para provedores de IA, permitindo a troca transparente entre diferentes implementações:

- OpenAI (GPT)
- DeepSeek
- Modelos locais (futuro)

### 7.2 Responsabilidades da IA

- Interpretar entradas do usuário em linguagem natural.
- Solicitar dados ausentes quando necessário.
- Validar informações fornecidas.
- Sugerir categorias para despesas.
- Gerar alertas contextuais e recomendações.

**A IA não executa regras financeiras** – seu papel é exclusivamente de assistência e suporte à decisão.

---

## 8. Módulos Futuros e Premium

A arquitetura foi projetada para receber novos módulos sem refatoração do núcleo. A seguir, as principais funcionalidades planejadas, categorizadas por natureza.

### 8.1 Funcionais (Produto)

1. Orçamento mensal por categoria.
2. Alerta de gasto anômalo (fora do padrão).
3. Histórico comparativo mês a mês.
4. Projeção de saldo futuro.
5. Simulador “e se eu gastar X?”.
6. Metas com prazo + progresso visual.
7. Parcelas inteligentes (impacto futuro).
8. Tags livres (além de categorias).
9. Importação/exportação CSV.
10. Modo revisão semanal guiada.
11. Detecção de despesas recorrentes.
12. Split de gastos (ex.: dividir aluguel).
13. Controle por contas (banco, dinheiro).
14. Modo “baixo estímulo” (menos gráficos).
15. Sugestão de categorias automática.

### 8.2 Psicológicos / UX (Diferencial Real)

16. Feedback emocional (“esse mês foi mais pesado”).
17. Recompensas simbólicas por constância.
18. Linguagem adaptativa (formal x casual).
19. Visualização de “liberdade financeira do mês”.
20. Redução progressiva de notificações se o usuário ignora.

### 8.3 Arquiteturais / Negócio Futuro

21. Sistema de plugins.
22. Templates premium.
23. Engine de recomendações (offline).
24. API pública para parcerias.
25. Modo multi‑perfil.
26. Feature flags.
27. Telemetria ética (opt‑in).
28. Versionamento de regras financeiras.
29. Logs de decisão da IA (transparência).
30. Engine de experimentos A/B local.

---

## 9. Auditoria Financeira Pessoal (Futuro / Premium)

Módulo especializado para **análise consciente e não punitiva**:

- Limite psicológico de gastos.
- Detecção de gastos compulsivos.
- Simulação de cortes.
- Visão consolidada de desperdícios.
- Histórico de decisões financeiras.

> A arquitetura já está preparada para incluir este módulo sem necessidade de refatoração.

---

## 10. Segurança e Privacidade

- **Dados armazenados localmente** – sem transmissão automática para a internet.
- **Criptografia em repouso** (AES) na camada de infraestrutura.
- **Sem coleta automática externa** – qualquer futura integração exigirá **opt‑in explícito** do usuário.
- **Transparência de decisões da IA** – logs de inferência e classificação.

---

## 11. Qualidade e Manutenibilidade

- **Código orientado a domínio** – domínio 100% independente de frameworks.
- **Alta coesão e baixo acoplamento** – separação estrita entre camadas.
- **Testes unitários obrigatórios** para todas as regras de negócio.
- **Testes de integração** para repositórios e serviços de infraestrutura.
- **Logs técnicos e funcionais** estruturados.

---

## 12. Evolução Planejada

| Versão     | Principais Entregas                                                                             |
|------------|-------------------------------------------------------------------------------------------------|
| **0.1.x**  | Base arquitetural, domínio consolidado, persistência local, primeiro caso de uso.               |
| **0.2.x**  | Casos de uso centrais (criação de mês financeiro, registro de despesas/receitas, parcelamento). |
| **0.3.x**  | Módulo de relatórios e read model otimizado (queries & projections).                            |
| **0.4.x**  | Assistente de IA (integração com provedores).                                                   |
| **1.0.0**  | Primeira versão estável com interface desktop funcional (JavaFX/Compose).                       |
| **Futuro** | Sincronização em nuvem, módulos premium, API pública, expansão multiplataforma.                 |

---

## 13. Considerações Finais

O **Finance Core** é projetado para ser:

- **Simples para o usuário** – entrada natural, visual limpo, baixa fricção.
- **Robusto para o desenvolvedor** – código testável, modular, aderente às melhores práticas.
- **Sustentável como produto** – preparado para monetização e evolução contínua.
- **Extensível como plataforma** – capaz de receber novos módulos sem refatoração dolorosa.

---

**Documentos relacionados:**  
- [02-requisitos.md](02-requisitos.md)  
- [03-casos-de-uso.md](03-casos-de-uso.md)  
- [04-modelo-dominio.md](04-modelo-dominio.md)  
- [05-regras-negocio.md](05-regras-negocio.md)  
- [06-arquitetura.md](06-arquitetura.md)  
- [07-modelo-dados.md](07-modelo-dados.md)  
- [08-migracoes.md](08-migracoes.md)  
- [09-testes.md](09-testes.md)  
- [10-contribuicao.md](10-contribuicao.md)