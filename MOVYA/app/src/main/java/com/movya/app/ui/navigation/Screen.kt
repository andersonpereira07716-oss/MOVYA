package com.movya.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String? = null, val icon: ImageVector? = null) {
    object Splash : Screen("splash")
    object Home : Screen("home", "Início", Icons.Default.Home)
    object Destination : Screen("destination")
    object Confirmation : Screen("confirmation")
    object Rides : Screen("rides", "Corridas", Icons.Default.DirectionsCar)
    object Expenses : Screen("expenses", "Gastos", Icons.Default.PieChart)
    object Profile : Screen("profile", "Perfil", Icons.Default.Person)
}
