# Casos de Uso Detalhados – Finance Core

---

## 1. Introdução

Este documento descreve de forma estruturada os **Casos de Uso (UC)** do sistema **Finance Core**, estabelecendo a relação direta entre os requisitos funcionais e o comportamento esperado do sistema.

Cada caso de uso é apresentado com:

- Identificador único e ator principal.
- Descrição clara do objetivo.
- Pré‑condições e pós‑condições.
- Fluxo principal e fluxos alternativos.
- Requisitos funcionais e não funcionais relacionados.
- Prioridade (Fundamental, Importante, Desejável).

Esta especificação serve como base para implementação, testes e validação.

---

## 2. Convenções

| Campo                       | Descrição                                           |
|-----------------------------|-----------------------------------------------------|
| **Identificador**           | Código único do caso de uso (ex.: UC-01).           |
| **Ator Principal**          | Usuário do sistema.                                 |
| **Descrição**               | Objetivo e escopo do caso de uso.                   |
| **Pré‑condições**           | Estado necessário do sistema antes da execução.     |
| **Fluxo Principal**         | Sequência de passos bem‑sucedida.                   |
| **Fluxos Alternativos**     | Caminhos alternativos ou de exceção.                |
| **Pós‑condições**           | Estado do sistema após a execução bem‑sucedida.     |
| **Requisitos Relacionados** | Códigos dos requisitos (RF, RNF, RN) associados.    |
| **Prioridade**              | Fundamental / Importante / Desejável.               |

---

## 3. Lista Geral de Casos de Uso

| Identificador | Nome do Caso de Uso                        |
|---------------|--------------------------------------------|
| UC-01         | Cadastrar Usuário                          |
| UC-02         | Autenticar Usuário                         |
| UC-03         | Cadastrar Fonte de Renda                   |
| UC-04         | Registrar Despesa                          |
| UC-05         | Cadastrar Despesa Fixa                     | 
| UC-06         | Classificar Despesa                        |
| UC-07         | Registrar Parcelamento                     |
| UC-08         | Fechar Mês Financeiro                      |
| UC-09         | Auditoria Financeira Pessoal               |
| UC-10         | Gerar Relatório Financeiro                 |
| UC-11         | Exportar Relatório                         |
| UC-12         | Interagir com Assistente Financeiro por IA |
| UC-13         | Configurar Preferências do Sistema         |
| UC-14         | Criar Mês Financeiro                       |
| UC-15         | Tratar Divergência Financeira              |

---

## 4. Casos de Uso Detalhados

---

### UC-01 – Cadastrar Usuário

| Campo                    | Descrição                                                                                                                                                                        |
|--------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Identificador**        | UC-01                                                                                                                                                                            |
| **Ator Principal**       | Usuário                                                                                                                                                                          |
| **Descrição**            | Permite ao usuário criar um perfil local no sistema, possibilitando personalização, persistência e isolamento dos dados financeiros.                                             |
| **Pré‑condições**        | Aplicação instalada e em execução; inexistência de perfil ativo.                                                                                                                 |
| **Fluxo Principal**      | 1. O usuário inicia o sistema pela primeira vez.2. O sistema solicita informações iniciais (nome e preferências básicas).3. O usuário confirma os dados.4. O sistema cria o perfil local e inicia a sessão. |
| **Fluxos Alternativos**  | **A1 – Cancelamento**: O usuário cancela o cadastro antes da confirmação → sistema retorna à tela inicial.<br>**A2 – Dados inválidos**: Dados inválidos ou incompletos → sistema solicita correção. |
| **Pós‑condições**           | Perfil criado e sessão iniciada.                                                                                                                                              |
| **Requisitos Relacionados** | RF01, RNF02                                                                                                                                                                   |
| **Prioridade**              | Fundamental                                                                                                                                                                   |

---

### UC-02 – Autenticar Usuário

