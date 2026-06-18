package br.com.lucolimac.xuxubank.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.lucolimac.xuxubank.R
import br.com.lucolimac.xuxubank.data.local.entity.UserEntity

/**
 * Generic Home screen (used as a fallback or placeholder).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    user: UserEntity?,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text(stringResource(R.string.logout))
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.hello_user, user?.name ?: stringResource(R.string.visitor)),
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = stringResource(R.string.role_label, user?.role?.name ?: stringResource(R.string.none)),
                style = MaterialTheme.typography.bodyLarge,
                color = if (user?.role == br.com.lucolimac.xuxubank.data.local.entity.UserRole.MANAGER) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(16.dp))
            val infoText = if (user?.role == br.com.lucolimac.xuxubank.data.local.entity.UserRole.MANAGER) {
                stringResource(R.string.manager_info)
            } else {
                stringResource(R.string.client_info)
            }
            Text(
                text = infoText,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
