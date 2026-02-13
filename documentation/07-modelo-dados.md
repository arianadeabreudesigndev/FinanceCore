# Modelo de Dados – Finance Core

---

## 1. Objetivo

Este documento descreve o **Modelo de Dados do sistema Finance Core**, estabelecendo a transição controlada entre:

- **Modelo Conceitual** – derivado diretamente do Modelo de Domínio (`04-modelo-dominio.md`).
- **Modelo Lógico** – estrutura de dados abstrata, independente de SGBD.
- **Modelo Físico** – implementação concreta para PostgreSQL, com decisões técnicas já consolidadas.

O objetivo é garantir:

- Fidelidade absoluta ao Modelo de Domínio e às Regras de Negócio.
- Base sólida para implementação da persistência.
- Rastreabilidade clara entre dados, regras e casos de uso.
- Preparação para evolução futura (sincronização, nuvem, novos módulos).

---

## 2. Princípios do Modelo de Dados

- Derivação direta do **Modelo de Domínio**.
- Nenhum atributo sem significado de negócio.
- Nenhuma tabela sem entidade correspondente.
- Nenhuma decisão física antecipada (no modelo lógico).
- Normalização lógica suficiente para evitar redundância conceitual.
- Preparação para persistência local e futura sincronização.

---

## 3. Modelo Conceitual de Dados

O modelo conceitual representa as entidades do domínio e seus relacionamentos, sem qualquer preocupação técnica.

### 3.1 Entidades Conceituais

| Entidade             | Descrição Resumida                                         |
|----------------------|------------------------------------------------------------|
| Usuário              | Proprietário dos dados financeiros.                        |
| PreferenciasSistema  | Configurações visuais e comportamentais do sistema.        |
| MêsFinanceiro        | Período mensal de agregação financeira (núcleo).           |
| Receita              | Entrada de valor financeiro.                               |
| Despesa              | Saída de valor financeiro.                                 |
| Categoria            | Classificação de despesas (essencial/não essencial).       |
| Parcelamento         | Compra parcelada distribuída no tempo.                     |
| Parcela              | Unidade mensal de um parcelamento.                         |
| RelatórioFinanceiro  | Visão consolidada de dados financeiros.                    |

### 3.2 Relacionamentos Conceituais

- Usuário **possui** PreferenciasSistema.
- Usuário **possui** um ou mais MêsFinanceiro.
- MêsFinanceiro **agrega** zero ou mais Receitas.
- MêsFinanceiro **agrega** zero ou mais Despesas.
- Despesa **pertence a** zero ou uma Categoria.
- Despesa **pode originar** zero ou um Parcelamento.
- Parcelamento **gera** uma ou mais Parcelas.
- Parcela **impacta** um MêsFinanceiro.
- RelatórioFinanceiro **consome** dados de um ou mais MêsFinanceiro.

### 3.3 Cardinalidades Conceituais

| Entidade A       | Cardinalidade | Entidade B          |
|------------------|---------------|---------------------|
| Usuário          | 1 ── 1        | PreferenciasSistema |
| Usuário          | 1 ── *        | MêsFinanceiro       |
| MêsFinanceiro    | 1 ── *        | Receita             |
| MêsFinanceiro    | 1 ── *        | Despesa             |
| Categoria        | 1 ── *        | Despesa             |
| Parcelamento     | 1 ── *        | Parcela             |
| MêsFinanceiro    | 1 ── *        | Parcela             |

---

## 4. Modelo Lógico de Dados (Independente de SGBD)

O modelo lógico traduz o modelo conceitual para estruturas de dados organizadas, ainda sem detalhes físicos.

### 4.1 Usuário

| Atributo        | Tipo Lógico   | Descrição                         |
|-----------------|---------------|-----------------------------------|
| usuario_id      | Identificador | Chave primária.                  |
| nome            | Texto (120)   | Nome do usuário.                |
| data_criacao    | Data/Hora     | Momento da criação do perfil.    |

**Relacionamentos:**  
- 1:1 com PreferenciasSistema  
- 1:N com MêsFinanceiro

---

