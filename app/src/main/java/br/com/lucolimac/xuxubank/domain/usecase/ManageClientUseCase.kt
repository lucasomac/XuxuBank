package br.com.lucolimac.xuxubank.domain.usecase

import br.com.lucolimac.xuxubank.domain.model.Client
import br.com.lucolimac.xuxubank.domain.repository.ClientRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case to manage client-related operations.
 * Decouples the UI from the repository and handle mapping between domain and entity models.
 */
class ManageClientUseCase(private val clientRepository: ClientRepository) {

    /**
     * Retrieves all registered clients as a flow.
     */
    fun getAllClients(): Flow<List<Client>> = clientRepository.getAllClients()

    /**
     * Finds a specific client by their unique ID.
     */
    suspend fun getClientById(id: String): Client? = clientRepository.getClientById(id)

    /**
     * Registers a new client in the system.
     */
    suspend fun createClient(name: String, email: String, phone: String): String {
        val client = Client("", name, email, phone)
        return clientRepository.saveClient(client)
    }

    /**
     * Updates an existing client's information.
     */
    suspend fun updateClient(client: Client) {
        clientRepository.updateClient(client)
    }

    /**
     * Removes a client from the system.
     */
    suspend fun deleteClient(clientId: String) {
        val client = clientRepository.getClientById(clientId)
        client?.let {
            clientRepository.deleteClient(it)
        }
    }
}
