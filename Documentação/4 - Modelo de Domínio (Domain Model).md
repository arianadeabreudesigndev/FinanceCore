
## 1. Objetivo

Este documento descreve o **Modelo de Domínio** do sistema **Finance Core**, representando conceitualmente as entidades centrais do negócio, seus atributos, relacionamentos e responsabilidades. O objetivo é estabelecer uma **base sólida e consistente** para as próximas etapas do projeto (regras de negócio, arquitetura e implementação), garantindo alinhamento absoluto com os **requisitos funcionais e não funcionais já definidos**.

Este modelo é **independente de tecnologia**, banco de dados ou frameworks, focando exclusivamente na **lógica do domínio financeiro pessoal**.

---

## 2. Princípios Adotados

- Separação clara entre **domínio** e **infraestrutura**;
    
- Modelo orientado a comportamento e significado, não a tabelas;
    
- Linguagem ubíqua consistente com requisitos e casos de uso;
    
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

**Descrição:**  
Representa o proprietário dos dados financeiros. No contexto atual, trata-se de um usuário local, único por instância da aplicação.

**Responsabilidades:**

- Ser o agregador raiz dos dados financeiros;
    
- Manter preferências do sistema;
    
- Iniciar e encerrar ciclos financeiros.
    

**Atributos principais:**

- id
    
- nome
    
- dataCriacao
    
- preferencias
    

**Relacionamentos:**

- Possui um ou mais MêsFinanceiro
    
- Possui PreferênciasSistema
    

---

### 4.2 PreferênciasSistema

**Descrição:**  
Representa as configurações personalizáveis do sistema.

**Responsabilidades:**

- Armazenar preferências visuais e comportamentais;
    
- Influenciar a experiência do usuário sem alterar regras de negócio.
    

**Atributos principais:**

- tema (claro | escuro)
    
- idioma
    
- notificacoesAtivas
    

**Relacionamentos:**

- Pertence a um Usuário
    

---

### 4.3 MêsFinanceiro

**Descrição:**  
Representa um período financeiro mensal, sendo o **núcleo do domínio**. Todas as receitas e despesas estão sempre associadas a um mês financeiro.

**Responsabilidades:**

- Agregar receitas e despesas;
    
- Calcular saldo mensal;
    
- Controlar estado (aberto ou fechado);
    
- Servir de base para relatórios e análises.
    

**Atributos principais:**

- id
    
- mes
    
- ano
    
- status (ABERTO | FECHADO)
    
- saldoInicial
    
- saldoFinal
    

**Relacionamentos:**

- Pertence a um Usuário
    
- Possui várias Receitas
    
- Possui várias Despesas
    
- Pode conter Parcelas
    

---

### 4.4 Receita

**Descrição:**  
Representa qualquer entrada de valor financeiro.

**Responsabilidades:**

- Impactar positivamente o saldo mensal;
    
- Fornecer dados para análise de renda.
    

**Atributos principais:**

- id
    
- descricao
    
- valor
    
- tipo (FIXA | VARIÁVEL)
    
- dataReferencia
    

**Relacionamentos:**

- Pertence a um MêsFinanceiro
    

---

### 4.5 Despesa

**Descrição:**  
Representa qualquer saída de valor financeiro.

**Responsabilidades:**

- Impactar negativamente o saldo mensal;
    
- Ser classificada e analisada;
    
- Participar de relatórios e auditorias.
    

**Atributos principais:**

- id
    
- descricao
    
- valor
    
- data
    
- tipo (PONTUAL | FIXA | PARCELADA)
    
- metodoPagamento
    

**Relacionamentos:**

- Pertence a um MêsFinanceiro
    
- Pode possuir uma Categoria
    
- Pode estar associada a um Parcelamento
    

---

### 4.6 Categoria

**Descrição:**  
Representa a classificação financeira de uma despesa.

**Responsabilidades:**

- Agrupar despesas para análise;
    
- Permitir distinção entre gastos essenciais e não essenciais.
    

**Atributos principais:**

- id
    
- nome
    
- essencial (true | false)
    

**Relacionamentos:**

- Associada a várias Despesas
    

---

### 4.7 Parcelamento

**Descrição:**  
Representa uma compra parcelada distribuída em vários meses financeiros.

**Responsabilidades:**

- Controlar número total de parcelas;
    
- Distribuir impacto financeiro ao longo do tempo;
    
- Garantir consistência entre parcelas e despesa original.
    

**Atributos principais:**

- id
    
- valorTotal
    
- numeroParcelas
    
- parcelaAtual
    

**Relacionamentos:**

- Origina várias Parcelas
    
- Associado a uma Despesa principal
    

---

### 4.8 Parcela

**Descrição:**  
Representa uma unidade mensal de um parcelamento.

**Responsabilidades:**

- Impactar o mês financeiro correspondente;
    
- Manter vínculo com o parcelamento original.
    

**Atributos principais:**

- id
    
- numero
    
- valor
    
- status (PAGA | PENDENTE)
    

**Relacionamentos:**

- Pertence a um Parcelamento
    
- Associada a um MêsFinanceiro
    

---

### 4.9 RelatórioFinanceiro

**Descrição:**  
Representa uma visão consolidada dos dados financeiros.

**Responsabilidades:**

- Consolidar informações de um ou mais meses;
    
- Servir de base para exportação e visualização.
    

**Atributos principais:**

- id
    
- periodoInicio
    
- periodoFim
    
- dataGeracao
    

**Relacionamentos:**

- Consome dados de MêsFinanceiro
    

---

## 5. Relacionamentos (Resumo Conceitual)

- Usuário 1 ── * MêsFinanceiro
    
- MêsFinanceiro 1 ── * Receita
    