### 4.2 PreferenciasSistema

| Atributo             | Tipo Lógico   | Descrição                                 |
|----------------------|---------------|-------------------------------------------|
| preferencias_id      | Identificador | Chave primária.                          |
| usuario_id           | Identificador | Chave estrangeira para Usuário.          |
| tema                 | Texto (10)    | CLARO / ESCURO.                          |
| idioma               | Texto (10)    | Ex.: pt-BR, en-US.                      |
| notificacoes_ativas  | Booleano      | Indica se notificações estão habilitadas. |

**Restrições:**  
- Exatamente uma instância por Usuário (RN-11).

---

### 4.3 MesFinanceiro

| Atributo           | Tipo Lógico   | Descrição                               |
|--------------------|---------------|-----------------------------------------|
| mes_financeiro_id  | Identificador | Chave primária.                        |
| usuario_id         | Identificador | Chave estrangeira para Usuário.        |
| mes                | Inteiro (1–12)| Mês do ano.                            |
| ano                | Inteiro (≥2000)| Ano.                                   |
| status             | Texto (10)    | ABERTO / FECHADO.                      |
| saldo_inicial      | Monetário     | Saldo no início do período.            |
| saldo_final        | Monetário     | Saldo calculado no fechamento.         |

**Restrições Lógicas:**  
- Apenas um mês ABERTO por período (RN-01).  
- Imutável quando FECHADO (RN-02).

---

### 4.4 Receita

| Atributo           | Tipo Lógico   | Descrição                            |
|--------------------|---------------|--------------------------------------|
| receita_id         | Identificador | Chave primária.                     |
| mes_financeiro_id  | Identificador | Chave estrangeira para MêsFinanceiro. |
| descricao          | Texto (255)   | Descrição da receita.               |
| valor              | Monetário     | Valor positivo.                     |
| tipo               | Texto (10)    | FIXA / VARIAVEL.                   |
| data_referencia    | Data          | Data de ocorrência.                |

**Regras Associadas:**  
- Obrigatoriamente associada a um MêsFinanceiro (RN-03).

---

### 4.5 Despesa

| Atributo           | Tipo Lógico   | Descrição                            |
|--------------------|---------------|--------------------------------------|
| despesa_id         | Identificador | Chave primária.                     |
| mes_financeiro_id  | Identificador | Chave estrangeira para MêsFinanceiro. |
| categoria_id       | Identificador | Chave estrangeira para Categoria (pode ser nula temporariamente). |
| descricao          | Texto (255)   | Descrição da despesa.               |
| valor              | Monetário     | Valor positivo.                     |
| data               | Data          | Data da despesa.                   |
| tipo               | Texto (15)    | PONTUAL / FIXA / PARCELADA.        |
| metodo_pagamento   | Texto (50)    | Forma de pagamento.                |

**Regras Associadas:**  
- Classificação obrigatória (RN-04).  
- Imutável se o mês estiver fechado (RN-02).

---

### 4.6 Categoria

| Atributo     | Tipo Lógico   | Descrição                           |
|--------------|---------------|-------------------------------------|
| categoria_id | Identificador | Chave primária.                    |
| nome         | Texto (100)   | Nome da categoria.                |
| essencial    | Booleano      | Verdadeiro se os gastos são essenciais. |

---

### 4.7 Parcelamento

| Atributo         | Tipo Lógico   | Descrição                               |
|------------------|---------------|-----------------------------------------|
| parcelamento_id  | Identificador | Chave primária.                        |
| despesa_id       | Identificador | Chave estrangeira para Despesa.        |
| valor_total      | Monetário     | Valor total da compra.                |
| numero_parcelas  | Inteiro (>1)  | Quantidade total de parcelas.         |
| parcela_atual    | Inteiro       | Número da parcela atual (default 1).  |

**Restrições:**  
- Uma despesa parcelada gera exatamente um Parcelamento (RN-06).

---

### 4.8 Parcela

