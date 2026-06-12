package br.com.lucolimac.xuxubank.data.local

import br.com.lucolimac.xuxubank.data.local.entity.DebtEntity
import br.com.lucolimac.xuxubank.data.local.entity.ClientEntity
import br.com.lucolimac.xuxubank.data.local.entity.UserEntity
import br.com.lucolimac.xuxubank.domain.model.Debt
import br.com.lucolimac.xuxubank.domain.model.Client
import br.com.lucolimac.xuxubank.domain.model.User

fun UserEntity.toDomain() = User(id, name, role, clientId)
fun User.toEntity() = UserEntity(id, name, role, clientId)

fun ClientEntity.toDomain() = Client(id, name, email, phone)
fun Client.toEntity() = ClientEntity(id, name, email, phone)

fun DebtEntity.toDomain() = Debt(
    id, clientId, description, amount, dueDate, status, totalInstallments, currentInstallment, createdAt
)
fun Debt.toEntity() = DebtEntity(
    id, clientId, description, amount, dueDate, status, totalInstallments, currentInstallment, createdAt
)
