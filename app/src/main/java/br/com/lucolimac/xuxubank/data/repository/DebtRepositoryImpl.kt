package br.com.lucolimac.xuxubank.data.repository

import br.com.lucolimac.xuxubank.data.local.dao.DebtDao
import br.com.lucolimac.xuxubank.data.local.entity.DebtStatus
import br.com.lucolimac.xuxubank.data.local.toDomain
import br.com.lucolimac.xuxubank.data.local.toEntity
import br.com.lucolimac.xuxubank.domain.model.Debt
import br.com.lucolimac.xuxubank.domain.repository.DebtRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DebtRepositoryImpl(private val debtDao: DebtDao) : DebtRepository {
    override fun getAllDebts(): Flow<List<Debt>> = debtDao.getAllDebts().map { list -> list.map { it.toDomain() } }

    override fun getDebtsByClient(clientId: String): Flow<List<Debt>> = debtDao.getDebtsByClient(clientId).map { list -> list.map { it.toDomain() } }

    override fun getDebtsByStatus(status: DebtStatus): Flow<List<Debt>> = debtDao.getDebtsByStatus(status).map { list -> list.map { it.toDomain() } }

    override suspend fun getDebtById(id: String): Debt? = debtDao.getDebtById(id)?.toDomain()

    override suspend fun saveDebt(debt: Debt) = debtDao.insertDebt(debt.toEntity())

    override suspend fun updateDebt(debt: Debt) = debtDao.updateDebt(debt.toEntity())

    override suspend fun deleteDebt(debt: Debt) = debtDao.deleteDebt(debt.toEntity())
}
