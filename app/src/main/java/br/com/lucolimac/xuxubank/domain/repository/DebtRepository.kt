package br.com.lucolimac.xuxubank.domain.repository

import br.com.lucolimac.xuxubank.data.local.entity.DebtStatus
import br.com.lucolimac.xuxubank.domain.model.Debt
import kotlinx.coroutines.flow.Flow

interface DebtRepository {
    fun getAllDebts(): Flow<List<Debt>>
    fun getDebtsByClient(clientId: String): Flow<List<Debt>>
    fun getDebtsByStatus(status: DebtStatus): Flow<List<Debt>>
    suspend fun getDebtById(id: String): Debt?
    suspend fun saveDebt(debt: Debt)
    suspend fun updateDebt(debt: Debt)
    suspend fun deleteDebt(debt: Debt)
}
