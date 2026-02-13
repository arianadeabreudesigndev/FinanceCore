## Entrada e Saída Limpa — Finance Core

---

## 1. Por que DTOs existem neste projeto (decisão arquitetural)

No Finance Core, **DTOs não são opcionais**. Eles existem para:

- isolar a Application Layer de:
    
    - entidades JPA;
        
    - detalhes de persistência;
        
- evitar vazamento de domínio para UI/API/IA;
    
- permitir evolução futura (desktop, mobile, IA, API REST) **sem quebrar contratos**;
    
- garantir **segurança, estabilidade e versionamento**.
    

📌 **Regra de ouro**:

> Nenhuma Entity sai da Application Layer.  
> Entrada e saída SEMPRE passam por DTO.

---

## 2. Classificação dos DTOs

Vamos padronizar **três tipos**, sem exagero:

```
br.com.financecore.application.dto
 ├── input
 ├── output
 └── mapper
```

### 2.1 DTO de Entrada (Input DTO)

- Representa **dados que entram no sistema**
    
- Validáveis
    
- Imutáveis
    
- Não contêm lógica
    

### 2.2 DTO de Saída (Output DTO)

- Representa **dados que saem do sistema**
    
- Modelados para leitura
    
- Nunca expõem entidades internas
    

### 2.3 Mapper

- Responsável por **converter**
    
- Sem regra de negócio
    
- Código determinístico
    

---

## 3. DTOs de Entrada (Input)

### 3.1 Criar Mês Financeiro

```
public record CriarMesFinanceiroInputDTO(
    int mes,
    int ano,
    BigDecimal saldoInicial
) {}
```

📌 Observação:

- `usuarioId` não entra aqui → vem do contexto (sessão / app / IA)
    

---

### 3.2 Registrar Despesa

```
public record RegistrarDespesaInputDTO(
    UUID mesFinanceiroId,
    UUID categoriaId,
    String descricao,
    BigDecimal valor,
    LocalDate data,
    String tipo,
    String metodoPagamento
) {}
```

Alinhado diretamente com:

- RF04
    
- UC-04
    
- RN-04
    

---

### 3.3 Classificar Despesa

```
public record ClassificarDespesaInputDTO(
    UUID despesaId,
    UUID categoriaId
) {}
```

Simples, direto, sem ruído.

---

## 4. DTOs de Saída (Output)

### 4.1 Visão Resumida de Despesa

```
public record DespesaResumoOutputDTO(
    UUID id,
    String descricao,
    BigDecimal valor,
    LocalDate data,
    String categoria,
    boolean essencial
) {}
```

📌 Ideal para:

- listagens
    
- dashboards
    
- IA (resposta natural)
    

---

### 4.2 Visão do Mês Financeiro

```
public record MesFinanceiroOutputDTO(
    UUID id,
    int mes,
    int ano,
    BigDecimal saldoInicial,
    BigDecimal saldoFinal,
    String status
) {}
```

📌 `saldoFinal` pode ser `null` se o mês estiver aberto (consistência com RN-02).

---

### 4.3 Relatório Consolidado


```
public record RelatorioFinanceiroOutputDTO(
    LocalDate periodoInicio,
    LocalDate periodoFim,
    BigDecimal totalReceitas,
    BigDecimal totalDespesas,
    BigDecimal saldoConsolidado
) {}
```

Usado por:

- RF11
    
- RF12
    
- IA futura
    

---

## 5. Mappers (conversão limpa e explícita)

### 5.1 Mapper de Despesa

```
public final class DespesaMapper {

    private DespesaMapper() {}

    public static DespesaResumoOutputDTO toResumoDTO(
            DespesaEntity entity) {

        return new DespesaResumoOutputDTO(
            entity.getId(),
            entity.getDescricao(),
            entity.getValor(),
            entity.getData(),
            entity.getCategoria().getNome(),
            entity.getCategoria().isEssencial()
        );
    }
}
```

📌 Características:

- estático
    
- puro
    
- testável
    
- sem dependência de framework
    

---

### 5.2 Mapper de Mês Financeiro

```
public final class MesFinanceiroMapper {

    private MesFinanceiroMapper() {}

    public static MesFinanceiroOutputDTO toDTO(
            MesFinanceiroEntity entity) {

        return new MesFinanceiroOutputDTO(
            entity.getId(),
            entity.getMes(),
            entity.getAno(),
            entity.getSaldoInicial(),
            entity.getSaldoFinal(),
            entity.getStatus().name()
        );
    }
}
```

## 6. Integração DTO → Use Case (fluxo correto)

Exemplo real de uso dentro da Application Layer:

```
public UUID registrarDespesa(
        RegistrarDespesaInputDTO dto) {

    RegistrarDespesaCommand command =
        new RegistrarDespesaCommand(
            dto.mesFinanceiroId(),
            dto.categoriaId(),
            dto.descricao(),
            dto.valor(),
            dto.data(),
            dto.tipo(),
            dto.metodoPagamento()
        );

    return registrarDespesaUseCase.executar(command);
}
```

📌 DTO **nunca** conversa direto com Entity.  
📌 Mapper **nunca** contém regra de negócio.

---

## 7. Decisões arquiteturais importantes (registradas)

✔ DTOs são imutáveis (`record`)  
✔ Nenhuma Entity é exposta  
✔ Mappers são manuais (sem MapStruct por enquanto)  
✔ Separação clara entre:

- Input
    
- Command
    
- Entity
    
- Output
    

Isso evita:

- acoplamento
    
- vazamento de domínio
    
- bugs silenciosos
    

---

## 8. Estado atual da arquitetura

Neste ponto, você tem:

- ✅ Domínio sólido
    
- ✅ Regras de negócio formais
    
- ✅ Casos de uso claros
    
- ✅ Repositórios definidos
    
- ✅ DTOs e mapeamento limpo
    
