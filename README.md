# 🌵 XuxuBank — Gerenciador de Dívidas Regionalizado

O **XuxuBank** é uma aplicação moderna de gerenciamento de dívidas pessoais, parte do universo fictício da **XuxuCorp**. O projeto une tecnologia de ponta no ecossistema Android com uma identidade visual vibrante inspirada na cultura e nas cores do **Nordeste Brasileiro**.

---

## 🚀 Funcionalidades Principais

- **Gestão de Clientes e Dívidas:** Operações completas de CRUD para cadastrar clientes e vincular obrigações financeiras.
- **Lógica de Parcelamento Inteligente:** Distribuição automática de valores totais em parcelas mensais com cálculo de vencimento.
- **Controle de Acesso (RBAC):** Experiências distintas para **Gerentes** (visão administrativa total) e **Clientes** (visualização restrita de dívidas próprias).
- **Dashboard Mensal:** Visão agrupada por mês com filtros de status (Pendente, Pago, Atrasado) e card de resumo financeiro.
- **Interface Adaptativa:** Layout otimizado para celulares e tablets utilizando o padrão *List-Detail* (Compose Material 3 Adaptive).
- **Experiência Imersiva:** Splash screen animada, suporte a *Edge-to-Edge* e ícone adaptativo regionalizado.

---

## 🏗️ Arquitetura e Padrões

O projeto foi construído seguindo rigorosamente os padrões de engenharia de software modernos:

- **Clean Architecture:** Separação clara em camadas (**Domain**, **Data**, **UI**).
- **MVVM (Model-View-ViewModel):** Gerenciamento de estado reativo com `StateFlow`.
- **State Hoisting:** Componentes desacoplados e altamente reutilizáveis.
- **Dependency Injection:** Utilização do **Koin 4.0.0** para gestão de dependências.
- **Navegação de Última Geração:** Implementação do **Jetpack Navigation 3** (baseado em estados).

---

## 🛠️ Tech Stack

- **Linguagem:** [Kotlin](https://kotlinlang.org/)
- **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
- **Persistência:** [Room Database](https://developer.android.com/training/data-storage/room) (v2 com Migração)
- **DI:** [Koin](https://insert-koin.io/)
- **Navegação:** [Jetpack Navigation 3](https://developer.android.com/guide/navigation/navigation-3)
- **Layout Adaptativo:** [Compose Material 3 Adaptive](https://developer.android.com/develop/ui/compose/layouts/adaptive)
- **Testes:** JUnit 4 e Fakes para UseCases e ViewModels.

---

## 🎨 Identidade Visual (XuxuCorp)

Inspirada no sertão nordestino, a paleta de cores e elementos visuais refletem autenticidade e confiança:

- **Cores:** Amarelo Sol (`#FDB813`), Vermelho Terra (`#C62828`), Laranja Pôr do Sol (`#FF6D00`) e Terra Seca (`#B8860B`).
- **Símbolos:** Chapéu de Cangaceiro e referências ao vegetal Chuchu, integrados no Ícone Adaptativo e no Logotipo.
- **Tipografia:** Nunito/Quicksand para uma comunicação amigável e moderna.

---

## 📦 Como Executar

1. Clone o repositório.
2. Certifique-se de ter o **Android Studio Ladybug (ou superior)** instalado.
3. Utilize o JDK 17 ou superior.
4. Execute o build via Gradle:
   ```bash
   ./gradlew assembleDebug
   ```
5. Para rodar os testes unitários:
   ```bash
   ./gradlew test
   ```

---
*Desenvolvido com ❤️ como parte do ecossistema Xuxu Corporation.*
