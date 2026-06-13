package br.com.lucolimac.xuxubank.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.lucolimac.xuxubank.R
import br.com.lucolimac.xuxubank.data.local.entity.DebtStatus
import br.com.lucolimac.xuxubank.data.local.entity.UserEntity
import br.com.lucolimac.xuxubank.ui.component.SummaryCard
import br.com.lucolimac.xuxubank.ui.component.debt.DebtItem
import br.com.lucolimac.xuxubank.ui.viewmodel.DebtViewModel

/**
 * Screen for standard Clients to view their own debts.
 * Restricts actions (cannot delete or add debts) as per business rules.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserHomeScreen(
    user: UserEntity,
    onLogout: () -> Unit,
    debtViewModel: DebtViewModel
) {
    val allDebts by debtViewModel.allDebts.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    
    // Role-based filtering: only show debts belonging to this user's linked client profile.
    val userDebts = allDebts.filter { it.clientId == user.clientId }
    val totalPending = userDebts.filter { it.status != DebtStatus.PAID }.sumOf { it.amount }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text(stringResource(R.string.logout))
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(R.string.hello_user, user.name),
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.client_info),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (user.clientId == null) {
                Text(
                    text = stringResource(R.string.no_linked_client),
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                SummaryCard(
                    title = stringResource(R.string.total_pending),
                    amount = totalPending,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
                
                Spacer(modifier = Modifier.height(16.dp))

                if (userDebts.isEmpty()) {
                    Text(text = stringResource(R.string.no_debts_recorded))
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(userDebts) { debt ->
                            DebtItem(debt = debt, onUpdateStatus = {})
                        }
                    }
                }
            }
        }
    }
}
