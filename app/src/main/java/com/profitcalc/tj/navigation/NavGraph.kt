package com.profitcalc.tj.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.profitcalc.tj.data.prefs.AppSettings
import com.profitcalc.tj.i18n.LocalAppStrings
import com.profitcalc.tj.ui.screens.BreakEvenScreen
import com.profitcalc.tj.ui.screens.CalculatorScreen
import com.profitcalc.tj.ui.screens.DashboardScreen
import com.profitcalc.tj.ui.screens.DiscountScreen
import com.profitcalc.tj.ui.screens.HistoryDetailScreen
import com.profitcalc.tj.ui.screens.HistoryScreen
import com.profitcalc.tj.ui.screens.MaxDiscountScreen
import com.profitcalc.tj.ui.screens.QuickCalculatorScreen
import com.profitcalc.tj.ui.screens.RoasScreen
import com.profitcalc.tj.ui.screens.SavedProductsScreen
import com.profitcalc.tj.ui.screens.SettingsScreen
import com.profitcalc.tj.ui.screens.TargetProfitScreen

private fun iconFor(route: String): ImageVector = when (route) {
    Screen.Dashboard.route -> Icons.Filled.Home
    Screen.Calculator.route -> Icons.Filled.Calculate
    Screen.History.route -> Icons.Filled.History
    Screen.SavedProducts.route -> Icons.Filled.Inventory2
    Screen.Settings.route -> Icons.Filled.Settings
    else -> Icons.Filled.Home
}

@Composable
fun ProfitCalcNavHost(settings: AppSettings) {
    val strings = LocalAppStrings.current
    val navController: NavHostController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    fun labelFor(route: String): String = when (route) {
        Screen.Dashboard.route -> strings.navDashboard
        Screen.Calculator.route -> strings.navCalculator
        Screen.History.route -> strings.navHistory
        Screen.SavedProducts.route -> strings.navSavedProducts
        Screen.Settings.route -> strings.navSettings
        else -> ""
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavScreens.forEach { screen ->
                    NavigationBarItem(
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(iconFor(screen.route), contentDescription = labelFor(screen.route)) },
                        label = { Text(labelFor(screen.route)) },
                    )
                }
            }
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = androidx.compose.ui.Modifier.padding(bottom = padding.calculateBottomPadding()),
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    currencySymbol = settings.currencySymbol,
                    onNavigateToCalculator = { navController.navigate(Screen.Calculator.route) },
                    onNavigateToHistory = { navController.navigate(Screen.History.route) },
                    onNavigateToSavedProducts = { navController.navigate(Screen.SavedProducts.route) },
                    onNavigateToQuickCalc = { navController.navigate(Screen.QuickCalc.route) },
                    onNavigateToDiscount = { navController.navigate(Screen.Discount.route) },
                    onNavigateToRoas = { navController.navigate(Screen.Roas.route) },
                )
            }
            composable(Screen.Calculator.route) {
                CalculatorScreen(
                    settings.currencySymbol,
                    settings.decimalPlaces,
                    onOpenBreakEven = { navController.navigate(Screen.BreakEven.route) },
                    onOpenTargetProfit = { navController.navigate(Screen.TargetProfit.route) },
                    onOpenMaxDiscount = { navController.navigate(Screen.MaxDiscount.route) },
                )
            }
            composable(Screen.BreakEven.route) {
                BreakEvenScreen(settings.currencySymbol, settings.decimalPlaces)
            }
            composable(Screen.TargetProfit.route) {
                TargetProfitScreen(settings.currencySymbol, settings.decimalPlaces)
            }
            composable(Screen.Discount.route) {
                DiscountScreen(settings.currencySymbol, settings.decimalPlaces)
            }
            composable(Screen.MaxDiscount.route) {
                MaxDiscountScreen(settings.currencySymbol, settings.decimalPlaces)
            }
            composable(Screen.Roas.route) {
                RoasScreen()
            }
            composable(Screen.QuickCalc.route) {
                QuickCalculatorScreen()
            }
            composable(Screen.History.route) {
                HistoryScreen(settings.currencySymbol, settings.decimalPlaces) { id ->
                    navController.navigate(Screen.HistoryDetail.buildRoute(id))
                }
            }
            composable(
                Screen.HistoryDetail.route,
                arguments = listOf(androidx.navigation.navArgument("id") { type = androidx.navigation.NavType.LongType }),
            ) { backStackEntry2 ->
                val id = backStackEntry2.arguments?.getLong("id") ?: -1L
                HistoryDetailScreen(id, settings.currencySymbol, settings.decimalPlaces)
            }
            composable(Screen.SavedProducts.route) {
                SavedProductsScreen(settings.currencySymbol, settings.decimalPlaces) {
                    navController.navigate(Screen.Calculator.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                    }
                }
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
        }
    }
}
