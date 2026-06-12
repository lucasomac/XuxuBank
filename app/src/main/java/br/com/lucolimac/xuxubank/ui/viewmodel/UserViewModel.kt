package br.com.lucolimac.xuxubank.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.lucolimac.xuxubank.data.local.entity.UserEntity
import br.com.lucolimac.xuxubank.domain.usecase.ManageUserUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Manages user authentication state and role assignments.
 */
class UserViewModel(private val manageUserUseCase: ManageUserUseCase) : ViewModel() {
    
    /**
     * Exposes the currently logged-in user as an observable StateFlow.
     */
    val currentUser: StateFlow<UserEntity?> = manageUserUseCase.getCurrentUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun loginAsManager() {
        viewModelScope.launch {
            manageUserUseCase.loginAsManager()
        }
    }

    /**
     * Simulates logging in as a specific client.
     */
    fun loginAsClient(clientId: Long, name: String) {
        viewModelScope.launch {
            manageUserUseCase.loginAsClient(clientId, name)
        }
    }

    fun logout() {
        viewModelScope.launch {
            manageUserUseCase.logout()
        }
    }
}
