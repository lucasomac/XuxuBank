package br.com.lucolimac.xuxubank.ui.component.debt

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.lucolimac.xuxubank.R
import br.com.lucolimac.xuxubank.data.local.entity.DebtStatus

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
