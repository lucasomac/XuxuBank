package br.com.lucolimac.xuxubank.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface NavRoute : NavKey {
    @Serializable
    data object Splash : NavRoute

    @Serializable
    data object Login : NavRoute

    @Serializable
    data object Home : NavRoute

    @Serializable
    data object MonthlyOverview : NavRoute

    @Serializable
    data object ClientList : NavRoute

    @Serializable
    data class ClientDetail(val id: String) : NavRoute

    @Serializable
    data class ClientForm(val id: String? = null) : NavRoute

    @Serializable
    data class DebtForm(val clientId: String, val debtId: String? = null) : NavRoute
}
