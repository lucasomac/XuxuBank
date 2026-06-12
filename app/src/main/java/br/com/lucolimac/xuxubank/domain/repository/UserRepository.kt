package br.com.lucolimac.xuxubank.domain.repository

import br.com.lucolimac.xuxubank.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getAllUsers(): Flow<List<UserEntity>>
    suspend fun getUserById(id: Long): UserEntity?
    suspend fun saveUser(user: UserEntity)
    suspend fun deleteUser(user: UserEntity)
}
