package br.com.lucolimac.xuxubank.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.lucolimac.xuxubank.R
import br.com.lucolimac.xuxubank.data.local.entity.DebtEntity
import br.com.lucolimac.xuxubank.data.local.entity.DebtStatus
import br.com.lucolimac.xuxubank.ui.theme.SurfaceContainerLowest
import java.text.SimpleDateFormat
import java.util.*

/**
 * Debt item component following the "Tonal Layering" and "No-Line" rules.
 */
@Composable
fun DebtItem(
    debt: DebtEntity,
    onUpdateStatus: (DebtStatus) -> Unit,
    onDelete: (() -> Unit)? = null,
    onEdit: (() -> Unit)? = null
) {
    // Surface Container Lowest provides a subtle "lift" against the beige background
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = SurfaceContainerLowest
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // Tonal depth over shadows
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = debt.description,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val dateText = debt.dueDate?.let {
                        SimpleDateFormat("dd 'de' MMMM, yyyy", Locale.forLanguageTag("pt-BR")).format(Date(it))
                    } ?: stringResource(R.string.no_date)
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.due_date_label, dateText),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
                StatusChip(status = debt.status)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.amount),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = "R$ ${String.format(Locale.forLanguageTag("pt-BR"), "%,.2f", debt.amount)}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (onEdit != null) {
                        IconButton(onClick = onEdit) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Debt", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                    if (onDelete != null) {
                        IconButton(onClick = onDelete) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Debt", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                    
                    val isPaid = debt.status == DebtStatus.PAID
                    Button(
                        onClick = { onUpdateStatus(if (isPaid) DebtStatus.PENDING else DebtStatus.PAID) },
                        shape = MaterialTheme.shapes.medium,
                        colors = if (isPaid) {
                            ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        } else {
                            ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    ) {
                        Text(if (isPaid) stringResource(R.string.mark_as_pending) else stringResource(R.string.mark_as_paid))
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
        color = color.copy(alpha = 0.1f),
        contentColor = color,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = statusText.uppercase(),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp
        )
    }
}
