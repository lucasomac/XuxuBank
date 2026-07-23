package br.com.lucolimac.xuxubank.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.lucolimac.xuxubank.data.local.entity.DebtStatus
import br.com.lucolimac.xuxubank.domain.model.Debt
import br.com.lucolimac.xuxubank.domain.usecase.ManageDebtUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal

class DebtViewModel(
    private val manageDebtUseCase: ManageDebtUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val FILTER_STATUS_KEY = "debt_filter_status"
    }

    /**
     * Preserves filter selection across process death.
     */
    val filterStatus = savedStateHandle.getStateFlow<DebtStatus?>(FILTER_STATUS_KEY, null)

    /**
     * Combines multiple flows to produce a resilient and filtered UI State.
     */
    val allDebts: StateFlow<List<Debt>> = manageDebtUseCase.getAllDebts()
        .map { list ->
            val now = System.currentTimeMillis()
            list.map { debt ->
                if (debt.status == DebtStatus.PENDING && debt.dueDate != null && debt.dueDate < now) {
                    debt.copy(status = DebtStatus.OVERDUE)
                } else {
                    debt
                }
            }
        }
        .combine(filterStatus) { debts, filter ->
            if (filter == null) debts else debts.filter { it.status == filter }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateFilter(status: DebtStatus?) {
        savedStateHandle[FILTER_STATUS_KEY] = status
    }

    fun saveDebt(
        clientId: String,
        description: String,
        amount: BigDecimal,
        dueDate: Long?,
        installments: Int
    ) {
        viewModelScope.launch {
            manageDebtUseCase.createDebt(clientId, description, amount, dueDate, installments)
        }
    }

    fun updateDebt(debt: Debt) {
        viewModelScope.launch {
            manageDebtUseCase.updateDebt(debt)
        }
    }

    fun updateStatus(debtId: String, status: DebtStatus) {
        viewModelScope.launch {
            manageDebtUseCase.updateDebtStatus(debtId, status)
        }
    }

    fun deleteDebt(debtId: String) {
        viewModelScope.launch {
            manageDebtUseCase.deleteDebt(debtId)
        }
    }
}