| Campo                       | Descrição                                                                                                                                                              |
|-----------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Identificador**           | UC-02                                                                                                                                                                  |
| **Ator Principal**          | Usuário                                                                                                                                                                |
| **Descrição**               | Permite ao usuário acessar o sistema por meio de autenticação local, protegendo os dados financeiros armazenados.                                                      |
| **Pré‑condições**           | Perfil previamente cadastrado.                                                                                                                                         |
| **Fluxo Principal**         | 1. O usuário inicia o sistema.2. O sistema solicita autenticação local.3. O usuário informa as credenciais.4. O sistema valida as credenciais.5. O acesso é concedido. |
| **Fluxos Alternativos**     | **A1 – Credenciais inválidas**: Acesso negado e nova tentativa permitida.<br>**A2 – Tentativas excedidas**: Sistema bloqueia temporariamente o acesso.                 |
| **Pós‑condições**           | Sessão autenticada iniciada.                                                                                                                                           |
| **Requisitos Relacionados** | RF02, RNF02                                                                                                                                                            |
| **Prioridade**              | Fundamental                                                                                                                                                            |

---

### UC-03 – Gerenciar Fontes de Renda

| Campo                       | Descrição                                                                                                                                                                  |
|-----------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Identificador**           | UC-03                                                                                                                                                                      |
| **Ator Principal**          | Usuário                                                                                                                                                                    |
| **Descrição**               | Permite cadastrar, editar e remover fontes de renda fixas ou variáveis que compõem o orçamento mensal.                                                                     |
| **Pré‑condições**           | Usuário autenticado.                                                                                                                                                       |
| **Fluxo Principal**         | 1. O usuário acessa a área de gestão de rendas.2. Seleciona a opção de cadastrar nova renda.3. Informa nome, tipo, valor e periodicidade.4. O sistema valida os dados.5. O sistema registra a fonte de renda.    |
| **Fluxos Alternativos**     | **A1 – Edição**: Usuário edita uma renda existente.**A2 – Remoção**: Usuário remove uma renda cadastrada.**A3 – Dados inválidos**: Sistema solicita correção.              |
| **Pós‑condições**           | Fontes de renda atualizadas e refletidas nos cálculos mensais.                                                                                                             |
| **Requisitos Relacionados** | RF03                                                                                                                                                                       |
| **Prioridade**              | Fundamental                                                                                                                                                                |

---

### UC-04 – Registrar Despesa

| Campo                       | Descrição                                                                                                                                                                    |
|-----------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Identificador**           | UC-04                                                                                                                                                                        |
| **Ator Principal**          | Usuário                                                                                                                                                                      |
| **Descrição**               | Permite registrar despesas pontuais associadas a um mês financeiro ativo.                                                                                                    |
| **Pré‑condições**           | Usuário autenticado; mês financeiro aberto.                                                                                                                                  |
| **Fluxo Principal**         | 1. O usuário seleciona a opção de registrar despesa.<br>2. Informa descrição, valor, data e método de pagamento.<br>3. Define categoria (se aplicável).<br>4. O sistema valida os dados.<br>5. O sistema registra a despesa no mês corrente. |
| **Fluxos Alternativos**     | **A1 – Cancelamento**: Usuário cancela o registro antes da confirmação.<br>**A2 – Valor inválido ou data fora do mês**: Sistema solicita correção.                           |
| **Pós‑condições**           | Despesa registrada e considerada no saldo mensal.                                                                                                                            |
| **Requisitos Relacionados** | RF04, RF06                                                                                                                                                                   |
| **Prioridade**              | Fundamental                                                                                                                                                                  |

---

### UC-05 – Gerenciar Despesas Fixas

| Campo                       | Descrição                                                                                                                                                                    |
|-----------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Identificador**           | UC-05                                                                                                                                                                        |
| **Ator Principal**          | Usuário                                                                                                                                                                      |
| **Descrição**               | Permite cadastrar, editar e remover despesas recorrentes (fixas), que são automaticamente replicadas para meses futuros.                                                     |
| **Pré‑condições**           | Usuário autenticado.                                                                                                                                                         |
| **Fluxo Principal**         | 1. O usuário acessa a área de gerenciamento de despesas fixas.<br>2. Seleciona a opção para cadastrar uma nova despesa fixa.<br>3. Informa descrição, valor, periodicidade e data de vencimento.<br>4. O sistema valida os dados.<br>5. O sistema registra a despesa fixa e a associa aos meses futuros.                                                                                       |
| **Fluxos Alternativos**     | **A1 – Edição**: Usuário edita uma despesa fixa existente → sistema valida e atualiza.<br>**A2 – Remoção**: Usuário remove uma despesa fixa → sistema exclui a recorrência futura.<br>**A3 – Dados inválidos**: Valor negativo, periodicidade inválida → sistema solicita correção. |
| **Pós‑condições**           | Despesa fixa registrada, atualizada ou removida, refletindo corretamente nos cálculos financeiros futuros.                                                                   |
| **Requisitos Relacionados** | RF05                                                                                                                                                                         |
| **Prioridade**              | Fundamental                                                                                                                                                                  |

