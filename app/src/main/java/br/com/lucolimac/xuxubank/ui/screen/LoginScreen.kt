package br.com.lucolimac.xuxubank.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.lucolimac.xuxubank.R
import br.com.lucolimac.xuxubank.data.local.entity.UserRole
import br.com.lucolimac.xuxubank.ui.theme.XuxuBankTheme

@Composable
fun LoginScreen(
    onRoleSelected: (UserRole) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.welcome_message),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = stringResource(R.string.role_selection_title),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onRoleSelected(UserRole.MANAGER) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.manager_role))
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            onClick = { onRoleSelected(UserRole.CLIENT) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.client_role))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    XuxuBankTheme {
        LoginScreen(onRoleSelected = {})
    }
}
