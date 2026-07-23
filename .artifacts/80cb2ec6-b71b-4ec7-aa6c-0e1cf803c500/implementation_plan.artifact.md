# Implementação de Arquitetura Offline-First (Room + Firestore)

O objetivo é transformar os repositórios em "Sincronizadores". O fluxo será: **UI -> Room (Instantâneo) -> Firestore (Sincronização em segundo plano)**. Isso resolve a sensação de "só local" dando controle total sobre o que está sincronizado.

## User Review Required

> [!IMPORTANT]
> A principal mudança será a criação de repositórios híbridos que coordenam a persistência local (Room) e a remota (Firestore). O Room será a "Single Source of Truth" para a UI.

## Proposed Changes

### Camada de Dados - DAOs

#### [MODIFY] [ClientDao.kt](file:///Users/lucasomac/StudioProjects/XuxuBank/app/src/main/java/br/com/lucolimac/xuxubank/data/local/dao/ClientDao.kt)
- Adicionar `insertClients(clients: List<ClientEntity>)` para sincronização em lote.

#### [MODIFY] [DebtDao.kt](file:///Users/lucasomac/StudioProjects/XuxuBank/app/src/main/java/br/com/lucolimac/xuxubank/data/local/dao/DebtDao.kt)
- Adicionar `insertDebts(debts: List<DebtEntity>)` para sincronização em lote.

#### [MODIFY] [UserDao.kt](file:///Users/lucasomac/StudioProjects/XuxuBank/app/src/main/java/br/com/lucolimac/xuxubank/data/local/dao/UserDao.kt)
- Adicionar `insertUsers(users: List<UserEntity>)`.

### Camada de Dados - Repositórios Híbridos

#### [NEW] [ClientRepositoryHybridImpl.kt](file:///Users/lucasomac/StudioProjects/XuxuBank/app/src/main/java/br/com/lucolimac/xuxubank/data/repository/ClientRepositoryHybridImpl.kt)
- Implementar lógica híbrida com `SnapshotListener` para manter o Room atualizado com o Firestore.

#### [NEW] [DebtRepositoryHybridImpl.kt](file:///Users/lucasomac/StudioProjects/XuxuBank/app/src/main/java/br/com/lucolimac/xuxubank/data/repository/DebtRepositoryHybridImpl.kt)
- Implementar lógica híbrida para dívidas.

#### [NEW] [UserRepositoryHybridImpl.kt](file:///Users/lucasomac/StudioProjects/XuxuBank/app/src/main/java/br/com/lucolimac/xuxubank/data/repository/UserRepositoryHybridImpl.kt)
- Implementar lógica híbrida para sessão do usuário.

### Injeção de Dependência

#### [MODIFY] [AppModule.kt](file:///Users/lucasomac/StudioProjects/XuxuBank/app/src/main/java/br/com/lucolimac/xuxubank/di/AppModule.kt)
- Atualizar Koin para injetar as novas implementações híbridas.

## Verification Plan

### Automated Tests
- Executar `./gradlew assembleDebug` para garantir que a injeção de dependência e os tipos estão corretos.

### Manual Verification
- Testar login e criação de cliente/dívida com e sem internet.
- Validar se os dados aparecem no Console do Firebase após a sincronização.
