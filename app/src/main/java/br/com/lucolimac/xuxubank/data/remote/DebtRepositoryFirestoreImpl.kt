package br.com.lucolimac.xuxubank.data.remote

import br.com.lucolimac.xuxubank.data.local.entity.DebtEntity
import br.com.lucolimac.xuxubank.data.local.entity.DebtStatus
import br.com.lucolimac.xuxubank.domain.repository.DebtRepository
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.math.BigDecimal

class DebtRepositoryFirestoreImpl(
    private val firestore: FirebaseFirestore
) : DebtRepository {

    private val debtsCollection = firestore.collection("debts")

    private fun DocumentSnapshot.toDebtEntity(): DebtEntity? {
        return try {
            val data = this.data ?: return null
            DebtEntity(
                id = this.id,
                clientId = data["clientId"] as? String ?: "",
                description = data["description"] as? String ?: "",
                amount = BigDecimal(data["amount"]?.toString() ?: "0"),
                dueDate = data["dueDate"] as? Long,
                status = DebtStatus.valueOf(data["status"] as? String ?: DebtStatus.PENDING.name),
                totalInstallments = (data["totalInstallments"] as? Long)?.toInt() ?: 1,
                currentInstallment = (data["currentInstallment"] as? Long)?.toInt() ?: 1,
                createdAt = data["createdAt"] as? Long ?: System.currentTimeMillis()
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun DebtEntity.toFirestoreMap(): Map<String, Any?> {
        return mapOf(
            "clientId" to clientId,
            "description" to description,
            "amount" to amount.toString(), // Store as String for precision
            "dueDate" to dueDate,
            "status" to status.name,
            "totalInstallments" to totalInstallments,
            "currentInstallment" to currentInstallment,
            "createdAt" to createdAt
        )
    }

    override fun getAllDebts(): Flow<List<DebtEntity>> {
        return debtsCollection.snapshots().map { snapshot ->
            snapshot.documents.mapNotNull { it.toDebtEntity() }
        }
    }

    override fun getDebtsByClient(clientId: String): Flow<List<DebtEntity>> {
        return debtsCollection.whereEqualTo("clientId", clientId).snapshots().map { snapshot ->
            snapshot.documents.mapNotNull { it.toDebtEntity() }
        }
    }

    override fun getDebtsByStatus(status: DebtStatus): Flow<List<DebtEntity>> {
        return debtsCollection.whereEqualTo("status", status.name).snapshots().map { snapshot ->
            snapshot.documents.mapNotNull { it.toDebtEntity() }
        }
    }

    override suspend fun getDebtById(id: String): DebtEntity? {
        return try {
            val doc = debtsCollection.document(id).get().await()
            doc.toDebtEntity()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun saveDebt(debt: DebtEntity) {
        val data = debt.toFirestoreMap()
        if (debt.id.isBlank()) {
            debtsCollection.add(data).await()
        } else {
            debtsCollection.document(debt.id).set(data).await()
        }
    }

    override suspend fun updateDebt(debt: DebtEntity) {
        debtsCollection.document(debt.id).set(debt.toFirestoreMap()).await()
    }

    override suspend fun deleteDebt(debt: DebtEntity) {
        debtsCollection.document(debt.id).delete().await()
    }
}
