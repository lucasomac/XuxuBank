package br.com.lucolimac.xuxubank.data.repository

import br.com.lucolimac.xuxubank.data.local.dao.ClientDao
import br.com.lucolimac.xuxubank.data.local.toDomain
import br.com.lucolimac.xuxubank.data.local.toEntity
import br.com.lucolimac.xuxubank.domain.model.Client
import br.com.lucolimac.xuxubank.domain.repository.ClientRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ClientRepositoryImpl(private val clientDao: ClientDao) : ClientRepository {
    override fun getAllClients(): Flow<List<Client>> = clientDao.getAllClients().map { list -> list.map { it.toDomain() } }

    override suspend fun getClientById(id: String): Client? = clientDao.getClientById(id)?.toDomain()

    override suspend fun getClientByIdentifier(identifier: String): Client? = clientDao.getClientByIdentifier(identifier)?.toDomain()

    override suspend fun saveClient(client: Client): String {
        clientDao.insertClient(client.toEntity())
        return client.id
    }

    override suspend fun updateClient(client: Client) = clientDao.updateClient(client.toEntity())

    override suspend fun deleteClient(client: Client) = clientDao.deleteClient(client.toEntity())
}
