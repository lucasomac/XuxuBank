# Registro de Versões (Changelog) — XuxuBank 🌵💰

Este arquivo mantém o histórico de todas as atualizações e melhorias implementadas no XuxuBank.

---

## [1.3.1] — 2026-06-12
### Corrigido
- **Desserialização do Firestore:** Adicionados construtores sem argumentos (valores padrão) para todas as entidades, corrigindo crash de execução.
- **Precisão Remota:** Implementado mapeamento manual nos repositórios do Firestore para garantir que valores em `BigDecimal` e `Enums` sejam persistidos e recuperados com precisão total.

## [1.3.0] — 2026-06-12
### Adicionado
- **Migração para Cloud Firestore:** Transição do banco de dados local Room para o Firebase Firestore como fonte primária de dados.
- **Sincronização em Tempo Real:** O app agora reflete mudanças nos clientes e dívidas instantaneamente em todos os dispositivos conectados.
- **IDs Baseados em String:** Atualização de toda a arquitetura para suportar identificadores alfanuméricos do Firestore.
- **Infraestrutura Remota:** Implementação de novos repositórios Firestore (`ClientRepositoryFirestoreImpl`, `DebtRepositoryFirestoreImpl`, `UserRepositoryFirestoreImpl`).

### Alterado
- **Persistência Local:** O Room foi mantido como uma camada de cache offline (preparado para futuras otimizações).

## [1.2.2] — 2026-06-12
### Adicionado
- **Resumos Financeiros Inteligentes:** Novas seções de resumo no topo das telas de Gerente e Cliente.
- **Visão do Mês Atual:** Cálculo automático de valores "A Receber/Pagar" e "Já Pago" para o mês vigente.
- **Alertas de Atraso:** Destaque visual em vermelho para o total acumulado de dívidas vencidas na visão do Cliente.
- **Refatoração de Componentes:** Organização modular do código, movendo componentes para arquivos individuais (ex: `DebtItem`, `StatusChip`).

## [1.2.1] — 2026-06-12
### Adicionado
- **Mobile Design Standards:** Aperfeiçoamento da interface seguindo os padrões `/mobile-app-design`.
- **Precisão Monetária:** Migração de todos os valores financeiros de `Double` para `BigDecimal`, garantindo cálculos exatos e sem erros de arredondamento.
- **Refatoração de Utilitários:** Criação do arquivo `FormatUtils.kt` para centralizar a formatação de moedas e datas, mantendo `ValidationUtils.kt` focado exclusivamente em regras de validação.
- **Acessibilidade (a11y):** Inclusão de `contentDescription` em telas e componentes financeiros para suporte a leitores de tela.
- **Otimização de Touch Targets:** Garantia de alvos de toque mínimos de 48dp em todos os elementos interativos.
- **Data de Vencimento Opcional:** A data de vencimento de dívidas agora é opcional durante o cadastro, permitindo maior flexibilidade no lançamento de cobranças.
- **Validação de Entrada:** Filtros em tempo real para campos numéricos e melhorias nos tipos de teclado (numérico vs. e-mail).
- **UX Refinada:** Melhorias no espaçamento (spacing system) e feedback visual em formulários.
- **Correção de Formatação:** Melhoria na função `formatMonetary` para suportar entradas com vírgula e garantir o prefixo "R$ " corretamente.

## [1.2.0] — 2026-06-08
### Adicionado
- **Design System "Modern Sertão":** Implementação de uma nova identidade visual premium baseada no documento `docs/DESIGN.md`.
- **Hierarquia de Superfícies:** Substituição de bordas físicas por variações tonais (`SurfaceContainer` tiers) para uma interface mais limpa e moderna.
- **Tipografia Editorial:** Escala tipográfica de alto contraste para maior autoridade e legibilidade.
- **Paleta de Cores Refinada:** Introdução do Vermelho Terra (`#A20513`) e Bege Light (`#FDF9F4`) para um visual "afetivo" e profissional.
- **Geometria Curva:** Aplicação da curvatura inspirada no Chapéu de Cangaceiro (24dp) em containers e cabeçalhos.
- **Componentes Refinados:** Atualização do `DebtItem` e `SummaryCard` para utilizar gradientes, profundidade tonal e zero bordas físicas.

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
