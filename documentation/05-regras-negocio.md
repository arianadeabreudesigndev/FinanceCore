# Regras de Negócio – Finance Core

---

## 1. Objetivo

Este documento descreve as **Regras de Negócio (RN)** do sistema **Finance Core**. As regras aqui definidas representam **restrições, invariantes e comportamentos obrigatórios do domínio**, independentemente de tecnologia, interface ou implementação.

As regras de negócio:

- Derivam diretamente do **Modelo de Domínio** (`04-modelo-dominio.md`).
- Garantem consistência lógica e financeira.
- Servem como base para validações, arquitetura, persistência e APIs.
- **Não descrevem interface** – apenas comportamento do domínio.

---

## 2. Convenções

- **RN-XX**: Identificador único da regra de negócio.
- As regras são **imutáveis por padrão**; qualquer alteração exige revisão formal do domínio.
- Cada regra referencia explicitamente:
  - Entidades impactadas.
  - Casos de uso relacionados (documentados em `03-casos-de-uso.md`).

---

## 3. Regras de Negócio

---

### RN-01 – Existência de Mês Financeiro Ativo

| Campo               | Descrição                                                                                             |
|---------------------|-------------------------------------------------------------------------------------------------------|
| **Descrição**       | O sistema deve garantir que exista **no máximo um Mês Financeiro em estado ABERTO por usuário para um mesmo mês/ano**. |
| **Justificativa**   | Evita sobreposição de lançamentos financeiros e inconsistências de consolidação.                     |
| **Entidades**       | `Usuário`, `MêsFinanceiro`                                                                           |
| **Casos de Uso**    | UC-14 (Criar Mês Financeiro), UC-04 (Registrar Despesa)                                             |

---

### RN-02 – Imutabilidade de Mês Financeiro Fechado

| Campo               | Descrição                                                                                             |
|---------------------|-------------------------------------------------------------------------------------------------------|
| **Descrição**       | Um Mês Financeiro em estado **FECHADO** não pode sofrer alterações em receitas, despesas, parcelas ou saldos. |
| **Exceção**         | Correções só podem ocorrer por ajustes compensatórios em meses posteriores ou reabertura explícita (caso de uso futuro). |
| **Entidades**       | `MêsFinanceiro`, `Receita`, `Despesa`, `Parcela`                                                     |
| **Casos de Uso**    | UC-08 (Fechar Mês Financeiro)                                                                        |

---

### RN-03 – Associação Obrigatória ao Mês Financeiro

| Campo               | Descrição                                                                                             |
|---------------------|-------------------------------------------------------------------------------------------------------|
| **Descrição**       | Toda `Receita`, `Despesa` ou `Parcela` deve estar associada **obrigatoriamente** a um único `MêsFinanceiro`. |
| **Justificativa**   | Garante rastreabilidade temporal e consolidação correta dos dados financeiros.                       |
| **Entidades**       | `Receita`, `Despesa`, `Parcela`, `MêsFinanceiro`                                                     |
| **Casos de Uso**    | UC-04 (Registrar Despesa), UC-07 (Gerenciar Parcelamentos)                                          |

---

### RN-04 – Classificação Obrigatória de Despesas

| Campo               | Descrição                                                                                             |
|---------------------|-------------------------------------------------------------------------------------------------------|
| **Descrição**       | Toda `Despesa` deve estar associada a **uma `Categoria` válida**.                                    |
| **Observação**      | A informação de essencialidade da despesa é **derivada da Categoria**, não da própria despesa.       |
| **Entidades**       | `Despesa`, `Categoria`                                                                               |
| **Casos de Uso**    | UC-06 (Classificar Despesa)                                                                          |

---

### RN-05 – Comportamento de Despesas Fixas

| Campo               | Descrição                                                                                             |
|---------------------|-------------------------------------------------------------------------------------------------------|
| **Descrição**       | Despesas do tipo **FIXA** devem ser replicadas automaticamente para Meses Financeiros futuros enquanto o mês estiver **ABERTO**. |
| **Restrições**      | Não podem ser replicadas para meses fechados; a replicação não altera valores históricos.            |
| **Entidades**       | `Despesa`, `MêsFinanceiro`                                                                           |
| **Casos de Uso**    | UC-05 (Gerenciar Despesas Fixas)                                                                    |

---

### RN-06 – Parcelamento como Compromisso Temporal

