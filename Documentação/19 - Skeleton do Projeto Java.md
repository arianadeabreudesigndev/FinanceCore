
## 1. Objetivo do Skeleton

Este skeleton define:

- estrutura de módulos e pacotes;
    
- responsabilidades por camada;
    
- regras rígidas de dependência;
    
- convenções de nomenclatura;
    
- base para crescimento sem acoplamento.
    

📌 **Nenhuma regra de negócio nasce aqui** — tudo já está nos documentos anteriores.  
Aqui só **materializamos a arquitetura**.

---

## 2. Visão Geral da Arquitetura

**Arquitetura em camadas, inspirada em Clean Architecture / Hexagonal**, adaptada ao contexto desktop/offline.

### Camadas (de dentro para fora):

1. **Domain** – núcleo do negócio
    
2. **Application** – casos de uso
    
3. **Interface (UI Adapters)** – controllers / interação
    
4. **Infrastructure** – persistência, criptografia, IO
    

📌 Regra de ouro:

> **Camadas externas dependem das internas. Nunca o contrário.**

---

## 3. Estrutura de Módulos (Gradle / Maven)

### Opção recomendada: **Multi-module**

```
finance-core/
│
├── finance-domain/
├── finance-application/
├── finance-interface/
├── finance-infrastructure/
├── finance-bootstrap/
└── build.gradle (ou pom.xml pai)
```

### Responsabilidade de cada módulo

|Módulo|Responsabilidade|
|---|---|
|domain|Entidades, VOs, Regras, Interfaces|
|application|Casos de uso, serviços, transações|
|interface|Controllers, DTOs, UI adapters|
|infrastructure|ORM, repositórios, crypto, storage|
|bootstrap|Configuração e inicialização|

---

## 4. Módulo: finance-domain (Coração do Sistema)

📌 **ZERO dependência de framework**.

```
finance-domain
└── src/main/java
    └── com.financecore.domain
        ├── model
        │   ├── entity
        │   ├── valueobject
        │   └── enum
        │
        ├── repository
        ├── service
        ├── exception
        └── rule
```

### Conteúdo permitido aqui

- Entidades (Despesa, Receita, MêsFinanceiro…)
    
- Value Objects (Money, Periodo, CategoriaId…)
    
- Interfaces de repositório
    
- Exceções de domínio
    
- Regras de negócio (invariantes)
    

🚫 Proibido:

- DTO
    
- ORM
    
- SQL
    
- Anotações de framework
    

---

## 5. Módulo: finance-application (Casos de Uso)

Aqui vivem **os fluxos de negócio**, não regras isoladas.

```
finance-application
└── src/main/java
    └── com.financecore.application
        ├── usecase
        │   ├── despesa
        │   ├── receita
        │   └── mesfinanceiro
        │
        ├── service
        ├── transaction
        ├── port
        └── exception
```

Estrutura típica de um caso de uso

```
registrardespesa/
├── RegistrarDespesaUseCase.java
├── RegistrarDespesaCommand.java
└── RegistrarDespesaResult.java
```

📌 Aqui:

- orquestra entidades;
    
- aplica regras RN;
    
- controla transações;
    
- **não conhece UI nem banco**.
    

---

## 6. Módulo: finance-interface (UI Adapters)

Responsável por **entrada e saída**.

```
finance-interface
└── src/main/java
    └── com.financecore.interfaceadapter
        ├── controller
        ├── dto
        │   ├── request
        │   └── response
        ├── mapper
        └── exception
```

### Princípios

- DTO ≠ Entidade
    
- Mapper obrigatório
    
- Controller só delega
    

📌 Pode ser:

- JavaFX
    
- Swing
    
- CLI
    
- API REST (no futuro)
    

---

## 7. Módulo: finance-infrastructure

Aqui fica tudo que **pode mudar**.

```
finance-infrastructure
└── src/main/java
    └── com.financecore.infrastructure
        ├── persistence
        │   ├── entity
        │   ├── repository
        │   └── mapper
        │
        ├── crypto
        ├── migration
        ├── config
        └── exception
```

### Contém

- Implementações ORM
    
- SQL / DDL
    
- Criptografia
    
- Migrações de dados
    
- Configurações técnicas
    

📌 Implementa interfaces do `domain`.

---

## 8. Módulo: finance-bootstrap

Ponto de entrada do sistema.

```
finance-bootstrap
└── src/main/java
    └── com.financecore.bootstrap
        ├── MainApplication.java
        └── AppConfig.java
```

Responsável por:

- instanciar dependências;
    
- conectar camadas;
    
- iniciar UI.
    

---

## 9. Convenções Rígidas

### Pacotes

- `domain` nunca importa nada externo
    
- `application` só depende de `domain`
    
- `interface` depende de `application`
    
- `infrastructure` implementa `domain`
    

### Nomes

- UseCase sempre termina com `UseCase`
    
- DTO sempre `Request` / `Response`
    
- Repositório = interface no domain, impl no infra
    

---

## 10. Resultado Prático

Com esse skeleton:

- você pode começar a codar **sem decisões arquiteturais pendentes**;
    
- qualquer novo módulo entra sem quebrar nada;
    
- testes ficam naturais;
    
- evolução para cloud/mobile é trivial.