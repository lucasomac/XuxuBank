package br.com.lucolimac.xuxubank.domain.repository

import br.com.lucolimac.xuxubank.data.local.entity.ClientEntity
import kotlinx.coroutines.flow.Flow

interface ClientRepository {
    fun getAllClients(): Flow<List<ClientEntity>>
    suspend fun getClientById(id: String): ClientEntity?
    suspend fun getClientByIdentifier(identifier: String): ClientEntity?
    suspend fun saveClient(client: ClientEntity): String
    suspend fun updateClient(client: ClientEntity)
    suspend fun deleteClient(client: ClientEntity)
}
