package com.example.quad_dev.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavRoute(val route: String, val title: String? = null, val icon: ImageVector? = null) {
    object Onboarding : NavRoute("onboarding")
    object Dashboard : NavRoute("dashboard", "Dashboard", Icons.Default.Home)
    object Simulation : NavRoute("simulation", "Simulation", Icons.Default.PlayArrow)
}

val bottomNavItems = listOf(
    NavRoute.Dashboard,
    NavRoute.Simulation
)