- MêsFinanceiro 1 ── * Despesa
    
- Despesa * ── 1 Categoria
    
- Parcelamento 1 ── * Parcela
    
- Parcela * ── 1 MêsFinanceiro
    

---

## 6. Observações Importantes

- Nenhuma entidade contém detalhes de persistência;
    
- Nenhuma decisão de banco de dados foi tomada neste documento;
    
- O modelo está preparado para futura separação em módulos;
    
- Este documento é **base obrigatória** para as próximas etapas.
    

---

## 7. Agregados do Domínio

A seguir são definidos os **Agregados**, seus **Agregados Raiz (Aggregate Root)** e os limites de consistência do modelo, conforme princípios de _Domain-Driven Design_.

### 7.1 Agregado Usuário

**Aggregate Root:** Usuário

**Entidades incluídas:**

- Usuário
    
- PreferênciasSistema
    

**Justificativa:**  
O Usuário é o proprietário lógico de todos os dados financeiros. Preferências do sistema não fazem sentido fora do contexto do usuário e não possuem identidade independente relevante no domínio.

**Responsabilidade do agregado:**

- Garantir a existência e integridade das PreferênciasSistema;
    
- Controlar o ciclo de vida dos MêsFinanceiro associados.
    

---

### 7.2 Agregado MêsFinanceiro

**Aggregate Root:** MêsFinanceiro

**Entidades incluídas:**

- MêsFinanceiro
    
- Receita
    
- Despesa
    
- Parcela
    

**Justificativa:**  
O MêsFinanceiro é o **núcleo do domínio**. Toda movimentação financeira ocorre sempre dentro de um contexto mensal. Receitas, despesas e parcelas não possuem significado isolado fora de um mês financeiro.

**Responsabilidade do agregado:**

- Garantir consistência do saldo mensal;
    
- Controlar estado (ABERTO / FECHADO);
    
- Impedir alterações após fechamento;
    
- Orquestrar impacto financeiro das receitas, despesas e parcelas.
    

---

### 7.3 Agregado Parcelamento

**Aggregate Root:** Parcelamento

**Entidades incluídas:**

- Parcelamento
    
- Parcela
    

**Justificativa:**  
O Parcelamento representa um compromisso financeiro distribuído no tempo, com regras próprias que independem de um único mês financeiro, embora cada parcela impacte meses distintos.

**Responsabilidade do agregado:**

- Garantir que o valor total seja corretamente distribuído;
    
- Controlar a numeração e estado das parcelas;
    
- Manter vínculo lógico com a despesa de origem.
    

---

### 7.4 Agregado Categoria

**Aggregate Root:** Categoria

**Entidades incluídas:**

- Categoria
    

**Justificativa:**  
Categoria é uma entidade de referência, com identidade própria, reutilizável por múltiplas despesas.

**Responsabilidade do agregado:**

- Garantir consistência semântica da classificação financeira.
    

---

### 7.5 Agregado RelatórioFinanceiro

**Aggregate Root:** RelatórioFinanceiro

**Entidades incluídas:**

- RelatórioFinanceiro
    

**Justificativa:**  
Relatórios não alteram estado do domínio; apenas consomem dados consolidados. São tratados como agregados independentes por clareza conceitual.

---

## 8. Invariantes do Domínio

As invariantes abaixo **devem ser sempre verdadeiras**, independentemente da implementação técnica.

### Invariantes Gerais

- Todo dado financeiro pertence a **exatamente um Usuário**.
    
- Nenhuma entidade financeira existe sem um **MêsFinanceiro associado**, direta ou indiretamente.
    

### Invariantes do MêsFinanceiro

- Um MêsFinanceiro **FECHADO** não pode ser alterado.
    
- O saldo final é sempre:
    
    > saldoFinal = saldoInicial + totalReceitas − totalDespesas − totalParcelas
    
- Receitas e despesas só podem ser registradas em meses **ABERTOS**.
    

### Invariantes de Receita e Despesa

- Valores financeiros devem ser **positivos e maiores que zero**.
    
- Toda Despesa pode ter **no máximo uma Categoria**.
    
- Despesas do tipo PARCELADA devem estar associadas a um Parcelamento válido.
    

### Invariantes de Parcelamento

- A soma do valor de todas as parcelas deve ser **igual ao valorTotal** do parcelamento.
    
- O número de parcelas geradas deve ser igual a **numeroParcelas**.
    
- Cada Parcela deve estar associada a **um único MêsFinanceiro**.
    

---

## 9. Diagrama Conceitual (Descrição Textual)

O diagrama conceitual do domínio pode ser descrito da seguinte forma:

- **Usuário** é o ponto de entrada do domínio e possui:
    
    - PreferênciasSistema
        
    - Um ou mais MêsFinanceiro
        
- **MêsFinanceiro** agrega:
    
    - Receitas
        
    - Despesas
        
    - Parcelas
        
- **Despesa**:
    
    - Pode estar associada a uma Categoria
        
    - Pode originar um Parcelamento
        
- **Parcelamento**:
    
    - Origina múltiplas Parcelas
        
    - Cada Parcela impacta um MêsFinanceiro específico
        
- **RelatórioFinanceiro**:
    
    - Consome dados de um ou mais MêsFinanceiro
        
    - Não altera o estado do domínio
        

Este diagrama reforça que **o tempo (mês)** é o eixo central do sistema, e não a despesa ou o pagamento isoladamente.

---

## 10. Encerramento do Documento

O **Modelo de Domínio do Finance Core** está agora **completo, fechado e consistente**, servindo como:

- base obrigatória para Regras de Negócio;
    
- referência canônica para Casos de Uso;
    
- guia estrutural para decisões arquiteturais futuras.
    
