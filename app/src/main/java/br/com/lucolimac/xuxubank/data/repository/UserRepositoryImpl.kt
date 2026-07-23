package br.com.lucolimac.xuxubank.data.repository

import br.com.lucolimac.xuxubank.data.local.dao.UserDao
import br.com.lucolimac.xuxubank.data.local.toDomain
import br.com.lucolimac.xuxubank.data.local.toEntity
import br.com.lucolimac.xuxubank.domain.model.User
import br.com.lucolimac.xuxubank.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepositoryImpl(private val userDao: UserDao) : UserRepository {
    override fun getAllUsers(): Flow<List<User>> = userDao.getAllUsers().map { list -> list.map { it.toDomain() } }

    override suspend fun getUserById(id: String): User? = userDao.getUserById(id)?.toDomain()

    override suspend fun saveUser(user: User) {
        userDao.insertUser(user.toEntity())
    }

    override suspend fun deleteUser(user: User) {
        userDao.deleteUser(user.toEntity())
    }
}
