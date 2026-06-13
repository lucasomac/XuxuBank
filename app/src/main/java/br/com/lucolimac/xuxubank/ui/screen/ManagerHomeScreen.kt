package br.com.lucolimac.xuxubank.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import br.com.lucolimac.xuxubank.R
import br.com.lucolimac.xuxubank.data.local.entity.ClientEntity
import br.com.lucolimac.xuxubank.data.local.entity.UserEntity
import br.com.lucolimac.xuxubank.ui.component.DebtItem
import br.com.lucolimac.xuxubank.ui.navigation.NavRoute
import br.com.lucolimac.xuxubank.ui.viewmodel.DebtViewModel
import br.com.lucolimac.xuxubank.ui.viewmodel.ClientViewModel

/**
 * Main entry point for the Manager's experience.
 * Features a bottom navigation to switch between Client management and the Monthly Overview.
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ManagerHomeScreen(
    user: UserEntity,
    onLogout: () -> Unit,
    clientViewModel: ClientViewModel,
    debtViewModel: DebtViewModel
) {
    var currentTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (currentTab == 0) stringResource(R.string.clients_management) else stringResource(R.string.monthly_overview)) },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text(stringResource(R.string.logout))
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentTab == 0,
                    onClick = { currentTab = 0 },
                    icon = { Icon(Icons.Default.Person, null) },
                    label = { Text(stringResource(R.string.clients)) }
                )
                NavigationBarItem(
                    selected = currentTab == 1,
                    onClick = { currentTab = 1 },
                    icon = { Icon(Icons.Default.DateRange, null) },
                    label = { Text(stringResource(R.string.monthly)) }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentTab) {
                0 -> ClientManagementContent(clientViewModel, debtViewModel)
                1 -> MonthlyOverviewScreen(debtViewModel)
            }
        }
    }
}

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
                ClientListScreen(
                    clients = clients,
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
                    clientName = client?.name ?: "Desconhecido",
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

@Composable
fun ClientDetailWithDebts(
    client: ClientEntity,
    debtViewModel: DebtViewModel,
    onAddDebt: () -> Unit,
    onEditClient: () -> Unit,
    onEditDebt: (Long) -> Unit
) {
    val allDebts by debtViewModel.allDebts.collectAsState()
    val clientDebts = allDebts.filter { it.clientId == client.id }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddDebt,
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text(stringResource(R.string.add_debt)) },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Header Section
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                shape = MaterialTheme.shapes.extraLarge,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = client.name,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Email,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = client.email,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        }
                        if (client.phone.isNotBlank()) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Phone,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = client.phone,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                    IconButton(
                        onClick = onEditClient,
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.edit_client))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Debt History Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.History,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.debt_history),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp) // Space for FAB
            ) {
                if (clientDebts.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            contentAlignment = androidx.compose.ui.Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.no_debts_found),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        }
                    }
                } else {
                    items(clientDebts) { debt ->
                        DebtItem(
                            debt = debt,
                            onUpdateStatus = { debtViewModel.updateStatus(debt.id, it) },
                            onDelete = { debtViewModel.deleteDebt(debt.id) },
                            onEdit = { onEditDebt(debt.id) }
                        )
                    }
                }
            }
        }
    }
}
