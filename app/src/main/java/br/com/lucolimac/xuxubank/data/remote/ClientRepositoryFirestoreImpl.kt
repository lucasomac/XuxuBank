package br.com.lucolimac.xuxubank.data.remote

import br.com.lucolimac.xuxubank.domain.model.Client
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

    private fun DocumentSnapshot.toClient(): Client? {
        return try {
            val data = this.data ?: return null
            Client(
                id = this.id,
                name = data["name"] as? String ?: "",
                email = data["email"] as? String ?: "",
                phone = data["phone"] as? String ?: ""
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun Client.toFirestoreMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "email" to email,
            "phone" to phone
        )
    }

    override fun getAllClients(): Flow<List<Client>> {
        return clientsCollection.snapshots().map { snapshot ->
            snapshot.documents.mapNotNull { it.toClient() }
        }
    }

    override suspend fun getClientById(id: String): Client? {
        return try {
            val doc = clientsCollection.document(id).get().await()
            doc.toClient()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getClientByIdentifier(identifier: String): Client? {
        return try {
            // Check email
            val emailQuery = clientsCollection.whereEqualTo("email", identifier).limit(1).get().await()
            if (!emailQuery.isEmpty) {
                return emailQuery.documents[0].toClient()
            }
            // Check phone
            val phoneQuery = clientsCollection.whereEqualTo("phone", identifier).limit(1).get().await()
            if (!phoneQuery.isEmpty) {
                return phoneQuery.documents[0].toClient()
            }
            null
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun saveClient(client: Client): String {
        val data = client.toFirestoreMap()
        return if (client.id.isBlank()) {
            val docRef = clientsCollection.add(data).await()
            docRef.id
        } else {
            clientsCollection.document(client.id).set(data).await()
            client.id
        }
    }

    override suspend fun updateClient(client: Client) {
        clientsCollection.document(client.id).set(client.toFirestoreMap()).await()
    }

    override suspend fun deleteClient(client: Client) {
        clientsCollection.document(client.id).delete().await()
    }
}
