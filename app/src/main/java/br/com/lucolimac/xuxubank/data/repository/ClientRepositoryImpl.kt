package br.com.lucolimac.xuxubank.data.repository

import br.com.lucolimac.xuxubank.data.local.dao.ClientDao
import br.com.lucolimac.xuxubank.data.local.toDomain
import br.com.lucolimac.xuxubank.data.local.toEntity
import br.com.lucolimac.xuxubank.domain.model.Client
import br.com.lucolimac.xuxubank.domain.repository.ClientRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ClientRepositoryImpl(
    private val clientDao: ClientDao,
    private val firestore: FirebaseFirestore
) : ClientRepository {

    private val clientsCollection = firestore.collection("clients")
    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        // Real-time synchronization: Firestore -> Room
        repositoryScope.launch {
            clientsCollection.snapshots().collect { snapshot ->
                val clients = snapshot.documents.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null
                    Client(
                        id = doc.id,
                        name = data["name"] as? String ?: "",
                        email = data["email"] as? String ?: "",
                        phone = data["phone"] as? String ?: ""
                    ).toEntity()
                }
                clientDao.insertClients(clients)
            }
        }
    }

    override fun getAllClients(): Flow<List<Client>> {
        return clientDao.getAllClients().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getClientById(id: String): Client? {
        return clientDao.getClientById(id)?.toDomain()
    }

    override suspend fun getClientByIdentifier(identifier: String): Client? {
        return clientDao.getClientByIdentifier(identifier)?.toDomain()
    }

    override suspend fun saveClient(client: Client): String {
        val data = mapOf(
            "name" to client.name,
            "email" to client.email,
            "phone" to client.phone
        )
        val id = if (client.id.isBlank()) {
            val docRef = clientsCollection.add(data).await()
            docRef.id
        } else {
            clientsCollection.document(client.id).set(data).await()
            client.id
        }
        clientDao.insertClient(client.copy(id = id).toEntity())
        return id
    }

    override suspend fun updateClient(client: Client) {
        val data = mapOf(
            "name" to client.name,
            "email" to client.email,
            "phone" to client.phone
        )
        firestore.runBatch { batch ->
            batch.set(clientsCollection.document(client.id), data)
        }.await()
        clientDao.updateClient(client.toEntity())
    }

    override suspend fun deleteClient(client: Client) {
        clientsCollection.document(client.id).delete().await()
        clientDao.deleteClient(client.toEntity())
    }
}
