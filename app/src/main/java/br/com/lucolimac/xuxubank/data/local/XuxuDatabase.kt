package br.com.lucolimac.xuxubank.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.lucolimac.xuxubank.data.local.dao.DebtDao
import br.com.lucolimac.xuxubank.data.local.dao.ClientDao
import br.com.lucolimac.xuxubank.data.local.dao.UserDao
import br.com.lucolimac.xuxubank.data.local.entity.DebtEntity
import br.com.lucolimac.xuxubank.data.local.entity.ClientEntity
import br.com.lucolimac.xuxubank.data.local.entity.UserEntity

/**
 * Central database for the XuxuBank application.
 * Manages persistence for Users, Clients, and Debts.
 */
@Database(
    entities = [UserEntity::class, ClientEntity::class, DebtEntity::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class XuxuDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun clientDao(): ClientDao
    abstract fun debtDao(): DebtDao

    companion object {
        const val DATABASE_NAME = "xuxu_bank_db"
    }
}
