package com.profitcalc.tj.navigation

sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object Calculator : Screen("calculator")
    data object BreakEven : Screen("break_even")
    data object TargetProfit : Screen("target_profit")
    data object Discount : Screen("discount")
    data object MaxDiscount : Screen("max_discount")
    data object Roas : Screen("roas")
    data object QuickCalc : Screen("quick_calc")
    data object History : Screen("history")
    data object HistoryDetail : Screen("history_detail/{id}") {
        fun buildRoute(id: Long) = "history_detail/$id"
    }
    data object SavedProducts : Screen("saved_products")
    data object Settings : Screen("settings")
}

/** Bottom navigation destinations (the most frequently used screens). */
val bottomNavScreens = listOf(
    Screen.Dashboard,
    Screen.Calculator,
    Screen.History,
    Screen.SavedProducts,
    Screen.Settings,
)
