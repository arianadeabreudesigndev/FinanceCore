# Política de Migração de Banco de Dados – Finance Core

---

## 1. Objetivo

Esta política define como o **Finance Core** evolui o banco de dados ao longo do tempo, garantindo:

- Integridade dos dados financeiros.
- Rastreabilidade das mudanças.
- Possibilidade de rollback controlado.
- Compatibilidade entre versões do software e do esquema de banco de dados.

Em sistemas financeiros, **uma migração mal definida equivale a perda de confiança**.

---

## 2. Princípios Fundamentais

Toda migração deve obedecer aos seguintes princípios:

1. **Dados nunca são descartados silenciosamente** – qualquer remoção ou transformação de dados deve ser explícita e versionada.
2. **Migrações são determinísticas** – executar a mesma migração em ambientes idênticos deve produzir o mesmo resultado.
3. **O estado do banco é sempre reprodutível** – a partir do histórico de migrações, é possível reconstruir qualquer versão anterior do esquema.
4. **A versão do esquema acompanha a versão da aplicação** – ambas evoluem de forma coordenada.
5. **Migração é código** – scripts de migração devem ser versionados, revisados e testados como qualquer outro artefato.

---

## 3. Versionamento do Banco de Dados

### 3.1 Versão do Esquema

O banco de dados possui uma versão explícita, no formato:

    schema_version = MAJOR.MINOR.PATCH


**Observação:** Esta versão **não é necessariamente igual à versão da aplicação**, mas deve ser compatível com ela. O mapeamento de compatibilidade é definido na seção 7.

### 3.2 Tabela de Controle de Migrações

A tabela abaixo é **obrigatória** em todos os ambientes e armazena o histórico completo de migrações aplicadas. Ela é a **fonte da verdade** do estado do banco.

