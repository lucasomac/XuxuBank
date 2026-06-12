package br.com.lucolimac.xuxubank.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.lucolimac.xuxubank.R
import br.com.lucolimac.xuxubank.data.local.entity.DebtStatus
import br.com.lucolimac.xuxubank.ui.component.DebtItem
import br.com.lucolimac.xuxubank.ui.component.SummaryCard
import br.com.lucolimac.xuxubank.ui.viewmodel.DebtViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MonthlyOverviewScreen(
    debtViewModel: DebtViewModel
) {
    val debts by debtViewModel.allDebts.collectAsState()
    var filterStatus by remember { mutableStateOf<DebtStatus?>(null) }
    var showFilterMenu by remember { mutableStateOf(false) }

    val filteredDebts = if (filterStatus == null) debts else debts.filter { it.status == filterStatus }
    val totalReceivable = filteredDebts.sumOf { it.amount }

    val sdf = SimpleDateFormat("MMMM yyyy", Locale("pt", "BR"))

    val groupedDebts = filteredDebts.sortedBy { it.dueDate ?: 0L }.groupBy {
        val cal = Calendar.getInstance()
        cal.timeInMillis = it.dueDate ?: 0L
        if (it.dueDate == null) {
            0L
        } else {
            cal.set(Calendar.DAY_OF_MONTH, 1)
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            cal.timeInMillis
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val statusDisplayName = when(filterStatus) {
                DebtStatus.PAID -> stringResource(R.string.status_paid)
                DebtStatus.PENDING -> stringResource(R.string.status_pending)
                DebtStatus.OVERDUE -> stringResource(R.string.status_overdue)
                null -> stringResource(R.string.all)
            }
            Text(
                text = stringResource(R.string.filter_label, statusDisplayName),
                style = MaterialTheme.typography.bodyMedium
            )
            Box {
                IconButton(onClick = { showFilterMenu = true }) {
                    Icon(Icons.Default.FilterList, contentDescription = stringResource(R.string.all))
                }
                DropdownMenu(
                    expanded = showFilterMenu,
                    onDismissRequest = { showFilterMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.all)) },
                        onClick = { filterStatus = null; showFilterMenu = false }
                    )
                    DebtStatus.entries.forEach { status ->
                        val itemDisplayName = when(status) {
                            DebtStatus.PAID -> stringResource(R.string.status_paid)
                            DebtStatus.PENDING -> stringResource(R.string.status_pending)
                            DebtStatus.OVERDUE -> stringResource(R.string.status_overdue)
                        }
                        DropdownMenuItem(
                            text = { Text(itemDisplayName) },
                            onClick = { filterStatus = status; showFilterMenu = false }
                        )
                    }
                }
            }
        }

        SummaryCard(
            title = stringResource(R.string.total_receivable),
            amount = totalReceivable,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (filteredDebts.isEmpty()) {
                item {
                    Text(text = stringResource(R.string.no_debts_recorded), style = MaterialTheme.typography.bodyLarge)
                }
            }

            groupedDebts.forEach { (monthMillis, monthDebts) ->
                item {
                    val header = if (monthMillis == 0L) stringResource(R.string.no_due_date_header) else sdf.format(Date(monthMillis))
                    Text(
                        text = header.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
                items(monthDebts) { debt ->
                    DebtItem(
                        debt = debt,
                        onUpdateStatus = { debtViewModel.updateStatus(debt.id, it) },
                        onDelete = { debtViewModel.deleteDebt(debt.id) }
                    )
                }
            }
        }
    }
}
