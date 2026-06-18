package br.com.lucolimac.xuxubank.domain.usecase

import br.com.lucolimac.xuxubank.data.local.entity.DebtEntity
import br.com.lucolimac.xuxubank.data.local.entity.DebtStatus
import br.com.lucolimac.xuxubank.domain.repository.DebtRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

/**
 * Unit tests for ManageDebtUseCase following the 'testing-setup' skill.
 * Updated for String IDs (Firestore compatibility).
 */
class ManageDebtUseCaseTest {

    private val fakeDebtRepository = object : DebtRepository {
        val savedDebts = mutableListOf<DebtEntity>()
        override fun getAllDebts(): Flow<List<DebtEntity>> = flowOf(savedDebts)
        override fun getDebtsByClient(clientId: String): Flow<List<DebtEntity>> = flowOf(savedDebts.filter { it.clientId == clientId })
        override fun getDebtsByStatus(status: DebtStatus): Flow<List<DebtEntity>> = flowOf(savedDebts.filter { it.status == status })
        override suspend fun getDebtById(id: String): DebtEntity? = savedDebts.find { it.id == id }
        override suspend fun saveDebt(debt: DebtEntity) { savedDebts.add(debt) }
        override suspend fun updateDebt(debt: DebtEntity) {
            val index = savedDebts.indexOfFirst { it.id == debt.id }
            if (index != -1) savedDebts[index] = debt
        }
        override suspend fun deleteDebt(debt: DebtEntity) { savedDebts.remove(debt) }
    }

    private val manageDebtUseCase = ManageDebtUseCase(fakeDebtRepository)

    @Test
    fun `createDebt should generate correct number of installments`() = runBlocking {
        // Given
        val clientId = "1"
        val description = "Test Debt"
        val totalAmount = BigDecimal("1000.00")
        val installments = 10
        val firstDueDate = System.currentTimeMillis()

        // When
        manageDebtUseCase.createDebt(clientId, description, totalAmount, firstDueDate, installments)

        // Then
        assertEquals(installments, fakeDebtRepository.savedDebts.size)
        assertEquals(BigDecimal("100.00"), fakeDebtRepository.savedDebts[0].amount)
        assertEquals("Test Debt (1/10)", fakeDebtRepository.savedDebts[0].description)
    }
}
