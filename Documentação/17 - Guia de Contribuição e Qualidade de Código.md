
## Finance Core

_(Git / CI / Pull Requests)_

---

## 1. Objetivo do Documento

Este guia define **regras obrigatórias** para:

- contribuição de código;
    
- versionamento;
    
- revisão;
    
- integração contínua;
    
- padrões de qualidade.
    

O objetivo é garantir que o **Finance Core permaneça consistente, legível, testável e evolutivo** ao longo do tempo.

---

## 2. Princípios de Engenharia

Toda contribuição deve respeitar:

- clareza antes de performance;
    
- regras de negócio explícitas;
    
- consistência arquitetural;
    
- rastreabilidade de decisões;
    
- zero “gambiarras”.
    

📌 Código financeiro **não admite ambiguidade**.

---

## 3. Estrutura de Branches (Git Flow Simplificado)

### 3.1 Branch Principal

- `main`
    
    - sempre estável;
        
    - sempre testada;
        
    - pronta para release.
        

---

### 3.2 Branches de Trabalho

|Tipo|Prefixo|Exemplo|
|---|---|---|
|Feature|`feature/`|`feature/registro-despesa`|
|Fix|`fix/`|`fix/rollback-fechamento`|
|Refactor|`refactor/`|`refactor/agregado-mes`|
|Docs|`docs/`|`docs/arquitetura-testes`|

---

## 4. Commits

### 4.1 Padrão de Mensagem (Obrigatório)

Formato:

```
<tipo>: <descrição curta no imperativo>

[opcional] contexto adicional
```

#### Tipos aceitos:

- `feat`
    
- `fix`
    
- `refactor`
    
- `test`
    
- `docs`
    
- `chore`
    

#### Exemplos:

```
feat: adicionar fechamento mensal financeiro
fix: corrigir rollback em falha de parcelamento
test: cobrir regra de saldo negativo
```

### 4.2 Regras

✔ Commits pequenos  
✔ Um propósito por commit  
❌ Commits genéricos (“ajustes”, “updates”)

---

## 5. Pull Requests (PRs)

### 5.1 Obrigatoriedade

Nenhum código entra em `main` sem PR — **mesmo sendo projeto solo**.

📌 O PR é o momento de revisão racional, não de pressa.

---

### 5.2 Estrutura do PR

Todo PR deve conter:

- **Descrição clara**
    
- **Motivação**
    
- **O que foi alterado**
    
- **Impacto esperado**
    
- **Checklist**
    

---

### 5.3 Checklist Obrigatório

-  Regras de negócio respeitadas
    
-  Testes unitários criados/atualizados
    
-  Testes de integração passando
    
-  Nenhuma regra duplicada
    
-  Código legível e documentado
    

---

## 6. Padrões de Código

### 6.1 Regras Gerais

✔ Código explícito > código “esperto”  
✔ Nomes claros e sem abreviações  
✔ Métodos pequenos  
✔ Classes com responsabilidade única

---

### 6.2 Arquitetura

- domínio **não depende** de nada externo;
    
- application coordena, não decide;
    
- infraestrutura apenas executa;
    
- controllers são adaptadores.
    

❌ Nenhuma violação dessas regras é aceitável.

---

## 7. Testes como Contrato

### 7.1 Regras

- toda regra de negócio deve ter teste;
    
- todo bug corrigido gera teste;
    
- testes quebrados bloqueiam merge.
    

---

### 7.2 Política de Falha

Se o CI falhar:

❌ merge bloqueado  
❌ release bloqueado

---

## 8. Integração Contínua (CI)

### 8.1 Pipeline Mínimo

Em todo PR:

1️⃣ build  
2️⃣ testes unitários  
3️⃣ testes de integração  
4️⃣ análise estática (se configurada)

---

### 8.2 Política

- CI é autoridade máxima;
    
- resultados locais não substituem CI.
    

---

## 9. Refatoração

### 9.1 Quando Refatorar

✔ Código duplicado  
✔ Complexidade crescente  
✔ Regra difícil de entender

---

### 9.2 Regras

- refatoração **não muda comportamento**;
    
- refatoração exige testes existentes;
    
- refatoração vai em PR próprio.
    

---

## 10. Documentação

### 10.1 Obrigatória Quando

- nova regra de negócio;
    
- mudança arquitetural;
    
- decisão relevante.
    

📌 Documento desatualizado é bug.

---

## 11. Versionamento Semântico

Formato:

```
MAJOR.MINOR.PATCH
```

|Tipo|Quando|
|---|---|
|MAJOR|quebra compatibilidade|
|MINOR|nova funcionalidade|
|PATCH|correção|

---

## 12. Definition of Done (Engenharia)

Uma entrega só é concluída se:

- código segue arquitetura;
    
- testes passam;
    
- PR aprovado;
    
- documentação atualizada;
    
- CI verde.

## Encerramento

Este guia não é sugestão.  
É **contrato de qualidade** do Finance Core.

Ele garante que o sistema continue:

- confiável;
    
- compreensível;
    
- sustentável;
    
- profissional.