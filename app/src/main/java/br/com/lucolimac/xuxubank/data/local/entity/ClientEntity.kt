package br.com.lucolimac.xuxubank.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clients")
data class ClientEntity(
    @PrimaryKey val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = ""
)
