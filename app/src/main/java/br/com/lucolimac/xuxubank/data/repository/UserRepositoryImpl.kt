package br.com.lucolimac.xuxubank.data.repository

import br.com.lucolimac.xuxubank.data.local.dao.UserDao
import br.com.lucolimac.xuxubank.data.local.entity.UserEntity
import br.com.lucolimac.xuxubank.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(private val userDao: UserDao) : UserRepository {
    override fun getAllUsers(): Flow<List<UserEntity>> = userDao.getAllUsers()

    override suspend fun getUserById(id: Long): UserEntity? = userDao.getUserById(id)

    override suspend fun saveUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    override suspend fun deleteUser(user: UserEntity) {
        userDao.deleteUser(user)
    }
}
