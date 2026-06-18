package br.com.lucolimac.xuxubank.domain.model

import br.com.lucolimac.xuxubank.data.local.entity.UserRole

data class User(
    val id: String,
    val name: String,
    val role: UserRole,
    val clientId: String?
)
