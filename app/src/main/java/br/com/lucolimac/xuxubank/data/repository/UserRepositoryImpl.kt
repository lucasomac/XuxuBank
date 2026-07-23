package br.com.lucolimac.xuxubank.data.repository

import br.com.lucolimac.xuxubank.data.local.dao.UserDao
import br.com.lucolimac.xuxubank.data.local.entity.UserRole
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
        if (user.id.isBlank()) {
            val docRef = usersCollection.add(data).await()
            userDao.insertUser(user.copy(id = docRef.id).toEntity())
        } else {
            usersCollection.document(user.id).set(data).await()
            userDao.insertUser(user.toEntity())
        }
    }

    override suspend fun deleteUser(user: User) {
        usersCollection.document(user.id).delete().await()
        userDao.deleteUser(user.toEntity())
    }
}
