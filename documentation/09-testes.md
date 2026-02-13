# Estratégia de Testes – Finance Core

---

## 1. Objetivo

Este documento define a **estratégia oficial de testes** do sistema **Finance Core**, estabelecendo:

- Tipos de testes adotados.
- Responsabilidades por camada.
- Critérios de cobertura e qualidade.
- Regras de aprovação (Definition of Done).

A estratégia garante que o sistema:

- Respeite as regras de negócio.
- Mantenha consistência financeira.
- Seja seguro para evolução futura.

---

## 2. Princípios Fundamentais

- Testes validam **comportamento**, não implementação.
- Regras de negócio são testadas **sem infraestrutura**.
- Falhas devem ser detectadas **o mais cedo possível**.
- Leitura e escrita são testadas separadamente.
- **Nenhuma funcionalidade é considerada pronta sem testes.**

---

## 3. Pirâmide de Testes

A estratégia segue o modelo clássico da pirâmide de testes, priorizando uma base sólida de testes unitários e reduzindo a quantidade de testes mais caros (integração e aceitação).

         /\
        /  \
       /    \
      /      \
     /  Acei  \
    /   tação  \
   /────────────\
  /  Integração  \
 /────────────────\
/ Testes Unitários \
────────────────────


---

## 4. Testes Unitários

### 4.1 Objetivo

- Validar regras de negócio isoladamente.
- Garantir invariantes do domínio.
- Testar comportamentos de entidades, serviços de domínio e casos de uso.

### 4.2 Camadas Testadas

| Camada        | Testada? | Observação                              |
|---------------|----------|-----------------------------------------|
| Domain        | Sim      | 100% das regras de negócio.            |
| Application   | Sim      | Casos de uso com repositórios mockados.|
| Infrastructure| Não      | Testada nos testes de integração.      |
| UI            | Não      | Testes de aceitação ou manuais.        |

### 4.3 Escopo dos Testes Unitários

**O que testar:**
- Entidades de domínio.
- Serviços de domínio (quando houver lógica não alocável em entidades).
- Casos de uso (camada de aplicação) – com mocks das dependências.
- Exceções de domínio e aplicação.

**O que não testar:**
- Frameworks (Spring, JPA, etc.).
- Banco de dados.
- Serialização/desserialização.
- Interface com usuário.

### 4.4 Exemplos de Casos de Teste

- Não permitir registrar despesa sem categoria (RN-04).
- Impedir alteração de um mês financeiro já fechado (RN-02).
- Calcular corretamente o saldo final no fechamento do mês (RN-07).
- Gerar parcelas com valores e quantidades corretas (RN-06).

---

## 5. Testes de Integração

### 5.1 Objetivo

- Validar a integração entre camadas.
- Garantir o funcionamento correto da persistência (repositórios).
- Testar transações e rollback.

### 5.2 Camadas Envolvidas

| Camada        | Participa? | Observação                              |
|---------------|------------|-----------------------------------------|
| Application   | Sim        | Orquestração dos casos de uso.         |
| Infrastructure| Sim        | Repositórios, Flyway, acesso a banco.  |
| Domain        | Sim        | Entidades e regras são exercitadas.    |

### 5.3 Cenários Críticos

- Registrar despesa completa com categoria e mês financeiro.
- Fechar mês financeiro e verificar cálculo de saldo.
- Criar parcelamento e distribuir parcelas nos meses corretos.
- Rollback em caso de falha (ex.: violação de constraint).

### 5.4 Estratégia

- Banco de dados isolado (ex.: PostgreSQL em container ou H2 em modo PostgreSQL).
- Dados controlados e conhecidos previamente.
- Execução automatizada no pipeline de CI.
- Limpeza do estado entre testes (reset ou rollback).

---

## 6. Testes de Aceitação

### 6.1 Objetivo

- Validar requisitos funcionais do ponto de vista do usuário.
- Exercitar fluxos completos de ponta a ponta.
- Servir como documentação executável do comportamento esperado.

### 6.2 Base

