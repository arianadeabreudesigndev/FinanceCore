## 1. Objetivo do Documento

Este documento define a **estratégia de leitura de dados (Read Model)** do sistema **Finance Core**, separando claramente:

- **modelo de escrita** (Domain + Application);
    
- **modelo de leitura** (Queries + Projections).
    

O objetivo é:

- melhorar performance;
    
- reduzir complexidade em consultas;
    
- evitar vazamento de entidades de domínio;
    
- preparar o sistema para crescimento futuro.
    

---

## 2. Decisão Arquitetural

### 2.1 Problema a evitar

Usar **Entities do domínio** para leitura gera:

- joins complexos;
    
- acoplamento excessivo;
    
- consultas lentas;
    
- código difícil de manter.
    

### 2.2 Decisão tomada

📌 **Separar claramente Write Model e Read Model**, sem adotar CQRS completo com event sourcing (overkill neste momento).

✔ Escrita → Use Cases + Domain  
✔ Leitura → Queries especializadas + Projections imutáveis

Essa decisão é **intencional, consciente e alinhada ao escopo atual**.

---

## 3. Princípios do Read Model

- Projections são **imutáveis**;
    
- Queries **não aplicam regras de negócio**;
    
- Queries **não retornam Entities**;
    
- Queries retornam **estruturas prontas para uso**;
    
- Queries são **otimizadas para leitura**, não para escrita.
    

---

## 4. Query vs Projection (conceito claro)

### 4.1 Query

- Classe responsável por **executar consultas**
    
- Pode usar SQL, JPQL ou Criteria
    
- Conhece apenas o modelo físico
    

Exemplo:

> “Buscar resumo mensal”  
> “Listar despesas por categoria”

---

### 4.2 Projection

- Estrutura de dados **somente leitura**
    
- Representa uma visão específica do domínio
    
- Não possui comportamento
    

Exemplo:

> ResumoMensalProjection  
> DespesaPorCategoriaProjection

---

## 5. Estrutura de Pacotes

```
br.com.financecore.readmodel
 ├── query
 │    ├── MesFinanceiroQuery
 │    ├── DespesaQuery
 │    ├── RelatorioQuery
 │
 └── projection
      ├── ResumoMensalProjection
      ├── DespesaCategoriaProjection
      ├── EvolucaoSaldoProjection
```

📌 Read Model **não depende** de Domain Layer.

---

## 6. Projections Definidas no Finance Core

### 6.1 ResumoMensalProjection

Usada em:

- Dashboard
    
- Fechamento de mês
    
- Auditoria

```
public record ResumoMensalProjection(
    UUID mesFinanceiroId,
    int mes,
    int ano,
    BigDecimal totalReceitas,
    BigDecimal totalDespesas,
    BigDecimal saldoFinal
) {}
```

### 6.2 DespesaCategoriaProjection

Usada em:

- Análise de gastos
    
- Gráficos
    
- IA financeira

```
public record DespesaCategoriaProjection(
    String categoria,
    boolean essencial,
    BigDecimal total
) {}
```

### 6.3 EvolucaoSaldoProjection

Usada em:

- Relatórios históricos
    
- Tendência financeira

```
public record EvolucaoSaldoProjection(
    int mes,
    int ano,
    BigDecimal saldoFinal
) {}
```

## 7. Exemplos de Queries (Java)

### 7.1 Resumo Mensal

```
public interface MesFinanceiroQuery {

    ResumoMensalProjection obterResumoMensal(UUID mesFinanceiroId);
}
```

Implementação:

```
public class MesFinanceiroQueryImpl
        implements MesFinanceiroQuery {

    private final EntityManager entityManager;

    public MesFinanceiroQueryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public ResumoMensalProjection obterResumoMensal(UUID id) {
        return entityManager.createQuery("""
            SELECT new br.com.financecore.readmodel.projection.ResumoMensalProjection(
                m.id,
                m.mes,
                m.ano,
                SUM(r.valor),
                SUM(d.valor),
                (SUM(r.valor) - SUM(d.valor))
            )
            FROM MesFinanceiro m
            LEFT JOIN m.receitas r
            LEFT JOIN m.despesas d
            WHERE m.id = :id
            GROUP BY m.id, m.mes, m.ano
        """, ResumoMensalProjection.class)
        .setParameter("id", id)
        .getSingleResult();
    }
}
```

📌 Query direta, sem entity carregada inteira.

---

## 8. Estratégias de Otimização

### 8.1 O que fazemos

✔ Projections pequenas  
✔ Consultas específicas  
✔ Sem N+1  
✔ Sem lazy loading  
✔ Sem conversão posterior

---

### 8.2 O que evitamos

❌ `SELECT *`  
❌ Retornar Entity  
❌ Cálculo no controller  
❌ Lógica em SQL que pertence ao domínio

---

## 9. Integração com Application Layer

📌 Regra importante:

- Use Case **pode chamar Query**
    
- Query **não chama Use Case**
    
- Query **não altera estado**
    

Exemplo:

```
ResumoMensalProjection resumo =
    mesFinanceiroQuery.obterResumoMensal(id);
```

## 10. Decisões Arquiteturais Registradas

✔ Read Model separado do Write Model  
✔ Queries são especializadas  
✔ Projections são imutáveis  
✔ Sem CQRS pesado  
✔ Preparado para crescimento futuro