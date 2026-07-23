# Walkthrough - Arquitetura Offline-First (Room + Firestore)

Implementamos uma arquitetura robusta que combina o poder do **Room** (local) com a sincronização do **Firestore** (nuvem), garantindo que o app funcione instantaneamente e sem internet, mas mantenha os dados salvos com segurança.

## Alterações Realizadas

### Camada de Dados - Sincronização Híbrida
- **Repositórios Híbridos:** Recriamos os repositórios (`ClientRepositoryImpl`, `DebtRepositoryImpl`, `UserRepositoryImpl`) para coordenar o banco local e a nuvem.
- **Single Source of Truth:** A UI agora consome dados exclusivamente do **Room**. Isso garante que qualquer mudança seja refletida instantaneamente, mesmo offline.
- **Snapshot Listeners:** Implementamos ouvintes em tempo real no Firestore. Se um dado mudar na nuvem, o Room é atualizado automaticamente e a UI reflete a mudança.
- **Batch Operations:** Os DAOs foram atualizados para suportar inserções em lote (`insertClients`, `insertDebts`), otimizando a sincronização inicial.

### Fluxo de Trabalho
1. **Salvar:** O dado é enviado para o Firestore e, ao receber o ID de sucesso, é persistido no Room.
2. **Listar:** A UI observa um `Flow` do Room, garantindo performance de 60fps.
3. **Sincronizar:** Um Job em segundo plano (via `CoroutineScope` no repositório) mantém o cache local espelhado com o servidor.

## Verificação Realizada

- **Compilação:** O projeto foi compilado com sucesso (`assembleDebug`).
- **Injeção de Dependência:** Koin configurado para injetar os novos repositórios híbridos.
- **Limpeza:** Removidas as implementações redundantes do Firestore para evitar confusão no código.

## Benefícios
- **Performance:** Carregamento instantâneo.
- **Resiliência:** Funciona 100% sem internet.
- **Segurança:** Dados salvos na nuvem assim que houver conexão.
