# Modelo de Domínio – Finance Core

---

## 1. Objetivo

Este documento descreve o **Modelo de Domínio** do sistema **Finance Core**, representando conceitualmente as entidades centrais do negócio, seus atributos, relacionamentos e responsabilidades. O objetivo é estabelecer uma **base sólida e consistente** para as próximas etapas do projeto (regras de negócio, arquitetura e implementação), garantindo alinhamento absoluto com os **requisitos funcionais e não funcionais já definidos**.

Este modelo é **independente de tecnologia**, banco de dados ou frameworks, focando exclusivamente na **lógica do domínio financeiro pessoal**.

---

## 2. Princípios Adotados

- Separação clara entre **domínio** e **infraestrutura**.
- Modelo orientado a comportamento e significado, não a tabelas.
- Linguagem ubíqua consistente com requisitos e casos de uso.
- Preparação para evolução futura (IA, novos módulos, exportações, nuvem).

---

## 3. Visão Geral do Domínio

O Finance Core é um sistema de **gestão financeira pessoal**, estruturado em torno de ciclos mensais, onde receitas, despesas e compromissos financeiros são registrados, classificados, analisados e consolidados.

O domínio pode ser organizado nos seguintes **subdomínios conceituais**:

- Identidade do Usuário
- Ciclo Financeiro Mensal
- Receitas
- Despesas
- Parcelamentos
- Classificação Financeira
- Relatórios e Consolidação
- Preferências do Sistema

---

## 4. Entidades do Domínio

### 4.1 Usuário

| Atributo          | Tipo         | Descrição                                  |
|-------------------|--------------|--------------------------------------------|
| `id`              | UUID         | Identificador único.                      |
| `nome`            | String       | Nome do usuário.                          |
| `dataCriacao`     | LocalDateTime| Data e hora de criação do perfil.         |
| `preferencias`    | PreferenciasSistema | Configurações personalizadas.       |

**Responsabilidades:**
- Ser o agregador raiz dos dados financeiros.
- Manter preferências do sistema.
- Iniciar e encerrar ciclos financeiros.

**Relacionamentos:**
- Possui um ou mais `MêsFinanceiro`.
- Possui uma `PreferenciasSistema`.

---

### 4.2 PreferenciasSistema

| Atributo              | Tipo    | Descrição                                     |
|-----------------------|---------|-----------------------------------------------|
| `tema`                | Enum    | CLARO / ESCURO.                              |
| `idioma`              | String  | Código do idioma (ex.: pt-BR).              |
| `notificacoesAtivas`  | boolean | Indica se notificações estão habilitadas.    |

**Responsabilidades:**
- Armazenar preferências visuais e comportamentais.
- Influenciar a experiência do usuário sem alterar regras de negócio.

**Relacionamentos:**
- Pertence a um `Usuário`.

---

### 4.3 MêsFinanceiro

| Atributo          | Tipo          | Descrição                                   |
|-------------------|---------------|---------------------------------------------|
| `id`              | UUID          | Identificador único.                       |
| `mes`             | int (1–12)    | Mês do ano.                                |
| `ano`             | int (≥2000)   | Ano.                                       |
| `status`          | Enum          | ABERTO / FECHADO.                         |
| `saldoInicial`    | BigDecimal    | Saldo no início do período.               |
| `saldoFinal`      | BigDecimal    | Saldo calculado no fechamento.            |

**Responsabilidades:**
- Agregar receitas e despesas.
- Calcular saldo mensal.
- Controlar estado (aberto/fechado).
- Servir de base para relatórios e análises.

**Relacionamentos:**
- Pertence a um `Usuário`.
- Possui várias `Receita`.
- Possui várias `Despesa`.
- Pode conter `Parcela`.

---

### 4.4 Receita

| Atributo          | Tipo         | Descrição                                |
|-------------------|--------------|------------------------------------------|
| `id`              | UUID         | Identificador único.                    |
| `descricao`       | String       | Descrição da receita.                  |
| `valor`           | BigDecimal   | Valor positivo.                        |
| `tipo`            | Enum         | FIXA / VARIÁVEL.                       |
| `dataReferencia`  | LocalDate    | Data de ocorrência.                   |

**Responsabilidades:**
- Impactar positivamente o saldo mensal.
- Fornecer dados para análise de renda.

**Relacionamentos:**
- Pertence a um `MêsFinanceiro`.

---

### 4.5 Despesa

| Atributo          | Tipo         | Descrição                                |
|-------------------|--------------|------------------------------------------|
| `id`              | UUID         | Identificador único.                    |
| `descricao`       | String       | Descrição da despesa.                  |
| `valor`           | BigDecimal   | Valor positivo.                        |
| `data`            | LocalDate    | Data da despesa.                       |
| `tipo`            | Enum         | PONTUAL / FIXA / PARCELADA.            |
| `metodoPagamento` | String       | Forma de pagamento.                    |

