package br.com.lucolimac.xuxubank.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import br.com.lucolimac.xuxubank.R
import br.com.lucolimac.xuxubank.domain.model.Client
import br.com.lucolimac.xuxubank.ui.util.PhoneVisualTransformation
import br.com.lucolimac.xuxubank.ui.util.ValidationUtils

/**
 * Screen for creating or editing client information following Mobile Design Standards.
 * Features mandatory fields, real-time validation feedback, and optimized touch targets.
 */
@Composable
fun ClientFormScreen(
    client: Client? = null,
    onSave: (String, String, String) -> Unit,
    onCancel: () -> Unit,
    onDelete: (() -> Unit)? = null
) {
    var name by remember { mutableStateOf(client?.name ?: "") }
    var email by remember { mutableStateOf(client?.email ?: "") }
    var phone by remember { mutableStateOf(client?.phone ?: "") }

    val isEmailValid = ValidationUtils.isValidEmail(email)
    val isPhoneValid = ValidationUtils.isValidPhone(phone)
    val isNameValid = ValidationUtils.isValidName(name)
    
    val clientFormA11y = stringResource(R.string.client_form_a11y)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .semantics { contentDescription = clientFormA11y }
    ) {
        Text(
            text = if (client == null) stringResource(R.string.client_registration) else stringResource(R.string.edit_client),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(stringResource(R.string.name)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = name.isNotBlank() && !isNameValid,
            supportingText = {
                if (name.isNotBlank() && !isNameValid) {
                    Text(stringResource(R.string.invalid_name))
                }
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.email)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = email.isNotBlank() && !isEmailValid,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            supportingText = {
                if (email.isNotBlank() && !isEmailValid) {
                    Text(stringResource(R.string.invalid_email))
                }
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text(stringResource(R.string.phone)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = phone.isNotBlank() && !isPhoneValid,
            visualTransformation = PhoneVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            supportingText = {
                if (phone.isNotBlank() && !isPhoneValid) {
                    Text(stringResource(R.string.invalid_phone))
                }
            }
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (client != null && onDelete != null) {
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
            
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f).height(56.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Text(stringResource(R.string.cancel))
            }
            
            Button(
                onClick = { onSave(name, email, phone) },
                modifier = Modifier.weight(1f).height(56.dp),
                enabled = isNameValid && isEmailValid && isPhoneValid,
                shape = MaterialTheme.shapes.large
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }
}
