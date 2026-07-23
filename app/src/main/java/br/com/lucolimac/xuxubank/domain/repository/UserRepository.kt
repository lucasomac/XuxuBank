package br.com.lucolimac.xuxubank.domain.repository

import br.com.lucolimac.xuxubank.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getAllUsers(): Flow<List<User>>
    suspend fun getUserById(id: String): User?
    suspend fun saveUser(user: User)
    suspend fun deleteUser(user: User)
}
