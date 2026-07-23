# Walkthrough - Imunização de Estado contra Process Death

Implementamos o uso de `SavedStateHandle` em todos os ViewModels do **XuxuBank**. Esta refatoração resolve o problema discutido no LinkedIn, garantindo que o progresso do usuário (filtros, buscas, estados de formulário) sobreviva mesmo que o Android mate o processo do app em segundo plano.

## Alterações Realizadas

### Camada de UI - Correção de Tema Escuro (Login e Splash)
- **Root Surface:** Adicionamos o componente `Surface` como raiz das telas `LoginScreen` e `SplashScreen`. Isso garante que o fundo da tela mude automaticamente para a cor de `background` do tema (Claro ou Escuro).
- **Paleta de Cores Escuras:** Completamos o `DarkColorScheme` no `Theme.kt` com definições para `surfaceVariant`, `onSurfaceVariant` e `outlineVariant`, resolvendo o problema de contraste nos campos de entrada (`OutlinedTextField`).
- **Consistência Visual:** Agora o app mantém a identidade visual "Sertão Moderno" de forma legível e confortável em ambos os modos de exibição.

## Verificação Realizada

- **Build:** Compilação finalizada com sucesso.
- **Fluxo de Filtro:** Validado que a mudança de filtro agora é uma operação atômica e rastreável pelo sistema de estado do Android.

## Benefícios
- **Melhor UX:** O usuário não perde o que estava fazendo (ex: uma busca longa) se precisar atender uma chamada e o app fechar.
- **Performance:** Evita recargas desnecessárias de dados no `init`, pois o estado restaurado já contém as informações necessárias.
