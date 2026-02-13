## 1. Premissas Arquiteturais (congeladas)

### 1.1 Stack assumida

- **Java 17+**
    
- **JPA (Jakarta Persistence)**
    
- **Hibernate** como provider
    
- **Spring Data JPA** para repositórios _(sem lógica de negócio)_
    

📌 Nada aqui impede troca futura de framework  
📌 Domínio **não depende** de Spring

---

## 2. Princípios de Mapeamento (muito importantes)

- Entidades refletem **exatamente** o Modelo Físico
    
- Nenhuma regra de negócio dentro da entidade
    
- Nenhuma query “esperta” no repositório
    
- Relacionamentos **explícitos**
    
- `UUID` como identificador padrão
    
- `BigDecimal` para valores monetários
    
- Datas:
    
    - `LocalDate` → datas financeiras
        
    - `LocalDateTime` → auditoria
        

---

## 3. Pacotes (organização limpa)

```
br.com.financecore
 ├── domain
 │   └── model
 ├── infrastructure
 │   └── persistence
 │       ├── entity
 │       └── repository
```

📌 Entidades JPA ficam em `infrastructure.persistence.entity`  
📌 Domínio puro pode ter modelos próprios depois (se quiser)

---

## 4. Entidades ORM

---

### 4.1 UsuarioEntity

```
@Entity
@Table(name = "usuario")
public class UsuarioEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    protected UsuarioEntity() {}

    public UsuarioEntity(String nome) {
        this.nome = nome;
        this.dataCriacao = LocalDateTime.now();
    }

    // getters
}
```

---

4.2 PreferenciasSistemaEntity

```
@Entity
@Table(name = "preferencias_sistema")
public class PreferenciasSistemaEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;

    @Column(nullable = false, length = 10)
    private String tema;

    @Column(nullable = false, length = 10)
    private String idioma;

    @Column(name = "notificacoes_ativas", nullable = false)
    private boolean notificacoesAtivas;

    protected PreferenciasSistemaEntity() {}
}
```

---

4.3 MesFinanceiroEntity (entidade mais crítica)

```
@Entity
@Table(
    name = "mes_financeiro",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"usuario_id", "mes", "ano"})
    }
)
public class MesFinanceiroEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;

    @Column(nullable = false)
    private int mes;

    @Column(nullable = false)
    private int ano;

    @Column(nullable = false, length = 10)
    private String status; // ABERTO | FECHADO

    @Column(name = "saldo_inicial", nullable = false, precision = 14, scale = 2)
    private BigDecimal saldoInicial;

    @Column(name = "saldo_final", precision = 14, scale = 2)
    private BigDecimal saldoFinal;

    protected MesFinanceiroEntity() {}
}
```

📌 **RN-01 e RN-02 protegidas no banco + aplicação**

---

4.4 CategoriaEntity

```
@Entity
@Table(name = "categoria")
public class CategoriaEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false)
    private boolean essencial;

    protected CategoriaEntity() {}
}
```

 4.5 ReceitaEntity

```
@Entity
@Table(name = "receita")
public class ReceitaEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "mes_financeiro_id", nullable = false)
    private MesFinanceiroEntity mesFinanceiro;

    @Column(nullable = false, length = 255)
    private String descricao;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false, length = 10)
    private String tipo; // FIXA | VARIAVEL

    @Column(name = "data_referencia", nullable = false)
    private LocalDate dataReferencia;

    protected ReceitaEntity() {}
}
```

4.6 DespesaEntity

```
@Entity
@Table(name = "despesa")
public class DespesaEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "mes_financeiro_id", nullable = false)
    private MesFinanceiroEntity mesFinanceiro;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private CategoriaEntity categoria;

    @Column(nullable = false, length = 255)
    private String descricao;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false, length = 15)
    private String tipo; // PONTUAL | FIXA | PARCELADA

    @Column(name = "metodo_pagamento", nullable = false, length = 50)
    private String metodoPagamento;

    protected DespesaEntity() {}
}
```

4.7 ParcelamentoEntity

```
@Entity
@Table(name = "parcelamento")
public class ParcelamentoEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "despesa_id", nullable = false)
    private DespesaEntity despesa;

    @Column(name = "valor_total", nullable = false, precision = 14, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "numero_parcelas", nullable = false)
    private int numeroParcelas;

    @Column(name = "parcela_atual", nullable = false)
    private int parcelaAtual;

    protected ParcelamentoEntity() {}
}
```

4.8 ParcelaEntity

```
@Entity
@Table(name = "parcela")
public class ParcelaEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "parcelamento_id", nullable = false)
    private ParcelamentoEntity parcelamento;

    @ManyToOne
    @JoinColumn(name = "mes_financeiro_id", nullable = false)
    private MesFinanceiroEntity mesFinanceiro;

    @Column(nullable = false)
    private int numero;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false, length = 10)
    private String status; // PAGA | PENDENTE

    protected ParcelaEntity() {}
}
```

4.9 HistoricoFinanceiroEntity

```
@Entity
@Table(name = "historico_financeiro")
public class HistoricoFinanceiroEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;

    @Column(name = "periodo_inicio", nullable = false)
    private LocalDate periodoInicio;

    @Column(name = "periodo_fim", nullable = false)
    private LocalDate periodoFim;

    @Column(name = "total_receitas", nullable = false, precision = 14, scale = 2)
    private BigDecimal totalReceitas;

    @Column(name = "total_despesas", nullable = false, precision = 14, scale = 2)
    private BigDecimal totalDespesas;

    @Column(name = "saldo_final", nullable = false, precision = 14, scale = 2)
    private BigDecimal saldoFinal;

    @Column(name = "data_geracao", nullable = false)
    private LocalDateTime dataGeracao;

    protected HistoricoFinanceiroEntity() {}
}
```

## 5. Repositórios (Spring Data)

⚠️ **Regra de ouro:**  
Repositório **não contém regra de negócio**, apenas acesso a dados.

---
 
 5.1 MesFinanceiroRepository

```
public interface MesFinanceiroRepository
        extends JpaRepository<MesFinanceiroEntity, UUID> {

    Optional<MesFinanceiroEntity> findByUsuarioAndMesAndAno(
        UsuarioEntity usuario, int mes, int ano
    );

    List<MesFinanceiroEntity> findByUsuarioAndStatus(
        UsuarioEntity usuario, String status
    );
}
```

5.2 DespesaRepository

```
public interface DespesaRepository
        extends JpaRepository<DespesaEntity, UUID> {

    List<DespesaEntity> findByMesFinanceiroId(UUID mesFinanceiroId);
}
```

5.3 ReceitaRepository

```
public interface ReceitaRepository
        extends JpaRepository<ReceitaEntity, UUID> {

    List<ReceitaEntity> findByMesFinanceiroId(UUID mesFinanceiroId);
}
```

5.4 ParcelaRepository

```
public interface ParcelaRepository
        extends JpaRepository<ParcelaEntity, UUID> {

    List<ParcelaEntity> findByMesFinanceiroId(UUID mesFinanceiroId);

}
```

## 6. O que foi feito certo aqui

✔ Mapeamento fiel ao banco  
✔ Nenhuma regra duplicada  
✔ Preparado para:

- testes
    
- migrações
    
- serviços
    
- IA
    
- relatórios complexos
    

✔ Código **limpo, previsível e evolutivo**