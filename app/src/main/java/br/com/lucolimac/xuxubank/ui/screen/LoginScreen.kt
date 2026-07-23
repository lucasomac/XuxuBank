package br.com.lucolimac.xuxubank.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.lucolimac.xuxubank.R
import br.com.lucolimac.xuxubank.ui.theme.XuxuBankTheme
import br.com.lucolimac.xuxubank.ui.util.PhoneVisualTransformation
import br.com.lucolimac.xuxubank.ui.util.ValidationUtils
import br.com.lucolimac.xuxubank.ui.viewmodel.LoginUIState

/**
 * Unified Login Screen following Mobile App Design Standards.
 * Features strict validation, accessibility labels, and adaptive touch targets.
 */
@Composable
fun LoginScreen(
    loginState: LoginUIState,
    onLogin: (String) -> Unit,
    onResetError: () -> Unit
) {
    var identifier by remember { mutableStateOf("") }
    val isEmail = identifier.contains("@")
    val containsLetters = identifier.any { it.isLetter() }
    
    // Only apply phone mask if the input looks like a phone (all digits and no @)
    val shouldApplyPhoneMask = !isEmail && !containsLetters && identifier.isNotEmpty()
    
    val isValid = if (isEmail) ValidationUtils.isValidEmail(identifier) else ValidationUtils.isValidPhone(identifier)
    
    val loginScreenA11y = stringResource(R.string.login_screen_a11y)
    val clientAccessInputA11y = stringResource(R.string.client_access_input_a11y)
    val loginButtonA11y = stringResource(R.string.login_button_a11y)
    val loadingA11y = stringResource(R.string.loading_a11y)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .semantics { contentDescription = loginScreenA11y },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.welcome_message),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.login_instruction),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(56.dp))
        
        OutlinedTextField(
            value = identifier,
            onValueChange = { 
                identifier = it
                if (loginState is LoginUIState.Error) onResetError()
            },
            label = { Text(stringResource(R.string.email_or_phone)) },
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = clientAccessInputA11y },
            singleLine = true,
            isError = loginState is LoginUIState.Error,
            supportingText = {
                if (loginState is LoginUIState.Error) {
                    Text(text = stringResource(loginState.messageRes), color = MaterialTheme.colorScheme.error)
                } else {
                    Text(text = stringResource(R.string.phone_mask_hint))
                }
            },
            visualTransformation = if (shouldApplyPhoneMask) PhoneVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (containsLetters || isEmail) KeyboardType.Email else KeyboardType.Number
            ),
            shape = MaterialTheme.shapes.medium
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = { onLogin(identifier) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp) // Enhanced touch target height
                .semantics { contentDescription = loginButtonA11y },
            enabled = isValid && loginState !is LoginUIState.Loading,
            shape = MaterialTheme.shapes.large
        ) {
            if (loginState is LoginUIState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp).semantics { contentDescription = loadingA11y },
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(stringResource(R.string.login), style = MaterialTheme.typography.titleMedium)
            }
        }

        // Demo Helper for Manager
        Spacer(modifier = Modifier.height(40.dp))
        TextButton(
            onClick = { onLogin("xuxu@xuxubank.com.br") },
            modifier = Modifier.minimumInteractiveComponentSize() // Ensures 48dp touch target
        ) {
            Text(
                text = stringResource(R.string.login_manager_demo),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    XuxuBankTheme {
        LoginScreen(
            loginState = LoginUIState.Idle,
            onLogin = {},
            onResetError = {}
        )
    }
}
