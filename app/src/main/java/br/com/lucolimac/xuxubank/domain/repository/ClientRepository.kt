package br.com.lucolimac.xuxubank.domain.repository

import br.com.lucolimac.xuxubank.domain.model.Client
import kotlinx.coroutines.flow.Flow

interface ClientRepository {
    fun getAllClients(): Flow<List<Client>>
    suspend fun getClientById(id: String): Client?
    suspend fun getClientByIdentifier(identifier: String): Client?
    suspend fun saveClient(client: Client): String
    suspend fun updateClient(client: Client)
    suspend fun deleteClient(client: Client)
}
