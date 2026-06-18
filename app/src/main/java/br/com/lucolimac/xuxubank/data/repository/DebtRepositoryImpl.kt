package br.com.lucolimac.xuxubank.data.repository

import br.com.lucolimac.xuxubank.data.local.dao.DebtDao
import br.com.lucolimac.xuxubank.data.local.entity.DebtEntity
import br.com.lucolimac.xuxubank.data.local.entity.DebtStatus
import br.com.lucolimac.xuxubank.domain.repository.DebtRepository
import kotlinx.coroutines.flow.Flow

class DebtRepositoryImpl(private val debtDao: DebtDao) : DebtRepository {
    override fun getAllDebts(): Flow<List<DebtEntity>> = debtDao.getAllDebts()

    override fun getDebtsByClient(clientId: String): Flow<List<DebtEntity>> = debtDao.getDebtsByClient(clientId)

    override fun getDebtsByStatus(status: DebtStatus): Flow<List<DebtEntity>> = debtDao.getDebtsByStatus(status)

    override suspend fun getDebtById(id: String): DebtEntity? = debtDao.getDebtById(id)

    override suspend fun saveDebt(debt: DebtEntity) = debtDao.insertDebt(debt)

    override suspend fun updateDebt(debt: DebtEntity) = debtDao.updateDebt(debt)

    override suspend fun deleteDebt(debt: DebtEntity) = debtDao.deleteDebt(debt)
}
