package br.com.lucolimac.xuxubank.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.math.BigDecimal

enum class DebtStatus {
    PENDING, OVERDUE, PAID
}

@Entity(
    tableName = "debts",
    foreignKeys = [
        ForeignKey(
            entity = ClientEntity::class,
            parentColumns = ["id"],
            childColumns = ["clientId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("clientId")]
)
data class DebtEntity(
    @PrimaryKey val id: String = "",
    val clientId: String = "",
    val description: String = "",
    val amount: BigDecimal = BigDecimal.ZERO,
    val dueDate: Long? = null,
    val status: DebtStatus = DebtStatus.PENDING,
    val totalInstallments: Int = 1,
    val currentInstallment: Int = 1,
    val createdAt: Long = System.currentTimeMillis()
)
