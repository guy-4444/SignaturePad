package com.guy.signaturepad

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.guy.signaturepad.screens.BasicExampleScreen
import com.guy.signaturepad.screens.CustomizationExampleScreen
import com.guy.signaturepad.screens.ExportExampleScreen
import com.guy.signaturepad.screens.FormExampleScreen
import com.guy.signaturepad.screens.GuidelineExampleScreen
import com.guy.signaturepad.screens.PreferencesExampleScreen

sealed class Screen(val route: String, val title: String) {
    data object Basic : Screen("basic", "Basic")
    data object Form : Screen("form", "Form")
    data object Export : Screen("export", "Export")
    data object Custom : Screen("custom", "Custom")
    data object Guideline : Screen("guideline", "Guideline")
    data object Preferences : Screen("preferences", "Prefs")
}

private val screens = listOf(
    Screen.Basic,
    Screen.Form,
    Screen.Export,
    Screen.Custom,
    Screen.Guideline,
    Screen.Preferences
)

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                screens.forEach { screen ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        label = { Text(screen.title, style = MaterialTheme.typography.labelSmall) },
                        icon = { }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Basic.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Basic.route) { BasicExampleScreen() }
            composable(Screen.Form.route) { FormExampleScreen() }
            composable(Screen.Export.route) { ExportExampleScreen() }
            composable(Screen.Custom.route) { CustomizationExampleScreen() }
            composable(Screen.Guideline.route) { GuidelineExampleScreen() }
            composable(Screen.Preferences.route) { PreferencesExampleScreen() }
        }
    }
}
