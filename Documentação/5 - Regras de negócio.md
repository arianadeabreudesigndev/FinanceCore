# Regras de Negócio — Finance Core

## 1. Objetivo do Documento

Este documento descreve as **Regras de Negócio (RN)** do sistema **Finance Core**. As regras aqui definidas representam **restrições, invariantes e comportamentos obrigatórios do domínio**, independentemente de tecnologia, interface ou implementação.

As regras de negócio:

- derivam diretamente do **Modelo de Domínio**;
    
- garantem consistência lógica e financeira;
    
- servem como base para validações, arquitetura, persistência e APIs;
    
- **não descrevem interface**, apenas comportamento do domínio.
    

---

## 2. Convenções

- **RN-XX**: Identificador da Regra de Negócio
    
- As regras são **imutáveis por padrão** e só podem ser alteradas mediante revisão formal do domínio.
    
- Cada regra referencia explicitamente:
    
    - entidades impactadas;
        
    - casos de uso relacionados.
        

---

## 3. Regras de Negócio

---

### RN-01 — Existência de Mês Financeiro Ativo

**Descrição:**  
O sistema deve garantir que exista **no máximo um Mês Financeiro em estado ABERTO por usuário para um mesmo mês/ano**.

**Justificativa:**  
Evita sobreposição de lançamentos financeiros e inconsistências de consolidação.

**Entidades Impactadas:**

- Usuário
    
- MêsFinanceiro
    

**Casos de Uso Relacionados:**

- UC-14 – Criar Mês Financeiro
    
- UC-04 – Registrar Despesa
    

---

### RN-02 — Imutabilidade de Mês Financeiro Fechado

**Descrição:**  
Um Mês Financeiro em estado **FECHADO** não pode sofrer alterações em:

- receitas;
    
- despesas;
    
- parcelas;
    
- saldos.
    

**Exceção Controlada:**  
Correções só podem ocorrer por:

- ajustes compensatórios em meses posteriores;
    
- reabertura explícita do mês (caso de uso futuro).
    

**Entidades Impactadas:**

- MêsFinanceiro
    
- Receita
    
- Despesa
    
- Parcela
    

**Casos de Uso Relacionados:**

- UC-08 – Fechar Mês Financeiro
    

---

### RN-03 — Associação Obrigatória ao Mês Financeiro

**Descrição:**  
Toda Receita, Despesa ou Parcela deve estar associada **obrigatoriamente** a um único Mês Financeiro.

**Justificativa:**  
Garante rastreabilidade temporal e consolidação correta dos dados financeiros.

**Entidades Impactadas:**

- Receita
    
- Despesa
    
- Parcela
    
- MêsFinanceiro
    

**Casos de Uso Relacionados:**

- UC-04 – Registrar Despesa
    
- UC-07 – Gerenciar Parcelamentos
    

---

### RN-04 — Classificação Obrigatória de Despesas

**Descrição:**  
Toda Despesa deve estar associada a **uma Categoria válida**.

A informação de essencialidade da despesa é **derivada da Categoria**, não da Despesa.

**Observação:**  
Despesas sem categoria devem ser sinalizadas para classificação posterior.

**Entidades Impactadas:**

- Despesa
    
- Categoria
    

**Casos de Uso Relacionados:**

- UC-06 – Classificar Despesa
    

---

### RN-05 — Comportamento de Despesas Fixas

**Descrição:**  
Despesas identificadas como **FIXAS** devem ser replicadas automaticamente para Meses Financeiros futuros enquanto o Mês Financeiro estiver em estado ABERTO.

**Restrições:**

- Não podem ser replicadas para meses fechados;
    
- A replicação não altera valores históricos.
    

**Entidades Impactadas:**

- Despesa
    
- MêsFinanceiro
    

**Casos de Uso Relacionados:**

- UC-05 – Gerenciar Despesas Fixas
    

---

### RN-06 — Parcelamento como Compromisso Temporal

**Descrição:**  
Uma Despesa do tipo **PARCELADA** deve originar um **Parcelamento**, que por sua vez gera Parcelas distribuídas em Meses Financeiros futuros.

**Restrições:**

- Parcelas não podem ser alteradas individualmente;
    
- Alterações ocorrem apenas no nível do Parcelamento.
    

**Entidades Impactadas:**

- Despesa
    
- Parcelamento
    
- Parcela
    
- MêsFinanceiro
    

**Casos de Uso Relacionados:**

- UC-07 – Gerenciar Parcelamentos
    

---

### RN-07 — Cálculo Determinístico de Saldo Mensal

**Descrição:**  
O saldo de um Mês Financeiro deve ser sempre calculado como:

> **Saldo Final = Saldo Inicial + Total de Receitas − Total de Despesas − Total de Parcelas**

O saldo **não pode ser editado manualmente**.

**Entidades Impactadas:**

- MêsFinanceiro
    

**Casos de Uso Relacionados:**

- UC-08 – Fechar Mês Financeiro
    

---

### RN-08 — Detecção de Divergência Financeira

**Descrição:**  
Quando houver divergência entre o saldo calculado e um saldo informado externamente pelo usuário, o sistema deve:

- registrar a divergência;
    
- solicitar confirmação ou correção.
    

**Entidades Impactadas:**

- MêsFinanceiro
    

**Casos de Uso Relacionados:**

- UC-15 – Tratar Divergência Financeira
    

---

### RN-09 — Consolidação Histórica de Meses Fechados

**Descrição:**  
Meses Financeiros fechados podem ser consolidados em registros históricos resumidos, preservando:

- totais;
    
- indicadores;
    
- referência temporal.
    

**Entidades Impactadas:**

- MêsFinanceiro
    
- RelatórioFinanceiro
    

**Casos de Uso Relacionados:**

- UC-10 – Gerar Relatórios Financeiros
    

---

### RN-10 — Isolamento Total de Dados por Usuário

**Descrição:**  
Os dados financeiros de um Usuário são totalmente isolados e não podem ser acessados ou inferidos por outro Usuário.

**Entidades Impactadas:**

- Usuário
    

**Casos de Uso Relacionados:**

- UC-01 – Cadastro de Usuário
    

---

### RN-11 — Persistência de Preferências do Sistema

**Descrição:**  
As preferências do sistema definidas pelo Usuário devem ser persistidas e reaplicadas automaticamente em novas sessões.

**Entidades Impactadas:**

- Usuário
    
- PreferênciasSistema
    

**Casos de Uso Relacionados:**

- UC-13 – Configurar Preferências do Sistema

---
## 4. Observações Finais

As Regras de Negócio aqui descritas são **normativas** e devem ser respeitadas em qualquer decisão técnica futura. Nenhuma otimização, refatoração ou escolha de stack pode violar essas regras.

Este documento serve como **ponte direta** entre o Modelo de Domínio e a Arquitetura do Sistema.