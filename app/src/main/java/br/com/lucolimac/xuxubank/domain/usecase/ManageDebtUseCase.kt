package br.com.lucolimac.xuxubank.domain.usecase

import br.com.lucolimac.xuxubank.data.local.entity.DebtStatus
import br.com.lucolimac.xuxubank.domain.model.Debt
import br.com.lucolimac.xuxubank.domain.repository.DebtRepository
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Calendar

/**
 * Business logic for managing debts.
 * Handles installment distribution and date calculations using BigDecimal for accuracy.
 */
class ManageDebtUseCase(private val debtRepository: DebtRepository) {

    /**
     * Retrieves all debts across all clients.
     */
    fun getAllDebts(): Flow<List<Debt>> = debtRepository.getAllDebts()

    /**
     * Filters debts for a specific client.
     */
    fun getDebtsByClient(clientId: String): Flow<List<Debt>> = debtRepository.getDebtsByClient(clientId)

    /**
     * Core logic for debt creation.
     * If installments > 1, it automatically splits the total amount and creates
     * multiple debt entries with sequential monthly due dates.
     */
    suspend fun createDebt(
        clientId: String,
        description: String,
        totalAmount: BigDecimal,
        firstDueDate: Long?,
        installments: Int
    ) {
        val amountPerInstallment = totalAmount.divide(BigDecimal(installments), 2, RoundingMode.HALF_UP)
        val calendar = Calendar.getInstance()
        firstDueDate?.let { calendar.timeInMillis = it }

        for (i in 1..installments) {
            val debt = Debt(
                id = "",
                clientId = clientId,
                description = if (installments > 1) "$description ($i/$installments)" else description,
                amount = amountPerInstallment,
                dueDate = if (firstDueDate != null) calendar.timeInMillis else null,
                status = DebtStatus.PENDING,
                totalInstallments = installments,
                currentInstallment = i,
                createdAt = System.currentTimeMillis()
            )
            debtRepository.saveDebt(debt)
            
            if (firstDueDate != null) {
                calendar.add(Calendar.MONTH, 1)
            }
        }
    }

    suspend fun updateDebt(debt: Debt) {
        debtRepository.updateDebt(debt)
    }

    /**
     * Directly toggles the status of a specific debt entry.
     */
    suspend fun updateDebtStatus(debtId: String, status: DebtStatus) {
        val debt = debtRepository.getDebtById(debtId)
        debt?.let {
            debtRepository.updateDebt(it.copy(status = status))
        }
    }

    suspend fun deleteDebt(debtId: String) {
        val debt = debtRepository.getDebtById(debtId)
        debt?.let {
            debtRepository.deleteDebt(it)
        }
    }
}
