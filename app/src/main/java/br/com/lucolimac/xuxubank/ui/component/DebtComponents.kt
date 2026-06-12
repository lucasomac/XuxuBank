package br.com.lucolimac.xuxubank.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.lucolimac.xuxubank.R
import br.com.lucolimac.xuxubank.data.local.entity.DebtEntity
import br.com.lucolimac.xuxubank.data.local.entity.DebtStatus
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DebtItem(
    debt: DebtEntity,
    onUpdateStatus: (DebtStatus) -> Unit,
    onDelete: (() -> Unit)? = null,
    onEdit: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = debt.description, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                    val dateText = debt.dueDate?.let { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(it)) } ?: stringResource(R.string.no_date)
                    Text(text = stringResource(R.string.due_date_label, dateText), style = MaterialTheme.typography.labelSmall)
                }
                Row {
                    if (onEdit != null) {
                        IconButton(onClick = onEdit) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Debt", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                    StatusChip(status = debt.status)
                }
            }
            Text(text = "R$ ${String.format(Locale.US, "%.2f", debt.amount)}", style = MaterialTheme.typography.bodyMedium)
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (onDelete != null) {
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Debt", tint = MaterialTheme.colorScheme.error)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
                if (debt.status != DebtStatus.PAID) {
                    TextButton(onClick = { onUpdateStatus(DebtStatus.PAID) }) {
                        Text(stringResource(R.string.mark_as_paid))
                    }
                } else {
                    TextButton(onClick = { onUpdateStatus(DebtStatus.PENDING) }) {
                        Text(stringResource(R.string.mark_as_pending))
                    }
                }
            }
        }
    }
}

@Composable
fun StatusChip(status: DebtStatus) {
    val color = when (status) {
        DebtStatus.PAID -> MaterialTheme.colorScheme.tertiary
        DebtStatus.PENDING -> MaterialTheme.colorScheme.secondary
        DebtStatus.OVERDUE -> MaterialTheme.colorScheme.error
    }
    val statusText = when (status) {
        DebtStatus.PAID -> stringResource(R.string.status_paid)
        DebtStatus.PENDING -> stringResource(R.string.status_pending)
        DebtStatus.OVERDUE -> stringResource(R.string.status_overdue)
    }
    Surface(
        color = color.copy(alpha = 0.2f),
        contentColor = color,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = statusText,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}