| Atributo           | Tipo Lógico   | Descrição                            |
|--------------------|---------------|--------------------------------------|
| parcela_id         | Identificador | Chave primária.                     |
| parcelamento_id    | Identificador | Chave estrangeira para Parcelamento. |
| mes_financeiro_id  | Identificador | Chave estrangeira para MêsFinanceiro. |
| numero             | Inteiro       | Número da parcela (1‑based).        |
| valor              | Monetário     | Valor da parcela.                  |
| status             | Texto (10)    | PAGA / PENDENTE.                   |

**Regras Associadas:**  
- Gerada automaticamente pelo parcelamento.  
- Não editável individualmente (RN-06).

---

### 4.9 RelatorioFinanceiro

| Atributo        | Tipo Lógico   | Descrição                            |
|-----------------|---------------|--------------------------------------|
| relatorio_id    | Identificador | Chave primária.                     |
| periodo_inicio  | Data          | Início do período consolidado.      |
| periodo_fim     | Data          | Fim do período consolidado.         |
| data_geracao    | Data/Hora     | Momento da geração do relatório.    |

**Observação:**  
Não armazena valores financeiros primários – apenas metadados de consolidação.

---

### 4.10 HistoricoFinanceiro (Tabela de Consolidação)

*Definida no modelo físico para otimização de consultas.*

| Atributo         | Tipo Lógico   | Descrição                            |
|------------------|---------------|--------------------------------------|
| historico_id     | Identificador | Chave primária.                     |
| usuario_id       | Identificador | Chave estrangeira para Usuário.     |
| periodo_inicio   | Data          | Início do período.                  |
| periodo_fim      | Data          | Fim do período.                     |
| total_receitas   | Monetário     | Soma das receitas no período.       |
| total_despesas   | Monetário     | Soma das despesas no período.       |
| saldo_final      | Monetário     | Saldo consolidado.                  |
| data_geracao     | Data/Hora     | Momento da consolidação.            |

---

## 5. Normalização Lógica

O modelo lógico respeita:

- **1ª Forma Normal (1FN):** todos os atributos são atômicos.
- **2ª Forma Normal (2FN):** dependência total da chave primária.
- **3ª Forma Normal (3FN):** ausência de dependências transitivas.

Nenhuma otimização física (desnormalização, índices, particionamento) foi aplicada neste estágio.

---

## 6. Modelo Físico – PostgreSQL

Com base na decisão arquitetural (ADR-02, ADR-03), adota‑se **PostgreSQL 16** como SGBD padrão, pelas seguintes justificativas:

- Tipos de dados ricos: `UUID`, `NUMERIC(14,2)`, `JSONB` (para futuras extensões).
- Forte integridade referencial e suporte a constraints complexas.
- Excelente desempenho para aplicações desktop locais e preparação para migração futura para nuvem.
- Ideal para auditoria e histórico financeiro devido à imutabilidade controlada.

### 6.1 Princípios do Modelo Físico

- **Integridade > Conveniência** – todas as relações são garantidas por chaves estrangeiras.
- Nenhum saldo é editável manualmente – apenas calculado.
- Histórico financeiro **imutável** após consolidação.
- Preparado para consolidação, auditoria, múltiplos perfis e provedores de IA.

### 6.2 Convenções Físicas

| Convenção          | Especificação                                  |
|--------------------|------------------------------------------------|
| Nomes de tabelas   | `snake_case`, singular.                       |
| Chaves primárias   | `UUID` com geração automática (`uuid-ossp`).  |
| Chaves estrangeiras| Nome da tabela referenciada + `_id`.          |
| Datas              | `TIMESTAMP` (com ou sem fuso) e `DATE`.       |
| Valores monetários | `NUMERIC(14,2)`.                              |
| Soft delete        | Não utilizado por padrão; remoção física com `ON DELETE CASCADE` ou `RESTRICT`. |
| Criptografia       | Aplicada na camada de infraestrutura, fora do banco. |

### 6.3 Extensão Obrigatória

