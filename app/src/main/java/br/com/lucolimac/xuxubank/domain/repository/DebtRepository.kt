package br.com.lucolimac.xuxubank.domain.repository

import br.com.lucolimac.xuxubank.data.local.entity.DebtEntity
import br.com.lucolimac.xuxubank.data.local.entity.DebtStatus
import kotlinx.coroutines.flow.Flow

interface DebtRepository {
    fun getAllDebts(): Flow<List<DebtEntity>>
    fun getDebtsByClient(clientId: String): Flow<List<DebtEntity>>
    fun getDebtsByStatus(status: DebtStatus): Flow<List<DebtEntity>>
    suspend fun getDebtById(id: String): DebtEntity?
    suspend fun saveDebt(debt: DebtEntity)
    suspend fun updateDebt(debt: DebtEntity)
    suspend fun deleteDebt(debt: DebtEntity)
}
