# Documento de Requisitos – Finance Core

---

## 1. Introdução

Este documento detalha os **Requisitos Funcionais (RF)** e **Requisitos Não Funcionais (RNF)** do sistema **Finance Core**, seguindo um padrão formal e reutilizável para projetos profissionais de software.

Cada requisito é identificado por um código único, associado aos casos de uso correspondentes e classificado quanto à prioridade:

| Prioridade     | Descrição                                                                 |
|----------------|---------------------------------------------------------------------------|
| **Fundamental**| Indispensável para o funcionamento básico do sistema.                     |
| **Importante** | Agrega valor significativo, mas sua ausência não impede o uso essencial.  |
| **Desejável**  | Melhoria ou diferencial futuro, pode ser implementado posteriormente.     |

---

## 2. Requisitos Funcionais (RF)

| ID    | Casos de Uso                | Descrição                                                                                                                    | Prioridade  |
|-------|-----------------------------|------------------------------------------------------------------------------------------------------------------------------|-------------|
| RF01  | Cadastro de Usuário         | O sistema deve permitir que o usuário crie um perfil local contendo nome, e‑mail (opcional) e configurações iniciais de uso. | Fundamental |
| RF02  | Autenticação                | O sistema deve permitir o acesso por meio de autenticação local, garantindo a proteção dos dados financeiros.                | Fundamental |
| RF03  | Gerenciar Rendas            | O sistema deve permitir o cadastro de fontes de renda, classificadas como fixas ou variáveis, com valor e periodicidade.     | Fundamental |
| RF04  | Registrar Despesa           | O sistema deve permitir o registro de despesas financeiras com valor, data, categoria e método de pagamento.                 | Fundamental |
| RF05  | Gerenciar Despesas Fixas    | O sistema deve permitir o cadastro de despesas recorrentes, que impactam automaticamente os meses subsequentes.              | Fundamental |
| RF06  | Classificar Despesa         | O sistema deve permitir classificar despesas em categorias personalizadas e marcá‑las como essenciais ou não essenciais.     | Importante  |
| RF07  | Gerenciar Parcelamentos     | O sistema deve permitir o registro de despesas parceladas, distribuindo automaticamente os valores pelos meses futuros.      | Fundamental |
| RF08  | Consolidar Mês Financeiro   | O sistema deve calcular automaticamente o saldo mensal com base nas receitas e despesas registradas.                         | Fundamental |
| RF09  | Fechar Mês                  | O sistema deve permitir o fechamento do mês financeiro, impedindo alterações diretas nos dados consolidados.                 | Importante  |
| RF10  | Auditoria Financeira        | O sistema deve comparar o saldo esperado com o saldo informado pelo usuário e sinalizar divergências.                        | Importante  |
| RF11  | Gerar Relatório             | O sistema deve gerar relatórios financeiros mensais e históricos consolidados.                                               | Importante  |
| RF12  | Exportar Dados              | O sistema deve permitir a exportação de relatórios em formato digital (ex.: PDF ou CSV).                                     | Desejável   |
| RF13  | Interação por IA            | O sistema deve permitir que o usuário registre e consulte informações financeiras por meio de linguagem natural.             | Desejável   |

---

## 3. Requisitos Não Funcionais (RNF)

| ID    | Casos de Uso                          | Descrição                                                                                                                  | Prioridade  |
|-------|---------------------------------------|----------------------------------------------------------------------------------------------------------------------------|-------------|
| RNF01 | Todos                                 | O sistema deve funcionar integralmente sem conexão com a internet.                                                         | Fundamental |
| RNF02 | Persistência de Dados                 | Os dados devem ser armazenados localmente de forma segura, com mecanismos de criptografia (AES).                           | Fundamental |
| RNF03 | Consultas e Relatórios                | O sistema deve responder às operações principais em tempo inferior a 1 segundo para bases de dados médias.                 | Importante  |
| RNF04 | Evolução do Sistema                   | A arquitetura deve permitir a adição de novos módulos sem impacto nos existentes.                                          | Fundamental |
| RNF05 | Manutenção                            | O código deve seguir padrões que facilitem manutenção, testes e evolução.                                                  | Importante  |
| RNF06 | Interação do Usuário                  | A interface deve ser intuitiva e exigir o mínimo de esforço cognitivo do usuário.                                          | Importante  |
| RNF07 | Todos                                 | Os dados financeiros do usuário não devem ser compartilhados sem consentimento explícito.                                  | Fundamental |
| RNF08 | Migração                              | O sistema deve permitir futura migração para ambiente mobile ou cloud.                                                     | Desejável   |
| RNF09 | Acessibilidade Visual (Tema)          | O sistema deve permitir alternar entre tema claro e tema escuro                                                            | Importante  |

---

## 4. Observações sobre a Rastreabilidade

- Todos os requisitos funcionais estão diretamente vinculados a **Casos de Uso** detalhados no documento `03-casos-de-uso.md`.
- Os requisitos não funcionais orientam decisões arquiteturais e de implementação descritas em `06-arquitetura.md`, `07-modelo-dados.md`, `08-migracoes.md` e `09-testes.md`.
- A priorização segue o planejamento de entregas definido na **Visão Geral do Sistema** (`01-visao-geral.md`), garantindo que as funcionalidades fundamentais sejam desenvolvidas primeiro.

---

**Documentos relacionados:**  
- [01-visao-geral.md](01-visao-geral.md)  
- [03-casos-de-uso.md](03-casos-de-uso.md)  
- [04-modelo-dominio.md](04-modelo-dominio.md)  
- [05-regras-negocio.md](05-regras-negocio.md)  
- [06-arquitetura.md](06-arquitetura.md)  
- [07-modelo-dados.md](07-modelo-dados.md)  
- [08-migracoes.md](08-migracoes.md)  
- [09-testes.md](09-testes.md)  
- [10-contribuicao.md](10-contribuicao.md)