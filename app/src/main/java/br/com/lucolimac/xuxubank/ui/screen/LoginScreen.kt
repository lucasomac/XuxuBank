package br.com.lucolimac.xuxubank.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.lucolimac.xuxubank.R
import br.com.lucolimac.xuxubank.ui.theme.XuxuBankTheme
import br.com.lucolimac.xuxubank.ui.util.PhoneVisualTransformation
import br.com.lucolimac.xuxubank.ui.util.ValidationUtils
import br.com.lucolimac.xuxubank.ui.viewmodel.LoginUIState

/**
 * Unified Login Screen.
 * Identifies users by Email or Phone and determines their role automatically.
 */
@Composable
fun LoginScreen(
    loginState: LoginUIState,
    onLogin: (String) -> Unit,
    onResetError: () -> Unit
) {
    var identifier by remember { mutableStateOf("") }
    val isEmail = identifier.contains("@")
    val isValid = if (isEmail) ValidationUtils.isValidEmail(identifier) else ValidationUtils.isValidPhone(identifier)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.welcome_message),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Identifique-se para acessar sua conta.",
            style = MaterialTheme.typography.bodyMedium
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        OutlinedTextField(
            value = identifier,
            onValueChange = { 
                identifier = it
                if (loginState is LoginUIState.Error) onResetError()
            },
            label = { Text("E-mail ou Telefone") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = loginState is LoginUIState.Error,
            supportingText = {
                if (loginState is LoginUIState.Error) {
                    Text(text = loginState.message)
                }
            },
            visualTransformation = if (isEmail) androidx.compose.ui.text.input.VisualTransformation.None else PhoneVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = if (identifier.any { it.isLetter() }) KeyboardType.Email else KeyboardType.Number
            )
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = { onLogin(identifier) },
            modifier = Modifier.fillMaxWidth(),
            enabled = isValid && loginState !is LoginUIState.Loading
        ) {
            if (loginState is LoginUIState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text(stringResource(R.string.login))
            }
        }

        // Demo Helper for Manager
        Spacer(modifier = Modifier.height(32.dp))
        TextButton(onClick = { onLogin("admin@xuxubank.com") }) {
            Text("Entrar como Gerente (Demo)")
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
