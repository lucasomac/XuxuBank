package br.com.lucolimac.xuxubank.domain.usecase

import br.com.lucolimac.xuxubank.data.local.entity.DebtEntity
import br.com.lucolimac.xuxubank.data.local.entity.DebtStatus
import br.com.lucolimac.xuxubank.data.local.toEntity
import br.com.lucolimac.xuxubank.domain.model.Debt
import br.com.lucolimac.xuxubank.domain.repository.DebtRepository
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

/**
 * Business logic for managing debts.
 * Handles installment distribution and date calculations.
 */
class ManageDebtUseCase(private val debtRepository: DebtRepository) {

    /**
     * Retrieves all debts across all clients.
     */
    fun getAllDebts(): Flow<List<DebtEntity>> = debtRepository.getAllDebts()

    /**
     * Filters debts for a specific client.
     */
    fun getDebtsByClient(clientId: Long): Flow<List<DebtEntity>> = debtRepository.getDebtsByClient(clientId)

    /**
     * Core logic for debt creation.
     * If installments > 1, it automatically splits the total amount and creates
     * multiple debt entries with sequential monthly due dates.
     */
    suspend fun createDebt(
        clientId: Long,
        description: String,
        totalAmount: Double,
        firstDueDate: Long?,
        installments: Int
    ) {
        val amountPerInstallment = totalAmount / installments
        val calendar = Calendar.getInstance()
        firstDueDate?.let { calendar.timeInMillis = it }

        for (i in 1..installments) {
            val debt = Debt(
                id = 0,
                clientId = clientId, // Mapping clientId to domain model's clientId
                description = if (installments > 1) "$description ($i/$installments)" else description,
                amount = amountPerInstallment,
                dueDate = if (firstDueDate != null) calendar.timeInMillis else null,
                status = DebtStatus.PENDING,
                totalInstallments = installments,
                currentInstallment = i,
                createdAt = System.currentTimeMillis()
            )
            debtRepository.saveDebt(debt.toEntity())
            
            // Increment month for the next installment entry
            if (firstDueDate != null) {
                calendar.add(Calendar.MONTH, 1)
            }
        }
    }

    suspend fun updateDebt(debt: DebtEntity) {
        debtRepository.updateDebt(debt)
    }

    /**
     * Directly toggles the status of a specific debt entry.
     */
    suspend fun updateDebtStatus(debtId: Long, status: DebtStatus) {
        val debt = debtRepository.getDebtById(debtId)
        debt?.let {
            debtRepository.updateDebt(it.copy(status = status))
        }
    }

    suspend fun deleteDebt(debtId: Long) {
        val debt = debtRepository.getDebtById(debtId)
        debt?.let {
            debtRepository.deleteDebt(it)
        }
    }
}
