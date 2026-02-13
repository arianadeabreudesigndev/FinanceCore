
## 1. Objetivo

Esta política define **como o Finance Core evolui o banco de dados ao longo do tempo**, garantindo:

- integridade dos dados financeiros;
    
- rastreabilidade das mudanças;
    
- possibilidade de rollback controlado;
    
- compatibilidade entre versões do software e do schema.
    

📌 Em sistemas financeiros, **migração mal definida = perda de confiança**.

---

## 2. Princípios Fundamentais

Toda migração deve obedecer:

1. **Dados nunca são descartados silenciosamente**
    
2. **Migrações são determinísticas**
    
3. **O estado do banco é sempre reprodutível**
    
4. **Versão do schema acompanha versão da aplicação**
    
5. **Migração é código, não script solto**
    

---

## 3. Versionamento do Banco de Dados

### 3.1 Versão do Schema

O banco possui uma versão explícita:

```
schema_version = MAJOR.MINOR.PATCH
```

📌 Essa versão **não é a mesma da aplicação**, mas deve ser compatível com ela.

---

### 3.2 Tabela de Controle de Migrações

Obrigatória em qualquer ambiente:

```
schema_migration (
    id                  BIGINT PK,
    version             VARCHAR(20) UNIQUE,
    description         VARCHAR(255),
    checksum            VARCHAR(64),
    executed_at         TIMESTAMP,
    execution_time_ms   BIGINT,
    status              ENUM('SUCCESS', 'FAILED')
)
```

Essa tabela é a **fonte da verdade** do estado do banco.

---

## 4. Ferramenta de Migração

### 4.1 Abordagem

- migrações **versionadas**
    
- executadas automaticamente no startup
    
- nunca manuais em produção
    

Ferramentas compatíveis:

- Flyway (preferencial)
    
- Liquibase (aceitável)
    

---

### 4.2 Convenção de Arquivos

Formato obrigatório:

```
V<major>_<minor>_<patch>__<descricao>.sql
```

Exemplo:

```
V1_2_0__criar_tabela_despesa_fixa.sql
```
## 5. Tipos de Migração

### 5.1 Migração Estrutural

✔ criação de tabelas  
✔ alteração de colunas  
✔ índices  
✔ constraints

Exemplo:

```
ALTER TABLE despesa
ADD COLUMN categoria_id BIGINT NOT NULL;
```

### 5.2 Migração de Dados

✔ transformação de dados existentes  
✔ normalização  
✔ preenchimento de novos campos

⚠️ Sempre idempotente ou controlada por versão.

---

### 5.3 Migração de Correção

- corrige erro de migração anterior
    
- nunca edita migração já aplicada
    
- sempre cria nova versão
    

---

## 6. Política de Rollback

### 6.1 Regra de Ouro

❌ **Rollback automático em produção é proibido**

---

### 6.2 Estratégia Correta

- rollback via **nova migração**
    
- dados preservados
    
- lógica compensatória explícita
    

Exemplo:

```
V1_2_1__rollback_categoria.sql
```

## 7. Compatibilidade Aplicação × Banco

### 7.1 Regra

Uma versão da aplicação deve ser compatível com:

- versão atual do schema
    
- no máximo **1 versão anterior**
    

📌 Isso permite deploy seguro e atualização gradual.

---

### 7.2 Estratégia de Transição

- campos novos começam **nullable**
    
- código aceita ambos os estados
    
- migração posterior torna obrigatório
    

---

## 8. Ambientes

### 8.1 Desenvolvimento

- migração automática no startup
    
- reset permitido
    
- dados descartáveis
    

---

### 8.2 Produção

- migração automática **com validação**
    
- logs obrigatórios
    
- falha bloqueia inicialização
    

---

## 9. Criptografia e Migrações

### 9.1 Regras

- dados criptografados **não são recriptografados sem necessidade**
    
- mudança de algoritmo exige:
    
    - migração controlada
        
    - versão intermediária
        
    - testes de integridade
        

---

## 10. Auditoria e Rastreabilidade

Cada migração deve:

- ter descrição clara;
    
- checksum validado;
    
- log persistido;
    
- ser revisada em PR.
    

📌 Migração sem PR = violação de política.

---

## 11. Testes de Migração

Obrigatórios:

- migração em banco vazio;
    
- migração incremental;
    
- migração com dados reais simulados.
    

Falha em qualquer cenário **bloqueia merge**.

---

## 12. Proibições Explícitas

❌ editar migração já aplicada  
❌ rodar SQL manual em produção  
❌ apagar dados financeiros sem versão  
❌ “corrigir direto no banco”

---

## 13. Definition of Done (Migração)

Uma migração só é considerada válida se:

- versionada;
    
- testada;
    
- revisada;
    
- compatível com aplicação;
    
- auditável.

---
## Encerramento

Essa política garante que o Finance Core:

- nunca perca dados;
    
- evolua com segurança;
    
- permaneça confiável;
    
- suporte crescimento de longo prazo.