package br.com.lucolimac.xuxubank.domain.usecase

import br.com.lucolimac.xuxubank.data.local.entity.UserEntity
import br.com.lucolimac.xuxubank.data.local.entity.UserRole
import br.com.lucolimac.xuxubank.domain.repository.ClientRepository
import br.com.lucolimac.xuxubank.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Business logic for authentication and role-based session management.
 * In XuxuBank, a user session is identified by an Email or Phone.
 */
class ManageUserUseCase(
    private val userRepository: UserRepository,
    private val clientRepository: ClientRepository
) {
    // Demo constants for the Manager
    companion object {
        const val MANAGER_IDENTIFIER = "xuxu@xuxubank.com.br"
        const val MANAGER_NAME = "Gerente Xuxu"
    }

    /**
     * Observes the active user session.
     */
    fun getCurrentUser(): Flow<UserEntity?> = userRepository.getAllUsers().map { it.firstOrNull() }

    /**
     * Unified login logic:
     * 1. Checks if the identifier belongs to the Manager.
     * 2. Checks if the identifier belongs to any registered Client.
     * @param identifier Email or formatted Phone string.
     * @return Result of the login attempt.
     */
    suspend fun login(identifier: String): LoginResult {
        // 1. Check for Manager
        if (identifier.equals(MANAGER_IDENTIFIER, ignoreCase = true)) {
            val manager = UserEntity(id = "1", name = MANAGER_NAME, role = UserRole.MANAGER)
            userRepository.saveUser(manager)
            return LoginResult.Success(UserRole.MANAGER)
        }

        // 2. Check for Client
        val client = clientRepository.getClientByIdentifier(identifier)
        return if (client != null) {
            val user = UserEntity(
                id = "1", 
                name = client.name, 
                role = UserRole.CLIENT, 
                clientId = client.id
            )
            userRepository.saveUser(user)
            LoginResult.Success(UserRole.CLIENT)
        } else {
            LoginResult.InvalidCredentials
        }
    }

    /**
     * Clears the current user session.
     */
    suspend fun logout() {
        userRepository.getUserById("1")?.let {
            userRepository.deleteUser(it)
        }
    }
}

/**
 * Result wrapper for login attempts.
 */
sealed interface LoginResult {
    data class Success(val role: UserRole) : LoginResult
    object InvalidCredentials : LoginResult
}