---

### UC-06 – Classificar Despesa

| Campo                       | Descrição                                                                                                                                                         |
|-----------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Identificador**           | UC-06                                                                                                                                                             |
| **Ator Principal**          | Usuário                                                                                                                                                           |
| **Descrição**               | Permite classificar despesas registradas, associando‑as a categorias financeiras e definindo seu nível de essencialidade (essencial ou não essencial).            |
| **Pré‑condições**           | Usuário autenticado; despesa previamente registrada no sistema.                                                                                                   |
| **Fluxo Principal**         | 1. O usuário seleciona uma despesa já registrada.<br>2. Define a categoria financeira da despesa.<br>3. Define o nível de essencialidade da despesa.<br>4. O sistema valida as informações.<br>5. O sistema atualiza a despesa com a classificação definida. |
| **Fluxos Alternativos**     | **A1 – Alteração**: Usuário altera uma classificação existente → sistema atualiza.<br>**A2 – Categoria inexistente ou inválida**: Sistema solicita nova seleção.  |
| **Pós‑condições**           | Despesa corretamente classificada e considerada nas análises financeiras, relatórios e auditorias.                                                                |
| **Requisitos Relacionados** | RF06                                                                                                                                                              |
| **Prioridade**              | Importante                                                                                                                                                        |

---

### UC-07 – Registrar Parcelamento

| Campo                    | Descrição                                                                                              |
|--------------------------|--------------------------------------------------------------------------------------------------------|
| **Identificador**        | UC-07                                                                                                  |
| **Ator Principal**       | Usuário                                                                                                |
| **Descrição**            | Permite registrar despesas parceladas e acompanhar automaticamente as parcelas nos meses futuros.      |
| **Pré‑condições**        | Usuário autenticado; mês financeiro ativo.                                                             |
| **Fluxo Principal**      | 1. Usuário informa descrição da compra parcelada.<br>2. Informa valor total e número de parcelas.<br>3. Define data inicial e método de pagamento.<br>4. Sistema distribui automaticamente as parcelas nos meses subsequentes. |
| **Fluxos Alternativos**  | **A1 – Cancelamento**: Usuário cancela o registro antes da confirmação.                               |
| **Pós‑condições**        | Parcelas registradas e refletidas no saldo mensal futuro.                                             |
| **Requisitos Relacionados** | RF07, RF08                                                                                           |
| **Prioridade**           | Fundamental                                                                                           |

---

### UC-08 – Fechar Mês Financeiro

| Campo                    | Descrição                                                                                              |
|--------------------------|--------------------------------------------------------------------------------------------------------|
| **Identificador**        | UC-08                                                                                                  |
| **Ator Principal**       | Usuário                                                                                                |
| **Descrição**            | Permite encerrar um mês financeiro, consolidando dados e impedindo alterações diretas.                |
| **Pré‑condições**        | Todas as receitas e despesas do mês registradas.                                                       |
| **Fluxo Principal**      | 1. Usuário solicita fechamento do mês.<br>2. Sistema valida consistência dos dados.<br>3. Sistema consolida saldos e marca o mês como fechado. |
| **Fluxos Alternativos**  | **A1 – Inconsistência**: Sistema identifica inconsistências e solicita correção.                      |
| **Pós‑condições**        | Mês consolidado e protegido contra alterações.                                                         |
| **Requisitos Relacionados** | RF09, RF10                                                                                           |
| **Prioridade**           | Importante                                                                                            |

---

### UC-09 – Auditoria Financeira Pessoal