**Responsabilidades:**
- Impactar negativamente o saldo mensal.
- Ser classificada e analisada.
- Participar de relatórios e auditorias.

**Relacionamentos:**
- Pertence a um `MêsFinanceiro`.
- Pode possuir uma `Categoria`.
- Pode estar associada a um `Parcelamento`.

---

### 4.6 Categoria

| Atributo      | Tipo     | Descrição                               |
|---------------|----------|-----------------------------------------|
| `id`          | UUID     | Identificador único.                   |
| `nome`        | String   | Nome da categoria.                    |
| `essencial`   | boolean  | Indica se os gastos são essenciais.   |

**Responsabilidades:**
- Agrupar despesas para análise.
- Permitir distinção entre gastos essenciais e não essenciais.

**Relacionamentos:**
- Associada a várias `Despesa`.

---

### 4.7 Parcelamento

| Atributo          | Tipo         | Descrição                                |
|-------------------|--------------|------------------------------------------|
| `id`              | UUID         | Identificador único.                    |
| `despesaId`       | UUID         | Referência à despesa origem.            |
| `valorTotal`      | BigDecimal   | Valor total da compra.                 |
| `numeroParcelas`  | int (>1)     | Quantidade total de parcelas.          |
| `parcelaAtual`    | int          | Número da parcela atual.               |

**Responsabilidades:**
- Controlar número total de parcelas.
- Distribuir impacto financeiro ao longo do tempo.
- Garantir consistência entre parcelas e despesa original.

**Relacionamentos:**
- Origina várias `Parcela`.
- Associado a uma `Despesa` principal.

---

### 4.8 Parcela

| Atributo          | Tipo         | Descrição                                |
|-------------------|--------------|------------------------------------------|
| `id`              | UUID         | Identificador único.                    |
| `numero`          | int          | Número da parcela (1‑based).           |
| `valor`           | BigDecimal   | Valor da parcela.                      |
| `status`          | Enum         | PAGA / PENDENTE.                       |

**Responsabilidades:**
- Impactar o mês financeiro correspondente.
- Manter vínculo com o parcelamento original.

**Relacionamentos:**
- Pertence a um `Parcelamento`.
- Associada a um `MêsFinanceiro`.

---

### 4.9 RelatórioFinanceiro

| Atributo          | Tipo         | Descrição                                |
|-------------------|--------------|------------------------------------------|
| `id`              | UUID         | Identificador único.                    |
| `periodoInicio`   | LocalDate    | Início do período consolidado.          |
| `periodoFim`      | LocalDate    | Fim do período consolidado.            |
| `dataGeracao`     | LocalDateTime| Data/hora da geração.                  |

**Responsabilidades:**
- Consolidar informações de um ou mais meses.
- Servir de base para exportação e visualização.

**Relacionamentos:**
- Consome dados de `MêsFinanceiro`.

---

## 5. Resumo dos Relacionamentos Conceituais

| Entidade A       | Cardinalidade | Entidade B         | Descrição                              |
|------------------|---------------|--------------------|----------------------------------------|
| Usuário          | 1 ── *        | MêsFinanceiro      | Um usuário possui vários meses.        |
| MêsFinanceiro    | 1 ── *        | Receita            | Um mês contém várias receitas.         |
| MêsFinanceiro    | 1 ── *        | Despesa            | Um mês contém várias despesas.         |
| Despesa          | * ── 1        | Categoria          | Uma categoria pode estar em várias despesas. |
| Parcelamento     | 1 ── *        | Parcela            | Um parcelamento gera várias parcelas.  |
| Parcela          | * ── 1        | MêsFinanceiro      | Uma parcela impacta um único mês.      |
| MêsFinanceiro    | * ── 1        | RelatórioFinanceiro| Um relatório consome dados de um ou mais meses. |

---

## 6. Agregados do Domínio (Aggregate Roots)

Conforme princípios de **Domain-Driven Design**, definimos os seguintes agregados:

### 6.1 Agregado Usuário

- **Aggregate Root:** `Usuário`
- **Entidades incluídas:** `Usuário`, `PreferenciasSistema`
- **Justificativa:** O Usuário é o proprietário lógico de todos os dados financeiros. Preferências não têm identidade fora do contexto do usuário.
- **Responsabilidades:**
  - Garantir a existência e integridade das `PreferenciasSistema`.
  - Controlar o ciclo de vida dos `MêsFinanceiro` associados.

---

### 6.2 Agregado MêsFinanceiro

- **Aggregate Root:** `MêsFinanceiro`
- **Entidades incluídas:** `MêsFinanceiro`, `Receita`, `Despesa`, `Parcela`
- **Justificativa:** Núcleo do domínio. Toda movimentação financeira ocorre dentro de um contexto mensal; receitas, despesas e parcelas não possuem significado isolado.
- **Responsabilidades:**
  - Garantir consistência do saldo mensal.
  - Controlar estado (ABERTO/FECHADO).
  - Impedir alterações após fechamento.
  - Orquestrar impacto financeiro das receitas, despesas e parcelas.

