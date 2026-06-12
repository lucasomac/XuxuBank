# Registro de Versões (Changelog) — XuxuBank 🌵💰

Este arquivo mantém o histórico de todas as atualizações e melhorias implementadas no XuxuBank.

---

## [1.1.0] — 2026-06-08
### Adicionado
- **Acesso Unificado:** Novo sistema de login que identifica automaticamente Gerente ou Cliente via E-mail/Telefone.
- **Validação Estrita:** Verificação de formato de e-mail e máscaras de telefone `(XX) XXXXX-XXXX`.
- **Campos Obrigatórios:** E-mail e Telefone agora são mandatórios no cadastro de clientes para possibilitar o login.
- **Diferenciação Automática:** Redirecionamento automático para a Home de Gerente ou Cliente após o login.
- **Feedback Visual:** Mensagens de erro e indicadores de carregamento na tela de login.

### Alterado
- **Terminologia:** Refatoração completa de "Devedor/Debtor" para "Cliente/Client" em todo o sistema.
- **Experiência do Usuário:** Botão de login habilitado dinamicamente com base na validade dos dados.
- **Comentários Técnicos:** Adição de documentação interna no código para facilitar a manutenção.

---

## [1.0.0] — 2026-06-08
### Adicionado
- **Lançamento Inicial:** Base do app com Clean Architecture e MVVM.
- **Gestão Financeira:** CRUD completo de clientes e dívidas com suporte a parcelamento automático.
- **Identidade Visual:** Implementação do tema "Earthy" inspirado no Nordeste brasileiro.
- **Navegação 3:** Integração com Jetpack Navigation 3 e chaves serializáveis.
- **Layout Adaptativo:** Suporte a celulares e tablets via List-Detail Scene Strategy.
- **Persistência:** Banco de dados Room configurado com suporte a múltiplos papéis (RBAC).
- **Ícone Regional:** Criação do ícone adaptativo com Chapéu de Cangaceiro e Chuchu.

### Técnico
- **DI:** Integração com Koin 4.0.0.
- **UI:** 100% Jetpack Compose com Material 3.
- **Infraestrutura:** Configuração de Fakes e testes unitários iniciais.

---
*XuxuBank — Evoluindo com raízes e tecnologia.*
