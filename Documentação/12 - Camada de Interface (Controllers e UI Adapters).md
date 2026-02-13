## 1. Papel da Camada de Interface

A Camada de Interface é responsável **exclusivamente** por:

- receber entradas externas (UI, API, IA, CLI);
    
- validar formato e presença de dados;
    
- traduzir entrada externa → DTO de entrada;
    
- chamar a Application Layer;
    
- traduzir saída → DTO de resposta.
    

📌 **Ela NÃO contém**:

- regra de negócio;
    
- lógica de cálculo;
    
- acesso direto a repositório;
    
- dependência de entidade de domínio.
    

> É uma camada **adaptadora**, não inteligente.

---

## 2. Princípios Arquiteturais Obrigatórios

### 2.1 Regras que NÃO podem ser quebradas

❌ Controller não conhece Entity  
❌ Controller não chama Repository  
❌ Controller não decide comportamento  
❌ Controller não calcula saldo  
❌ Controller não “corrige” regra de negócio

### 2.2 Regras que DEVEM ser seguidas

✅ Controller conhece apenas:

- DTOs
    
- Use Cases (interfaces)
    
- tipos primitivos
    

✅ Controller:

- orquestra
    
- delega
    
- traduz
    

---

## 3. Tipos de UI Adapters no Finance Core

O projeto já nasce preparado para **múltiplas interfaces**:

### 3.1 UI Desktop (JavaFX / Swing)

- Adapter direto para Application Layer
    
- Sem HTTP
    
- Controllers “locais”
    

### 3.2 API (futura)

- REST ou GraphQL
    
- Mesmos DTOs
    
- Mesmos Use Cases
    

### 3.3 Interface por IA

- Texto natural
    
- Conversão → DTO
    
- Mesmo fluxo
    

📌 Isso só é possível porque você separou corretamente:

- Domínio
    
- Aplicação
    
- Interface
    

---

## 4. Estrutura de Pacotes (Interface Layer)

```
br.com.financecore.interfaceadapter
 ├── controller
 │    ├── MesFinanceiroController
 │    ├── DespesaController
 │    ├── RelatorioController
 │
 ├── request
 │    ├── RegistrarDespesaRequest
 │    ├── CriarMesFinanceiroRequest
 │
 └── response
      ├── MesFinanceiroResponse
      ├── DespesaResponse
```

📌 **Request/Response ≠ DTO**  
Eles são **formatos externos**, adaptados à interface.

---

## 5. Exemplo Completo — DespesaController

### 5.1 Request (Interface → Sistema)

```
public record RegistrarDespesaRequest(
    UUID mesFinanceiroId,
    UUID categoriaId,
    String descricao,
    BigDecimal valor,
    LocalDate data,
    String tipo,
    String metodoPagamento
) {}
```

📌 Aqui entram:

- validações básicas
    
- formato externo
    
- nada de regra de negócio
    

---

### 5.2 Controller

```
public class DespesaController {

    private final RegistrarDespesaUseCase registrarDespesaUseCase;

    public DespesaController(
            RegistrarDespesaUseCase registrarDespesaUseCase) {
        this.registrarDespesaUseCase = registrarDespesaUseCase;
    }

    public UUID registrarDespesa(
            RegistrarDespesaRequest request) {

        RegistrarDespesaInputDTO inputDTO =
            new RegistrarDespesaInputDTO(
                request.mesFinanceiroId(),
                request.categoriaId(),
                request.descricao(),
                request.valor(),
                request.data(),
                request.tipo(),
                request.metodoPagamento()
            );

        return registrarDespesaUseCase.executar(inputDTO);
    }
}
```

📌 Observe:

- Controller não conhece Entity
    
- Controller não valida regra
    
- Controller apenas traduz e delega
    

---

### 5.3 Response (Sistema → Interface)

```
public record DespesaResponse(
    UUID id,
    String descricao,
    BigDecimal valor,
    LocalDate data,
    String categoria,
    boolean essencial
) {}
```

Se a interface for desktop:

- retorna direto
    

Se for API:

- vira JSON
    

---

## 6. Fluxo Completo (importantíssimo)

```
UI / IA / API
   ↓
Request (Interface)
   ↓
Controller
   ↓
InputDTO
   ↓
Use Case (Application Layer)
   ↓
Domain
   ↓
OutputDTO
   ↓
Response
   ↓
UI / IA / API
```

📌 Esse fluxo **nunca** deve ser quebrado.  
É isso que garante longevidade do projeto.

---

## 7. Tratamento de Erros na Interface

### Regra importante:

> Controller não trata regra de negócio,  
> mas trata **exceção de apresentação**.

Exemplo:

- Domínio lança: `MesFinanceiroFechadoException`
    
- Controller:
    
    - captura
        
    - traduz para mensagem amigável
        
    - sem alterar o comportamento
        

---

## 8. Decisões Arquiteturais Registradas

✔ Controllers são finos  
✔ Interface não conhece domínio  
✔ Request/Response são adaptadores  
✔ DTOs continuam intactos  
✔ IA futura usa os mesmos controllers

Isso coloca seu projeto **num nível de arquitetura que muita empresa não tem**.