| Campo               | Descrição                                                                                             |
|---------------------|-------------------------------------------------------------------------------------------------------|
| **Descrição**       | Uma `Despesa` do tipo **PARCELADA** deve originar um `Parcelamento`, que gera `Parcela`s distribuídas em Meses Financeiros futuros. |
| **Restrições**      | Parcelas não podem ser alteradas individualmente; alterações ocorrem apenas no nível do `Parcelamento`. |
| **Entidades**       | `Despesa`, `Parcelamento`, `Parcela`, `MêsFinanceiro`                                               |
| **Casos de Uso**    | UC-07 (Gerenciar Parcelamentos)                                                                     |

---

### RN-07 – Cálculo Determinístico de Saldo Mensal

| Campo               | Descrição                                                                                             |
|---------------------|-------------------------------------------------------------------------------------------------------|
| **Descrição**       | O saldo de um `MêsFinanceiro` deve ser sempre calculado como:                                        |
|                     | `Saldo Final = Saldo Inicial + Total Receitas – Total Despesas – Total Parcelas`                     |
|                     | O saldo **não pode ser editado manualmente**.                                                        |
| **Entidades**       | `MêsFinanceiro`                                                                                      |
| **Casos de Uso**    | UC-08 (Fechar Mês Financeiro)                                                                        |

---

### RN-08 – Detecção de Divergência Financeira

| Campo               | Descrição                                                                                             |
|---------------------|-------------------------------------------------------------------------------------------------------|
| **Descrição**       | Quando houver divergência entre o saldo calculado pelo sistema e um saldo informado externamente pelo usuário, o sistema deve: |
|                     | - Registrar a divergência;                                                                            |
|                     | - Solicitar confirmação ou correção.                                                                 |
| **Entidades**       | `MêsFinanceiro`                                                                                      |
| **Casos de Uso**    | UC-15 (Tratar Divergência Financeira)                                                               |

---

### RN-09 – Consolidação Histórica de Meses Fechados

| Campo               | Descrição                                                                                             |
|---------------------|-------------------------------------------------------------------------------------------------------|
| **Descrição**       | Meses Financeiros fechados podem ser consolidados em registros históricos resumidos, preservando totais, indicadores e referência temporal. |
| **Entidades**       | `MêsFinanceiro`, `RelatórioFinanceiro`                                                               |
| **Casos de Uso**    | UC-10 (Gerar Relatórios Financeiros)                                                                |

---

### RN-10 – Isolamento Total de Dados por Usuário

| Campo               | Descrição                                                                                             |
|---------------------|-------------------------------------------------------------------------------------------------------|
| **Descrição**       | Os dados financeiros de um `Usuário` são totalmente isolados e não podem ser acessados ou inferidos por outro `Usuário`. |
| **Entidades**       | `Usuário`                                                                                            |
| **Casos de Uso**    | UC-01 (Cadastrar Usuário)                                                                           |

---

### RN-11 – Persistência de Preferências do Sistema

| Campo               | Descrição                                                                                             |
|---------------------|-------------------------------------------------------------------------------------------------------|
| **Descrição**       | As preferências do sistema definidas pelo `Usuário` devem ser persistidas e reaplicadas automaticamente em novas sessões. |
| **Entidades**       | `Usuário`, `PreferenciasSistema`                                                                     |
| **Casos de Uso**    | UC-13 (Configurar Preferências do Sistema)                                                          |

---

## 4. Observações Finais

As Regras de Negócio aqui descritas são **normativas** e devem ser respeitadas em qualquer decisão técnica futura. Nenhuma otimização, refatoração ou escolha de stack pode violar essas regras.

Este documento serve como **ponte direta** entre o Modelo de Domínio (`04-modelo-dominio.md`) e a Arquitetura do Sistema (`06-arquitetura.md`).

---

**Documentos relacionados:**  
- [01-visao-geral.md](01-visao-geral.md)  
- [02-requisitos.md](02-requisitos.md)  
- [03-casos-de-uso.md](03-casos-de-uso.md)  
- [04-modelo-dominio.md](04-modelo-dominio.md)  
- [06-arquitetura.md](06-arquitetura.md)  
- [07-modelo-dados.md](07-modelo-dados.md)  
- [08-migracoes.md](08-migracoes.md)  
- [09-testes.md](09-testes.md)  
- [10-contribuicao.md](10-contribuicao.md)