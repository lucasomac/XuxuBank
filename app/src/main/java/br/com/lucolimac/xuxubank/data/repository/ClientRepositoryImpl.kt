package br.com.lucolimac.xuxubank.data.repository

import br.com.lucolimac.xuxubank.data.local.dao.ClientDao
import br.com.lucolimac.xuxubank.data.local.entity.ClientEntity
import br.com.lucolimac.xuxubank.domain.repository.ClientRepository
import kotlinx.coroutines.flow.Flow

class ClientRepositoryImpl(private val clientDao: ClientDao) : ClientRepository {
    override fun getAllClients(): Flow<List<ClientEntity>> = clientDao.getAllClients()

    override suspend fun getClientById(id: Long): ClientEntity? = clientDao.getClientById(id)

    override suspend fun saveClient(client: ClientEntity): Long = clientDao.insertClient(client)

    override suspend fun updateClient(client: ClientEntity) = clientDao.updateClient(client)

    override suspend fun deleteClient(client: ClientEntity) = clientDao.deleteClient(client)
}
