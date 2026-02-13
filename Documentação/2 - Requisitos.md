# Documento de Requisitos

## 1. Introdução

Este documento descreve de forma detalhada os **Requisitos Funcionais (RF)** e **Requisitos Não Funcionais (RNF)** do sistema **Finance Core**, seguindo um padrão formal e reutilizável para projetos profissionais de software.

Todos os requisitos estão identificados, associados a casos de uso, descritos de forma objetiva e classificados por prioridade:

- **Fundamental**: indispensável para o funcionamento do sistema.
    
- **Importante**: agrega valor significativo, mas não impede o uso básico.
    
- **Desejável**: melhorias ou diferenciais futuros.
    

---

## 2. Requisitos Funcionais (RF)

### RF01 – Cadastro de Usuário

|Identificador|RF01|
|---|---|
|**Casos de Uso**|Cadastro de Usuário|
|**Descrição**|O sistema deve permitir que o usuário crie um perfil local, contendo nome, e-mail (opcional) e configurações iniciais de uso.|
|**Prioridade**|Fundamental|

---

### RF02 – Autenticação de Usuário

|Identificador|RF02|
|---|---|
|**Casos de Uso**|Autenticação|
|**Descrição**|O sistema deve permitir que o usuário acesse o sistema por meio de autenticação local, garantindo a proteção dos dados financeiros.|
|**Prioridade**|Fundamental|

---

### RF03 – Cadastro de Fontes de Renda

|Identificador|RF03|
|---|---|
|**Casos de Uso**|Gerenciar Rendas|
|**Descrição**|O sistema deve permitir o cadastro de fontes de renda, classificadas como fixas ou variáveis, com valor e periodicidade.|
|**Prioridade**|Fundamental|

---

### RF04 – Registro de Despesas

|Identificador|RF04|
|---|---|
|**Casos de Uso**|Registrar Despesa|
|**Descrição**|O sistema deve permitir o registro de despesas financeiras, informando valor, data, categoria e método de pagamento.|
|**Prioridade**|Fundamental|

---

### RF05 – Cadastro de Despesas Fixas

|Identificador|RF05|
|---|---|
|**Casos de Uso**|Gerenciar Despesas Fixas|
|**Descrição**|O sistema deve permitir o cadastro de despesas recorrentes, que impactam automaticamente os meses subsequentes.|
|**Prioridade**|Fundamental|

---

### RF06 – Classificação de Despesas

|Identificador|RF06|
|---|---|
|**Casos de Uso**|Classificar Despesa|
|**Descrição**|O sistema deve permitir classificar despesas em categorias personalizadas e marcá-las como essenciais ou não essenciais.|
|**Prioridade**|Importante|

---

### RF07 – Registro de Parcelamentos

|Identificador|RF07|
|---|---|
|**Casos de Uso**|Gerenciar Parcelamentos|
|**Descrição**|O sistema deve permitir o registro de despesas parceladas, distribuindo automaticamente os valores pelos meses futuros.|
|**Prioridade**|Fundamental|

---

### RF08 – Cálculo Automático de Saldo Mensal

|Identificador|RF08|
|---|---|
|**Casos de Uso**|Consolidar Mês Financeiro|
|**Descrição**|O sistema deve calcular automaticamente o saldo mensal com base nas receitas e despesas registradas.|
|**Prioridade**|Fundamental|

---

### RF09 – Fechamento de Mês Financeiro

|Identificador|RF09|
|---|---|
|**Casos de Uso**|Fechar Mês|
|**Descrição**|O sistema deve permitir o fechamento do mês financeiro, impedindo alterações diretas nos dados consolidados.|
|**Prioridade**|Importante|

---

### RF10 – Detecção de Inconsistência Financeira

|Identificador|RF10|
|---|---|
|**Casos de Uso**|Auditoria Financeira|
|**Descrição**|O sistema deve comparar o saldo esperado com o saldo informado pelo usuário e sinalizar divergências.|
|**Prioridade**|Importante|

---

### RF11 – Geração de Relatórios Financeiros

|Identificador|RF11|
|---|---|
|**Casos de Uso**|Gerar Relatório|
|**Descrição**|O sistema deve gerar relatórios financeiros mensais e históricos consolidados.|
|**Prioridade**|Importante|

---

### RF12 – Exportação de Relatórios

|Identificador|RF12|
|---|---|
|**Casos de Uso**|Exportar Dados|
|**Descrição**|O sistema deve permitir a exportação de relatórios em formato digital (ex.: PDF ou CSV).|
|**Prioridade**|Desejável|

---

### RF13 – Assistente Financeiro por IA

|Identificador|RF13|
|---|---|
|**Casos de Uso**|Interação por IA|
|**Descrição**|O sistema deve permitir que o usuário registre e consulte informações financeiras por meio de linguagem natural.|
|**Prioridade**|Desejável|

---

## 3. Requisitos Não Funcionais (RNF)

### RNF01 – Funcionamento Offline

|Identificador|RNF01|
|---|---|
|**Casos de Uso**|Todos|
|**Descrição**|O sistema deve funcionar integralmente sem conexão com a internet.|
|**Prioridade**|Fundamental|

---

### RNF02 – Persistência Local Segura

|Identificador|RNF02|
|---|---|
|**Casos de Uso**|Persistência de Dados|
|**Descrição**|Os dados devem ser armazenados localmente de forma segura, com mecanismos de criptografia.|
|**Prioridade**|Fundamental|

---

### RNF03 – Performance

|Identificador|RNF03|
|---|---|
|**Casos de Uso**|Consultas e Relatórios|
|**Descrição**|O sistema deve responder às operações principais em tempo inferior a 1 segundo para bases de dados médias.|
|**Prioridade**|Importante|

---

### RNF04 – Escalabilidade Modular

|Identificador|RNF04|
|---|---|
|**Casos de Uso**|Evolução do Sistema|
|**Descrição**|A arquitetura deve permitir a adição de novos módulos sem impacto nos existentes.|
|**Prioridade**|Fundamental|

---

### RNF05 – Manutenibilidade

|Identificador|RNF05|
|---|---|
|**Casos de Uso**|Manutenção|
|**Descrição**|O código deve seguir padrões que facilitem manutenção, testes e evolução.|
|**Prioridade**|Importante|

---

### RNF06 – Usabilidade

|Identificador|RNF06|
|---|---|
|**Casos de Uso**|Interação do Usuário|
|**Descrição**|A interface deve ser intuitiva e exigir o mínimo de esforço cognitivo do usuário.|
|**Prioridade**|Importante|

---

### RNF07 – Privacidade dos Dados

|Identificador|RNF07|
|---|---|
|**Casos de Uso**|Todos|
|**Descrição**|Os dados financeiros do usuário não devem ser compartilhados sem consentimento explícito.|
|**Prioridade**|Fundamental|

---

### RNF08 – Portabilidade

|Identificador|RNF08|
|---|---|
|**Casos de Uso**|Migração|
|**Descrição**|O sistema deve permitir futura migração para ambiente mobile ou cloud.|
|**Prioridade**|Desejável|


| Identificador    | [RNF09]                                                                                                                                                                                                                                    |
| ---------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **Casos de Uso** | Acessibilidade Visual (Tema Claro e Tema Escuro)                                                                                                                                                                                           |
| **Descrição**    | O sistema deve permitir ao usuário alternar entre tema claro e tema escuro, respeitando preferências visuais, critérios de acessibilidade e redução de fadiga ocular, podendo também herdar automaticamente o tema do sistema operacional. |
| **Prioridade**   | Importante                                                                                                                                                                                                                                 |
