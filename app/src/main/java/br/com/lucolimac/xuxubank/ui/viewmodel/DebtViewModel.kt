package br.com.lucolimac.xuxubank.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.lucolimac.xuxubank.data.local.entity.DebtEntity
import br.com.lucolimac.xuxubank.data.local.entity.DebtStatus
import br.com.lucolimac.xuxubank.domain.usecase.ManageDebtUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal

class DebtViewModel(private val manageDebtUseCase: ManageDebtUseCase) : ViewModel() {
    val allDebts: StateFlow<List<DebtEntity>> = manageDebtUseCase.getAllDebts()
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
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun saveDebt(
        clientId: Long,
        description: String,
        amount: BigDecimal,
        dueDate: Long?,
        installments: Int
    ) {
        viewModelScope.launch {
            manageDebtUseCase.createDebt(clientId, description, amount, dueDate, installments)
        }
    }

    fun updateDebt(debt: DebtEntity) {
        viewModelScope.launch {
            manageDebtUseCase.updateDebt(debt)
        }
    }

    fun updateStatus(debtId: Long, status: DebtStatus) {
        viewModelScope.launch {
            manageDebtUseCase.updateDebtStatus(debtId, status)
        }
    }

    fun deleteDebt(debtId: Long) {
        viewModelScope.launch {
            manageDebtUseCase.deleteDebt(debtId)
        }
    }
}
