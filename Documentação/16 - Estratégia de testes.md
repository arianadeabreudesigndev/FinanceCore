
## 1. Objetivo do Documento

Este documento define a **estratégia oficial de testes** do sistema **Finance Core**, estabelecendo:

- tipos de testes adotados;
    
- responsabilidades por camada;
    
- critérios de cobertura e qualidade;
    
- regras de aprovação (Definition of Done).
    

A estratégia garante que o sistema:

- respeite regras de negócio;
    
- mantenha consistência financeira;
    
- seja seguro para evolução futura.
    

---

## 2. Princípios Fundamentais

- Testes validam **comportamento**, não implementação;
    
- Regras de negócio são testadas **sem infraestrutura**;
    
- Falhas devem ser detectadas **o mais cedo possível**;
    
- Leitura e escrita são testadas separadamente;
    
- Nenhuma funcionalidade é considerada pronta sem testes.
    

---

## 3. Pirâmide de Testes Adotada


```
        Testes de Aceitação
     -----------------------
       Testes de Integração
   ---------------------------
        Testes Unitários
```

📌 Forte base em testes unitários, poucos testes caros.

---

## 4. Testes Unitários

### 4.1 Objetivo

Validar:

- regras de negócio;
    
- invariantes do domínio;
    
- comportamentos isolados.
    

---

### 4.2 Camadas Testadas

|Camada|Testada?|
|---|---|
|Domain|✅ Sim|
|Application|✅ Sim|
|Infrastructure|❌ Não|
|UI|❌ Não|

---

### 4.3 O Que Testar

✔ Entidades de domínio  
✔ Serviços de domínio  
✔ Casos de uso  
✔ Exceções de domínio

---

### 4.4 O Que Não Testar

❌ Frameworks  
❌ Banco de dados  
❌ Serialização  
❌ UI

---

### 4.5 Exemplos de Casos

- não permitir despesa sem categoria;
    
- impedir alteração de mês fechado;
    
- cálculo correto de saldo;
    
- geração correta de parcelas.
    

---

## 5. Testes de Integração

### 5.1 Objetivo

Validar:

- integração entre camadas;
    
- persistência;
    
- transações;
    
- rollback.
    

---

### 5.2 Camadas Envolvidas

|Camada|Participa?|
|---|---|
|Application|✅|
|Infrastructure|✅|
|Domain|✅|

---

### 5.3 Cenários Críticos

✔ Registrar despesa completa  
✔ Fechar mês financeiro  
✔ Criar parcelamento  
✔ Rollback em falha

---

### 5.4 Estratégia

- banco isolado por teste;
    
- dados controlados;
    
- execução automatizada.
    

---

## 6. Testes de Aceitação

### 6.1 Objetivo

Validar:

- requisitos funcionais;
    
- fluxos reais do usuário;
    
- comportamento de ponta a ponta.
    

---

### 6.2 Base

📌 Derivados **diretamente dos Casos de Uso**.

---

### 6.3 Exemplos

- usuário registra despesa e vê impacto no saldo;
    
- usuário fecha mês e não consegue alterar dados;
    
- usuário detecta inconsistência financeira.
    

---

## 7. Testes de Regressão

### 7.1 Objetivo

Garantir que alterações futuras:

- não quebrem regras existentes;
    
- não alterem comportamento esperado.
    

---

### 7.2 Estratégia

- suíte automatizada;
    
- execução contínua;
    
- foco em regras críticas.
    

---

## 8. Testes de Read Model (Queries & Projections)

### 8.1 Objetivo

Validar:

- corretude das projeções;
    
- performance;
    
- consistência com o write model.
    

---

### 8.2 Estratégia

- dados conhecidos;
    
- consultas determinísticas;
    
- validação de agregações.
    

---

## 9. Testes de Exceção e Falha

### 9.1 O Que Validar

✔ Exceções corretas lançadas  
✔ Rollback executado  
✔ Estado consistente após falha

---

## 10. Métricas e Cobertura

|Tipo|Cobertura Esperada|
|---|---|
|Domínio|~100%|
|Casos de Uso|≥ 90%|
|Infraestrutura|Pontual|
|UI|Manual|

📌 Cobertura não substitui qualidade, mas sinaliza riscos.

---

## 11. Definition of Done (DoD)

Uma funcionalidade só é considerada pronta se:

- regras de negócio testadas;
    
- testes unitários aprovados;
    
- testes de integração aprovados;
    
- nenhum teste quebrado;
    
- código revisado.
    

---

## 12. Decisões Arquiteturais Registradas

✔ Pirâmide de testes  
✔ Domínio testado isoladamente  
✔ Integração com rollback  
✔ Testes baseados em casos de uso  
✔ Regressão obrigatória