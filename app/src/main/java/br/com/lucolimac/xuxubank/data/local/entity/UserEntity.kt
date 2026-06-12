package br.com.lucolimac.xuxubank.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserRole {
    MANAGER, CLIENT
}

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val role: UserRole,
    val clientId: Long? = null // Associated client ID, null if Manager
)
