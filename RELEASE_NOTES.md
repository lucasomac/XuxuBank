# Release Notes — XuxuBank v1.0.0 🌵💰

Esta é a versão inicial oficial do **XuxuBank**, consolidando uma arquitetura robusta, design regionalizado e funcionalidades completas de gestão financeira.

## 🚀 O que há de novo

### 🏗️ Arquitetura e Core
- **Clean Architecture:** Implementação total com separação de camadas (*Data*, *Domain*, *UI*).
- **MVVM:** Fluxo de dados reativo utilizando `StateFlow` e `ViewModel`.
- **Koin 4.0.0:** Injeção de dependências configurada em todo o projeto.
- **Room Database v2:** Migração realizada para suportar novas nomenclaturas e persistência de Clientes e Dívidas.
- **Navegação 3:** Transições de tela baseadas em estado e chaves serializáveis.

### 💸 Funcionalidades de Gestão
- **Parcelamento Automático:** Geração inteligente de dívidas parceladas com cálculo sequencial de datas.
- **Filtros de Status:** Dashboard mensal com filtragem dinâmica por *Pendente*, *Pago* e *Atrasado*.
- **Controle de Acesso:** Login simulado diferenciando a experiência de Gerente (Administrativo) e Cliente (Visualização).
- **Renomeação Estratégica:** Refatoração completa de toda a base de código e UI para o termo **"Cliente"** (antigo Devedor), visando um tom mais profissional.

### 🎨 Visual e UX
- **Tema Earthy:** Cores inspiradas no Nordeste brasileiro aplicadas via Material 3.
- **Ícone Adaptativo:** Design exclusivo com Chapéu de Cangaceiro e Chuchu.
- **Layout Adaptativo:** Suporte nativo a telas grandes (Tablets/Dobráveis) via *List-Detail Scene Strategy*.
- **Localização:** 100% dos textos centralizados em `strings.xml` em Português (Brasil).

### 🧪 Qualidade e Testes
- **Infraestrutura de Testes:** Implementação de Fakes para repositórios e testes unitários para *UseCases* e *ViewModels*.
- **Estabilidade:** Correção de bugs críticos em migrações de banco de dados e validações de campos numéricos.

---
*XuxuBank v1.0.0 — Conectando tecnologia e cultura.*