| Campo                    | Descrição                                                                                              |
|--------------------------|--------------------------------------------------------------------------------------------------------|
| **Identificador**        | UC-09                                                                                                  |
| **Ator Principal**       | Usuário                                                                                                |
| **Descrição**            | Permite analisar gastos compulsivos, limites psicológicos e simulações de corte.                      |
| **Pré‑condições**        | Histórico financeiro mínimo disponível.                                                                |
| **Fluxo Principal**      | 1. Usuário acessa aba de auditoria.<br>2. Sistema identifica gastos não essenciais recorrentes.<br>3. Sistema sugere limites e simulações de economia. |
| **Fluxos Alternativos**  | **A1 – Ignorar recomendações**: Usuário ignora as sugestões.                                          |
| **Pós‑condições**        | Relatório de auditoria exibido ao usuário.                                                             |
| **Requisitos Relacionados** | RF06, RF10, RF11                                                                                     |
| **Prioridade**           | Importante                                                                                            |

---

### UC-10 – Gerar Relatórios Financeiros

| Campo                    | Descrição                                                                                              |
|--------------------------|--------------------------------------------------------------------------------------------------------|
| **Identificador**        | UC-10                                                                                                  |
| **Ator Principal**       | Usuário                                                                                                |
| **Descrição**            | Permite a visualização de relatórios financeiros mensais e históricos consolidados.                   |
| **Pré‑condições**        | Dados financeiros registrados.                                                                         |
| **Fluxo Principal**      | 1. Usuário seleciona período desejado.<br>2. Sistema consolida dados.<br>3. Sistema exibe gráficos e indicadores. |
| **Fluxos Alternativos**  | **A1 – Período sem dados**: Mensagem informativa exibida.                                             |
| **Pós‑condições**        | Relatório visual apresentado.                                                                          |
| **Requisitos Relacionados** | RF11                                                                                                 |
| **Prioridade**           | Importante                                                                                            |

---

### UC-11 – Exportar Relatórios

| Campo                    | Descrição                                                                                              |
|--------------------------|--------------------------------------------------------------------------------------------------------|
| **Identificador**        | UC-11                                                                                                  |
| **Ator Principal**       | Usuário                                                                                                |
| **Descrição**            | Permite exportar relatórios financeiros para arquivos externos (PDF, CSV).                            |
| **Pré‑condições**        | Relatório previamente gerado.                                                                          |
| **Fluxo Principal**      | 1. Usuário escolhe formato de exportação.<br>2. Sistema gera arquivo.<br>3. Arquivo é disponibilizado para download. |
| **Fluxos Alternativos**  | **A1 – Falha na geração**: Mensagem de erro exibida.                                                  |
| **Pós‑condições**        | Arquivo exportado com sucesso.                                                                         |
| **Requisitos Relacionados** | RF12                                                                                                 |
| **Prioridade**           | Desejável                                                                                             |

---

### UC-12 – Interagir com Assistente Financeiro por IA

| Campo                    | Descrição                                                                                              |
|--------------------------|--------------------------------------------------------------------------------------------------------|
| **Identificador**        | UC-12                                                                                                  |
| **Ator Principal**       | Usuário                                                                                                |
| **Descrição**            | Permite interação por linguagem natural (texto/voz) para registro e consulta de dados financeiros.    |
| **Pré‑condições**        | IA configurada; usuário autenticado.                                                                   |
| **Fluxo Principal**      | 1. Usuário envia comando por texto ou voz.<br>2. Sistema interpreta intenção.<br>3. Dados são registrados ou consultados automaticamente. |
| **Fluxos Alternativos**  | **A1 – Esclarecimento**: IA solicita confirmação ou esclarecimento.                                   |
| **Pós‑condições**        | Informação processada corretamente.                                                                    |
| **Requisitos Relacionados** | RF13                                                                                                 |
| **Prioridade**           | Desejável                                                                                             |

---

### UC-13 – Configurar Preferências do Sistema

