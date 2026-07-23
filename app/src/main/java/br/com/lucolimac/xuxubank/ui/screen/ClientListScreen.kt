package br.com.lucolimac.xuxubank.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.lucolimac.xuxubank.R
import br.com.lucolimac.xuxubank.domain.model.Client
import br.com.lucolimac.xuxubank.ui.component.ClientItem

@Composable
fun ClientListScreen(
    clients: List<Client>,
    onClientClick: (Client) -> Unit,
    onAddClient: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClient) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_client))
            }
        }
    ) { innerPadding ->
        if (clients.isEmpty()) {
            Text(
                text = stringResource(R.string.no_clients),
                modifier = Modifier.padding(innerPadding).padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            ) {
                items(clients) { client ->
                    ClientItem(
                        client = client,
                        onClick = { onClientClick(client) }
                    )
                }
            }
        }
    }
}
