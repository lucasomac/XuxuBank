package br.com.lucolimac.xuxubank.ui.screen.manager

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import br.com.lucolimac.xuxubank.R
import br.com.lucolimac.xuxubank.ui.navigation.NavRoute
import br.com.lucolimac.xuxubank.ui.screen.ClientFormScreen
import br.com.lucolimac.xuxubank.ui.screen.ClientListScreen
import br.com.lucolimac.xuxubank.ui.screen.DebtFormScreen
import br.com.lucolimac.xuxubank.ui.viewmodel.ClientViewModel
import br.com.lucolimac.xuxubank.ui.viewmodel.DebtViewModel

/**
 * Implements the List-Detail pattern for managing clients.
 * Uses Compose Material Adaptive to automatically switch between single-pane (phone) 
 * and multi-pane (tablet) layouts based on window size.
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ClientManagementContent(
    clientViewModel: ClientViewModel,
    debtViewModel: DebtViewModel
) {
    val backStack = rememberNavBackStack(NavRoute.ClientList)
    val clients by clientViewModel.allClients.collectAsState()
    
    // Adaptive logic: calculates how many panes to show
    val adaptiveInfo = currentWindowAdaptiveInfoV2()
    val directive = remember(adaptiveInfo) {
        calculatePaneScaffoldDirective(adaptiveInfo)
    }
    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>(directive = directive)

    NavDisplay(
        backStack = backStack,
        onBack = {
            if (backStack.size > 1) {
                backStack.removeLastOrNull()
            }
        },
        sceneStrategies = listOf(listDetailStrategy),
        entryProvider = entryProvider {
            // The List Pane
            entry<NavRoute.ClientList>(
                metadata = ListDetailSceneStrategy.listPane(
                    detailPlaceholder = {
                        Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                            Text(stringResource(R.string.select_client_instruction))
                        }
                    }
                )
            ) {
                val searchQuery by clientViewModel.searchQuery.collectAsState()
                ClientListScreen(
                    clients = clients,
                    searchQuery = searchQuery,
                    onSearchQueryChange = { clientViewModel.updateSearchQuery(it) },
                    onClientClick = { client ->
                        backStack.add(NavRoute.ClientDetail(client.id))
                    },
                    onAddClient = {
                        backStack.add(NavRoute.ClientForm())
                    }
                )
            }
            // The Detail Pane
            entry<NavRoute.ClientDetail>(
                metadata = ListDetailSceneStrategy.detailPane()
            ) { key ->
                val client = clients.find { it.id == key.id }
                if (client != null) {
                    ClientDetailWithDebts(
                        client = client,
                        debtViewModel = debtViewModel,
                        onAddDebt = {
                            backStack.add(NavRoute.DebtForm(client.id))
                        },
                        onEditClient = {
                            backStack.add(NavRoute.ClientForm(client.id))
                        },
                        onEditDebt = { debtId ->
                            backStack.add(NavRoute.DebtForm(client.id, debtId))
                        }
                    )
                }
            }
            // Form Pane (overlay or side-by-side depending on strategy)
            entry<NavRoute.ClientForm>(
                metadata = ListDetailSceneStrategy.detailPane()
            ) { key ->
                val existingClient = if (key.id != null) clients.find { it.id == key.id } else null
                ClientFormScreen(
                    client = existingClient,
                    onSave = { name, email, phone ->
                        if (existingClient != null) {
                            clientViewModel.updateClient(existingClient.copy(name = name, email = email, phone = phone))
                        } else {
                            clientViewModel.saveClient(name, email, phone)
                        }
                        backStack.removeLastOrNull()
                    },
                    onCancel = { backStack.removeLastOrNull() },
                    onDelete = if (existingClient != null) {
                        {
                            clientViewModel.deleteClient(existingClient.id)
                            backStack.removeLastOrNull()
                        }
                    } else null
                )
            }
            entry<NavRoute.DebtForm>(
                metadata = ListDetailSceneStrategy.detailPane()
            ) { key ->
                val client = clients.find { it.id == key.clientId }
                val allDebts by debtViewModel.allDebts.collectAsState()
                val existingDebt = if (key.debtId != null) allDebts.find { it.id == key.debtId } else null
                DebtFormScreen(
                    clientId = key.clientId,
                    clientName = client?.name ?: stringResource(R.string.unknown),
                    debt = existingDebt,
                    onSave = { id, desc, amt, date, inst ->
                        if (existingDebt != null) {
                            debtViewModel.updateDebt(existingDebt.copy(description = desc, amount = amt, dueDate = date))
                        } else {
                            debtViewModel.saveDebt(id, desc, amt, date, inst)
                        }
                        backStack.removeLastOrNull()
                    },
                    onCancel = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}
