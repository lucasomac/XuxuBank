package br.com.lucolimac.xuxubank.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import br.com.lucolimac.xuxubank.R
import br.com.lucolimac.xuxubank.data.local.entity.DebtEntity
import java.text.SimpleDateFormat
import java.util.*

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

    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val dateDisplay = datePickerState.selectedDateMillis?.let { dateFormatter.format(Date(it)) } ?: stringResource(R.string.no_date)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = if (debt == null) stringResource(R.string.new_debt_for, clientName) else stringResource(R.string.edit_debt_for, clientName),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(stringResource(R.string.description)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text(stringResource(R.string.amount)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (debt == null) {
            OutlinedTextField(
                value = installments,
                onValueChange = { installments = it },
                label = { Text(stringResource(R.string.installments)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        OutlinedButton(
            onClick = { showDatePicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.due_date, dateDisplay))
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("OK")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onCancel) {
                Text(stringResource(R.string.cancel))
            }
            Spacer(modifier = Modifier.width(8.dp))
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
                enabled = description.isNotBlank() && amount.toDoubleOrNull() != null
            ) {
                Text(if (debt == null) stringResource(R.string.save_debt) else stringResource(R.string.update_debt))
            }
        }
    }
}
