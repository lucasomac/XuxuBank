package br.com.lucolimac.xuxubank.domain.model

import br.com.lucolimac.xuxubank.data.local.entity.DebtStatus
import java.math.BigDecimal

data class Debt(
    val id: String,
    val clientId: String,
    val description: String,
    val amount: BigDecimal,
    val dueDate: Long?,
    val status: DebtStatus,
    val totalInstallments: Int,
    val currentInstallment: Int,
    val createdAt: Long
)
