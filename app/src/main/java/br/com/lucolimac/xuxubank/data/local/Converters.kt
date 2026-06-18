package br.com.lucolimac.xuxubank.data.local

import androidx.room.TypeConverter
import br.com.lucolimac.xuxubank.data.local.entity.DebtStatus
import br.com.lucolimac.xuxubank.data.local.entity.UserRole
import java.math.BigDecimal

class Converters {
    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): String? = value?.toString()

    @TypeConverter
    fun toBigDecimal(value: String?): BigDecimal? = value?.let { BigDecimal(it) }

    @TypeConverter
    fun fromUserRole(value: UserRole): String = value.name

    @TypeConverter
    fun toUserRole(value: String): UserRole = UserRole.valueOf(value)

    @TypeConverter
    fun fromDebtStatus(value: DebtStatus): String = value.name

    @TypeConverter
    fun toDebtStatus(value: String): DebtStatus = DebtStatus.valueOf(value)
}
