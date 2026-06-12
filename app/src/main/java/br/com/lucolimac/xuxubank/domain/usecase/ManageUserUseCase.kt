package br.com.lucolimac.xuxubank.domain.usecase

import br.com.lucolimac.xuxubank.data.local.entity.UserEntity
import br.com.lucolimac.xuxubank.data.local.entity.UserRole
import br.com.lucolimac.xuxubank.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Handles simulated authentication and role assignment.
 */
class ManageUserUseCase(private val userRepository: UserRepository) {

    /**
     * Observes the active user session.
     */
    fun getCurrentUser(): Flow<UserEntity?> = userRepository.getAllUsers().map { it.firstOrNull() }

    /**
     * Enforces the Manager role, granting full system access.
     */
    suspend fun loginAsManager() {
        val manager = UserEntity(id = 1, name = "Manager", role = UserRole.MANAGER)
        userRepository.saveUser(manager)
    }

    /**
     * Enforces the Client role, restricting views to only the specific clientId's data.
     */
    suspend fun loginAsClient(clientId: Long, name: String) {
        val user = UserEntity(id = 1, name = name, role = UserRole.CLIENT, clientId = clientId)
        userRepository.saveUser(user)
    }

    suspend fun logout() {
        userRepository.getUserById(1)?.let {
            userRepository.deleteUser(it)
        }
    }
}