| Campo                    | Descrição                                                                                              |
|--------------------------|--------------------------------------------------------------------------------------------------------|
| **Identificador**        | UC-13                                                                                                  |
| **Ator Principal**       | Usuário                                                                                                |
| **Descrição**            | Permite configurar preferências gerais, como tema visual, notificações e comportamento do sistema.    |
| **Pré‑condições**        | Usuário autenticado.                                                                                   |
| **Fluxo Principal**      | 1. Usuário acessa configurações.<br>2. Ajusta preferências desejadas.<br>3. Sistema aplica alterações imediatamente. |
| **Fluxos Alternativos**  | **A1 – Preferência não suportada**: Sistema informa indisponibilidade.                                |
| **Pós‑condições**        | Preferências persistidas.                                                                              |
| **Requisitos Relacionados** | RNF06, RNF07, RNF09                                                                                  |
| **Prioridade**           | Importante                                                                                            |

---

### UC-14 – Criar Mês Financeiro

| Campo                    | Descrição                                                                                              |
|--------------------------|--------------------------------------------------------------------------------------------------------|
| **Identificador**        | UC-14                                                                                                  |
| **Ator Principal**       | Usuário                                                                                                |
| **Descrição**            | Permite criar um novo mês financeiro, definindo saldo inicial e garantindo a unicidade de mês aberto por período (RN-01). |
| **Pré‑condições**        | Usuário autenticado; inexistência de mês financeiro ABERTO para o mesmo mês/ano.                      |
| **Fluxo Principal**      | 1. Usuário solicita criação de um novo mês financeiro.<br>2. Sistema verifica se já existe mês ABERTO para o mesmo período.<br>3. Sistema valida o saldo inicial informado.<br>4. Sistema cria o novo mês financeiro em estado ABERTO. |
| **Fluxos Alternativos**  | **A1 – Mês já existe**: Sistema bloqueia operação e informa motivo.                                   |
| **Pós‑condições**        | Novo mês financeiro criado em estado ABERTO, pronto para receber receitas e despesas.                 |
| **Requisitos Relacionados** | RN-01, RF08                                                                                          |
| **Prioridade**           | Fundamental                                                                                           |

---

### UC-15 – Tratar Divergência Financeira

| Campo                    | Descrição                                                                                              |
|--------------------------|--------------------------------------------------------------------------------------------------------|
| **Identificador**        | UC-15                                                                                                  |
| **Ator Principal**       | Usuário                                                                                                |
| **Descrição**            | Permite tratar divergências entre o saldo calculado pelo sistema e o saldo real informado pelo usuário. |
| **Pré‑condições**        | Mês financeiro existente; saldo calculado disponível; usuário informa saldo externo para comparação.   |
| **Fluxo Principal**      | 1. Usuário informa saldo externo para um mês financeiro.<br>2. Sistema compara saldo calculado com saldo informado.<br>3. Sistema identifica divergência.<br>4. Sistema registra a divergência e exibe detalhes.<br>5. Usuário escolhe ajustar lançamentos ou aceitar a divergência. |
| **Fluxos Alternativos**  | **A1 – Sem divergência**: Sistema apenas registra a verificação.                                      |
| **Pós‑condições**        | Divergência registrada e, quando aplicável, encaminhada para ajuste em meses posteriores.             |
| **Requisitos Relacionados** | RN-08, RF10, RF11                                                                                    |
| **Prioridade**           | Importante                                                                                            |

---

## 5. Rastreabilidade com Requisitos e Regras de Negócio

- Cada caso de uso está diretamente vinculado a um ou mais **Requisitos Funcionais (RF)** e, quando aplicável, a **Requisitos Não Funcionais (RNF)** e **Regras de Negócio (RN)**.
- A implementação dos casos de uso deve garantir a satisfação dos requisitos e a aplicação das regras de negócio definidas nos documentos correspondentes.
- Os fluxos descritos servem como base para a construção dos testes de aceitação.

---

**Documentos relacionados:**  
- [01-visao-geral.md](01-visao-geral.md)  
- [02-requisitos.md](02-requisitos.md)  
- [04-modelo-dominio.md](04-modelo-dominio.md)  
- [05-regras-negocio.md](05-regras-negocio.md)  
- [06-arquitetura.md](06-arquitetura.md)  
- [07-modelo-dados.md](07-modelo-dados.md)  
- [08-migracoes.md](08-migracoes.md)  
- [09-testes.md](09-testes.md)  
- [10-contribuicao.md](10-contribuicao.md)