---

### 6.3 Agregado Parcelamento

- **Aggregate Root:** `Parcelamento`
- **Entidades incluídas:** `Parcelamento`, `Parcela`
- **Justificativa:** Representa um compromisso financeiro distribuído no tempo, com regras próprias que independem de um único mês.
- **Responsabilidades:**
  - Garantir que o valor total seja corretamente distribuído.
  - Controlar a numeração e estado das parcelas.
  - Manter vínculo lógico com a despesa de origem.

---

### 6.4 Agregado Categoria

- **Aggregate Root:** `Categoria`
- **Entidades incluídas:** `Categoria`
- **Justificativa:** Entidade de referência, com identidade própria, reutilizável por múltiplas despesas.
- **Responsabilidades:**
  - Garantir consistência semântica da classificação financeira.

---

### 6.5 Agregado RelatórioFinanceiro

- **Aggregate Root:** `RelatórioFinanceiro`
- **Entidades incluídas:** `RelatórioFinanceiro`
- **Justificativa:** Relatórios não alteram estado do domínio; apenas consomem dados consolidados. Tratados como agregados independentes por clareza conceitual.

---

## 7. Invariantes do Domínio

As invariantes abaixo **devem ser sempre verdadeiras**, independentemente da implementação técnica.

### 7.1 Invariantes Gerais

- Todo dado financeiro pertence a **exatamente um Usuário**.
- Nenhuma entidade financeira (`Receita`, `Despesa`, `Parcela`) existe sem um **MêsFinanceiro associado**, direta ou indiretamente.

### 7.2 Invariantes do MêsFinanceiro

- Um `MêsFinanceiro` **FECHADO** não pode ser alterado.
- O saldo final é sempre calculado como:
  
  saldoFinal = saldoInicial + totalReceitas − totalDespesas − totalParcelas


- Receitas e despesas só podem ser registradas em meses **ABERTOS**.

### 7.3 Invariantes de Receita e Despesa

- Valores financeiros devem ser **positivos e maiores que zero**.
- Toda `Despesa` pode ter **no máximo uma Categoria**.
- Despesas do tipo `PARCELADA` devem estar associadas a um `Parcelamento` válido.

### 7.4 Invariantes de Parcelamento

- A soma do valor de todas as parcelas deve ser **igual ao `valorTotal`** do parcelamento.
- O número de parcelas geradas deve ser igual a **`numeroParcelas`**.
- Cada `Parcela` deve estar associada a **um único `MêsFinanceiro`**.

---

## 8. Diagrama Conceitual (Descrição Textual)

Usuário
├── PreferenciasSistema
└── MêsFinanceiro (1..)
├── Receita (0..)
├── Despesa (0..)
│ └── Categoria (0..1)
└── Parcela (0..)
└── Parcelamento (1)


- **Usuário** é o ponto de entrada do domínio.
- **MêsFinanceiro** agrega receitas, despesas e parcelas.
- **Despesa** pode estar associada a uma **Categoria** e pode originar um **Parcelamento**.
- **Parcelamento** origina múltiplas **Parcelas**, cada uma impactando um **MêsFinanceiro** específico.
- **RelatórioFinanceiro** consome dados de um ou mais **MêsFinanceiro** e não altera o estado do domínio.

Este diagrama reforça que **o tempo (mês)** é o eixo central do sistema, e não a despesa ou o pagamento isoladamente.

---

## 9. Observações Importantes

- Nenhuma entidade contém detalhes de persistência (anotações JPA, SQL etc.).
- Nenhuma decisão de banco de dados foi tomada neste documento – o modelo é puramente conceitual.
- O modelo está preparado para futura separação em módulos (ex.: FinanceConfig, FinanceFlow, FinanceAnalytics, FinanceAssistant).
- Este documento é **base obrigatória** para as próximas etapas: regras de negócio, arquitetura e implementação.

---

## 10. Encerramento

O **Modelo de Domínio do Finance Core** está agora **completo, fechado e consistente**, servindo como:

- base obrigatória para as Regras de Negócio (`05-regras-negocio.md`);
- referência canônica para os Casos de Uso (`03-casos-de-uso.md`);
- guia estrutural para as decisões arquiteturais (`06-arquitetura.md`) e de dados (`07-modelo-dados.md`).

---

**Documentos relacionados:**  
- [01-visao-geral.md](01-visao-geral.md)  
- [02-requisitos.md](02-requisitos.md)  
- [03-casos-de-uso.md](03-casos-de-uso.md)  
- [05-regras-negocio.md](05-regras-negocio.md)  
- [06-arquitetura.md](06-arquitetura.md)  
- [07-modelo-dados.md](07-modelo-dados.md)  
- [08-migracoes.md](08-migracoes.md)  
- [09-testes.md](09-testes.md)  
- [10-contribuicao.md](10-contribuicao.md)