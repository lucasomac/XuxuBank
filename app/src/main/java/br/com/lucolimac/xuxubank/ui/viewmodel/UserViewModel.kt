package br.com.lucolimac.xuxubank.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.lucolimac.xuxubank.data.local.entity.UserEntity
import br.com.lucolimac.xuxubank.domain.usecase.LoginResult
import br.com.lucolimac.xuxubank.domain.usecase.ManageUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Manages user authentication state and login flows.
 */
class UserViewModel(private val manageUserUseCase: ManageUserUseCase) : ViewModel() {
    
    /**
     * Exposes the currently logged-in user session.
     */
    val currentUser: StateFlow<UserEntity?> = manageUserUseCase.getCurrentUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _loginState = MutableStateFlow<LoginUIState>(LoginUIState.Idle)
    val loginState: StateFlow<LoginUIState> = _loginState.asStateFlow()

    /**
     * Unified login attempt using Email or Phone.
     */
    fun login(identifier: String) {
        viewModelScope.launch {
            _loginState.value = LoginUIState.Loading
            when (val result = manageUserUseCase.login(identifier)) {
                is LoginResult.Success -> {
                    _loginState.value = LoginUIState.Success
                }
                is LoginResult.InvalidCredentials -> {
                    _loginState.value = LoginUIState.Error("Dados de acesso inválidos.")
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _loginState.value = LoginUIState.Idle
            manageUserUseCase.logout()
        }
    }

    fun resetLoginState() {
        _loginState.value = LoginUIState.Idle
    }
}

/**
 * Represents the various states of the login process.
 */
sealed interface LoginUIState {
    object Idle : LoginUIState
    object Loading : LoginUIState
    object Success : LoginUIState
    data class Error(val message: String) : LoginUIState
}
