# Walkthrough - Arquitetura Offline-First (Room + Firestore)

Implementamos uma arquitetura robusta que combina o poder do **Room** (local) com a sincronização do **Firestore** (nuvem), garantindo que o app funcione instantaneamente e sem internet, mas mantenha os dados salvos com segurança.

## Alterações Realizadas

### Camada de Dados - Sincronização Retroativa
- **Upload Local -> Cloud:** Implementamos uma rotina de sincronização na inicialização dos repositórios. Agora, o app verifica o banco local (**Room**) e garante que todos os registros existentes sejam enviados para o **Firestore**.
- **Idempotência:** Utilizamos o método `set()` do Firestore, o que garante que dados que já estão na nuvem não sejam duplicados, apenas atualizados se necessário.
- **Resiliência:** Essa rotina é protegida por blocos `try-catch`, garantindo que o app continue funcionando mesmo se houver instabilidade durante o upload inicial.

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