```sql
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

### 6.4 Estrutura Física das Tabelas
### 6.4.1 Usuário

    CREATE TABLE usuario (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nome VARCHAR(120) NOT NULL,
    data_criacao TIMESTAMP NOT NULL DEFAULT NOW()
    );

Observação: Dados sensíveis (autenticação) não são armazenados nesta tabela – podem evoluir futuramente sem quebrar o domínio.

### 6.4.2 Preferências do Sistema

    CREATE TABLE preferencias_sistema (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    usuario_id UUID NOT NULL,
    tema VARCHAR(10) NOT NULL CHECK (tema IN ('CLARO', 'ESCURO')),
    idioma VARCHAR(10) NOT NULL DEFAULT 'pt-BR',
    notificacoes_ativas BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_pref_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuario(id)
        ON DELETE CASCADE
    );

### 6.4.3 Mês Financeiro

    CREATE TABLE mes_financeiro (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    usuario_id UUID NOT NULL,
    mes INTEGER NOT NULL CHECK (mes BETWEEN 1 AND 12),
    ano INTEGER NOT NULL CHECK (ano >= 2000),
    status VARCHAR(10) NOT NULL CHECK (status IN ('ABERTO', 'FECHADO')),
    saldo_inicial NUMERIC(14,2) NOT NULL,
    saldo_final NUMERIC(14,2),
    CONSTRAINT uq_mes_usuario UNIQUE (usuario_id, mes, ano),
    CONSTRAINT fk_mes_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuario(id)
        ON DELETE CASCADE
    );

Garantias:

RN-01 (único mês ABERTO por período) e RN-02 (imutabilidade após fechamento) são reforçadas por esta estrutura.

### 6.4.4 Receita

    CREATE TABLE receita (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    mes_financeiro_id UUID NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    valor NUMERIC(14,2) NOT NULL CHECK (valor > 0),
    tipo VARCHAR(10) NOT NULL CHECK (tipo IN ('FIXA', 'VARIAVEL')),
    data_referencia DATE NOT NULL,
    CONSTRAINT fk_receita_mes
        FOREIGN KEY (mes_financeiro_id)
        REFERENCES mes_financeiro(id)
        ON DELETE RESTRICT
    );

### 6.4.5 Categoria

    CREATE TABLE categoria (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nome VARCHAR(100) NOT NULL,
    essencial BOOLEAN NOT NULL
    );

### 6.4.6 Despesa

    CREATE TABLE despesa (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    mes_financeiro_id UUID NOT NULL,
    categoria_id UUID,
    descricao VARCHAR(255) NOT NULL,
    valor NUMERIC(14,2) NOT NULL CHECK (valor > 0),
    data DATE NOT NULL,
    tipo VARCHAR(15) NOT NULL CHECK (tipo IN ('PONTUAL', 'FIXA', 'PARCELADA')),
    metodo_pagamento VARCHAR(50) NOT NULL,
    CONSTRAINT fk_despesa_mes
        FOREIGN KEY (mes_financeiro_id)
        REFERENCES mes_financeiro(id)
        ON DELETE RESTRICT,
    CONSTRAINT fk_despesa_categoria
        FOREIGN KEY (categoria_id)
        REFERENCES categoria(id)
    );

Nota: categoria_id pode ser NULL temporariamente, mas a RN-04 exige que toda despesa seja classificada; o sistema deve auditar e solicitar correção.

### 6.4.7 Parcelamento

    CREATE TABLE parcelamento (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    despesa_id UUID NOT NULL,
    valor_total NUMERIC(14,2) NOT NULL CHECK (valor_total > 0),
    numero_parcelas INTEGER NOT NULL CHECK (numero_parcelas > 1),
    parcela_atual INTEGER NOT NULL DEFAULT 1,
    CONSTRAINT fk_parcelamento_despesa
        FOREIGN KEY (despesa_id)
        REFERENCES despesa(id)
        ON DELETE CASCADE
    );

### 6.4.8 Parcela

    CREATE TABLE parcela (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    parcelamento_id UUID NOT NULL,
    mes_financeiro_id UUID NOT NULL,
    numero INTEGER NOT NULL,
    valor NUMERIC(14,2) NOT NULL CHECK (valor > 0),
    status VARCHAR(10) NOT NULL CHECK (status IN ('PAGA', 'PENDENTE')),
    CONSTRAINT fk_parcela_parcelamento
        FOREIGN KEY (parcelamento_id)
        REFERENCES parcelamento(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_parcela_mes
        FOREIGN KEY (mes_financeiro_id)
        REFERENCES mes_financeiro(id)
        ON DELETE RESTRICT
    );

### 6.4.9 Histórico Financeiro Consolidado

    CREATE TABLE historico_financeiro (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    usuario_id UUID NOT NULL,
    periodo_inicio DATE NOT NULL,
    periodo_fim DATE NOT NULL,
    total_receitas NUMERIC(14,2) NOT NULL,
    total_despesas NUMERIC(14,2) NOT NULL,
    saldo_final NUMERIC(14,2) NOT NULL,
    data_geracao TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_historico_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuario(id)
        ON DELETE CASCADE
    );

Esta tabela é uma projeção otimizada para leitura, não faz parte do modelo de domínio, mas sim do Read Model (13-queries-projections.md).

### 6.5 Índices Estratégicos

Para garantir performance mesmo com bases de dados volumosas, os seguintes índices são obrigatórios:

    -- Filtros por usuário e status (dashboards)
CREATE INDEX idx_mes_usuario_status
    ON mes_financeiro (usuario_id, status);

-- Consultas de despesas por mês (relatórios, fechamento)
CREATE INDEX idx_despesa_mes
    ON despesa (mes_financeiro_id);

-- Consultas de parcelas por mês (cálculo de saldo)
CREATE INDEX idx_parcela_mes
    ON parcela (mes_financeiro_id);

Índices adicionais podem ser criados conforme a evolução dos padrões de acesso.

---

## 7. Queries Fundamentais (Uso Real)
As consultas abaixo representam operações críticas e devem ser implementadas com alta performance.

###7.1 Saldo Mensal Determinístico (RN-07)

SELECT
    m.id,
    COALESCE(SUM(r.valor), 0) - COALESCE(SUM(d.valor), 0) AS saldo
FROM mes_financeiro m
LEFT JOIN receita r ON r.mes_financeiro_id = m.id
LEFT JOIN despesa d ON d.mes_financeiro_id = m.id
WHERE m.id = :mesId
GROUP BY m.id;

### 7.2 Gastos Não Essenciais (Auditoria)

SELECT d.*
FROM despesa d
JOIN categoria c ON c.id = d.categoria_id
WHERE c.essencial = FALSE
  AND d.mes_financeiro_id = :mesId;

---

## 8. Rastreabilidade com o Domínio

|Entidade de Domínio      | Tabela Física
|-------------------------|---------------|
|Usuario                  | usuario
|PreferenciasSistema      | preferencias_sistema
|MêsFinanceiro	          | mes_financeiro
|Receita	              | receita
|Despesa	              | despesa
|Categoria	              | categoria
|Parcelamento	          | parcelamento
|Parcela	              | parcela
|RelatórioFinanceiro      | não há tabela dedicada (dados são lidos das demais tabelas e consolidados em historico_financeiro).

---

## 9. Observações Finais

O modelo físico aqui descrito é a fonte da verdade para a implementação da persistência.

As migrações de banco de dados serão gerenciadas pelo Flyway, seguindo a política definida em 08-migracoes.md.

A criptografia de dados sensíveis é responsabilidade da camada de infraestrutura, não do banco de dados.

Este documento substitui e consolida as versões anteriores (modelo conceitual + lógico + decisão técnica), servindo como referência única para a equipe de desenvolvimento.

---

**Documentos relacionados:**  
- [01-visao-geral.md](01-visao-geral.md)  
- [02-requisitos.md](02-requisitos.md)  
- [03-casos-de-uso.md](03-casos-de-uso.md)  
- [04-modelo-dominio.md](04-modelo-dominio.md)  
- [05-regras-negocio.md](05-regras-negocio.md)  
- [06-arquitetura.md](06-arquitetura.md)  
- [08-migracoes.md](08-migracoes.md)  
- [09-testes.md](09-testes.md)  
- [10-contribuicao.md](10-contribuicao.md)