Os testes de aceitação são **derivados diretamente dos Casos de Uso** documentados em `03-casos-de-uso.md`.

### 6.3 Exemplos

- Usuário cadastra uma despesa e o saldo mensal é atualizado corretamente.
- Usuário fecha o mês e não consegue mais editar os lançamentos.
- Usuário informa um saldo externo divergente e o sistema registra a inconsistência.

---

## 7. Testes de Regressão

### 7.1 Objetivo

Garantir que novas alterações não quebrem funcionalidades existentes nem alterem o comportamento esperado.

### 7.2 Estratégia

- Suíte automatizada executada a cada commit (CI).
- Ênfase em testes unitários e de integração.
- Regressão visual (UI) será manual até que haja interface estável.

---

## 8. Testes de Read Model (Queries & Projections)

### 8.1 Objetivo

- Validar a corretude das projeções de leitura.
- Garantir performance adequada das consultas.
- Verificar consistência entre o modelo de escrita e o modelo de leitura.

### 8.2 Estratégia

- Utilização de dados conhecidos e determinísticos.
- Execução de consultas SQL/JPQL diretamente sobre o banco de testes.
- Comparação dos resultados com os valores esperados.

---

## 9. Testes de Exceção e Falha

### 9.1 Escopo

- Exceções de domínio (ex.: `MesFinanceiroFechadoException`).
- Exceções de aplicação (ex.: `UsuarioNaoEncontradoException`).
- Falhas de infraestrutura (ex.: timeout de banco, violação de constraint).

### 9.2 O que Validar

- A exceção correta é lançada.
- A transação é revertida (rollback).
- O estado do sistema permanece consistente após a falha.

---

## 10. Métricas e Cobertura

| Tipo de Teste        | Cobertura Esperada | Observação                                      |
|----------------------|--------------------|-------------------------------------------------|
| Domínio              | ~100%              | Todas as regras de negócio devem ser testadas.  |
| Casos de Uso         | ≥ 90%              | Cobertura de linhas e branches.                 |
| Infraestrutura       | Pontual            | Apenas cenários críticos de integração.         |
| Interface (UI)       | Manual             | Automação futura quando a UI estiver estável.   |

**Nota:** Cobertura não substitui qualidade, mas é um indicador importante de riscos.

---

## 11. Definition of Done (DoD) – Critérios de Aceitação

Uma funcionalidade ou correção só é considerada **pronta** quando:

- [x] Todas as regras de negócio envolvidas estão cobertas por testes unitários.
- [x] Testes unitários e de integração estão **verdes** (passando).
- [x] Nenhum teste existente foi quebrado (regressão).
- [x] Código revisado em Pull Request (mínimo 1 aprovação).
- [x] Documentação atualizada, quando aplicável.

---

## 12. Decisões Arquiteturais Registradas

| Decisão                              | Status    | Descrição Resumida                                      |
|--------------------------------------|-----------|---------------------------------------------------------|
| Pirâmide de testes                  | Aprovado  | Ênfase em testes unitários, poucos testes de integração.|
| Domínio testado isoladamente        | Aprovado  | Domínio não depende de infraestrutura para testes.      |
| Integração com rollback             | Aprovado  | Testes de integração com transação e rollback.          |
| Testes baseados em casos de uso     | Aprovado  | Testes de aceitação derivados dos UCs.                 |
| Regressão obrigatória              | Aprovado  | Suíte de regressão executada continuamente.            |

---

**Documentos relacionados:**  
- [01-visao-geral.md](01-visao-geral.md)  
- [02-requisitos.md](02-requisitos.md)  
- [03-casos-de-uso.md](03-casos-de-uso.md)  
- [04-modelo-dominio.md](04-modelo-dominio.md)  
- [05-regras-negocio.md](05-regras-negocio.md)  
- [06-arquitetura.md](06-arquitetura.md)  
- [07-modelo-dados.md](07-modelo-dados.md)  
- [08-migracoes.md](08-migracoes.md)  
- [10-contribuicao.md](10-contribuicao.md)