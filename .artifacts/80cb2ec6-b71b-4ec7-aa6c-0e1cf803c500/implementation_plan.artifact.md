# Ajuste de Tema Escuro (Dark Mode) na Tela de Login

O objetivo é garantir que a `LoginScreen` e a `SplashScreen` respeitem o tema escuro do sistema de forma consistente, corrigindo problemas de legibilidade onde elementos escuros aparecem sobre fundos claros (ou vice-versa).

## User Review Required

> [!IMPORTANT]
> Vou adicionar um componente `Surface` como raiz das telas de Login e Splash. No Compose, isso é essencial para que o fundo da tela mude automaticamente para a cor de `background` do tema selecionado (Claro ou Escuro). Também completarei a paleta de cores escuras no `Theme.kt`.

## Proposed Changes

### Camada de UI - Temas

#### [MODIFY] [Theme.kt](file:///Users/lucasomac/StudioProjects/XuxuBank/app/src/main/java/br/com/lucolimac/xuxubank/ui/theme/Theme.kt)
- Adicionar definições para `surfaceVariant`, `onSurfaceVariant` e `outlineVariant` no `DarkColorScheme`. Isso garante que componentes como `OutlinedTextField` fiquem visíveis no modo escuro.

### Camada de UI - Telas

#### [MODIFY] [LoginScreen.kt](file:///Users/lucasomac/StudioProjects/XuxuBank/app/src/main/java/br/com/lucolimac/xuxubank/ui/screen/LoginScreen.kt)
- Envolver o conteúdo principal em um `Surface` com a cor de fundo do tema.
- Garantir que não existam cores hardcoded (estilo `Color.Black`) que possam quebrar o contraste.

#### [MODIFY] [SplashScreen.kt](file:///Users/lucasomac/StudioProjects/XuxuBank/app/src/main/java/br/com/lucolimac/xuxubank/ui/screen/SplashScreen.kt)
- Adicionar um `Surface` para garantir que a transição do Splash para o Login seja suave visualmente.

## Verification Plan

### Automated Tests
- Executar `./gradlew assembleDebug` para validar a compilação.

### Manual Verification
- Alternar o celular entre Modo Claro e Modo Escuro na tela de Login.
- Verificar se o texto "E-mail ou Telefone" e as instruções estão legíveis em ambos os modos.
- Garantir que o fundo da tela mude de branco (Light) para grafite/preto (Dark).
