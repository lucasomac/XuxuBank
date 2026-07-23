package br.com.lucolimac.xuxubank

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import br.com.lucolimac.xuxubank.data.local.entity.UserRole
import br.com.lucolimac.xuxubank.domain.model.User
import br.com.lucolimac.xuxubank.ui.navigation.NavRoute
import br.com.lucolimac.xuxubank.ui.screen.LoginScreen
import br.com.lucolimac.xuxubank.ui.screen.ManagerHomeScreen
import br.com.lucolimac.xuxubank.ui.screen.SplashScreen
import br.com.lucolimac.xuxubank.ui.screen.UserHomeScreen
import br.com.lucolimac.xuxubank.ui.theme.XuxuBankTheme
import br.com.lucolimac.xuxubank.ui.viewmodel.DebtViewModel
import br.com.lucolimac.xuxubank.ui.viewmodel.ClientViewModel
import br.com.lucolimac.xuxubank.ui.viewmodel.UserViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            XuxuBankTheme {
                val userViewModel: UserViewModel = koinViewModel()
                val clientViewModel: ClientViewModel = koinViewModel()
                val debtViewModel: DebtViewModel = koinViewModel()
                XuxuBankApp(userViewModel, clientViewModel, debtViewModel)
            }
        }
    }
}

@Composable
fun XuxuBankApp(userViewModel: UserViewModel, clientViewModel: ClientViewModel, debtViewModel: DebtViewModel) {
    val currentUser by userViewModel.currentUser.collectAsState()
    val loginState by userViewModel.loginState.collectAsState()
    val backStack = rememberNavBackStack(NavRoute.Splash)

    // Synchronize navigation with auth state
    LaunchedEffect(currentUser) {
        val currentRoute = backStack.lastOrNull()
        if (currentRoute == NavRoute.Splash) return@LaunchedEffect

        if (currentUser == null && currentRoute != NavRoute.Login) {
            backStack.clear()
            backStack.add(NavRoute.Login)
        } else if (currentUser != null && (currentRoute == NavRoute.Login || currentRoute == null)) {
            backStack.clear()
            backStack.add(NavRoute.Home)
        }
    }

    NavDisplay(
        backStack = backStack,
        onBack = {
            if (backStack.size > 1) {
                backStack.removeLastOrNull()
            }
        },
        entryProvider = { key ->
            when (key) {
                NavRoute.Splash -> NavEntry(key) {
                    SplashScreen {
                        backStack.clear()
                        if (currentUser == null) {
                            backStack.add(NavRoute.Login)
                        } else {
                            backStack.add(NavRoute.Home)
                        }
                    }
                }
                NavRoute.Login -> NavEntry(key) {
                    LoginScreen(
                        loginState = loginState,
                        onLogin = { identifier ->
                            userViewModel.login(identifier)
                        },
                        onResetError = {
                            userViewModel.resetLoginState()
                        }
                    )
                }
                NavRoute.Home -> NavEntry(key) {
                    currentUser?.let { user ->
                        if (user.role == UserRole.MANAGER) {
                            ManagerHomeScreen(
                                user = user,
                                onLogout = { userViewModel.logout() },
                                clientViewModel = clientViewModel,
                                debtViewModel = debtViewModel
                            )
                        } else {
                            UserHomeScreen(
                                user = user,
                                onLogout = { userViewModel.logout() },
                                debtViewModel = debtViewModel
                            )
                        }
                    } ?: Text(stringResource(R.string.loading))
                }
                else -> NavEntry(key) { Text(stringResource(R.string.unknown_route)) }
            }
        }
    )
}
