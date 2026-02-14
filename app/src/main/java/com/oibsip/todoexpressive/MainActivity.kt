package com.oibsip.todoexpressive

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.oibsip.todoexpressive.data.repo.AppRepository
import com.oibsip.todoexpressive.ui.screens.HomeScreen
import com.oibsip.todoexpressive.ui.screens.LoginScreen
import com.oibsip.todoexpressive.ui.screens.SignUpScreen
import com.oibsip.todoexpressive.ui.theme.TodoExpressiveTheme
import com.oibsip.todoexpressive.ui.viewmodel.AppViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = AppRepository(applicationContext)

        setContent {
            TodoExpressiveTheme {
                val navController = rememberNavController()
                val vm: AppViewModel = viewModel(factory = AppViewModel.Factory(repository))
                val userId by vm.currentUserId.collectAsStateWithLifecycle()

                NavHost(
                    navController = navController,
                    startDestination = if (userId != null) "home" else "login"
                ) {
                    composable("login") {
                        LoginScreen(
                            viewModel = vm,
                            onLoginSuccess = {
                                navController.navigate("home") {
                                    popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                                }
                            },
                            onSignUpClick = { navController.navigate("signup") }
                        )
                    }
                    composable("signup") {
                        SignUpScreen(
                            viewModel = vm,
                            onSignUpSuccess = {
                                navController.navigate("home") {
                                    popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                                }
                            },
                            onBackToLogin = { navController.popBackStack() }
                        )
                    }
                    composable("home") {
                        HomeScreen(
                            viewModel = vm,
                            onLogout = {
                                vm.logout {
                                    navController.navigate("login") {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            inclusive = true
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
