
## 1. Papel da Application Layer (bem definido)

A Camada de Aplicação é responsável por:

- Orquestrar **Casos de Uso**
    
- Coordenar entidades e repositórios
    
- Garantir execução correta das **Regras de Negócio**
    
- Controlar transações
    
- Servir de ponte entre:
    
    - Interface (UI / API / IA)
        
    - Domínio e Persistência
        

📌 **Ela NÃO:**

- contém lógica de interface
    
- conhece detalhes de banco
    
- implementa regra “matemática” de domínio pesado

---

## 2. Estrutura de Pacotes

```
br.com.financecore.application
 ├── service
 ├── usecase
 ├── command
 ├── query
 └── exception
```

### Significado:

- **usecase** → um caso de uso = uma intenção do usuário
    
- **service** → serviços de aplicação reutilizáveis
    
- **command** → dados de entrada (write)
    
- **query** → dados de saída (read)
    
- **exception** → erros semânticos do domínio

---
## 3. Padrão adotado para Casos de Uso

### Convenção

- Um caso de uso = **uma classe**
    
- Nome começa com verbo
    
- Método público único: `executar(...)`
    

Exemplo mental:

> “Registrar Despesa”  
> → `RegistrarDespesaUseCase`

---

## 4. Exceções de Aplicação

### 4.1 Exceção base

```
public abstract class ApplicationException extends RuntimeException {

    protected ApplicationException(String message) {
        super(message);
    }
}
```

4.2 Exceções específicas

```
public class MesFinanceiroFechadoException
        extends ApplicationException {

    public MesFinanceiroFechadoException() {
        super("Mês financeiro está fechado e não pode ser alterado.");
    }
}
```

```
public class MesFinanceiroInexistenteException
        extends ApplicationException {

    public MesFinanceiroInexistenteException() {
        super("Mês financeiro não encontrado.");
    }
}
```

## 5. Caso de Uso — Criar Mês Financeiro

### 5.1 Command

```
public record CriarMesFinanceiroCommand(
    UUID usuarioId,
    int mes,
    int ano,
    BigDecimal saldoInicial
) {}
```

5.2 Use Case

```
@Service
public class CriarMesFinanceiroUseCase {

    private final UsuarioRepository usuarioRepository;
    private final MesFinanceiroRepository mesRepository;

    public CriarMesFinanceiroUseCase(
            UsuarioRepository usuarioRepository,
            MesFinanceiroRepository mesRepository) {
        this.usuarioRepository = usuarioRepository;
        this.mesRepository = mesRepository;
    }

    @Transactional
    public UUID executar(CriarMesFinanceiroCommand command) {

        UsuarioEntity usuario = usuarioRepository
                .findById(command.usuarioId())
                .orElseThrow();

        mesRepository.findByUsuarioAndMesAndAno(
                usuario, command.mes(), command.ano()
        ).ifPresent(m -> {
            throw new IllegalStateException(
                "Mês financeiro já existe."
            );
        });

        MesFinanceiroEntity mes = new MesFinanceiroEntity(
            usuario,
            command.mes(),
            command.ano(),
            command.saldoInicial()
        );

        mesRepository.save(mes);
        return mes.getId();
    }
}
```

📌 **RN-01 garantida aqui**

---

## 6. Caso de Uso — Registrar Despesa

### 6.1 Command

```
public record RegistrarDespesaCommand(
    UUID mesFinanceiroId,
    UUID categoriaId,
    String descricao,
    BigDecimal valor,
    LocalDate data,
    String tipo,
    String metodoPagamento
) {}
```

6.2 Use Case

```
@Service
public class RegistrarDespesaUseCase {

    private final MesFinanceiroRepository mesRepository;
    private final CategoriaRepository categoriaRepository;
    private final DespesaRepository despesaRepository;

    public RegistrarDespesaUseCase(
            MesFinanceiroRepository mesRepository,
            CategoriaRepository categoriaRepository,
            DespesaRepository despesaRepository) {
        this.mesRepository = mesRepository;
        this.categoriaRepository = categoriaRepository;
        this.despesaRepository = despesaRepository;
    }

    @Transactional
    public UUID executar(RegistrarDespesaCommand command) {

        MesFinanceiroEntity mes = mesRepository
                .findById(command.mesFinanceiroId())
                .orElseThrow(MesFinanceiroInexistenteException::new);

        if ("FECHADO".equals(mes.getStatus())) {
            throw new MesFinanceiroFechadoException();
        }

        CategoriaEntity categoria = categoriaRepository
                .findById(command.categoriaId())
                .orElseThrow();

        DespesaEntity despesa = new DespesaEntity(
            mes,
            categoria,
            command.descricao(),
            command.valor(),
            command.data(),
            command.tipo(),
            command.metodoPagamento()
        );

        despesaRepository.save(despesa);
        return despesa.getId();
    }
}
```

📌 **RN-02 e RN-04 aplicadas**

---

## 7. Caso de Uso — Classificar Despesa (UC-06)

```
@Service
public class ClassificarDespesaUseCase {

    private final DespesaRepository despesaRepository;
    private final CategoriaRepository categoriaRepository;

    public ClassificarDespesaUseCase(
            DespesaRepository despesaRepository,
            CategoriaRepository categoriaRepository) {
        this.despesaRepository = despesaRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public void executar(UUID despesaId, UUID categoriaId) {

        DespesaEntity despesa = despesaRepository
                .findById(despesaId)
                .orElseThrow();

        CategoriaEntity categoria = categoriaRepository
                .findById(categoriaId)
                .orElseThrow();

        despesa.classificar(categoria);
    }
}
```

📌 **Esse UC é simples, mas essencial para IA futura**

---

## 8. Caso de Uso — Fechar Mês Financeiro

```
@Service
public class FecharMesFinanceiroUseCase {

    private final MesFinanceiroRepository mesRepository;
    private final ReceitaRepository receitaRepository;
    private final DespesaRepository despesaRepository;

    public FecharMesFinanceiroUseCase(
            MesFinanceiroRepository mesRepository,
            ReceitaRepository receitaRepository,
            DespesaRepository despesaRepository) {
        this.mesRepository = mesRepository;
        this.receitaRepository = receitaRepository;
        this.despesaRepository = despesaRepository;
    }

    @Transactional
    public void executar(UUID mesId) {

        MesFinanceiroEntity mes = mesRepository
                .findById(mesId)
                .orElseThrow();

        BigDecimal totalReceitas =
            receitaRepository.findByMesFinanceiroId(mesId)
                .stream()
                .map(ReceitaEntity::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDespesas =
            despesaRepository.findByMesFinanceiroId(mesId)
                .stream()
                .map(DespesaEntity::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal saldoFinal =
            mes.getSaldoInicial()
               .add(totalReceitas)
               .subtract(totalDespesas);

        mes.fechar(saldoFinal);
    }
}
```

📌 **RN-07 aplicada de forma determinística**

---

## 9. O que você tem agora (nível profissional)

✔ Casos de Uso isolados  
✔ Transações bem posicionadas  
✔ Regras de negócio respeitadas  
✔ Código testável  
✔ Base sólida para:

- Controllers
    
- Desktop UI
    
- IA
    
- Automação