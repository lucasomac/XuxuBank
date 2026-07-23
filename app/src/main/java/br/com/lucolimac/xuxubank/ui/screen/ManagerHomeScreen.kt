package br.com.lucolimac.xuxubank.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import br.com.lucolimac.xuxubank.R
import br.com.lucolimac.xuxubank.domain.model.User
import br.com.lucolimac.xuxubank.ui.screen.manager.ClientManagementContent
import br.com.lucolimac.xuxubank.ui.viewmodel.ClientViewModel
import br.com.lucolimac.xuxubank.ui.viewmodel.DebtViewModel

/**
 * Main entry point for the Manager's experience.
 * Features a bottom navigation to switch between Client management and the Monthly Overview.
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ManagerHomeScreen(
    user: User,
    onLogout: () -> Unit,
    clientViewModel: ClientViewModel,
    debtViewModel: DebtViewModel
) {
    var currentTab by remember { mutableIntStateOf(0) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(if (currentTab == 0) stringResource(R.string.clients_management) else stringResource(R.string.monthly_overview)) },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text(stringResource(R.string.logout))
                    }
                },
                scrollBehavior = scrollBehavior
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
