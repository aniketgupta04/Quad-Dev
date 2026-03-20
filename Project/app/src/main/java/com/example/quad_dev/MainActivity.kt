package com.example.quad_dev

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.quad_dev.ui.navigation.NavRoute
import com.example.quad_dev.ui.navigation.bottomNavItems
import com.example.quad_dev.ui.screens.DashboardScreen
import com.example.quad_dev.ui.screens.OnboardingScreen
import com.example.quad_dev.ui.screens.SimulationScreen
import com.example.quad_dev.ui.theme.Quad_devTheme
import com.example.quad_dev.ui.viewmodel.GigViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Quad_devTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: GigViewModel = viewModel()) {
    val navController = rememberNavController()
    val userOnboarded by viewModel.userOnboarded.collectAsState()
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            if (userOnboarded && currentDestination?.route != NavRoute.Onboarding.route) {
                NavigationBar {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { 
                                screen.icon?.let { Icon(it, contentDescription = null) }
                            },
                            label = { screen.title?.let { Text(it) } },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (userOnboarded) NavRoute.Dashboard.route else NavRoute.Onboarding.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NavRoute.Onboarding.route) {
                OnboardingScreen(
                    viewModel = viewModel,
                    onComplete = {
                        viewModel.completeOnboarding()
                        navController.navigate(NavRoute.Dashboard.route) {
                            popUpTo(NavRoute.Onboarding.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(NavRoute.Dashboard.route) {
                DashboardScreen(viewModel = viewModel)
            }
            composable(NavRoute.Simulation.route) {
                SimulationScreen(viewModel = viewModel)
            }
        }
    }
}
