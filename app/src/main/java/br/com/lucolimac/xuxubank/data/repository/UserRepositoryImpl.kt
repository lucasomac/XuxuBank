package br.com.lucolimac.xuxubank.data.repository

import android.util.Log
import br.com.lucolimac.xuxubank.data.local.dao.UserDao
import br.com.lucolimac.xuxubank.data.local.toDomain
import br.com.lucolimac.xuxubank.data.local.toEntity
import br.com.lucolimac.xuxubank.domain.model.User
import br.com.lucolimac.xuxubank.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(
    private val userDao: UserDao,
    private val firestore: FirebaseFirestore
) : UserRepository {

    private val usersCollection = firestore.collection("sessions")

    override fun getAllUsers(): Flow<List<User>> {
        return userDao.getAllUsers().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getUserById(id: String): User? {
        return userDao.getUserById(id)?.toDomain()
    }

    override suspend fun saveUser(user: User) {
        val data = mapOf(
            "name" to user.name,
            "role" to user.role.name,
            "clientId" to user.clientId
        )
        try {
            if (user.id.isBlank()) {
                val docRef = usersCollection.add(data).await()
                userDao.insertUser(user.copy(id = docRef.id).toEntity())
            } else {
                usersCollection.document(user.id).set(data).await()
                userDao.insertUser(user.toEntity())
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Firestore save failed, falling back to local: ${e.message}")
            // Use a stable ID for manager if ID is blank and Firestore failed
            val finalUser = if (user.id.isBlank()) user.copy(id = "temp_${System.currentTimeMillis()}") else user
            userDao.insertUser(finalUser.toEntity())
        }
    }

    override suspend fun deleteUser(user: User) {
        try {
            usersCollection.document(user.id).delete().await()
        } catch (e: Exception) {
            Log.e("UserRepository", "Firestore delete failed: ${e.message}")
        }
        userDao.deleteUser(user.toEntity())
    }
}