```sql
CREATE TABLE schema_migration (
    id                  BIGINT PRIMARY KEY,
    version             VARCHAR(20) UNIQUE NOT NULL,
    description         VARCHAR(255) NOT NULL,
    checksum            VARCHAR(64) NOT NULL,
    executed_at         TIMESTAMP NOT NULL DEFAULT NOW(),
    execution_time_ms   BIGINT NOT NULL,
    status              VARCHAR(10) NOT NULL CHECK (status IN ('SUCCESS', 'FAILED'))
);

---

## 4. Ferramenta de Migração

### 4.1 Abordagem
Migrações versionadas e ordenadas.

Executadas automaticamente durante a inicialização da aplicação.

Nunca executadas manualmente em produção.

### 4.2 Ferramenta Preferencial
Ferramenta	Status	Justificativa
Flyway	Preferencial	Simplicidade, integração nativa com Spring Boot, suporte a múltiplos SGBDs.
Liquibase	Aceitável	Oferece maior flexibilidade, porém com maior complexidade.
### 4.3 Convenção de Nomenclatura de Arquivos
Os scripts de migração devem seguir o formato:

    V<major>_<minor>_<patch>__<descricao>.sql

Exemplos:

V1_0_0__criar_tabelas_iniciais.sql

V1_2_0__adicionar_coluna_categoria_despesa.sql

V2_0_1__rollback_categoria.sql

Regras:

Os números major, minor e patch são inteiros, separados por underscore.

O separador __ (dois underscores) antecede a descrição.

A descrição deve ser concisa e em português ou inglês, usando _ no lugar de espaços.

---

## 5. Tipos de Migração

### 5.1 Migração Estrutural

Altera a estrutura do banco de dados:

Criação, alteração ou remoção de tabelas.

Adição/remoção de colunas, constraints, índices.

Criação de schemas, sequências, visões, etc.

Exemplo:

    ALTER TABLE despesa
    ADD COLUMN categoria_id UUID;

### 5.2 Migração de Dados
Transforma ou popula dados existentes:

Correção de dados inconsistentes.

Preenchimento de novos campos com valores derivados.

Normalização de informações.

Requisito: Toda migração de dados deve ser idempotente ou controlada por versão para evitar duplicações.

### 5.3 Migração de Correção
Corrige uma migração anterior que foi aplicada com erro.

Nunca edita um script de migração já aplicado.

Sempre cria um novo script com versão superior.

---


## 6. Política de Rollback

### 6.1 Regra de Ouro
Rollback automático em produção é proibido.

### 6.2 Estratégia Correta para Reversão
Criar uma nova migração que desfaça as alterações indesejadas.

Preservar os dados originais sempre que possível.

Implementar lógica compensatória explícita.

Exemplo:

    V1_2_1__rollback_categoria.sql

    -- Remove a coluna adicionada e restaura estrutura anterior
    ALTER TABLE despesa DROP COLUMN categoria_id;

---

## 7. Compatibilidade Aplicação × Banco

### 7.1 Regra de Compatibilidade
Uma versão da aplicação deve ser compatível com:

A versão atual do esquema de banco de dados.

No máximo 1 versão anterior do esquema.

### 7.2 Estratégia de Transição
Novos campos e tabelas devem ser criados como nullable ou com valores padrão.

O código da aplicação deve aceitar tanto a presença quanto a ausência desses novos elementos.

Após um ciclo de release, uma migração posterior pode tornar o campo NOT NULL ou remover estruturas obsoletas.

---

## 8. Ambientes

### 8.1 Desenvolvimento
Migração automática na inicialização.

Reset total do banco permitido (ex.: spring.flyway.clean-disabled=false).

Dados descartáveis e recriáveis a qualquer momento.

### 8.2 Produção
Migração automática com validação rigorosa.

Logs detalhados obrigatórios.

Falha na migração bloqueia a inicialização da aplicação.

Rollback nunca automático; apenas via nova migração.

---

## 9. Criptografia e Migrações
### 9.1 Regras para Dados Criptografados
Dados já criptografados não devem ser recriptografados sem necessidade.

Qualquer mudança no algoritmo de criptografia deve ser tratada como uma migração controlada:

Versão intermediária que lê ambos os formatos.

Migração progressiva dos dados.

Testes de integridade obrigatórios antes e depois.

---

## 10. Auditoria e Rastreabilidade
Cada migração deve atender aos seguintes critérios:

Critério	Descrição
Descrição	O script deve conter comentários explicando o propósito da migração.
Checksum	O Flyway calcula e valida automaticamente; qualquer alteração no script já aplicado gera erro.
Log persistido	A tabela schema_migration registra cada execução.
Revisão	Toda migração deve passar por Pull Request e ser revisada por outro desenvolvedor.
Violação: Migração sem PR é considerada violação grave da política.

---

## 11. Testes de Migração
Os seguintes cenários de teste são obrigatórios antes de qualquer release:

Migração em banco vazio – aplica todas as migrações sequencialmente e verifica a estrutura final.

Migração incremental – aplica migrações a partir de uma versão anterior conhecida.

Migração com dados reais simulados – insere dados de teste e valida a integridade após as migrações.

Falha em qualquer um desses cenários bloqueia o merge do Pull Request.

---

## 12. Proibições Explícitas
❌ Editar uma migração que já foi aplicada em qualquer ambiente.

❌ Executar SQL manualmente em produção (exceto em situações de emergência aprovadas e documentadas).

❌ Apagar dados financeiros sem uma migração versionada que registre a operação.

❌ “Corrigir direto no banco” sem o correspondente script de migração.

---

## 13. Definition of Done (Migração)
Uma migração só é considerada pronta quando:

Versionada conforme convenção.

Testada nos três cenários obrigatórios.

Revisada em Pull Request.

Compatível com a versão atual e anterior da aplicação.

Auditável (checksum, descrição, log).

---

## 14. Encerramento
A adoção rigorosa desta política garante que o Finance Core:

Nunca perca dados por migrações mal planejadas.

Evolua com segurança, mesmo em ambientes críticos.

Permaneça confiável e auditável ao longo de todo o seu ciclo de vida.

Suporte crescimento de longo prazo com múltiplos módulos e futura sincronização em nuvem.

---

Documentos relacionados:

**Documentos relacionados:**  
- [01-visao-geral.md](01-visao-geral.md)  
- [02-requisitos.md](02-requisitos.md)  
- [03-casos-de-uso.md](03-casos-de-uso.md)  
- [04-modelo-dominio.md](04-modelo-dominio.md)  
- [05-regras-negocio.md](05-regras-negocio.md)  
- [06-arquitetura.md](06-arquitetura.md)  
- [07-modelo-dados.md](07-modelo-dados.md)  
- [09-testes.md](09-testes.md)  
- [10-contribuicao.md](10-contribuicao.md)
