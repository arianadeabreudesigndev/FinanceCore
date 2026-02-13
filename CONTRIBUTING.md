Obrigado por considerar contribuir! Este documento estabelece as diretrizes para contribuir com o projeto.

---

### Política de Branches

Utilizamos **Git Flow simplificado**:

- `main` → versão estável, releases.
- `develop` → integração de funcionalidades.
- `feature/*` → branches para novos casos de uso.
- `fix/*` → branches para correções.

### Padrão de Commits

Seguimos [Conventional Commits](https://www.conventionalcommits.org/):

<tipo>: <descrição curta no imperativo>

[corpo opcional]

---

Tipos permitidos: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`.

### Pull Requests

1. Crie um PR da sua branch para `develop`.
2. Preencha o template com:
   - O que foi alterado
   - Motivação
   - Checklist de qualidade
3. O PR será revisado e, após aprovação, mergeado.

### Testes

- Toda regra de negócio deve ter teste unitário.
- Testes de integração para repositórios.
- Execute `mvn test` antes de abrir o PR.

### Documentação

- Atualize a documentação na pasta `/documentation` sempre que houver mudança arquitetural ou de regras de negócio.
- Mantenha a linguagem ubíqua.

---

Qualquer dúvida, abra uma issue.