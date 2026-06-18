package br.com.lucolimac.xuxubank.data.remote

import br.com.lucolimac.xuxubank.data.local.entity.UserEntity
import br.com.lucolimac.xuxubank.data.local.entity.UserRole
import br.com.lucolimac.xuxubank.domain.repository.UserRepository
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class UserRepositoryFirestoreImpl(
    firestore: FirebaseFirestore
) : UserRepository {

    private val usersCollection = firestore.collection("sessions")

    private fun DocumentSnapshot.toUserEntity(): UserEntity? {
        return try {
            val data = this.data ?: return null
            UserEntity(
                id = this.id,
                name = data["name"] as? String ?: "",
                role = UserRole.valueOf(data["role"] as? String ?: UserRole.CLIENT.name),
                clientId = data["clientId"] as? String
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun UserEntity.toFirestoreMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "role" to role.name,
            "clientId" to clientId
        )
    }

    override fun getAllUsers(): Flow<List<UserEntity>> {
        return usersCollection.snapshots().map { snapshot ->
            snapshot.documents.mapNotNull { it.toUserEntity() }
        }
    }

    override suspend fun getUserById(id: String): UserEntity? {
        return try {
            val doc = usersCollection.document(id).get().await()
            doc.toUserEntity()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun saveUser(user: UserEntity) {
        val data = user.toFirestoreMap()
        if (user.id.isBlank()) {
            usersCollection.add(data).await()
        } else {
            usersCollection.document(user.id).set(data).await()
        }
    }

    override suspend fun deleteUser(user: UserEntity) {
        usersCollection.document(user.id).delete().await()
    }
}
