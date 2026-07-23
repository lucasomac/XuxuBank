package br.com.lucolimac.xuxubank.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.lucolimac.xuxubank.R
import br.com.lucolimac.xuxubank.data.local.entity.DebtStatus
import br.com.lucolimac.xuxubank.domain.model.User
import br.com.lucolimac.xuxubank.ui.component.debt.DebtItem
import br.com.lucolimac.xuxubank.ui.util.FormatUtils
import br.com.lucolimac.xuxubank.ui.viewmodel.DebtViewModel
import java.math.BigDecimal
import java.util.*

/**
 * Screen for standard Clients to view their own debts.
 * Enhanced with a detailed monthly summary for better financial awareness.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserHomeScreen(
    user: User, onLogout: () -> Unit, debtViewModel: DebtViewModel
) {
    val allDebts by debtViewModel.allDebts.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    // Role-based filtering: only show debts belonging to this user's linked client profile.
    val userDebts = remember(allDebts, user.clientId) {
        allDebts.filter { it.clientId == user.clientId }
    }

    // Calculations for the requested summaries (Current Month)
    val currentMonthMillis = remember {
        Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    val nextMonthMillis = remember(currentMonthMillis) {
        Calendar.getInstance().apply {
            timeInMillis = currentMonthMillis
            add(Calendar.MONTH, 1)
        }.timeInMillis
    }

    val currentMonthDebts = remember(userDebts, currentMonthMillis, nextMonthMillis) {
        userDebts.filter {
            it.dueDate != null && it.dueDate >= currentMonthMillis && it.dueDate < nextMonthMillis
        }
    }

    val currentMonthToPay = currentMonthDebts.filter { it.status == DebtStatus.PENDING }
        .fold(BigDecimal.ZERO) { acc, debt -> acc + debt.amount }
        
    val currentMonthPaid = currentMonthDebts.filter { it.status == DebtStatus.PAID }
        .fold(BigDecimal.ZERO) { acc, debt -> acc + debt.amount }
        
    val totalOverdue = userDebts.filter { it.status == DebtStatus.OVERDUE }
        .fold(BigDecimal.ZERO) { acc, debt -> acc + debt.amount }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) }, actions = {
                TextButton(onClick = onLogout) {
                    Text(stringResource(R.string.logout))
                }
            }, scrollBehavior = scrollBehavior
            )
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text(
                    text = stringResource(R.string.hello_user, user.name),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.client_info),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (user.clientId == null) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(R.string.no_linked_client),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            } else {
                // Detailed Monthly Summary Card for Client
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = stringResource(R.string.financial_summary),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(stringResource(R.string.to_pay), style = MaterialTheme.typography.labelSmall)
                                Text(
                                    text = FormatUtils.formatMonetary(currentMonthToPay),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(stringResource(R.string.already_paid), style = MaterialTheme.typography.labelSmall)
                                Text(
                                    text = FormatUtils.formatMonetary(currentMonthPaid),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Overdue Highlight
                        Surface(
                            color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f),
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    stringResource(R.string.overdue_label),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                                Text(
                                    text = FormatUtils.formatMonetary(totalOverdue),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }

                Text(
                    text = stringResource(R.string.debt_history_label),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                if (userDebts.isEmpty()) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(32.dp), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_debts_recorded),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(userDebts, key = { it.id }) { debt ->
                            DebtItem(debt = debt, onUpdateStatus = {})
                        }
                    }
                }
            }
        }
    }
}
