package br.com.lucolimac.xuxubank.data.repository

import android.util.Log
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
import java.util.UUID

class ClientRepositoryImpl(
    private val clientDao: ClientDao,
    private val firestore: FirebaseFirestore
) : ClientRepository {

    private val clientsCollection = firestore.collection("clients")
    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        // 1. Retroactive Sync: Local -> Cloud
        repositoryScope.launch {
            try {
                val localClients = clientDao.getAllClientsList()
                localClients.forEach { clientEntity ->
                    val data = mapOf(
                        "name" to clientEntity.name,
                        "email" to clientEntity.email,
                        "phone" to clientEntity.phone
                    )
                    clientsCollection.document(clientEntity.id).set(data).await()
                }
                Log.d("ClientRepository", "Retroactive sync completed for ${localClients.size} clients")
            } catch (e: Exception) {
                Log.e("ClientRepository", "Retroactive sync failed: ${e.message}")
            }
        }

        // 2. Real-time synchronization: Firestore -> Room
        repositoryScope.launch {
            try {
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
            } catch (e: Exception) {
                Log.e("ClientRepository", "Firestore listener failed: ${e.message}")
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
        var finalId = client.id
        try {
            if (client.id.isBlank()) {
                val docRef = clientsCollection.add(data).await()
                finalId = docRef.id
            } else {
                clientsCollection.document(client.id).set(data).await()
            }
        } catch (e: Exception) {
            Log.e("ClientRepository", "Firestore save failed: ${e.message}")
            if (finalId.isBlank()) finalId = UUID.randomUUID().toString()
        }
        clientDao.insertClient(client.copy(id = finalId).toEntity())
        return finalId
    }

    override suspend fun updateClient(client: Client) {
        val data = mapOf(
            "name" to client.name,
            "email" to client.email,
            "phone" to client.phone
        )
        try {
            firestore.runBatch { batch ->
                batch.set(clientsCollection.document(client.id), data)
            }.await()
        } catch (e: Exception) {
            Log.e("ClientRepository", "Firestore update failed: ${e.message}")
        }
        clientDao.updateClient(client.toEntity())
    }

    override suspend fun deleteClient(client: Client) {
        try {
            clientsCollection.document(client.id).delete().await()
        } catch (e: Exception) {
            Log.e("ClientRepository", "Firestore delete failed: ${e.message}")
        }
        clientDao.deleteClient(client.toEntity())
    }
}
