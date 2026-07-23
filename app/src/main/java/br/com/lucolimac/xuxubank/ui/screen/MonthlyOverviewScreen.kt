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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.lucolimac.xuxubank.R
import br.com.lucolimac.xuxubank.data.local.entity.DebtStatus
import br.com.lucolimac.xuxubank.ui.component.SummaryCard
import br.com.lucolimac.xuxubank.ui.component.debt.DebtItem
import br.com.lucolimac.xuxubank.ui.util.FormatUtils
import br.com.lucolimac.xuxubank.ui.viewmodel.DebtViewModel
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

/**
 * Monthly overview screen with a11y support and refined spacing.
 */
@Composable
fun MonthlyOverviewScreen(
    debtViewModel: DebtViewModel
) {
    val debts by debtViewModel.allDebts.collectAsState()
    val filterStatus by debtViewModel.filterStatus.collectAsState()
    var showFilterMenu by remember { mutableStateOf(false) }

    // Logic moved to ViewModel for better resilience
    val filteredDebts = debts
    
    // New calculations for the requested summaries using BigDecimal
    val overallTotalReceivable = remember(debts) {
        debts.filter { it.status != DebtStatus.PAID }
            .fold(BigDecimal.ZERO) { acc, debt -> acc + debt.amount }
    }

    val currentMonthMillis = remember {
        Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    val currentMonthDebts = remember(debts, currentMonthMillis) {
        debts.filter {
            it.dueDate != null && it.dueDate >= currentMonthMillis && it.dueDate < Calendar.getInstance().apply {
                timeInMillis = currentMonthMillis
                add(Calendar.MONTH, 1)
            }.timeInMillis
        }
    }

    val currentMonthReceivable = currentMonthDebts.filter { it.status != DebtStatus.PAID }
        .fold(BigDecimal.ZERO) { acc, debt -> acc + debt.amount }
        
    val currentMonthPaid = currentMonthDebts.filter { it.status == DebtStatus.PAID }
        .fold(BigDecimal.ZERO) { acc, debt -> acc + debt.amount }

    val sdf = remember { SimpleDateFormat("MMMM yyyy", Locale.forLanguageTag("pt-BR")) }
    val monthlyOverviewA11y = stringResource(R.string.monthly_overview_a11y)

    val groupedDebts = remember(filteredDebts) {
        filteredDebts.sortedBy { it.dueDate ?: 0L }.groupBy {
            if (it.dueDate == null) {
                0L
            } else {
                val cal = Calendar.getInstance().apply {
                    timeInMillis = it.dueDate
                    set(Calendar.DAY_OF_MONTH, 1)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                cal.timeInMillis
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .semantics { contentDescription = monthlyOverviewA11y }
    ) {
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
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Box {
                IconButton(
                    onClick = { showFilterMenu = true },
                    modifier = Modifier.size(48.dp) // Minimum touch target
                ) {
                    Icon(Icons.Default.FilterList, contentDescription = stringResource(R.string.filter_by_status))
                }
                DropdownMenu(
                    expanded = showFilterMenu,
                    onDismissRequest = { showFilterMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.all)) },
                        onClick = { debtViewModel.updateFilter(null); showFilterMenu = false }
                    )
                    DebtStatus.entries.forEach { status ->
                        val itemDisplayName = when(status) {
                            DebtStatus.PAID -> stringResource(R.string.status_paid)
                            DebtStatus.PENDING -> stringResource(R.string.status_pending)
                            DebtStatus.OVERDUE -> stringResource(R.string.status_overdue)
                        }
                        DropdownMenuItem(
                            text = { Text(itemDisplayName) },
                            onClick = { debtViewModel.updateFilter(status); showFilterMenu = false }
                        )
                    }
                }
            }
        }

        SummaryCard(
            title = stringResource(R.string.total_receivable),
            amount = overallTotalReceivable,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Current Month Details
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.current_month_summary),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(stringResource(R.string.to_receive), style = MaterialTheme.typography.labelSmall)
                        Text(
                            text = FormatUtils.formatMonetary(currentMonthReceivable),
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
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (filteredDebts.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.no_debts_recorded),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 32.dp)
                    )
                }
            }

            groupedDebts.forEach { (monthMillis, monthDebts) ->
                item(key = "header_$monthMillis") {
                    val header = if (monthMillis == 0L) stringResource(R.string.no_due_date_header) else sdf.format(Date(monthMillis))
                    Text(
                        text = header.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(vertical = 8.dp).semantics { heading() }
                    )
                }
                items(
                    items = monthDebts,
                    key = { it.id } // Essential for identifying updates
                ) { debt ->
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
