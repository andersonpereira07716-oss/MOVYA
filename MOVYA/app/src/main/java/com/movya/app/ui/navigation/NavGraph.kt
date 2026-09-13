package com.movya.app.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.movya.app.domain.model.LocationPoint
import com.movya.app.ui.components.MovyaBottomBar
import com.movya.app.ui.screens.confirmation.RideConfirmationScreen
import com.movya.app.ui.screens.destination.DestinationScreen
import com.movya.app.ui.screens.home.HomeScreen
import com.movya.app.ui.screens.splash.SplashScreen

@Composable
fun NavGraph(navController: NavHostController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    val showBottomBar = currentRoute in listOf(
        Screen.Home.route,
        Screen.Rides.route,
        Screen.Expenses.route,
        Screen.Profile.route
    )

    var selectedOrigin by remember {
        mutableStateOf(LocationPoint("Localização Atual", -7.025, -37.275))
    }
    var selectedDestination by remember {
        mutableStateOf<LocationPoint?>(null)
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                MovyaBottomBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(navController = navController)
            }
            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToDestination = {
                        navController.navigate(Screen.Destination.route)
                    }
                )
            }
            composable(Screen.Destination.route) {
                DestinationScreen(
                    onBackClick = { navController.popBackStack() },
                    onDestinationSelected = { destination ->
                        selectedDestination = destination
                        navController.navigate(Screen.Confirmation.route)
                    }
                )
            }
            composable(Screen.Confirmation.route) {
                val dest = selectedDestination
                if (dest != null) {
                    RideConfirmationScreen(
                        origin = selectedOrigin,
                        destination = dest,
                        onBackClick = { navController.popBackStack() },
                        onConfirmClick = { vehicle ->
                            navController.navigate(Screen.Rides.route) {
                                popUpTo(Screen.Home.route)
                            }
                        }
                    )
                }
            }
            composable(Screen.Rides.route) {
                PlaceholderScreen(title = "Minhas Corridas")
            }
            composable(Screen.Expenses.route) {
                PlaceholderScreen(title = "Painel Financeiro & Gastos")
            }
            composable(Screen.Profile.route) {
                PlaceholderScreen(title = "Perfil & Configurações")
            }
        }
    }
}

@Composable
private fun PlaceholderScreen(title: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = title)
    }
}
	
