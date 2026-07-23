package br.com.lucolimac.xuxubank.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.lucolimac.xuxubank.domain.model.Client
import br.com.lucolimac.xuxubank.domain.usecase.ManageClientUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Manages the state and user actions for Client-related screens.
 */
class ClientViewModel(private val manageClientUseCase: ManageClientUseCase) : ViewModel() {
    
    /**
     * Observable state of all clients for the Manager dashboard.
     */
    val allClients: StateFlow<List<Client>> = manageClientUseCase.getAllClients()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun saveClient(name: String, email: String, phone: String) {
        viewModelScope.launch {
            manageClientUseCase.createClient(name, email, phone)
        }
    }

    fun updateClient(client: Client) {
        viewModelScope.launch {
            manageClientUseCase.updateClient(client)
        }
    }

    fun deleteClient(clientId: String) {
        viewModelScope.launch {
            manageClientUseCase.deleteClient(clientId)
        }
    }
}
