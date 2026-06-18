package br.com.lucolimac.xuxubank.data.remote

import br.com.lucolimac.xuxubank.data.local.entity.ClientEntity
import br.com.lucolimac.xuxubank.domain.repository.ClientRepository
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class ClientRepositoryFirestoreImpl(
    private val firestore: FirebaseFirestore
) : ClientRepository {

    private val clientsCollection = firestore.collection("clients")

    private fun DocumentSnapshot.toClientEntity(): ClientEntity? {
        return try {
            val data = this.data ?: return null
            ClientEntity(
                id = this.id,
                name = data["name"] as? String ?: "",
                email = data["email"] as? String ?: "",
                phone = data["phone"] as? String ?: ""
            )
        } catch (e: Exception) {
            null
        }
    }

    override fun getAllClients(): Flow<List<ClientEntity>> {
        return clientsCollection.snapshots().map { snapshot ->
            snapshot.documents.mapNotNull { it.toClientEntity() }
        }
    }

    override suspend fun getClientById(id: String): ClientEntity? {
        return try {
            val doc = clientsCollection.document(id).get().await()
            doc.toClientEntity()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getClientByIdentifier(identifier: String): ClientEntity? {
        return try {
            // Check email
            val emailQuery = clientsCollection.whereEqualTo("email", identifier).limit(1).get().await()
            if (!emailQuery.isEmpty) {
                return emailQuery.documents[0].toClientEntity()
            }
            // Check phone
            val phoneQuery = clientsCollection.whereEqualTo("phone", identifier).limit(1).get().await()
            if (!phoneQuery.isEmpty) {
                return phoneQuery.documents[0].toClientEntity()
            }
            null
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun saveClient(client: ClientEntity): String {
        return if (client.id.isBlank()) {
            val docRef = clientsCollection.add(client).await()
            docRef.id
        } else {
            clientsCollection.document(client.id).set(client).await()
            client.id
        }
    }

    override suspend fun updateClient(client: ClientEntity) {
        clientsCollection.document(client.id).set(client).await()
    }

    override suspend fun deleteClient(client: ClientEntity) {
        clientsCollection.document(client.id).delete().await()
    }
}
