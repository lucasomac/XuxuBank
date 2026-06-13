package br.com.lucolimac.xuxubank.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import br.com.lucolimac.xuxubank.R
import br.com.lucolimac.xuxubank.data.local.entity.DebtEntity
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.platform.LocalLocale

/**
 * Screen for creating or editing debt information following Mobile Design Standards.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtFormScreen(
    clientId: Long,
    clientName: String,
    debt: DebtEntity? = null,
    onSave: (Long, String, Double, Long?, Int) -> Unit,
    onCancel: () -> Unit
) {
    var description by remember { mutableStateOf(debt?.description ?: "") }
    var amount by remember { mutableStateOf(debt?.amount?.toString() ?: "") }
    var installments by remember { mutableStateOf("1") }

    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = debt?.dueDate ?: System.currentTimeMillis())
    var showDatePicker by remember { mutableStateOf(false) }

    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", LocalLocale.current.platformLocale)
    val dateDisplay = datePickerState.selectedDateMillis?.let { dateFormatter.format(Date(it)) } ?: stringResource(R.string.no_date)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .semantics { contentDescription = "Formulário de lançamento de dívida" }
    ) {
        Text(
            text = if (debt == null) stringResource(R.string.new_debt_for, clientName) else stringResource(R.string.edit_debt_for, clientName),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(stringResource(R.string.description)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text("Ex: Compra de materiais") }
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { newValue ->
                // Basic validation: permit only digits and one dot
                if (newValue.isEmpty() || newValue.matches(Regex("""^\d*\.?\d*$"""))) {
                    amount = newValue
                }
            },
            label = { Text(stringResource(R.string.amount)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            prefix = { Text("R$ ") },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(12.dp))

        if (debt == null) {
            OutlinedTextField(
                value = installments,
                onValueChange = { installments = it.filter { char -> char.isDigit() } },
                label = { Text(stringResource(R.string.installments)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        OutlinedButton(
            onClick = { showDatePicker = true },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(text = stringResource(R.string.due_date, dateDisplay))
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Confirmar")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f).height(56.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Text(stringResource(R.string.cancel))
            }
            
            Button(
                onClick = {
                    onSave(
                        clientId,
                        description,
                        amount.toDoubleOrNull() ?: 0.0,
                        datePickerState.selectedDateMillis,
                        installments.toIntOrNull() ?: 1
                    )
                },
                modifier = Modifier.weight(1f).height(56.dp),
                enabled = description.isNotBlank() && amount.toDoubleOrNull() != null,
                shape = MaterialTheme.shapes.large
            ) {
                Text(if (debt == null) stringResource(R.string.save_debt) else stringResource(R.string.update_debt))
            }
        }
    }
}
