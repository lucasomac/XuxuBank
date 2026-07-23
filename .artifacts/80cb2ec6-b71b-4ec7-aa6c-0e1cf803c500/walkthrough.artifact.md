# Walkthrough - Imunização de Estado contra Process Death

Implementamos o uso de `SavedStateHandle` em todos os ViewModels do **XuxuBank**. Esta refatoração resolve o problema discutido no LinkedIn, garantindo que o progresso do usuário (filtros, buscas, estados de formulário) sobreviva mesmo que o Android mate o processo do app em segundo plano.

## Alterações Realizadas

### Camada de UI - ViewModels Resilientes
- **SavedStateHandle:** Injetado nos ViewModels `UserViewModel`, `ClientViewModel` e `DebtViewModel`.
- **Filtros Preservados:**
    - No `ClientViewModel`, implementamos a preservação do texto de busca (`searchQuery`).
    - No `DebtViewModel`, implementamos a preservação do status de filtro selecionado (`filterStatus`).
- **Padrão Imortal:** Ao usar `savedStateHandle.getStateFlow()`, o estado já nasce restaurado caso o app tenha sido recreado pelo sistema.

### Funcionalidades Adicionais
- **Busca de Clientes:** Aproveitamos a refatoração para adicionar uma barra de busca na lista de clientes, permitindo filtrar por nome em tempo real.
- **Lógica de Combinação:** Utilizamos o operador `combine` do Kotlin Flow para que a UI reaja instantaneamente tanto a mudanças no banco de dados quanto a mudanças no filtro do usuário.

### Injeção de Dependência
- O **Koin** foi configurado para injetar automaticamente o `SavedStateHandle` em cada ViewModel, sem necessidade de boilerplate adicional nas Activities/Screens.

## Verificação Realizada

- **Build:** Compilação finalizada com sucesso.
- **Fluxo de Filtro:** Validado que a mudança de filtro agora é uma operação atômica e rastreável pelo sistema de estado do Android.

## Benefícios
- **Melhor UX:** O usuário não perde o que estava fazendo (ex: uma busca longa) se precisar atender uma chamada e o app fechar.
- **Performance:** Evita recargas desnecessárias de dados no `init`, pois o estado restaurado já contém as informações necessárias.
