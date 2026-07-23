package br.com.lucolimac.xuxubank.data.repository

import br.com.lucolimac.xuxubank.data.local.dao.DebtDao
import br.com.lucolimac.xuxubank.data.local.entity.DebtStatus
import br.com.lucolimac.xuxubank.data.local.toDomain
import br.com.lucolimac.xuxubank.data.local.toEntity
import br.com.lucolimac.xuxubank.domain.model.Debt
import br.com.lucolimac.xuxubank.domain.repository.DebtRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.math.BigDecimal

class DebtRepositoryImpl(
    private val debtDao: DebtDao,
    private val firestore: FirebaseFirestore
) : DebtRepository {

    private val debtsCollection = firestore.collection("debts")
    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        // Real-time synchronization: Firestore -> Room
        repositoryScope.launch {
            debtsCollection.snapshots().collect { snapshot ->
                val debts = snapshot.documents.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null
                    Debt(
                        id = doc.id,
                        clientId = data["clientId"] as? String ?: "",
                        description = data["description"] as? String ?: "",
                        amount = BigDecimal(data["amount"]?.toString() ?: "0"),
                        dueDate = data["dueDate"] as? Long,
                        status = DebtStatus.valueOf(data["status"] as? String ?: DebtStatus.PENDING.name),
                        totalInstallments = (data["totalInstallments"] as? Long)?.toInt() ?: 1,
                        currentInstallment = (data["currentInstallment"] as? Long)?.toInt() ?: 1,
                        createdAt = data["createdAt"] as? Long ?: System.currentTimeMillis()
                    ).toEntity()
                }
                debtDao.insertDebts(debts)
            }
        }
    }

    override fun getAllDebts(): Flow<List<Debt>> {
        return debtDao.getAllDebts().map { list -> list.map { it.toDomain() } }
    }

    override fun getDebtsByClient(clientId: String): Flow<List<Debt>> {
        return debtDao.getDebtsByClient(clientId).map { list -> list.map { it.toDomain() } }
    }

    override fun getDebtsByStatus(status: DebtStatus): Flow<List<Debt>> {
        return debtDao.getDebtsByStatus(status).map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getDebtById(id: String): Debt? {
        return debtDao.getDebtById(id)?.toDomain()
    }

    override suspend fun saveDebt(debt: Debt) {
        val data = mapOf(
            "clientId" to debt.clientId,
            "description" to debt.description,
            "amount" to debt.amount.toString(),
            "dueDate" to debt.dueDate,
            "status" to debt.status.name,
            "totalInstallments" to debt.totalInstallments,
            "currentInstallment" to debt.currentInstallment,
            "createdAt" to debt.createdAt
        )
        val id = if (debt.id.isBlank()) {
            val docRef = debtsCollection.add(data).await()
            docRef.id
        } else {
            debtsCollection.document(debt.id).set(data).await()
            debt.id
        }
        debtDao.insertDebt(debt.copy(id = id).toEntity())
    }

    override suspend fun updateDebt(debt: Debt) {
        val data = mapOf(
            "clientId" to debt.clientId,
            "description" to debt.description,
            "amount" to debt.amount.toString(),
            "dueDate" to debt.dueDate,
            "status" to debt.status.name,
            "totalInstallments" to debt.totalInstallments,
            "currentInstallment" to debt.currentInstallment,
            "createdAt" to debt.createdAt
        )
        debtsCollection.document(debt.id).set(data).await()
        debtDao.updateDebt(debt.toEntity())
    }

    override suspend fun deleteDebt(debt: Debt) {
        debtsCollection.document(debt.id).delete().await()
        debtDao.deleteDebt(debt.toEntity())
    }
}
