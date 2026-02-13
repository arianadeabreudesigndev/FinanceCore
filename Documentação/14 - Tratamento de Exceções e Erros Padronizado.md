## 1. Objetivo do Documento

Este documento define a **estratégia oficial de tratamento de exceções e erros** do sistema **Finance Core**, estabelecendo:

- padrões claros para lançamento e captura de exceções;
    
- separação entre erros de domínio, aplicação e infraestrutura;
    
- comportamento previsível do sistema em cenários de falha;
    
- base consistente para logs, testes e interface.
    

O tratamento de exceções **não é mecanismo de controle de fluxo**, mas sim de **sinalização de falhas**.

---

## 2. Princípios Fundamentais

- Exceções representam **situações anômalas**, não fluxo normal;
    
- Toda exceção deve ter **significado semântico claro**;
    
- O domínio **não conhece infraestrutura**;
    
- A interface **não interpreta regras de negócio**;
    
- Mensagens técnicas ≠ mensagens para usuário final.
    

---

## 3. Classificação de Exceções

O sistema adota três níveis formais de exceções:

|Camada|Tipo|Responsabilidade|
|---|---|---|
|Domain|Violação de regra de negócio|Garantir invariantes|
|Application|Uso incorreto do sistema|Orquestração|
|Infrastructure|Falhas técnicas|Persistência, IO|

---

## 4. Exceções de Domínio (Domain Layer)

### 4.1 Finalidade

Sinalizar **violação de regras de negócio** e **invariantes do domínio**.

📌 Características:

- São **checadas semanticamente**;
    
- Não conhecem banco, UI ou framework;
    
- Não possuem códigos HTTP.
    

---

### 4.2 Exceção Base

```
public abstract class DomainException extends RuntimeException {

    protected DomainException(String message) {
        super(message);
    }
}
```

4.3 Exemplos de Exceções de Domínio

```
public class MesFinanceiroFechadoException
        extends DomainException {

    public MesFinanceiroFechadoException() {
        super("Mês financeiro está fechado e não pode ser alterado.");
    }
}
```

```
public class DespesaSemCategoriaException
        extends DomainException {

    public DespesaSemCategoriaException() {
        super("Despesa deve possuir uma categoria válida.");
    }
}
```

📌 Cada exceção representa **uma regra violada**, não um erro genérico.

---

## 5. Exceções de Aplicação (Application Layer)

### 5.1 Finalidade

Sinalizar:

- uso incorreto de casos de uso;
    
- ausência de recursos;
    
- falhas de orquestração.
    

---

### 5.2 Exceção Base

```
public abstract class ApplicationException
        extends RuntimeException {

    protected ApplicationException(String message) {
        super(message);
    }
}
```

5.3 Exemplos

```
public class MesFinanceiroNaoEncontradoException
        extends ApplicationException {

    public MesFinanceiroNaoEncontradoException(UUID id) {
        super("Mês financeiro não encontrado: " + id);
    }
}
```

```
public class OperacaoNaoPermitidaException
        extends ApplicationException {

    public OperacaoNaoPermitidaException(String motivo) {
        super(motivo);
    }
}
```

📌 Aqui o foco é **fluxo de uso**, não regra de negócio.

---

## 6. Exceções de Infraestrutura

### 6.1 Finalidade

Encapsular falhas técnicas e impedir vazamento de detalhes.

Exemplos:

- falha de conexão;
    
- erro de IO;
    
- corrupção de dados.
    

---

### 6.2 Exceção Base

```
public class InfrastructureException
        extends RuntimeException {

    public InfrastructureException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

6.3 Uso Correto

```
try {
    repository.save(entity);
} catch (PersistenceException e) {
    throw new InfrastructureException(
        "Erro ao persistir dados financeiros",
        e
    );
}
```

📌 Nunca propagar exceções técnicas diretamente.

---

## 7. Estratégia de Captura (Boundary)

### 7.1 Regra de Ouro

📌 **Exceções são capturadas apenas nos limites do sistema**:

- Controllers
    
- UI Adapters
    

Camadas internas **lançam**, não capturam.

---

## 8. Mapeamento de Exceções → Resposta ao Usuário

|Tipo de Exceção|Mensagem ao Usuário|
|---|---|
|DomainException|Regra violada (mensagem amigável)|
|ApplicationException|Operação inválida|
|InfrastructureException|Erro inesperado|

📌 Mensagens técnicas nunca chegam ao usuário final.

---

## 9. Logging

- DomainException → WARN
    
- ApplicationException → INFO
    
- InfrastructureException → ERROR
    

Exemplo:

```
log.error("Falha ao salvar despesa", exception);
```
## 10. Diretrizes de Testes

- Cada exceção de domínio deve ter **teste dedicado**;
    
- Casos de uso devem validar exceções esperadas;
    
- Erros técnicos devem ser simulados.
    

---

## 11. Decisões Arquiteturais Registradas

✔ Exceções semânticas  
✔ Separação clara por camada  
✔ Sem vazamento técnico  
✔ Captura apenas nos limites  
✔ Mensagens previsíveis