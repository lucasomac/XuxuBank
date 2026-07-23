package br.com.lucolimac.xuxubank.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.lucolimac.xuxubank.domain.model.Client
import br.com.lucolimac.xuxubank.domain.usecase.ManageClientUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Manages the state and user actions for Client-related screens.
 * Demonstrates the use of SavedStateHandle to handle Process Death.
 */
class ClientViewModel(
    private val manageClientUseCase: ManageClientUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    companion object {
        private const val SEARCH_QUERY_KEY = "search_query"
    }

    /**
     * Preserves search query even if the process is killed.
     */
    val searchQuery = savedStateHandle.getStateFlow(SEARCH_QUERY_KEY, "")

    /**
     * Observable state of all clients, filtered by query.
     * Uses 'combine' to react to both database changes and UI filter changes.
     */
    val allClients: StateFlow<List<Client>> = manageClientUseCase.getAllClients()
        .combine(searchQuery) { clients, query ->
            if (query.isBlank()) clients else clients.filter { it.name.contains(query, ignoreCase = true) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateSearchQuery(query: String) {
        savedStateHandle[SEARCH_QUERY_KEY] = query
    }

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
