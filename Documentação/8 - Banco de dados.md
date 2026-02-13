
## 1. Decisão Técnica (congelada aqui)

### 1.1 SGBD escolhido: **PostgreSQL**

**Justificativas técnicas (objetivas):**

- Tipos ricos (`UUID`, `NUMERIC`, `JSONB`)
    
- Integridade referencial forte
    
- Ótimo suporte a índices, constraints e checks
    
- Preparado para:
    
    - desktop local
        
    - futura sincronização
        
    - migração para nuvem
        
- Excelente para **auditoria e histórico financeiro**
    

📌 **Nada aqui impede abstração por ORM depois**  
📌 SQL é a **fonte da verdade**

---

## 2. Princípios do Modelo Físico

- **Integridade > Conveniência**
    
- Nenhum dado financeiro sem FK
    
- Nenhum saldo editável manualmente
    
- Histórico **imutável**
    
- Preparado para:
    
    - consolidação
        
    - auditoria
        
    - IA
        
    - múltiplos perfis no futuro
        

---

## 3. Convenções Físicas

- Nomes em `snake_case`
    
- PKs com `UUID`
    
- Datas em `TIMESTAMP`
    
- Valores monetários em `NUMERIC(14,2)`
    
- Soft delete **somente quando fizer sentido**
    
- Criptografia **fora do banco**, mas banco preparado

---
## 4. Extensões Necessárias

```
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
```

---

### 5. Estrutura Física das Tabelas


```
CREATE TABLE usuario (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nome VARCHAR(120) NOT NULL,
    data_criacao TIMESTAMP NOT NULL DEFAULT NOW()
);
```

🔒 **Observação:**  
Nenhum dado sensível direto aqui. Autenticação pode evoluir sem quebrar o domínio.

### 5.2 Preferências do Sistema

```
CREATE TABLE preferencias_sistema (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    usuario_id UUID NOT NULL,
    tema VARCHAR(10) NOT NULL CHECK (tema IN ('CLARO', 'ESCURO')),
    idioma VARCHAR(10) NOT NULL DEFAULT 'pt-BR',
    notificacoes_ativas BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_pref_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuario(id)
        ON DELETE CASCADE
);
```

### 5.3 Mês Financeiro (núcleo absoluto)

```
CREATE TABLE mes_financeiro (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    usuario_id UUID NOT NULL,
    mes INTEGER NOT NULL CHECK (mes BETWEEN 1 AND 12),
    ano INTEGER NOT NULL CHECK (ano >= 2000),
    status VARCHAR(10) NOT NULL CHECK (status IN ('ABERTO', 'FECHADO')),
    saldo_inicial NUMERIC(14,2) NOT NULL,
    saldo_final NUMERIC(14,2),

    CONSTRAINT uq_mes_usuario UNIQUE (usuario_id, mes, ano),

    CONSTRAINT fk_mes_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuario(id)
        ON DELETE CASCADE
);
```

📌 **RN-01 e RN-02 garantidas aqui**  
📌 Um único mês por usuário/ano/mês

5.4 Receita

```
CREATE TABLE receita (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    mes_financeiro_id UUID NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    valor NUMERIC(14,2) NOT NULL CHECK (valor > 0),
    tipo VARCHAR(10) NOT NULL CHECK (tipo IN ('FIXA', 'VARIAVEL')),
    data_referencia DATE NOT NULL,

    CONSTRAINT fk_receita_mes
        FOREIGN KEY (mes_financeiro_id)
        REFERENCES mes_financeiro(id)
        ON DELETE RESTRICT
);
```

5.5 Categoria

```
CREATE TABLE categoria (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nome VARCHAR(100) NOT NULL,
    essencial BOOLEAN NOT NULL
);
```

5.6 Despesa

```
CREATE TABLE despesa (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    mes_financeiro_id UUID NOT NULL,
    categoria_id UUID,
    descricao VARCHAR(255) NOT NULL,
    valor NUMERIC(14,2) NOT NULL CHECK (valor > 0),
    data DATE NOT NULL,
    tipo VARCHAR(15) NOT NULL CHECK (tipo IN ('PONTUAL', 'FIXA', 'PARCELADA')),
    metodo_pagamento VARCHAR(50) NOT NULL,

    CONSTRAINT fk_despesa_mes
        FOREIGN KEY (mes_financeiro_id)
        REFERENCES mes_financeiro(id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_despesa_categoria
        FOREIGN KEY (categoria_id)
        REFERENCES categoria(id)
);
```

📌 Categoria pode ser nula **temporariamente**, mas será auditada (RN-04)

5.7 Parcelamento

```
CREATE TABLE parcelamento (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    despesa_id UUID NOT NULL,
    valor_total NUMERIC(14,2) NOT NULL CHECK (valor_total > 0),
    numero_parcelas INTEGER NOT NULL CHECK (numero_parcelas > 1),
    parcela_atual INTEGER NOT NULL DEFAULT 1,

    CONSTRAINT fk_parcelamento_despesa
        FOREIGN KEY (despesa_id)
        REFERENCES despesa(id)
        ON DELETE CASCADE
);
```

5.8 Parcela

```
CREATE TABLE parcela (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    parcelamento_id UUID NOT NULL,
    mes_financeiro_id UUID NOT NULL,
    numero INTEGER NOT NULL,
    valor NUMERIC(14,2) NOT NULL CHECK (valor > 0),
    status VARCHAR(10) NOT NULL CHECK (status IN ('PAGA', 'PENDENTE')),

    CONSTRAINT fk_parcela_parcelamento
        FOREIGN KEY (parcelamento_id)
        REFERENCES parcelamento(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_parcela_mes
        FOREIGN KEY (mes_financeiro_id)
        REFERENCES mes_financeiro(id)
        ON DELETE RESTRICT
);
```

5.9 Histórico Financeiro Consolidado

```
CREATE TABLE historico_financeiro (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    usuario_id UUID NOT NULL,
    periodo_inicio DATE NOT NULL,
    periodo_fim DATE NOT NULL,
    total_receitas NUMERIC(14,2) NOT NULL,
    total_despesas NUMERIC(14,2) NOT NULL,
    saldo_final NUMERIC(14,2) NOT NULL,
    data_geracao TIMESTAMP NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_historico_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuario(id)
        ON DELETE CASCADE
);
```

6. Índices Estratégicos (Performance real)

```
CREATE INDEX idx_mes_usuario_status
    ON mes_financeiro (usuario_id, status);

CREATE INDEX idx_despesa_mes
    ON despesa (mes_financeiro_id);

CREATE INDEX idx_parcela_mes
    ON parcela (mes_financeiro_id);
```

## 7. Queries Fundamentais (uso real)

Saldo mensal (determinístico)

```
SELECT
    m.id,
    SUM(r.valor) - SUM(d.valor) AS saldo
FROM mes_financeiro m
LEFT JOIN receita r ON r.mes_financeiro_id = m.id
LEFT JOIN despesa d ON d.mes_financeiro_id = m.id
WHERE m.id = :mesId
GROUP BY m.id;
```

Gastos não essenciais (auditoria)

```
SELECT d.*
FROM despesa d
JOIN categoria c ON c.id = d.categoria_id
WHERE c.essencial = FALSE
AND d.mes_financeiro_id = :mesId;
```

