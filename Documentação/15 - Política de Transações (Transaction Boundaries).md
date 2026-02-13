
## 1. Objetivo do Documento

Este documento define a **política oficial de transações** do sistema **Finance Core**, estabelecendo **onde**, **quando** e **como** transações devem ser iniciadas, controladas e finalizadas.

O objetivo é garantir:

- **consistência financeira absoluta**;
    
- **atomicidade** das operações críticas;
    
- ausência de efeitos colaterais parciais;
    
- previsibilidade em falhas e exceções.
    

Este documento é **normativo** para implementação.

---

## 2. Princípios Fundamentais

- Transações são **responsabilidade da camada de aplicação**;
    
- O domínio **não inicia, não controla e não encerra transações**;
    
- Uma transação deve abranger **uma unidade de trabalho de negócio**;
    
- Falhas devem resultar em **rollback completo**;
    
- Leitura ≠ escrita (CQRS aplicado).
    

---

## 3. Onde as Transações Devem Existir

### 3.1 Camada Responsável

📌 **Application Layer (Casos de Uso / Services)**

|Camada|Pode controlar transação?|
|---|---|
|Domain|❌ Não|
|Application|✅ Sim|
|Infrastructure|❌ Não|
|UI / Controllers|❌ Não|

---

### 3.2 Regra de Ouro

> **Cada Caso de Uso que altera estado deve ser executado dentro de exatamente uma transação.**

---

## 4. Unidade de Trabalho (Unit of Work)

### 4.1 Definição

Uma **Unidade de Trabalho** corresponde a:

- um Caso de Uso;
    
- uma intenção de negócio completa;
    
- um conjunto consistente de alterações.
    

---

### 4.2 Exemplos de Unidade de Trabalho

|Caso de Uso|Unidade Transacional|
|---|---|
|Registrar Despesa|Criação da despesa + atualização do saldo|
|Fechar Mês|Cálculo final + mudança de status|
|Gerar Parcelamento|Criação da despesa + parcelas|

---

## 5. Escopo Transacional

### 5.1 Escrita (Command)

Operações que **alteram estado**:

✔ Devem ser transacionais  
✔ Devem ser atômicas  
✔ Devem ser isoladas

---

### 5.2 Leitura (Query)

Operações de leitura:

❌ Não devem abrir transações longas  
✔ Podem usar transações somente para consistência momentânea  
✔ Devem ser otimizadas para leitura

---

## 6. Política de Commit e Rollback

### 6.1 Commit

Uma transação deve ser **confirmada** somente quando:

- todas as regras de negócio forem satisfeitas;
    
- todas as entidades estiverem em estado válido;
    
- nenhuma exceção tiver sido lançada.
    

---

### 6.2 Rollback

A transação deve ser revertida quando ocorrer:

- `DomainException`
    
- `ApplicationException`
    
- `InfrastructureException`
    

📌 **Rollback é obrigatório e automático**.

---

## 7. Isolamento Transacional

### 7.1 Nível de Isolamento Recomendado

Para o Finance Core:

> **READ COMMITTED**

Justificativa:

- evita leituras sujas;
    
- custo baixo;
    
- adequado para uso local.
    

---

### 7.2 Operações Sensíveis

Para:

- fechamento de mês;
    
- consolidação histórica;
    

Pode ser aplicado isolamento mais alto (**SERIALIZABLE**) pontualmente.

---

## 8. Concorrência

### 8.1 Estratégia Adotada

📌 **Concorrência otimista**

- versionamento de entidades (`version`);
    
- verificação no commit.
    

---

### 8.2 Justificativa

- uso local;
    
- baixo volume concorrente;
    
- melhor performance.
    

---

## 9. Transações e Parcelamentos

### 9.1 Regra Crítica

A criação de um parcelamento é **indivisível**:

✔ Despesa principal  
✔ Todas as parcelas  
✔ Associações com meses

Tudo ocorre **na mesma transação**.

---

## 10. Transações e Fechamento de Mês

### 10.1 Regras

- fechamento é operação crítica;
    
- nenhuma escrita paralela permitida;
    
- rollback total em falha.
    

---

## 11. Integração com Persistência

### 11.1 Repositórios

- não abrem transação;
    
- não fazem commit;
    
- não fazem rollback.
    

Responsabilidade exclusiva da Application Layer.

---

## 12. Testes Transacionais

- cada caso de uso deve ter teste de sucesso;
    
- cada falha deve validar rollback;
    
- estados intermediários não podem persistir.
    

---

## 13. Decisões Arquiteturais Registradas

✔ Transações na Application Layer  
✔ Uma transação por Caso de Uso  
✔ CQRS aplicado  
✔ Rollback automático  
✔ Concorrência otimista