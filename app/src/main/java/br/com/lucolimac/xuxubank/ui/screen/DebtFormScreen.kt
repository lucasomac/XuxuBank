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
import br.com.lucolimac.xuxubank.domain.model.Debt
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

/**
 * Screen for creating or editing debt information following Mobile Design Standards.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtFormScreen(
    clientId: String,
    clientName: String,
    debt: Debt? = null,
    onSave: (String, String, BigDecimal, Long?, Int) -> Unit,
    onCancel: () -> Unit
) {
    var description by remember { mutableStateOf(debt?.description ?: "") }
    var amount by remember { mutableStateOf(debt?.amount?.toString() ?: "") }
    var installments by remember { mutableStateOf("1") }

    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = debt?.dueDate)
    var showDatePicker by remember { mutableStateOf(false) }

    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("pt-BR")) }
    val dateDisplay = datePickerState.selectedDateMillis?.let { dateFormatter.format(Date(it)) } ?: stringResource(R.string.no_date)
    val debtFormA11y = stringResource(R.string.debt_form_a11y)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .semantics { contentDescription = debtFormA11y }
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
            placeholder = { Text(stringResource(R.string.description_placeholder)) }
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { newValue ->
                // Basic validation: permit only digits and one dot or comma
                if (newValue.isEmpty() || newValue.matches(Regex("""^\d*([.,]\d{0,2})?$"""))) {
                    amount = newValue
                }
            },
            label = { Text(stringResource(R.string.amount)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            prefix = { Text(stringResource(R.string.currency_prefix, "")) },
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
                        Text(stringResource(R.string.confirm))
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis = null
                        showDatePicker = false
                    }) {
                        Text(stringResource(R.string.clear))
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
            
            val bigDecimalAmount = amount.replace(",", ".").toBigDecimalOrNull()
            
            Button(
                onClick = {
                    bigDecimalAmount?.let {
                        onSave(
                            clientId,
                            description,
                            it,
                            datePickerState.selectedDateMillis,
                            installments.toIntOrNull() ?: 1
                        )
                    }
                },
                modifier = Modifier.weight(1f).height(56.dp),
                enabled = description.isNotBlank() && bigDecimalAmount != null,
                shape = MaterialTheme.shapes.large
            ) {
                Text(if (debt == null) stringResource(R.string.save_debt) else stringResource(R.string.update_debt))
            }
        }
    }
}
