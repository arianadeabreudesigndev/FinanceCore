## 1. Objetivo do Documento

Este documento descreve o **Modelo de Dados do sistema Finance Core**, estabelecendo a transição controlada entre:

- **Modelo Conceitual** (derivado do domínio);
    
- **Modelo Lógico** (estrutura de dados abstrata, independente de SGBD).
    

O objetivo é garantir:

- fidelidade absoluta ao **Modelo de Domínio**;
    
- aderência às **Regras de Negócio**;
    
- base sólida para implementação futura da persistência;
    
- rastreabilidade clara entre dados, regras e casos de uso.
    

⚠️ Este documento **não define banco de dados físico**, tipos específicos de SGBD ou otimizações técnicas.

---

## 2. Princípios do Modelo de Dados

O modelo de dados segue os seguintes princípios:

- Derivação direta do **Modelo de Domínio**;
    
- Nenhum atributo sem significado de negócio;
    
- Nenhuma tabela sem entidade correspondente;
    
- Nenhuma decisão física antecipada;
    
- Normalização lógica suficiente para evitar redundância conceitual;
    
- Preparação para persistência local e futura sincronização.
    

---

## 3. Modelo Conceitual de Dados

O **Modelo Conceitual** representa as entidades do domínio e seus relacionamentos, sem preocupações técnicas.

### 3.1 Entidades Conceituais

As entidades centrais do modelo conceitual são:

- Usuário
    
- PreferênciasSistema
    
- MêsFinanceiro
    
- Receita
    
- Despesa
    
- Categoria
    
- Parcelamento
    
- Parcela
    
- RelatórioFinanceiro
    

Essas entidades já foram definidas semanticamente no **Modelo de Domínio** e aqui são vistas sob a ótica de dados.

---

### 3.2 Relacionamentos Conceituais

- Usuário **possui** PreferênciasSistema
    
- Usuário **possui** MêsFinanceiro
    
- MêsFinanceiro **agrega** Receita
    
- MêsFinanceiro **agrega** Despesa
    
- Despesa **pertence** a Categoria
    
- Despesa **pode originar** Parcelamento
    
- Parcelamento **gera** Parcela
    
- Parcela **impacta** MêsFinanceiro
    
- RelatórioFinanceiro **consome** dados de MêsFinanceiro
    

---

### 3.3 Cardinalidades (Conceitual)

- Usuário 1 ── 1 PreferênciasSistema
    
- Usuário 1 ── * MêsFinanceiro
    
- MêsFinanceiro 1 ── * Receita
    
- MêsFinanceiro 1 ── * Despesa
    
- Categoria 1 ── * Despesa
    
- Parcelamento 1 ── * Parcela
    
- MêsFinanceiro 1 ── * Parcela
    

---

## 4. Modelo Lógico de Dados

O **Modelo Lógico** traduz o modelo conceitual para estruturas de dados organizadas, ainda independentes de tecnologia.

---

### 4.1 Usuário

**Descrição:**  
Representa o proprietário dos dados financeiros.

**Atributos:**

- usuario_id (PK)
    
- nome
    
- data_criacao
    

**Relacionamentos:**

- 1:1 com PreferenciasSistema
    
- 1:N com MêsFinanceiro
    

---

### 4.2 PreferenciasSistema

**Descrição:**  
Configurações persistentes do sistema.

**Atributos:**

- preferencias_id (PK)
    
- usuario_id (FK)
    
- tema (CLARO | ESCURO)
    
- idioma
    
- notificacoes_ativas
    

**Restrições:**

- Deve existir exatamente uma instância por Usuário (RN-11)
    

---

### 4.3 MesFinanceiro

**Descrição:**  
Unidade central de agregação financeira mensal.

**Atributos:**

- mes_financeiro_id (PK)
    
- usuario_id (FK)
    
- mes
    
- ano
    
- status (ABERTO | FECHADO)
    
- saldo_inicial
    
- saldo_final
    

**Restrições Lógicas:**

- Apenas um mês ABERTO por período (RN-01)
    
- Imutável quando FECHADO (RN-02)
    

---

### 4.4 Receita

**Descrição:**  
Registro de entrada financeira.

**Atributos:**

- receita_id (PK)
    
- mes_financeiro_id (FK)
    
- descricao
    
- valor
    
- tipo (FIXA | VARIAVEL)
    
- data_referencia
    

**Regras Associadas:**

- Obrigatoriamente associada a um MêsFinanceiro (RN-03)
    

---

### 4.5 Despesa

**Descrição:**  
Registro de saída financeira.

**Atributos:**

- despesa_id (PK)
    
- mes_financeiro_id (FK)
    
- categoria_id (FK)
    
- descricao
    
- valor
    
- data
    
- tipo (PONTUAL | FIXA | PARCELADA)
    
- metodo_pagamento
    

**Regras Associadas:**

- Classificação obrigatória (RN-04)
    
- Imutável se mês estiver fechado (RN-02)
    

---

### 4.6 Categoria

**Descrição:**  
Classificação financeira das despesas.

**Atributos:**

- categoria_id (PK)
    
- nome
    
- essencial (BOOLEAN)
    

---

### 4.7 Parcelamento

**Descrição:**  
Controle lógico de uma compra parcelada.

**Atributos:**

- parcelamento_id (PK)
    
- despesa_id (FK)
    
- valor_total
    
- numero_parcelas
    

**Restrições:**

- Uma despesa parcelada gera exatamente um Parcelamento (RN-06)
    

---

### 4.8 Parcela

**Descrição:**  
Unidade mensal de um parcelamento.

**Atributos:**

- parcela_id (PK)
    
- parcelamento_id (FK)
    
- mes_financeiro_id (FK)
    
- numero_parcela
    
- valor
    
- status (PAGA | PENDENTE)
    

**Regras Associadas:**

- Gerada automaticamente
    
- Não editável individualmente (RN-06)
    

---

### 4.9 RelatorioFinanceiro

**Descrição:**  
Representação consolidada de dados financeiros.

**Atributos:**

- relatorio_id (PK)
    
- periodo_inicio
    
- periodo_fim
    
- data_geracao
    

**Observação:**  
Não armazena valores financeiros primários, apenas consolida.

---

## 5. Normalização Lógica

O modelo lógico respeita:

- **1FN:** atributos atômicos;
    
- **2FN:** dependência total da chave;
    
- **3FN:** ausência de dependência transitiva.
    

Nenhuma otimização física foi aplicada neste estágio.

---

## 6. Rastreabilidade com o Domínio

|Entidade de Domínio|Estrutura Lógica|
|---|---|
|Usuário|Usuário|
|PreferênciasSistema|PreferenciasSistema|
|MêsFinanceiro|MesFinanceiro|
|Receita|Receita|
|Despesa|Despesa|
|Categoria|Categoria|
|Parcelamento|Parcelamento|
|Parcela|Parcela|
|RelatórioFinanceiro|RelatorioFinanceiro|

---

## 7. Observações Importantes

- Nenhum detalhe de SGBD foi definido;
    
- Nenhuma escolha de persistência foi feita;
    
- O modelo está preparado para:
    
    - persistência local;
        
    - evolução futura;
        
    - sincronização externa.
        

Este documento **não substitui** regras de negócio nem arquitetura — ele as materializa em dados.