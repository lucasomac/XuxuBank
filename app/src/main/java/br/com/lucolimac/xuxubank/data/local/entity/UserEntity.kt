package br.com.lucolimac.xuxubank.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserRole {
    MANAGER, CLIENT
}

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String = "",
    val name: String = "",
    val role: UserRole = UserRole.CLIENT,
    val clientId: String? = null // Associated client ID, null if Manager
)
